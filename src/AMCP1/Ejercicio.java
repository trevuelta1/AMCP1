package AMCP1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

    public static int pt = 0;
    public static int ct = 0;
    public static int vuni = 0;
    public static int vbi = 0;

    public Punto[] leeFichero() throws FileNotFoundException, IOException, Exception {
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
        while (b == false) {
            line = br.readLine();
            if ("NODE_COORD_SECTION".equals(line)) {
                b = true;
            }
        }
        if (b == true) {
            b = false;
            while (b == false) {
                line = br.readLine();
                if ("EOF".equals(line)) {
                    b = true;
                } else {
                    String[] datos = line.split(" ");
                    int id = Integer.parseInt(datos[0].trim());
                    double x = Double.parseDouble(datos[1].trim());
                    double y = Double.parseDouble(datos[2].trim());
                    Punto p = new Punto(x, y);
                    p.setId(id);
                    puntos.add(p);
                }
            }
        } else {
            throw new Exception("Formato de fichero incorrecto.");
        }
        if (puntos.size() >= 2) {
            return puntos.toArray(new Punto[puntos.size()]);
        } else {
            throw new Exception("El fichero no tiene puntos suficientes.");
        }
    }

    public void escribeFicheroTSP(Punto[] puntos) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("files\\puntostest" + pt + ".tsp");
            fw.write("NAME: puntostest" + pt + "\n");
            fw.write("TYPE: TSP\n");
            fw.write("COMMENT: Example file with random points\n");
            fw.write("DIMENSION: " + puntos.length + "\n");
            fw.write("EDGE_WEIGHT_TYPE: EUC_2D\n");
            fw.write("NODE_COORD_SECTION\n");
            for (int i = 0; i < puntos.length; i++) {
                fw.write(puntos[i].getId() + " " + puntos[i].getX() + " " + puntos[i].getY() + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void escribeFicheroTOUR(SolucionCiudades sol) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("files\\soluCiudades" + ct + ".opt.tour");
            fw.write("NAME: soluCiudades" + ct + ".opt\n");
            fw.write("TYPE: TOUR\n");
            fw.write("DIMENSION: " + sol.getDistancias().length + "\n");
            fw.write("SOLUTION: " + sol.getDistanciaTotal() + "\n");
            fw.write("TOUR_SECTION\n");
            for (int i = 0; i < sol.getDistancias().length; i++) {
                fw.write(sol.getDistancias()[i] + " - " + sol.getCiudades()[i].getCoordenadas().getId() + ", " + sol.getCiudades()[i + 1].getCoordenadas().getId() + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int particionX(Punto vector[], int izquierda, int derecha) {
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
            } else {
                Punto temporal = vector[izquierda];
                vector[izquierda] = vector[derecha];
                vector[derecha] = temporal;
                izquierda++;
                derecha--;
            }
        }
    }

    private void quicksortX(Punto vector[], int izquierda, int derecha) {
        if (izquierda < derecha) {
            int indiceParticion = particionX(vector, izquierda, derecha);
            quicksortX(vector, izquierda, indiceParticion);
            quicksortX(vector, indiceParticion + 1, derecha);
        }
    }

    public void quicksortX(Punto vector[]) {
        quicksortX(vector, 0, vector.length - 1);
    }

    private int particionY(Punto vector[], int izquierda, int derecha) {
        double pivote = vector[izquierda].getY();
        // Ciclo infinito
        while (true) {
            while (vector[izquierda].getY() < pivote) {
                izquierda++;
            }
            while (vector[derecha].getY() > pivote) {
                derecha--;
            }
            if (izquierda >= derecha) {
                return derecha;
            } else {
                Punto temporal = vector[izquierda];
                vector[izquierda] = vector[derecha];
                vector[derecha] = temporal;
                izquierda++;
                derecha--;
            }
        }
    }

    private void quicksortY(Punto vector[], int izquierda, int derecha) {
        if (izquierda < derecha) {
            int indiceParticion = particionY(vector, izquierda, derecha);
            quicksortY(vector, izquierda, indiceParticion);
            quicksortY(vector, indiceParticion + 1, derecha);
        }
    }

    public void quicksortY(Punto vector[]) {
        quicksortY(vector, 0, vector.length - 1);
    }

    public Punto[] busquedaExhaustiva(Punto[] puntos) {
        Punto[] solucion = new Punto[2];
        solucion[0] = puntos[0];
        solucion[1] = puntos[1];
        double dminima = Punto.distancia(puntos[0], puntos[1]);
        for (int i = 0; i < puntos.length; i++) {
            for (int j = i + 1; j < puntos.length; j++) {
                double distancia = Punto.distancia(puntos[i], puntos[j]);
                if (distancia < dminima) {
                    dminima = distancia;
                    solucion[0] = puntos[i];
                    solucion[1] = puntos[j];
                }
            }
        }
        return solucion;
    }

    public Punto[] busquedaPoda(Punto[] puntos) {
        Punto[] solucion = new Punto[2];
        quicksortX(puntos);
        solucion[0] = puntos[0];
        solucion[1] = puntos[1];
        double dminima = Punto.distancia(puntos[0], puntos[1]);
        int i = 0;
        while (i < puntos.length) {
            int j = i + 1;
            boolean b = false;
            while (b == false && j < puntos.length) {
                double dist = puntos[i].getX() - puntos[j].getX();
                if (dist < 0) {
                    dist = dist * -1;
                }
                if (dist < dminima) {
                    double distancia = Punto.distancia(puntos[i], puntos[j]);
                    if (distancia < dminima) {
                        dminima = distancia;
                        solucion[0] = puntos[i];
                        solucion[1] = puntos[j];
                    }
                    j++;
                } else {
                    b = true;
                }
            }
            i++;
        }
        return solucion;
    }

    public Punto[] divideYvenceras(Punto[] puntos, int iz, int de) {
        Punto[] solucion = new Punto[2];
        if (de - iz + 1 > 3) { //conjunto de más de 2 puntos
            int mitad = iz + ((de - iz + 1) / 2); //divide en 2 subconjuntos
            Punto[] solucionIzq = divideYvenceras(puntos, iz, mitad - 1); //vuelve a resolver recursivamente hasta que quedan conjuntos de 2 ó 1 puntos
            Punto[] solucionDer = divideYvenceras(puntos, mitad, de);
            double dizq = Punto.distancia(solucionIzq[0], solucionIzq[1]);
            double dder = Punto.distancia(solucionDer[0], solucionDer[1]);
            double dminima; //cálculo de distancia mínima
            if (dizq < dder) {
                dminima = dizq;
                solucion = solucionIzq;
            } else {
                dminima = dder;
                solucion = solucionDer;
            }
            boolean b = false; //una vez tenemos la distancia mínima de la parte izquierda y la derecha, se comprueba si alguna distancia en la frontera es menor
            int i = iz;
            double xminima = puntos[mitad].getX() - dminima;
            if (xminima < 0) {
                xminima = 0;
            }
            double xmaxima = puntos[mitad].getX() + dminima;
            while (b == false && i <= de) {
                if (puntos[i].getX() <= xmaxima && puntos[i].getX() >= xminima) {
                    int j = i + 1;
                    while (b == false && j <= de) {
                        if (puntos[j].getX() <= xmaxima) {
                            double distancia = Punto.distancia(puntos[i], puntos[j]);
                            if (distancia < dminima) {
                                dminima = distancia;
                                solucion[0] = puntos[i];
                                solucion[1] = puntos[j];
                            }
                            j++;
                        } else {
                            b = true;
                        }
                    }
                    b = false;
                    i++;
                } else if(puntos[i].getX() < xminima){
                    i++;
                } else{
                    b = true;
                }
            }
        } else if (de - iz + 1 == 2) { //conjunto de 2 puntos
            solucion[0] = puntos[iz];
            solucion[1] = puntos[de];
        } else if (de - iz + 1 == 3) { //conjunto de 3 puntos
            double dist1 = Punto.distancia(puntos[iz], puntos[iz + 1]);
            double dist2 = Punto.distancia(puntos[iz + 1], puntos[de]);
            double dist3 = Punto.distancia(puntos[iz], puntos[de]);
            if (dist1 < dist2 && dist1 < dist3) {
                solucion[0] = puntos[iz];
                solucion[1] = puntos[iz + 1];
            } else if (dist2 < dist3) {
                solucion[0] = puntos[iz + 1];
                solucion[1] = puntos[de];
            } else {
                solucion[0] = puntos[iz];
                solucion[1] = puntos[de];
            }
        }
        return solucion;
    }

    public Punto[] divideYvencerasMejora(Punto[] puntos, int iz, int de) {
        Punto[] solucion = new Punto[2];
        quicksortX(puntos);
        if (de - iz + 1 > 3) { //conjunto de más de 2 puntos
            int mitad = iz + ((de - iz + 1) / 2); //divide en 2 subconjuntos
            Punto[] solucionIzq = divideYvenceras(puntos, iz, mitad - 1); //vuelve a resolver recursivamente hasta que quedan conjuntos de 2 ó 1 puntos
            Punto[] solucionDer = divideYvenceras(puntos, mitad, de);
            double dizq = Punto.distancia(solucionIzq[0], solucionIzq[1]);
            double dder = Punto.distancia(solucionDer[0], solucionDer[1]);
            double dminima; //cálculo de distancia mínima
            if (dizq < dder) {
                dminima = dizq;
                solucion = solucionIzq;
            } else {
                dminima = dder;
                solucion = solucionDer;
            }
            boolean b = false; //una vez tenemos la distancia mínima de la parte izquierda y la derecha, se comprueba si alguna distancia en la frontera es menor
            int i = iz;
            double xminima = puntos[mitad].getX() - dminima;
            if (xminima < 0) {
                xminima = 0;
            }
            double xmaxima = puntos[mitad].getX() + dminima;
            while (b == false && i <= de) {
                if (puntos[i].getX() >= xminima) {
                    b = true;
                } else {
                    i++;
                }
            }
            b = false;
            int j = i;
            while (b == false && j <= de) {
                if (puntos[j].getX() <= xmaxima) {
                    j++;
                } else {
                    b = true;
                }
            }
            j--;
            quicksortY(puntos, i, j);
            for(int k = i; k < j && k < puntos.length; k++){
                for(int y = k + 1; y < k + 12 && y < puntos.length; y++){
                    double distancia = Punto.distancia(puntos[k], puntos[y]);
                    if(distancia < dminima){
                        dminima = distancia;
                        solucion[0] = puntos[k];
                        solucion[1] = puntos[y];
                    }
                }
            }
        } else if (de - iz + 1 == 2) { //conjunto de 2 puntos
            solucion[0] = puntos[iz];
            solucion[1] = puntos[de];
        } else if (de - iz + 1 == 3) { //conjunto de 3 puntos
            double dist1 = Punto.distancia(puntos[iz], puntos[iz + 1]);
            double dist2 = Punto.distancia(puntos[iz + 1], puntos[de]);
            double dist3 = Punto.distancia(puntos[iz], puntos[de]);
            if (dist1 < dist2 && dist1 < dist3) {
                solucion[0] = puntos[iz];
                solucion[1] = puntos[iz + 1];
            } else if (dist2 < dist3) {
                solucion[0] = puntos[iz + 1];
                solucion[1] = puntos[de];
            } else {
                solucion[0] = puntos[iz];
                solucion[1] = puntos[de];
            }
        }
        return solucion;
    }

    public Ciudad ciudadMasCercanaSinVisitar(Ciudad inicio, Ciudad[] ciudades, boolean visitar) throws Exception {
        double dmin = Double.MAX_VALUE;
        int indice = -1;
        for (int i = 0; i < ciudades.length; i++) {
            if (ciudades[i].getCoordenadas().getId() != inicio.getCoordenadas().getId()) {
                if (ciudades[i].getVisitado() == false) {
                    double distancia = Punto.distancia(ciudades[i].getCoordenadas(), inicio.getCoordenadas());
                    if (distancia < dmin) {
                        dmin = distancia;
                        indice = i;
                    }
                }
            }
        }
        if (indice != -1) {
            if (visitar == true) {
                ciudades[indice].setVisitado(true);
            }
            return ciudades[indice];
        } else {
            throw new Exception("Todas las ciudades han sido visitadas.");
        }
    }

    public SolucionCiudades vorazCiudades(Ciudad[] ciudades, Ciudad inicio) throws Exception {
        SolucionCiudades solucion = new SolucionCiudades(ciudades);
        solucion.addCiudad(inicio);
        for (int i = 0; i < ciudades.length - 1; i++) {
            Ciudad c = ciudadMasCercanaSinVisitar(inicio, ciudades, true);
            solucion.addCiudad(c);
            solucion.addDistancia(Punto.distancia(inicio.getCoordenadas(), c.getCoordenadas()));
            inicio = c;
        }
        solucion.addCiudad(solucion.getCiudades()[0]);
        solucion.addDistancia(Punto.distancia(solucion.getCiudades()[0].getCoordenadas(), inicio.getCoordenadas()));
        solucion.actualizarDistanciaTotal();
        for (int i = 0; i < ciudades.length; i++) {
            ciudades[i].setVisitado(false);
        }
        return solucion;
    }

    public SolucionCiudades vorazDoble(Ciudad[] ciudades, Ciudad inicio) throws Exception {
        SolucionCiudades solucion = new SolucionCiudades(ciudades);
        solucion.addCiudad(inicio);
        Ciudad[] camino = new Ciudad[ciudades.length * 2 + 2]; //tabla doble de ciudades
        int indizq = ciudades.length; //índice izquierdo
        int indder = ciudades.length + 1; //índice derecho
        Ciudad iz = inicio; //extremo izquierdo en la tabla doble de ciudades
        Ciudad de = ciudadMasCercanaSinVisitar(inicio, ciudades, true); //extremo derecho en la tabla doble de ciudades
        camino[indizq] = iz;
        camino[indder] = de;
        for (int i = 0; i < ciudades.length - 2; i++) {
            Ciudad diz = ciudadMasCercanaSinVisitar(iz, ciudades, false); //ciudad más cercana al extremo izquierdo
            Ciudad dde = ciudadMasCercanaSinVisitar(de, ciudades, false); //ciudad más cercana al extremo derecho
            if (Punto.distancia(diz.getCoordenadas(), iz.getCoordenadas()) < Punto.distancia(dde.getCoordenadas(), de.getCoordenadas())) { //en función de cuál de las anteriores sea más cercana, se modifica camino y se reestablece ciudades con la copia correcta
                indizq--;
                camino[indizq] = diz;
                iz = diz;
                boolean f = false;
                int j = 0;
                while (f == false && j < ciudades.length) {
                    if (ciudades[j].getCoordenadas().getId() == diz.getCoordenadas().getId()) {
                        ciudades[j].setVisitado(true);
                        f = true;
                    } else{j++;}
                }
            } else {
                indder++;
                camino[indder] = dde;
                de = dde;
                boolean f = false;
                int j = 0;
                while (f == false && j < ciudades.length) {
                    if (ciudades[j].getCoordenadas().getId() == dde.getCoordenadas().getId()) {
                        ciudades[j].setVisitado(true);
                        f = true;
                    } else{j++;}
                }
            }
        }
        boolean b = false; //a partir de aquí se prepara la solución
        int i = ciudades.length + 1;
        int cantidad = 1;
        while (b == false) {
            solucion.addCiudad(camino[i]);
            solucion.addDistancia(Punto.distancia(camino[i].getCoordenadas(), camino[i - 1].getCoordenadas()));
            cantidad++;
            if (camino[i + 1] == null) {
                b = true;
            } else {
                i++;
            }
        }
        i = 0;
        while (camino[i] == null) {
            i++;
        }
        for (int j = i; j < ciudades.length; j++) {
            solucion.addCiudad(camino[j]);
            double distancia = Punto.distancia(solucion.getCiudades()[cantidad].getCoordenadas(), solucion.getCiudades()[cantidad - 1].getCoordenadas());
            solucion.addDistancia(distancia);
            cantidad++;
        }
        solucion.addCiudad(solucion.getCiudades()[0]);
        solucion.addDistancia(Punto.distancia(solucion.getCiudades()[0].getCoordenadas(), solucion.getCiudades()[ciudades.length - 1].getCoordenadas()));
        solucion.actualizarDistanciaTotal();
        for (int k = 0; k < ciudades.length; k++) {
            ciudades[k].setVisitado(false);
        }
        return solucion;
    }

    public int menuPrincipal() {
        int a = 0;
        System.out.println("Indique si desea trabajar con conjuntos de puntos o ciudades:");
        System.out.println("");
        System.out.println("1. Conjunto de puntos (estrategias exh, exh con poda, dYv, dYv con mejora).");
        System.out.println("2. Conjunto de ciudades (estrategias voraces unilateral y bilateral).");
        System.out.println("3. Salir.");
        System.out.println("");
        do {
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while (a < 1 || a > 3);
        return a;
    }

    public int menuPuntos() {
        int a = 0;
        System.out.println("Indique la opcion que desee:");
        System.out.println("");
        System.out.println("1. Probar estrategia.");
        System.out.println("2. Comparar todas las estrategias.");
        System.out.println("3. Comparar todas las estrategias con todos los puntos en la misma vertical.");
        System.out.println("4. Salir.");
        System.out.println("");
        do {
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while (a < 1 || a > 4);
        return a;
    }

    public int menuCiudades() {
        int a = 0;
        System.out.println("Indique la opcion que desee:");
        System.out.println("");
        System.out.println("1. Probar estrategia.");
        System.out.println("2. Comparar estrategias.");
        System.out.println("3. Salir.");
        System.out.println("");
        do {
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while (a < 1 || a > 3);
        return a;
    }

    public void probarEstrategiaPuntos() {
        int a = 0;
        Punto[] puntos;
        System.out.println("Indique si desea generar un conjunto aleatorio o leer un conjunto desde fichero:");
        System.out.println("");
        System.out.println("1. Conjunto de puntos aleatorio.");
        System.out.println("2. Conjunto de puntos desde fichero.");
        System.out.println("3. Salir.");
        System.out.println("");
        do {
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while (a < 1 || a > 3);
        switch (a) {
            case 1: {
                Random x = new Random();
                x.setSeed(System.currentTimeMillis());
                Random y = new Random();
                y.setSeed(System.currentTimeMillis() + 1);
                int talla;
                Scanner sc = new Scanner(System.in);
                do {
                    System.out.println("Indique cuantos puntos debe tener el conjunto (el valor debe ser mayor o igual a 2): ");
                    talla = sc.nextInt();
                } while (talla < 2);
                puntos = new Punto[talla];
                for (int i = 0; i < talla; i++) {
                    puntos[i] = new Punto(x.nextDouble() * 1000, y.nextDouble() * 1000);
                }
                escribeFicheroTSP(puntos);
                pt++;
                int opt;
                do {
                    System.out.println("Indique la estrategia que desea probar: ");
                    System.out.println("");
                    System.out.println("1. Busqueda exhaustiva.");
                    System.out.println("2. Busqueda con poda.");
                    System.out.println("3. Divide y Venceras.");
                    System.out.println("4. Divide y Venceras con mejora.");
                    System.out.println("");
                    System.out.println("Elige una opcion: ");
                    opt = sc.nextInt();
                } while (opt < 1 || opt > 4);
                switch (opt) {
                    case 1: {
                        Punto[] solucion = busquedaExhaustiva(puntos);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    case 2: {
                        Punto[] solucion = busquedaPoda(puntos);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    case 3: {
                        quicksortX(puntos);
                        Punto[] solucion = divideYvenceras(puntos, 0, puntos.length - 1);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    case 4: {
                        Punto[] solucion = divideYvencerasMejora(puntos, 0, puntos.length - 1);
                        System.out.println("");
                        System.out.println(solucion[0].getPunto());
                        System.out.println(solucion[1].getPunto());
                        System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                        System.out.println("");
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 2: {
                try {
                    puntos = leeFichero();
                    Scanner sc = new Scanner(System.in);
                    int opt;
                    do {
                        System.out.println("Indique la estrategia que desea probar: ");
                        System.out.println("");
                        System.out.println("1. Busqueda exhaustiva.");
                        System.out.println("2. Busqueda con poda.");
                        System.out.println("3. Divide y Venceras.");
                        System.out.println("4. Divide y Venceras con mejora.");
                        System.out.println("");
                        System.out.println("Elige una opcion: ");
                        opt = sc.nextInt();
                    } while (opt < 1 || opt > 4);
                    switch (opt) {
                        case 1: {
                            Punto[] solucion = busquedaExhaustiva(puntos);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        case 2: {
                            Punto[] solucion = busquedaPoda(puntos);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        case 3: {
                            quicksortX(puntos);
                            Punto[] solucion = divideYvenceras(puntos, 0, puntos.length - 1);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        case 4: {
                            Punto[] solucion = divideYvencerasMejora(puntos, 0, puntos.length - 1);
                            System.out.println("");
                            System.out.println(solucion[0].getPunto());
                            System.out.println(solucion[1].getPunto());
                            System.out.println("Distancia: " + Punto.distancia(solucion[0], solucion[1]));
                            System.out.println("");
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            default: {
                break;
            }
        }
    }

    public void probarEstrategiaCiudades() {
        int a = 0;
        Punto[] puntos;
        Ciudad[] ciudades;
        System.out.println("Indique si desea generar un conjunto aleatorio o leer un conjunto desde fichero:");
        System.out.println("");
        System.out.println("1. Conjunto de ciudades aleatorio.");
        System.out.println("2. Conjunto de ciudades desde fichero.");
        System.out.println("3. Salir.");
        System.out.println("");
        do {
            System.out.println("Elija una opcion: ");
            Scanner sc = new Scanner(System.in);
            a = sc.nextInt();
        } while (a < 1 || a > 3);
        switch (a) {
            case 1: {
                Random x = new Random();
                x.setSeed(System.currentTimeMillis());
                Random y = new Random();
                y.setSeed(System.currentTimeMillis() + 1);
                int talla;
                Scanner sc = new Scanner(System.in);
                do {
                    System.out.println("Indique cuantos puntos debe tener el conjunto (el valor debe ser mayor o igual a 2): ");
                    talla = sc.nextInt();
                } while (talla < 2);
                puntos = new Punto[talla];
                ciudades = new Ciudad[talla];
                for (int i = 0; i < talla; i++) {
                    puntos[i] = new Punto(x.nextDouble() * 1000, y.nextDouble() * 1000);
                    ciudades[i] = new Ciudad(puntos[i]);
                }
                escribeFicheroTSP(puntos);
                pt++;
                int opt;
                do {
                    System.out.println("Indique la estrategia que desea probar: ");
                    System.out.println("");
                    System.out.println("1. Voraz unidireccional.");
                    System.out.println("2. Voraz bidireccional.");
                    System.out.println("");
                    System.out.println("Elige una opcion: ");
                    opt = sc.nextInt();
                } while (opt < 1 || opt > 2);
                switch (opt) {
                    case 1: {
                        try {
                            ciudades[0].setVisitado(true);
                            SolucionCiudades solucion = vorazCiudades(ciudades, ciudades[0]);
                            solucion.getSolucion();
                            escribeFicheroTOUR(solucion);
                            ct++;
                            break;
                        } catch (Exception ex) {
                            Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    case 2: {
                        try {
                            ciudades[0].setVisitado(true);
                            SolucionCiudades solucion = vorazDoble(ciudades, ciudades[0]);
                            solucion.getSolucion();
                            escribeFicheroTOUR(solucion);
                            ct++;
                            break;
                        } catch (Exception ex) {
                            Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 2: {
                try {
                    Scanner sc = new Scanner(System.in);
                    puntos = leeFichero();
                    ciudades = new Ciudad[puntos.length];
                    for (int i = 0; i < puntos.length; i++) {
                        ciudades[i] = new Ciudad(puntos[i]);
                    }
                    int opt;
                    do {
                        System.out.println("Indique la estrategia que desea probar: ");
                        System.out.println("");
                        System.out.println("1. Voraz unidireccional.");
                        System.out.println("2. Voraz bidireccional.");
                        System.out.println("");
                        System.out.println("Elige una opcion: ");
                        opt = sc.nextInt();
                    } while (opt < 1 || opt > 2);
                    switch (opt) {
                        case 1: {
                            try {
                                ciudades[0].setVisitado(true);
                                SolucionCiudades solucion = vorazCiudades(ciudades, ciudades[0]);
                                solucion.getSolucion();
                                escribeFicheroTOUR(solucion);
                                ct++;
                                break;
                            } catch (Exception ex) {
                                Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        case 2: {
                            try {
                                ciudades[0].setVisitado(true);
                                SolucionCiudades solucion = vorazDoble(ciudades, ciudades[0]);
                                solucion.getSolucion();
                                escribeFicheroTOUR(solucion);
                                ct++;
                                break;
                            } catch (Exception ex) {
                                Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            default: {
                break;
            }
        }
    }

    public void compararEstrategiasPuntos() {
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
        for (int a = 0; a < 10; a++) {
            for (int i = 0; i < 10; i++) {
                puntos = new Punto[size];
                for (int j = 0; j < size; j++) {
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
                quicksortX(puntos);
                divideYvenceras(puntos, 0, puntos.length - 1);
                tdyv = System.currentTimeMillis() - tdyv;
                totaldyv = totaldyv + tdyv;
                long tdyvmej = System.currentTimeMillis();
                divideYvencerasMejora(puntos, 0, puntos.length - 1);
                tdyvmej = System.currentTimeMillis() - tdyvmej;
                totaldyvmej = totaldyvmej + tdyvmej;
            }
            System.out.println("\t" + size + "\t" + totalexh / 10 + "ms\t\t" + totalpod / 10 + "ms\t\t" + totaldyv / 10 + "ms\t\t" + totaldyvmej / 10 + "ms");
            size = size + 500;
            totalexh = 0;
            totalpod = 0;
            totaldyv = 0;
            totaldyvmej = 0;
        }
    }

    public void compararEstrategiasMismaVertical() {
        int size = 500;
        Punto[] puntos;
        Random y = new Random();
        y.setSeed(System.currentTimeMillis());
        long totalexh = 0, totalpod = 0, totaldyv = 0, totaldyvmej = 0;
        System.out.println("\tTalla\tExhaustivo\tExhaustivoPoda\tDivideYVenceras\tDyVMejora");
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("");
        for (int a = 0; a < 10; a++) {
            for (int i = 0; i < 10; i++) {
                puntos = new Punto[size];
                for (int j = 0; j < size; j++) {
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
                quicksortX(puntos);
                divideYvenceras(puntos, 0, puntos.length - 1);
                tdyv = System.currentTimeMillis() - tdyv;
                totaldyv = totaldyv + tdyv;
                long tdyvmej = System.currentTimeMillis();
                divideYvencerasMejora(puntos, 0, puntos.length - 1);
                tdyvmej = System.currentTimeMillis() - tdyvmej;
                totaldyvmej = totaldyvmej + tdyvmej;
            }
            System.out.println("\t" + size + "\t" + totalexh / 10 + "ms\t\t" + totalpod / 10 + "ms\t\t" + totaldyv / 10 + "ms\t\t" + totaldyvmej / 10 + "ms");
            size = size + 500;
            totalexh = 0;
            totalpod = 0;
            totaldyv = 0;
            totaldyvmej = 0;
        }
    }

    public void compararEstrategiasCiudades() {
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
        for (int a = 0; a < 10; a++) {
            for (int i = 0; i < 100; i++) {
                try {
                    puntos = new Punto[size];
                    ciudades = new Ciudad[size];
                    for (int j = 0; j < size; j++) {
                        puntos[j] = new Punto(x.nextDouble() * 1000, y.nextDouble() * 1000);
                        ciudades[j] = new Ciudad(puntos[j]);
                    }
                    ciudades[0].setVisitado(true);
                    long tuni = System.currentTimeMillis();
                    SolucionCiudades soluni = vorazCiudades(ciudades, ciudades[0]);
                    tuni = System.currentTimeMillis() - tuni;
                    totaluni = totaluni + tuni;
                    ciudades[0].setVisitado(true);
                    long tbi = System.currentTimeMillis();
                    SolucionCiudades solbi = vorazDoble(ciudades, ciudades[0]);
                    tbi = System.currentTimeMillis() - tbi;
                    totalbi = totalbi + tbi;
                    if(soluni.getDistanciaTotal() < solbi.getDistanciaTotal()){
                        vuni++;
                    } else if(solbi.getDistanciaTotal() < soluni.getDistanciaTotal()){
                        vbi++;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Ejercicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("\t" + size + "\t" + totaluni / 100 + "ms\t\t\t" + totalbi / 100 + "ms");
            size = size + 500;
            totaluni = 0;
            totalbi = 0;
        }
        System.out.println("");
        System.out.println("------------------------------------------------------------------");
        System.out.println("");
        System.out.println("La mejor solución ha sido obtenida por:");
        double puni = (((double)vuni / ((double)vuni + (double)vbi)) * 100);
        double pbi = (((double)vbi / ((double)vuni + (double)vbi)) * 100);
        System.out.println("Voraz unidireccional: " + vuni + " veces (" + puni + "%).");
        System.out.println("Voraz bidireccional: " + vbi + " veces (" + pbi + "%).");
        System.out.println("");
        vuni = 0;
        vbi = 0;
    }

    public static void main(String[] args) {
        Ejercicio ej = new Ejercicio();
        int menuprincipal;
        do {
            menuprincipal = ej.menuPrincipal();
            switch (menuprincipal) {
                case 1: {
                    int menupuntos = ej.menuPuntos();
                    switch (menupuntos) {
                        case 1: {
                            ej.probarEstrategiaPuntos();
                            break;
                        }
                        case 2: {
                            ej.compararEstrategiasPuntos();
                            break;
                        }
                        case 3: {
                            ej.compararEstrategiasMismaVertical();
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    int menuciudades = ej.menuCiudades();
                    switch (menuciudades) {
                        case 1: {
                            ej.probarEstrategiaCiudades();
                            break;
                        }
                        case 2: {
                            ej.compararEstrategiasCiudades();
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        } while (menuprincipal != 3);
    }
}
