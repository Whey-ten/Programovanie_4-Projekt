import javafx.scene.image.ImageView;

/**
 * Classa Items, ktora si uchovava informacie o predmetoch.
 * x_1, y_1, x_2, y_2 - suradnice predmetu
 * image - informacia o obrazku
 * id - id predmetu aby bolo mozne ich rozoznat
 * active - informacia, ktora hovori o tom ci bol predmet zdvyhnuty alebo nie
 */
public class Items {
    double x_1;
    double y_1;
    double x_2;
    double y_2;
    transient ImageView image;
    int id;
    boolean active;

    public Items(double x_1, double y_1, double x_2, double y_2, ImageView image, int id){
        this.x_1 = x_1;
        this.y_1 = y_1;
        this.x_2 = x_2;
        this.y_2 = y_2;
        this.image = image;
        this.id = id;
        this.active = true;
    }

    public double getX_1() {
        return x_1;
    }

    public void setX_1(double x_1) {
        this.x_1 = x_1;
    }

    public double getY_1() {
        return y_1;
    }

    public void setY_1(double y_1) {
        this.y_1 = y_1;
    }

    public double getX_2() {
        return x_2;
    }

    public void setX_2(double x_2) {
        this.x_2 = x_2;
    }

    public double getY_2() {
        return y_2;
    }

    public void setY_2(double y_2) {
        this.y_2 = y_2;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
