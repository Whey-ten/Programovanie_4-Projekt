/**
 * Classa steny, ktora vytvara stenu a uchovava jej suradnice na mape
 */
public class Steny {
    double x;
    double y;
    double vyska;
    double sirka;
    double x2, y2;
    public Steny(double x, double y, double sirka, double vyska){
        this.x = x;
        this.y = y;
        this.vyska = vyska;
        this.sirka = sirka;
        this.x2 = x + sirka;
        this.y2 = y + vyska;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVyska() {
        return vyska;
    }

    public void setVyska(double vyska) {
        this.vyska = vyska;
    }

    public double getSirka() {
        return sirka;
    }

    public void setSirka(double sirka) {
        this.sirka = sirka;
    }
}
