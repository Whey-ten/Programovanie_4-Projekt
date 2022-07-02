import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Autor Sebastian Jankovic
 */
public class Vlak extends Application {
    /**
     * Arraylisty, v ktorych su ulozene viditelne prvky v hre
     * steny - obsahuje informacie o stenach v hre
     * vlak - uchovava si informacie o lokomotive a vagonoch
     * items - uchovava informacie o predmetoch nachadzajucich sa v danom levely
     */
    List<Steny> steny = new ArrayList();
    List<Part> vlak = new ArrayList<>();
    List<Items> items = new ArrayList<>();
    List<Integer> dosiahnute_levely = new ArrayList<>();

    /**
     * level - momentalny level
     * strana - urcuje, ktorym smerom sa bude vlak pohybovat
     * door - boolean, ktory drzi hodnotu ci su dvere zamknute alebo nie
     * f_x1, f_y1, f_x2, f_y2 - uchovavaju si hodnoty x,y, ktore su neskor vyuzite pri pohybe
     * score_current - momentalne score pocas levelu, pri naraze sa meni na ulozene znova
     * score_saved - ulozene skore, zapise sa vzdy po prejdeni levelu
     */
    int level;
    int strana;
    boolean door;
    double f_x1, f_y1, f_x2, f_y2;
    int score_current, score_saved;

    /**
     * Nacitanie zvukov na vyuzitie v hre
     */
    private File pohyb_zvuk = new File("sounds/train.wav");
    private final String pohyb_zvuk_URL = pohyb_zvuk.toURI().toString();
    private File naraz_zvuk = new File("sounds/explosion.wav");
    private final String naraz_zvuk_URL = naraz_zvuk.toURI().toString();
    private File next_zvuk = new File("sounds/whistle.wav");
    private final String next_zvuk_URL = next_zvuk.toURI().toString();

    AudioClip moveSound = new AudioClip(pohyb_zvuk_URL);
    AudioClip hitSound = new AudioClip(naraz_zvuk_URL);
    AudioClip next_lvlSound = new AudioClip(next_zvuk_URL);

    private String heslo_1 = "DEFAULT";
    private String heslo_2 = "PEPSI";
    private String heslo_3 = "TRIK1";

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Sceny
         * scene_menu - uvodna scena, ktoru uvidi hrac a ma vnej moznosti pre Start hry a jej ukoncenie
         * scene_game - scena, v ktorej prebieha hra
         */
        VlakPane p = new VlakPane();
        Scene scene_game = new Scene(p, 800, 800, Color.BLACK);

        VBox menuVBox = new VBox(30);
        Scene scene_menu = new Scene(menuVBox, 800, 800, Color.BLACK);
        Image background = new Image("images/background_final.png");

        /**
         * Automaticky pri spusteni nastavi level na 1 a "vytvori" hru (ulozi do arraylistov)
         */
        level = 1;
        vytvor();

