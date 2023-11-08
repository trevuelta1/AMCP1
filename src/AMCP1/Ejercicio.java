package AMCP1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO1
 */

public class Ejercicio {
    
    public Punto[] leeFichero() throws FileNotFoundException, IOException, Exception{
        ArrayList<Punto> puntos = new ArrayList();
        FileReader fr;
        String url;
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe la ruta del fichero que deseas utilizar: ");
        url = sc.next();
        fr = new FileReader(url);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        boolean b = false;
        while(b == false){
            line = br.readLine();
            if("NODE_COORD_SECTION".equals(line)){
                b = true;
            }
        }
        if(b == true){
            b = false;
            while(b == false){
                line = br.readLine();
                if("EOF".equals(line)){
                    b = true;
                } else{
                    String[] datos = line.split(" ");
                    int id = Integer.valueOf(datos[0].trim());
                    double x = Double.valueOf(datos[1].trim());
                    double y = Double.valueOf(datos[2].trim());
                    Punto p = new Punto(x, y);
                    p.setId(id);
                    puntos.add(p);
                }
            }
        } else{
            throw new Exception("Formato de fichero incorrecto.");
        }
        if(puntos.size() >= 2){
            return puntos.toArray(new Punto[puntos.size()]);
        } else{
            throw new Exception("El fichero no tiene puntos suficientes."); 
        }
    }
    
    
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
    
    
    public Punto[] divideYvencerasMejora(Punto[] puntos){
        return null;
    }
    
    
    public Ciudad ciudadMasCercanaSinVisitar(Ciudad inicio, Ciudad[] ciudades) throws Exception{
        double dmin = Double.MAX_VALUE;
        int indice = -1;
        for(int i = 0; i < ciudades.length; i++){
            if(ciudades[i].getCoordenadas().getId() != inicio.getCoordenadas().getId()){
                if(ciudades[i].getVisitado() == false){
                    double distancia = Punto.distancia(ciudades[i].getCoordenadas(), inicio.getCoordenadas());
                    if(distancia < dmin){
                        dmin = distancia;
                        indice = i;
                    }
                }
            }
        }
        if(indice != -1){
            ciudades[indice].setVisitado(true);
            return ciudades[indice];
        } else{
            throw new Exception("Todas las ciudades han sido visitadas.");
        }
    }
    
    
    public SolucionCiudades vorazCiudades(Ciudad[] ciudades, Ciudad inicio) throws Exception{
        SolucionCiudades solucion = new SolucionCiudades(ciudades);
        solucion.addCiudad(inicio);
        for(int i = 0; i < ciudades.length - 1; i++){
            Ciudad c = ciudadMasCercanaSinVisitar(inicio, ciudades);
            solucion.addCiudad(c);
            solucion.addDistancia(Punto.distancia(inicio.getCoordenadas(), c.getCoordenadas()));
            inicio = c;
        }
        solucion.addCiudad(solucion.getCiudades()[0]);
        solucion.addDistancia(Punto.distancia(solucion.getCiudades()[0].getCoordenadas(), inicio.getCoordenadas()));
        solucion.actualizarDistanciaTotal();
        for(int i = 0; i < ciudades.length; i++){
            ciudades[i].setVisitado(false);
        }
        return solucion;
    }
    
    
    public SolucionCiudades vorazDoble(Ciudad[] ciudades, Ciudad inicio) throws Exception{
        return null;
    }
    
    
    public int menuPrincipal(){
        int a = 0;
        System.out.println("Indique si desea trabajar con conjuntos de puntos o ciudades:");
        System.out.println("");
        System.out.println("1. Conjunto de puntos (estrategias exh, exh con poda, dYv, dYv con mejora).");
        System.out.println("2. Conjunto de ciudades (estrategias voraces unilateral y bilateral).");
        System.out.println("3. Salir.");
        System.out.println("");
        do{
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while(a < 1 || a > 3);
        return a;
    }
    
    
    public int menuPuntos(){
        int a = 0;
        System.out.println("Indique la opcion que desee:");
        System.out.println("");
        System.out.println("1. Probar estrategia.");
        System.out.println("2. Comparar todas las estrategias.");
        System.out.println("3. Comparar todas las estrategias con todos los puntos en la misma vertical.");
        System.out.println("4. Salir.");
        System.out.println("");
        do{
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while(a < 1 || a > 4);
        return a;
    }
    
    
    public int menuCiudades(){
        int a = 0;
        System.out.println("Indique la opcion que desee:");
        System.out.println("");
        System.out.println("1. Probar estrategia.");
        System.out.println("2. Comparar estrategias.");
        System.out.println("3. Salir.");
        System.out.println("");
        do{
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while(a < 1 || a > 3);
        return a;
    }
    
    
    public void probarEstrategiaPuntos(){
        int a = 0;
        Punto[] puntos;
        System.out.println("Indique si desea generar un conjunto aleatorio o leer un conjunto desde fichero:");
        System.out.println("");
        System.out.println("1. Conjunto de puntos aleatorio.");
        System.out.println("2. Conjunto de puntos desde fichero.");
        System.out.println("3. Salir.");
        System.out.println("");
        do{
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while(a < 1 || a > 3);
        switch(a){
            case 1:{
                Random x = new Random();
                x.setSeed(System.currentTimeMillis());
                Random y = new Random();
                y.setSeed(System.currentTimeMillis() + 1);
                int talla;
                Scanner sc = new Scanner(System.in);
                do{
                    System.out.println("Indique cuantos puntos debe tener el conjunto (el valor debe ser mayor o igual a 2): ");
                    talla = sc.nextInt();
                } while(talla < 2);
                puntos = new Punto[talla];
                for(int i = 0; i < talla; i++){
                    puntos[i] = new Punto(x.nextDouble() * 1000, y.nextDouble() * 1000);
                }
                int opt;
                do{
                    System.out.println("Indique la estrategia que desea probar: ");
                    System.out.println("");
                    System.out.println("1. Busqueda exhaustiva.");
                    System.out.println("2. Busqueda con poda.");
                    System.out.println("3. Divide y Venceras.");
                    System.out.println("4. Divide y Venceras con mejora.");
                    System.out.println("");
                    System.out.println("Elige una opcion: ");
                    opt = sc.nextInt();
                } while(opt < 1 || opt > 4);
                switch(opt){
                    case 1:{
                        Punto[] solucion = busquedaExhaustiva(puntos);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    case 2:{
                        Punto[] solucion = busquedaPoda(puntos);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    case 3:{
                        Punto[] solucion = divideYvenceras(puntos);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    case 4:{
                        Punto[] solucion = divideYvencerasMejora(puntos);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    default:{
                        break;
                    }
                }
                break;
            }
            case 2:{
                try {
                    puntos = leeFichero();
                    Scanner sc = new Scanner(System.in);
                    int opt;
                    do{
                        System.out.println("Indique la estrategia que desea probar: ");
                        System.out.println("");
                        System.out.println("1. Busqueda exhaustiva.");
                        System.out.println("2. Busqueda con poda.");
                        System.out.println("3. Divide y Venceras.");
                        System.out.println("4. Divide y Venceras con mejora.");
                        System.out.println("");
                        System.out.println("Elige una opcion: ");
                        opt = sc.nextInt();
                    } while(opt < 1 || opt > 4);
                    switch(opt){
                        case 1:{
                            Punto[] solucion = busquedaExhaustiva(puntos);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        case 2:{
                            Punto[] solucion = busquedaPoda(puntos);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        case 3:{
                            Punto[] solucion = divideYvenceras(puntos);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        case 4:{
                            Punto[] solucion = divideYvencerasMejora(puntos);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        default:{
                            break;
                        }
                    }
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            default:{
                break;
            }
        }
    }
    
    
    public void probarEstrategiaCiudades(){
        int a = 0;
        Punto[] puntos;
        Ciudad[] ciudades;
        System.out.println("Indique si desea generar un conjunto aleatorio o leer un conjunto desde fichero:");
        System.out.println("");
        System.out.println("1. Conjunto de ciudades aleatorio.");
        System.out.println("2. Conjunto de ciudades desde fichero.");
        System.out.println("3. Salir.");
        System.out.println("");
        do{
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while(a < 1 || a > 3);
        switch(a){
            case 1:{
                Random x = new Random();
                x.setSeed(System.currentTimeMillis());
                Random y = new Random();
                y.setSeed(System.currentTimeMillis() + 1);
                int talla;
                Scanner sc = new Scanner(System.in);
                do{
                    System.out.println("Indique cuantos puntos debe tener el conjunto (el valor debe ser mayor o igual a 2): ");
                    talla = sc.nextInt();
                } while(talla < 2);
                puntos = new Punto[talla];
                ciudades = new Ciudad[talla];
                for(int i = 0; i < talla; i++){
                    puntos[i] = new Punto(x.nextDouble() * 1000, y.nextDouble() * 1000);
                    ciudades[i] = new Ciudad(puntos[i]);
                }
                int opt;
                do{
                    System.out.println("Indique la estrategia que desea probar: ");
                    System.out.println("");
                    System.out.println("1. Voraz unidireccional.");
                    System.out.println("2. Voraz bidireccional.");
                    System.out.println("");
                    System.out.println("Elige una opcion: ");
                    opt = sc.nextInt();
                } while(opt < 1 || opt > 2);
                switch(opt){
                    case 1:{
                        try {
                            ciudades[0].setVisitado(true);
                            SolucionCiudades solucion = vorazCiudades(ciudades, ciudades[0]);
                            solucion.getSolucion();
                            break;
                        } catch (Exception ex) {
                            Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    case 2:{
                        try {
                            ciudades[0].setVisitado(true);
                            SolucionCiudades solucion = vorazDoble(ciudades, ciudades[0]);
                            solucion.getSolucion();
                            break;
                        } catch (Exception ex) {
                            Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    default:{
                        break;
                    }
                }
                break;
            }
            case 2:{
                try {
                    Scanner sc = new Scanner(System.in);
                    puntos = leeFichero();
                    ciudades = new Ciudad[puntos.length];
                    for(int i = 0; i < puntos.length; i++){
                        ciudades[i] = new Ciudad(puntos[i]);
                    }
                    int opt;
                    do{
                        System.out.println("Indique la estrategia que desea probar: ");
                        System.out.println("");
                        System.out.println("1. Voraz unidireccional.");
                        System.out.println("2. Voraz bidireccional.");
                        System.out.println("");
                        System.out.println("Elige una opcion: ");
                        opt = sc.nextInt();
                    } while(opt < 1 || opt > 2);
                    switch(opt){
                        case 1:{
                            try {
                                ciudades[0].setVisitado(true);
                                SolucionCiudades solucion = vorazCiudades(ciudades, ciudades[0]);
                                solucion.getSolucion();
                                break;
                            } catch (Exception ex) {
                                Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        case 2:{
                            try {
                                ciudades[0].setVisitado(true);
                                SolucionCiudades solucion = vorazDoble(ciudades, ciudades[0]);
                                solucion.getSolucion();
                                break;
                            } catch (Exception ex) {
                                Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        default:{
                            break;
                        }
                    }
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            default:{
                break;
            }
        }
    }
    
    
    public void compararEstrategiasPuntos(){
        int size = 500;
        Punto[] puntos;
        Random x = new Random();
        x.setSeed(System.currentTimeMillis());
        Random y = new Random();
        y.setSeed(System.currentTimeMillis() + 1);
        long totalexh = 0, totalpod = 0, totaldyv = 0, totaldyvmej = 0;
        System.out.println("\tTalla\tExhaustivo\tExhaustivoPoda\tDivideYVenceras\tDyVMejora");
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("");
        for(int a = 0; a < 10; a++){
            for(int i = 0; i < 10; i++){
                puntos = new Punto[size];
                for(int j = 0; j < size; j++){
                    puntos[j] = new Punto(x.nextDouble() * 1000, y.nextDouble() * 1000);
                }
                long texh = System.currentTimeMillis();
                busquedaExhaustiva(puntos);
                texh = System.currentTimeMillis() - texh;
                totalexh = totalexh + texh;
                long tpod = System.currentTimeMillis();
                busquedaPoda(puntos);
                tpod = System.currentTimeMillis() - tpod;
                totalpod = totalpod + tpod;
                long tdyv = System.currentTimeMillis();
                divideYvenceras(puntos);
                tdyv = System.currentTimeMillis() - tdyv;
                totaldyv = totaldyv + tdyv;
                long tdyvmej = System.currentTimeMillis();
                divideYvencerasMejora(puntos);
                tdyvmej = System.currentTimeMillis() - tdyvmej;
                totaldyvmej = totaldyvmej + tdyvmej;
            }
            System.out.println("\t" + size + "\t" + totalexh / 10 + "\t" + totalpod / 10 + "\t" + totaldyv / 10 + "\t" + totaldyvmej / 10);
            size = size + 500;
            totalexh = 0; totalpod = 0; totaldyv = 0; totaldyvmej = 0;
        }
    }
    
    
    public void compararEstrategiasMismaVertical(){
        int size = 500;
        Punto[] puntos;
        Random y = new Random();
        y.setSeed(System.currentTimeMillis());
        long totalexh = 0, totalpod = 0, totaldyv = 0, totaldyvmej = 0;
        System.out.println("\tTalla\tExhaustivo\tExhaustivoPoda\tDivideYVenceras\tDyVMejora");
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("");
        for(int a = 0; a < 10; a++){
            for(int i = 0; i < 10; i++){
                puntos = new Punto[size];
                for(int j = 0; j < size; j++){
                    puntos[j] = new Punto(50, y.nextDouble() * 1000);
                }
                long texh = System.currentTimeMillis();
                busquedaExhaustiva(puntos);
                texh = System.currentTimeMillis() - texh;
                totalexh = totalexh + texh;
                long tpod = System.currentTimeMillis();
                busquedaPoda(puntos);
                tpod = System.currentTimeMillis() - tpod;
                totalpod = totalpod + tpod;
                long tdyv = System.currentTimeMillis();
                divideYvenceras(puntos);
                tdyv = System.currentTimeMillis() - tdyv;
                totaldyv = totaldyv + tdyv;
                long tdyvmej = System.currentTimeMillis();
                divideYvencerasMejora(puntos);
                tdyvmej = System.currentTimeMillis() - tdyvmej;
                totaldyvmej = totaldyvmej + tdyvmej;
            }
            System.out.println("\t" + size + "\t" + totalexh / 10 + "\t" + totalpod / 10 + "\t" + totaldyv / 10 + "\t" + totaldyvmej / 10);
            size = size + 500;
            totalexh = 0; totalpod = 0; totaldyv = 0; totaldyvmej = 0;
        }
    }
    
    
    
    public void compararEstrategiasCiudades(){
        int size = 500;
        Punto[] puntos;
        Ciudad[] ciudades;
        Random x = new Random();
        x.setSeed(System.currentTimeMillis());
        Random y = new Random();
        y.setSeed(System.currentTimeMillis() + 1);
        long totaluni = 0, totalbi = 0;
        System.out.println("\tTalla\tVorazUnidireccional\tVorazBidireccional");
        System.out.println("");
        System.out.println("------------------------------------------------------------------");
        System.out.println("");
        for(int a = 0; a < 10; a++){
            for(int i = 0; i < 100; i++){
                try {
                    puntos = new Punto[size];
                    ciudades = new Ciudad[size];
                    for(int j = 0; j < size; j++){
                        puntos[j] = new Punto(x.nextDouble() * 1000, y.nextDouble() * 1000);
                        ciudades[j] = new Ciudad(puntos[j]);
                    }
                    ciudades[0].setVisitado(true);
                    long tuni = System.currentTimeMillis();
                    vorazCiudades(ciudades, ciudades[0]);
                    tuni = System.currentTimeMillis() - tuni;
                    totaluni = totaluni + tuni;
                    ciudades[0].setVisitado(true);
                    long tbi = System.currentTimeMillis();
                    vorazDoble(ciudades, ciudades[0]);
                    tbi = System.currentTimeMillis() - tbi;
                    totalbi = totalbi + tbi;
                } catch (Exception ex) {
                    Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("\t" + size + "\t" + totaluni / 100 + "\t" + totalbi / 100);
            size = size + 500;
            totaluni = 0; totalbi = 0;
        }
    }
    
    
    public static void main(String[] args) {
        Ejercicio ej = new Ejercicio();
        int menuprincipal;
        do{
            menuprincipal = ej.menuPrincipal();
            switch(menuprincipal){
                case 1:{
                    int menupuntos = ej.menuPuntos();
                    switch(menupuntos){
                        case 1:{
                            ej.probarEstrategiaPuntos();
                            break;
                        }
                        case 2:{
                            ej.compararEstrategiasPuntos();
                            break;
                        }
                        case 3:{
                            ej.compararEstrategiasMismaVertical();
                            break;
                        }
                        default:{
                            break;
                        }
                    }
                    break;
                }
                case 2:{
                    int menuciudades = ej.menuCiudades();
                    switch(menuciudades){
                        case 1:{
                            ej.probarEstrategiaCiudades();
                            break;
                        }
                        case 2:{
                            ej.compararEstrategiasCiudades();
                            break;
                        }
                        default:{
                            break;
                        }
                    }
                    break;
                }
                default:{
                    break;
                }
            }
        } while(menuprincipal != 3);
    }
}
