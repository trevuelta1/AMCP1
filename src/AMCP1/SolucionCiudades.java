/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AMCP1;

/**
 *
 * @author USUARIO1
 */
public class SolucionCiudades {
    private Ciudad[] ciudades;
    private double[] distancias;
    private double distanciaTotal;
    private static int inc = 0;
    private static int ind = 0;
    
    public SolucionCiudades(Ciudad[] ciudadesIniciales){
        this.ciudades = new Ciudad[ciudadesIniciales.length + 1];
        this.distancias = new double[ciudadesIniciales.length];
        this.distanciaTotal = 0;
    }
    
    public Ciudad[] getCiudades(){return this.ciudades;}
    public void addCiudad(Ciudad c){
        if(inc < this.ciudades.length){
            this.ciudades[inc] = c;
            inc++;
        }
    }
    public double[] getDistancias(){return this.distancias;}
    public void addDistancia(double dist){
        if(ind < this.distancias.length){
            this.distancias[ind] = dist;
            ind++;
        }
    }
    public double getDistanciaTotal(){return this.distanciaTotal;}
    public boolean actualizarDistanciaTotal(){
        double primera = this.distanciaTotal;
        for(int i = 0; i < this.distancias.length; i++){
            this.distanciaTotal = this.distanciaTotal + this.distancias[i];
        }
        return (primera != this.distanciaTotal);
    }
}
