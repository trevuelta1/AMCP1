/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AMCP1;

/**
 *
 * @author USUARIO1
 */
public class Ciudad {
    private Punto coordenadas;
    private boolean visitado;
    
    public Ciudad(Punto p){
        this.coordenadas = p;
        this.visitado = false;
    }
    
    public Punto getCoordenadas(){return this.coordenadas;}
    public void setCoordenadas(Punto p){this.coordenadas = p;}
    public boolean getVisitado(){return this.visitado;}
    public void setVisitado(boolean b){this.visitado = b;}
}
