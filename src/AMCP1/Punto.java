package AMCP1;

import static java.lang.Math.*;

/**
 *
 * @author USUARIO1
 */
public class Punto {
    private double x;
    private double y;
    public Punto(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}
    public String getPunto(){return "X = " + this.x + ". Y = " + this.y + ".";}
    public static double distancia(Punto p1, Punto p2){
        double distancia = Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
        return distancia;        
    }
}