        /**
         * Chod hry
         */
        Timeline timeline = new Timeline(new KeyFrame(new Duration(500), event -> {
            if (level <= 3){
                pohyb();
                //moveSound.play();
                if(lock()){
                    door = true;
                }
                try {
                    collision();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                p.paint();
            } else {
                p.paint_end();
            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        /**
         * Eventhandlery pre klavesy
         * umoznuje vyuzivat sipky, klavesu "R", klavesu "ESC" a klavesu "F4"
         * klavesa "F5" je pristupna iba pokial hrac presiel hru a umoznuje mu ulozit svoje score a zaroven ho presunie
         * znova do uvodneho menu
         */
        scene_game.setOnKeyPressed(event -> {
            Part lokomitiva = vlak.get(0);
            if (event.getCode() == KeyCode.LEFT){
                //Part lokomitiva = vlak.get(0);
                lokomitiva.setImage(new ImageView("images/vlak_left.gif"));
                strana = 3;
            }
            if (event.getCode() == KeyCode.RIGHT){
                //Part lokomitiva = vlak.get(0);
                lokomitiva.setImage(new ImageView("images/vlak_right.gif"));
                strana = 1;
            }
            if (event.getCode() == KeyCode.UP){
                if(strana == 1){
                    lokomitiva.setImage(new ImageView("images/vlak_up_2.gif"));

                } else {
                    lokomitiva.setImage(new ImageView("images/vlak_up_1.gif"));

                }
                strana = 2;
            }
            if (event.getCode() == KeyCode.DOWN){
                if(strana == 1){
                    lokomitiva.setImage(new ImageView("images/vlak_down_2.gif"));

                } else {
                    lokomitiva.setImage(new ImageView("images/vlak_down_1.gif"));

                }
                strana = 0;
            }
            if (event.getCode() == KeyCode.R){
                try {
                    score_current = score_saved;
                    vytvor();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (event.getCode() == KeyCode.ESCAPE){
                timeline.stop();
                primaryStage.setScene(scene_menu);
            }
            if (event.getCode() == KeyCode.F4){
                timeline.pause();
                TextAreaInputDialog dialog = new TextAreaInputDialog();
                dialog.setHeaderText(null);
                dialog.setGraphic(null);

                Optional result = dialog.showAndWait();
                if (result.isPresent()){
                    if(result.get().equals(heslo_1)){
                        level = 1;
                        timeline.play();
                    } else if(result.get().equals(heslo_2)){
                        level = 2;
                        try {
                            vytvor();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        timeline.play();

                    } else if(result.get().equals(heslo_3)){
                        level = 3;
                        try {
                            vytvor();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        timeline.play();

                    } else {
                        System.out.println("Zadal si zle heslo.");
                        timeline.play();
                    }
                }
            }
            if (event.getCode() == KeyCode.F5){
                if(level > 3){
                    String meno;
                    timeline.pause();
                    TextAreaInputDialog dialog = new TextAreaInputDialog();
                    dialog.setHeaderText(null);
                    dialog.setGraphic(null);

                    Optional result = dialog.showAndWait();
                    if(result.isPresent()){
                        meno = String.valueOf(result.get());
                        try {
                            uloz_score(meno);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    timeline.stop();
                    primaryStage.setScene(scene_menu);
                }
            }
            event.consume();
        });



        /**
         * Tlacidla v menu hry
         * endButton - ukonci hru
         * startButton - vytvori novu hru na predvolenom levely(hrac po stlaceni strati doterajsi progress)
         * continueButton - hidden pokial nebola vytvorena hra, dovoli pokracovat v hre
         * leaderboardButton - Otvori nove okno a v nom vypise top5 ulozenych score
         * hesloButton - otvori text areu, kde hrac vie zadat heslo, pokial je spravne tak nastavi level na dany a znova
         * vygeneruje hru pre dany level a aj ju hned zapne
         */
        Button endButton = new Button("Exit Game");
        endButton.setOnAction(e -> Platform.exit());

        Button continueButton = new Button("Continue Game");
        continueButton.setOnAction(e ->{
            primaryStage.setScene(scene_game);
            timeline.play();
        });

        Button startButton = new Button("Start New Game");
        startButton.setOnAction(e ->{
            level = 1;
            score_current = 0;
            score_saved = 0;
            dosiahnute_levely = new ArrayList<>();
            continueButton.setVisible(true);
            try {
                vytvor();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            primaryStage.setScene(scene_game);
            timeline.play();
        });



        Button leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setOnAction(e -> {
            Label secondLabel = null;
            try {
                secondLabel = new Label(nacitaj());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            StackPane secondaryLayout = new StackPane();
            secondaryLayout.getChildren().add(secondLabel);

            Scene secondScene = new Scene(secondaryLayout, 230, 100);

            Stage newWindow = new Stage();
            newWindow.setTitle("Second Stage");
            newWindow.setScene(secondScene);

            newWindow.setX(primaryStage.getX() + 200);
            newWindow.setY(primaryStage.getY() + 100);

            newWindow.show();
        });

        Button hesloButton = new Button("Set Level");
        hesloButton.setOnAction(e -> {
            TextAreaInputDialog dialog = new TextAreaInputDialog();
            dialog.setHeaderText(null);
            dialog.setGraphic(null);

            Optional result = dialog.showAndWait();

            if (result.isPresent()) {
                if(result.get().equals(heslo_1)){
                    level = 1;
                    primaryStage.setScene(scene_game);
                    timeline.play();
                } else if(result.get().equals(heslo_2)){
                    if(dosiahnute_levely.contains(2)){
                        level = 2;
                        try {
                            vytvor();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        primaryStage.setScene(scene_game);
                        timeline.play();
                    } else {
                        System.out.println("Tento level si este nedosiahol.");
                    }

                } else if(result.get().equals(heslo_3)){
                    if(dosiahnute_levely.contains(3)){
                        level = 3;
                        try {
                            vytvor();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        primaryStage.setScene(scene_game);
                        timeline.play();
                    } else {
                        System.out.println("Tento level si este nedosiahol.");
                    }

                } else {
                    System.out.println("Zadal si zle heslo.");
                }

            }

        });
        /**
         * Ulozi vsetky buttony a vytvori background
         */
        menuVBox.getChildren().addAll(startButton, continueButton, hesloButton, leaderboardButton, endButton);
        continueButton.setVisible(false);
        menuVBox.setAlignment(Pos.CENTER);
        menuVBox.setBackground(new Background(new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));


        primaryStage.setTitle("VLAK - Jankovic");
        primaryStage.setScene(scene_menu);
        primaryStage.show();
    }

    /**
     * Funkcia dostane meno hraca a do textoveho suboru ulozi jeho meno a scorem ktore dosiahol v hre.
     * @param meno_hraca
     * @throws IOException
     */

    public void uloz_score(String meno_hraca) throws IOException {
        String vysl = meno_hraca + ",";
        vysl += String.valueOf(score_saved);
        vysl += "\n";
        try {
            FileWriter fw = new FileWriter("leaderboard.txt",true);
            fw.write(vysl);
            fw.close();
        } catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    /**
     * Funkcia nacita textovy subor obsahujuci ulozene score hracov. Ten po riadkoch precita a do Sorted map, tu reverse
     * sortne a cyklom vrati String obsahujuci 5 hracov s najlepsim ulozenym skore
     * @return
     * @throws IOException
     */

    public String nacitaj() throws IOException {
        String vysl = "";
        List<String> values = Files.readAllLines(Paths.get("leaderboard.txt"));
        SortedMap<Integer, String> sm = new TreeMap<Integer, String>(Collections.reverseOrder());
        for (String s : values){
            String[] i_v = s.split(",");
            sm.put(Integer.valueOf(i_v[1]),i_v[0]);
        }
        int p = 1;
        Set set = sm.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext() && p < 6){
            Map.Entry me = (Map.Entry)i.next();
            vysl += p + ". " + me.getValue() + " - " + String.valueOf(me.getKey()) + "\n";
            p ++;
        }

        return vysl;
    }

    /**
     * Funkcia, ktora zobere hodnotu levelu, podla neho z textoveho suboru na cita do Arraylistov hodnoty a tym vygeneruje
     * level
     * @throws IOException
     */
    public void vytvor() throws IOException {
        if(level <= 3){
            steny = new ArrayList<>();
            vlak = new ArrayList<>();
            items = new ArrayList<>();
            strana = 1;
            String subor = "levels/level_";
            subor += String.valueOf(level);
            subor += ".txt";
            List<String> values = Files.readAllLines(Paths.get(subor));
            for (String s : values){
                String[] i_v = s.split(",");
                int id = Integer.valueOf(i_v[0]);
                int x = Integer.valueOf(i_v[1]);
                int y = Integer.valueOf(i_v[2]);
                int x2 = Integer.valueOf(i_v[3]);
                int y2 = Integer.valueOf(i_v[4]);
                if(id == 0){
                    vlak.add(new Part(x,y,x2,y2, new ImageView("images/vlak_right.gif"), id));
                } else if(id == 1){
                    items.add(new Items(x,y,x2,y2,new ImageView("images/jablko.gif"), id));
                } else if(id == 2){
                    items.add(new Items(x,y,x2,y2,new ImageView("images/Korunka.gif"), id));
                } else if(id == 3){
                    items.add(new Items(x,y,x2,y2,new ImageView("images/hviezda.gif"), id));
                } else if(id == 4){
                    items.add(new Items(x,y,x2,y2,new ImageView("images/trapdoor_closed.gif"), id));
                } else if(id == 5){
                    steny.add(new Steny(x,y,x2,y2));
                }
            }
        } else {
            steny = new ArrayList<>();
            vlak = new ArrayList<>();
            vlak.add(new Part(0,0,0,0, new ImageView("images/vlak_right.gif"), 0));
            items = new ArrayList<>();
            strana = 1;
        }

    }

    /**
     * Funkcia, ktora zabezpecuje pohyb vlaku. Zoberie si momentalne suradnice lokomotivy a podla smeru ich upravi.
     * Potom ulozi ich nove hodnoty. Dalej sa precykli cez vsetky vagony a posunie ich. V f_x1, f_y1, f_x2, f_y2 si
     * ulozi pomocne hodnoty a postupne poposuva vsetky casti vlaku
     */
    public void pohyb(){
        double x1,y1,x2,y2;
        Part lokomotiva = vlak.get(0);
        x1 = lokomotiva.x_1;
        y1 = lokomotiva.y_1;
        x2 = lokomotiva.x_2;
        y2 = lokomotiva.y_2;
        double x1p,y1p,x2p,y2p;
        //pohyb lokomotivy
        if (strana == 0){
            lokomotiva.setY_1(lokomotiva.y_1 + 40);
            lokomotiva.setY_2(lokomotiva.y_2 + 40);
        } else if( strana == 1){
            lokomotiva.setX_1(lokomotiva.x_1 + 40);
            lokomotiva.setX_2(lokomotiva.x_2 + 40);
        } else if(strana == 2){
            lokomotiva.setY_1(lokomotiva.y_1 - 40);
            lokomotiva.setY_2(lokomotiva.y_2 - 40);
        } else {
            lokomotiva.setX_1(lokomotiva.x_1 - 40);
            lokomotiva.setX_2(lokomotiva.x_2 - 40);
        }
        for(Part p : vlak){
            if(p.id == 0){
                continue;
            }
            x1p = p.x_1;
            y1p = p.y_1;
            x2p = p.x_2;
            y2p = p.y_2;
            //
            p.setX_1(x1);
            p.setY_1(y1);
            p.setX_2(x2);
            p.setY_2(y2);
            //
            x1 = x1p;
            y1 = y1p;
            x2 = x2p;
            y2 = y2p;
        }
        f_x1 = x1;
        f_y1 = y1;
        f_x2 = x2;
        f_y2 = y2;


    }

    /**
     * Pomocna funkcia na kontrolu ci hrac pozbieral vsetky prvky na mape a tym otvoril dvere. Ak su dvere otvorene
     * zmeni premennu imagu v nich aby ukazovali ze su otvorene.
     * @return
     */
    public boolean lock(){
        for (Items i : items){
            if(i.id == 4 ){
                continue;
            }
            if(i.active){
                return false;
            }
        }
        Items dvere = items.get(0);
        dvere.image = new ImageView("images/trapdoor_opened.gif");
        return true;
    }

    /**
     * Pomocna funkcia, ktora je zavolana ak hrac zoberie item v hre. Dostava hodnotu id podla, ktorej vie aky image ma
     * priradit vagonu na zaklade itemu, ktory zobral.
     * @param id
     */
    public void pridaj(int id){
        if(id == 1){
            vlak.add(new Part(f_x1,f_y1,f_x2,f_y2, new ImageView("images/vagon_jablko.gif"),id));
        } else if(id == 2){
            vlak.add(new Part(f_x1,f_y1,f_x2,f_y2, new ImageView("images/vagon_koruna.gif"),id));
        } else if(id == 3){
            vlak.add(new Part(f_x1,f_y1,f_x2,f_y2, new ImageView("images/vagon_hviezda.gif"),id));
        }
    }

    /**
     * Funkcia, ktora kontroluje vsetky kolizie v hre.
     * Ako prve kontroluje ci sa vlak nachadza na iteame ak ano tak iteamu nastavi, ze bol zodvyhnuty, nasledne zavola
     * funkciu pridaj(), ktora prida novy vagon do vlaku a nakoniec prida hracovi body.
     * Kontroluje, ze ak sa vlak dostal do dveri ci su zamknute alebo nie. Ak ano resetuje level ak nie tak zvysi level,
     * vygeneruje novy level, ulozi momentalne skore aby sa prenieslo do neveho levelu.
     * Kontroluje ci vlak nenarazil do steny. Ak ano resetuje level a score.
     * Kontroluje ci vlak nahodou nenarazil do seba sameho. Ak ano znova zresetuje level a score.
     * @throws IOException
     */
    public void collision() throws IOException {
        Part lokomotiva = vlak.get(0);
        double x_1 = lokomotiva.x_1;
        double y_1 = lokomotiva.y_1;
        double x_2 = lokomotiva.x_2;
        double y_2 = lokomotiva.y_2;
        int count = 0;

        for(Items i : items){
            if(!i.active){
                continue;
            }
            if(x_1 >= i.x_1 && x_1 <= i.x_2 && y_1 >= i.y_1 && y_1 <= i.y_2){
                if(x_2 >= i.x_1 && x_2 <= i.x_2 && y_2 >= i.y_1 && y_2 <= i.y_2){
                    if(i.id == 1){
                        score_current += 20;
                        i.active = false;
                        pridaj(i.id);

                    } else if(i.id == 2){
                        score_current += 30;
                        i.active = false;
                        pridaj(i.id);

                    } else if(i.id == 3){
                        score_current += 40;
                        i.active = false;
                        pridaj(i.id);

                    } else {
                        if(!door){
                            System.out.println("doors were closed");
                            score_current = score_saved;
                            //hitSound.play();
                            vytvor();
                        } else {
                            System.out.println("doors were open");
                            score_saved = score_current;
                            level++;
                            dosiahnute_levely.add(level);
                            if(level == 4){

                            } else {
                                //next_lvlSound.play();
                                vytvor();
                                door = false;
                            }

                        }
                    }

                }

            }
        }

        for(Steny s : steny){
            if(x_1 >= s.x && x_1 <= s.x2 && y_1 >= s.y && y_1 <= s.y2){
                if(x_2 >= s.x && x_2 <= s.x2 && y_2 >= s.y && y_2 <= s.y2){
                    score_current = score_saved;
                    //hitSound.play();
                    vytvor();
                    System.out.println("NARAZIL SI");
                }

            }
        }
        for(Part p : vlak){
            if(x_1 >= p.x_1 && x_1 <= p.x_2 && y_1 >= p.y_1 && y_1 <= p.y_2){
                if(x_2 >= p.x_1 && x_2 <= p.x_2 && y_2 >= p.y_1 && y_2 <= p.y_2){
                    count++;
                }
            }
        }
        if(count > 1){
            score_current = score_saved;
            //hitSound.play();
            vytvor();
            System.out.println("Narazil si sameho seba");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Classa Pane, ktora sa stara o vykreslovanie hry
     */
    class VlakPane extends Pane {
        public void paint(){
            getChildren().clear();
            for(Steny s : steny){
                Color farba = Color.LIGHTBLUE;
                Rectangle rectangle = new Rectangle(s.getX(), s.getY(), s.getSirka(), s.getVyska());
                rectangle.setFill(farba);
                getChildren().add(rectangle);
            }
            for(Part p : vlak){
                ImageView pic = p.image;
                pic.setX(p.x_1);
                pic.setY(p.y_1);
                pic.setFitHeight(400/10);
                pic.setFitWidth(400/10);
                getChildren().add(pic);
            }
            for(Items i : items){
                if(!i.active){
                    continue;
                }
                ImageView pic = i.image;
                pic.setX(i.x_1);
                pic.setY(i.y_1);
                pic.setFitHeight(400/10);
                pic.setFitWidth(400/10);
                getChildren().add(pic);
            }

            String txt_lvl = "LEVEL: ";
            txt_lvl += String.valueOf(level);
            Text text_level = new Text(txt_lvl);
            text_level.setX(10);
            text_level.setY(20);
            text_level.setFont(new Font(20));
            getChildren().add(text_level);

            String txt_score = "SCORE: ";
            txt_score += String.valueOf(score_current);
            Text text_score = new Text(txt_score);
            text_score.setX(100);
            text_score.setY(20);
            text_score.setFont(new Font(20));
            getChildren().add(text_score);

            String txt_heslo = "HESLO: ";
            if(level == 1){
                txt_heslo += heslo_1;
            } else if(level == 2){
                txt_heslo += heslo_2;
            } else if(level == 3){
                txt_heslo += heslo_3;
            }
            Text text_heslo = new Text(txt_heslo);
            text_heslo.setX(300);
            text_heslo.setY(790);
            text_heslo.setFont(new Font(20));
            getChildren().add(text_heslo);

        }
        public void paint_end(){
            getChildren().clear();
            Color farba = Color.LIGHTBLUE;
            Rectangle rectangle = new Rectangle(0,0,800,800);
            rectangle.setFill(farba);

            String txt_end = "GAME OVER YOU WON!";
            Text text_end = new Text(txt_end);
            text_end.setX(110);
            text_end.setY(320);
            text_end.setFont(new Font(50));
            getChildren().add(text_end);

            String txt_score = "SCORE: ";
            txt_score += String.valueOf(score_current);
            Text text_score = new Text(txt_score);
            text_score.setX(350);
            text_score.setY(540);
            text_score.setFont(new Font(20));
            getChildren().add(text_score);

            String txt_save = "Press f5 to save score.";
            Text text_save = new Text(txt_save);
            text_save.setX(330);
            text_save.setY(640);
            text_save.setFont(new Font(15));
            text_save.setFill(Color.RED);
            getChildren().add(text_save);
        }

    }

}
