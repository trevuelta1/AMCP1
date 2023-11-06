package AMCP1;

/**
 *
 * @author USUARIO1
 */

public class Punto {
    public static int idp = 1;
    private double x;
    private double y;
    private int id;
    public Punto(double x, double y){
        this.x = x;
        this.y = y;
        this.id = idp;
        idp++;
    }
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}
    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}
    public String getPunto(){return "Punto " + this.id + ". X = " + this.x + ". Y = " + this.y + ".";}
    public static double distancia(Punto p1, Punto p2){
        double distancia = Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
        return distancia;        
    }
}
