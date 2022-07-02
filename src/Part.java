import javafx.scene.image.ImageView;

/**
 * Classa, ktora si uchovava informacie o vlaku.
 * x_1, y_1, x_2, y_2 - suradnice vagonu/lokomotivy.
 * image - informacia o obrazku
 * id - id casti vlaku aby bolo mozne ich rozoznat
 */
public class Part {
    double x_1;
    double y_1;
    double x_2;
    double y_2;
    transient ImageView image;
    int id;

    public Part(double x_1, double y_1, double x_2, double y_2, ImageView image, int id){
        this.x_1 = x_1;
        this.y_1 = y_1;
        this.x_2 = x_2;
        this.y_2 = y_2;
        this.image = image;
        this.id = id;
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
}