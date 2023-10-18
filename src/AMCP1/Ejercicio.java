package AMCP1;

/**
 *
 * @author USUARIO1
 */
public class Ejercicio {
    private int particion(Punto vector[], int izquierda, int derecha) {
        double pivote = vector[izquierda].getX();
        // Ciclo infinito
        while (true) {
            while (vector[izquierda].getX() < pivote) {
                izquierda++;
            }
            while (vector[derecha].getX() > pivote) {
                derecha--;
            }
            if (izquierda >= derecha) {
                return derecha;
            } else{
                Punto temporal = vector[izquierda];
                vector[izquierda] = vector[derecha];
                vector[derecha] = temporal;
                izquierda++;
                derecha--;
            }
        }
    }
    private void quicksort(Punto vector[], int izquierda, int derecha) {
        if (izquierda < derecha) {
            int indiceParticion = particion(vector, izquierda, derecha);
            quicksort(vector, izquierda, indiceParticion);
            quicksort(vector, indiceParticion + 1, derecha);
        }
    }
    public void quicksort(Punto vector[]){
        quicksort(vector, 0, vector.length - 1);
    }
    public Punto[] busquedaExhaustiva(Punto[] puntos){
        Punto[] solucion = new Punto[2];
        solucion[0] = puntos[0];
        solucion[1] = puntos[1];
        double dminima = Punto.distancia(puntos[0], puntos[1]);
        for(int i = 0; i < puntos.length; i++){
            for(int j = i + 1; j < puntos.length; j++){
                if(Punto.distancia(puntos[i], puntos[j]) < dminima){
                    dminima = Punto.distancia(puntos[i], puntos[j]);
                    solucion[0] = puntos[i];
                    solucion[1] = puntos[j];
                }
            }
        }
        return solucion;
    }
    public Punto[] busquedaPoda(Punto[] puntos){
        Punto[] solucion = new Punto[2];
        quicksort(puntos);
        solucion[0] = puntos[0];
        solucion[1] = puntos[1];
        double dminima = Punto.distancia(puntos[0], puntos[1]);
        boolean b = false;
        int i = 0;
        while(b == false && i < puntos.length){
            int j = i + 1;
            while(b == false && j < puntos.length){
                double dist = puntos[i].getX() - puntos[j].getX();
                if(dist < 0){
                    dist = dist * -1;
                }
                if(dist < dminima){
                    if(Punto.distancia(puntos[i], puntos[j]) < dminima){
                        dminima = Punto.distancia(puntos[i], puntos[j]);
                        solucion[0] = puntos[i];
                        solucion[1] = puntos[j];
                    }
                    j++;
                } else{
                    b = true;
                }
            }
            i++;
        }
        return solucion;
    }
    public Punto[] divideYvenceras(Punto[] puntos){
        Punto[] solucion = new Punto[2];
        quicksort(puntos);
        if(puntos.length > 2){ //conjunto de más de 2 puntos
            int mitad = puntos.length / 2; //divide en 2 subconjuntos
            Punto[] izq = new Punto[mitad];
            Punto[] der = new Punto[puntos.length - mitad];
            for(int i = 0; i < mitad; i++){
                izq[i] = puntos[i];
            }
            for(int i = mitad; i < puntos.length; i++){
                der[i - mitad] = puntos[i];
            }
            Punto[] solucionIzq = divideYvenceras(izq); //vuelve a resolver recursivamente hasta que quedan conjuntos de 2 ó 1 puntos
            Punto[] solucionDer = divideYvenceras(der);
            double dizq = Punto.distancia(solucionIzq[0], solucionIzq[1]);
            double dder = Punto.distancia(solucionDer[0], solucionDer[1]);
            double dminima; //cálculo de distancia mínima
            if(dizq == 0){ //solo el subconjunto izquierdo puede ser de un punto, en cuyo caso, se toma como distancia mínima la del subconjunto derecho
                dminima = dder;
                solucion = solucionDer;
            } else{
                if(dizq < dder){
                    dminima = dizq;
                    solucion = solucionIzq;
                } else{
                    dminima = dder;
                    solucion = solucionDer;
                }
            }
            boolean b = false; //una vez tenemos la distancia mínima de la parte izquierda y la derecha, se comprueba si alguna distancia en la frontera es menor
            int i = 0;
            double xminima = puntos[mitad].getX() - dminima;
            if(xminima < 0){xminima = 0;}
            double xmaxima = puntos[mitad].getX() + dminima;
            while(b == false && i < puntos.length){
                if(puntos[i].getX() >= xminima){
                    b = true;
                } else{i++;}
            }
            b = false;
            while(b == false && i < puntos.length){
                int j = i + 1;
                while(b == false && j < puntos.length){
                    if(puntos[j].getX() <= xmaxima){
                        if(Punto.distancia(puntos[i], puntos[j]) < dminima){
                            solucion[0] = puntos[i];
                            solucion[1] = puntos[j];
                        }
                        j++;
                    } else{b = true;}
                }
                i++;
            }
        } else if(puntos.length == 2){ //conjunto de 2 puntos
            solucion[0] = puntos[0];
            solucion[1] = puntos[1];
        } else{ //conjunto de 1 punto
            solucion[0] = puntos[0];
            solucion[1] = puntos[0];
        }
        return solucion;
    }
    public static void main(String[] args) {
        Punto[] puntos;
        
    }
}
