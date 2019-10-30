
package server;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import javax.swing.JFrame;

public class tablero{

    Sala servidorDelTablero;
    private int tablero[][] = new int[20][20];
    private Casilla tableroDePartida[][];
    int totalDeFilas;
    int totalDeColumnas;
    int totalDeBombas;
    int minasDescubiertas = 0;
    public boolean ganada = false;
    //public boolean terminoPartida = false;
    public String coordenadasAfectados = "";
    public ArrayList<Casilla> coordenadasDeLasMinas = new ArrayList<Casilla>();
    HashMap<String, Usuario> usuariosJugando;

    public tablero(int filasDelTablerox, int columnasDelTableroy, int minasTotales, Sala tuServidor) {
        servidorDelTablero = tuServidor;
        this.totalDeFilas = filasDelTablerox;
        this.totalDeColumnas = columnasDelTableroy;
        totalDeBombas = minasTotales;
        usuariosJugando = servidorDelTablero.usuariosJugando;       
        tableroDePartida = new Casilla[totalDeFilas][totalDeColumnas];
        //setLayout(new GridLayout(totalDeColumnas, totalDeFilas, 0, 0));
        for (int fila = 0; fila < totalDeFilas; fila++) {
            for (int colum = 0; colum < totalDeColumnas; colum++) {
                final int coordenadax = fila;
                final int coordenaday = colum;
                tableroDePartida[fila][colum] = new Casilla(coordenadax, coordenaday, false, true);
            }
        }
        //pack();
        colocarminas(minasTotales, filasDelTablerox, columnasDelTableroy);
    }

    synchronized public void dondeyqueclickeo(int posicionx, int posiciony, int botonpulsado, String quienPulso) {
        coordenadasAfectados = "";
        if (!usuariosJugando.get(quienPulso).isHizo_primeromovimiento()) {
            aceptarSuPrimerMovimiento(quienPulso,posicionx,posiciony);
        }
        if (usuariosJugando.get(quienPulso).isHizo_primeromovimiento()) {
            if (tableroDePartida[posicionx][posiciony].esClickeable) {
                
                boolean dondePulsaNoTienebandera = dondePulsaNoTienebandera(posicionx,posiciony);
                boolean dondePulsaTieneMina = dondePulsaTieneMina(posicionx,posiciony);
                
                if (botonpulsado == 1 && dondePulsaNoTienebandera && !dondePulsaTieneMina) {
                    //llamar a despejar 
                    //Cuando sea click izquierda, no tiene bandera y no sea bomba
                    despejar(posicionx, posiciony);
                } else if (botonpulsado == 1 && dondePulsaNoTienebandera && dondePulsaTieneMina) {
                    //Cuando sea click izquierda, no tengas bandera y si sea bomba

                    esmina(posicionx, posiciony, quienPulso);
                } else if (botonpulsado == 3) {
                    ponerBandera(posicionx, posiciony, quienPulso);
                }
            }
            enviarMensajeAlCliente(coordenadasAfectados,ganada);
            terminar();
        }
    }
    
    public void enviarMensajeAlCliente(String mensaje, boolean terminoPartida){
        if(!terminoPartida){
            servidorDelTablero.actualizar(mensaje);
        }
        else{
            servidorDelTablero.actualizar(mensaje);
        }
        
    }
    
    public boolean dondePulsaNoTienebandera(int posicionx, int posiciony){
        return !tableroDePartida[posicionx][posiciony].tieneBandera();
    }
    
    public boolean dondePulsaTieneMina(int posicionx, int posiciony){
         return tableroDePartida[posicionx][posiciony].esMina();
    }
    public void aceptarSuPrimerMovimiento(String quienPulso, int posicionx, int posiciony){
        if ((posicionx == 0 && posiciony <= totalDeColumnas) && tableroDePartida[posicionx][posiciony].esClickeable && quienPulso.equals("cafe")) {
                //puso en la primera fila
                usuariosJugando.get(quienPulso).setHizo_primeromovimiento(true);
            } else if ((posiciony == 0 && posicionx <= totalDeFilas) && tableroDePartida[posicionx][posiciony].esClickeable && quienPulso.equals("amarillo")) {
                //en la primera columna
                usuariosJugando.get(quienPulso).setHizo_primeromovimiento(true);
            } else if ((posicionx + 1 == totalDeFilas) && tableroDePartida[posicionx][posiciony].esClickeable && quienPulso.equals("verde")) {
                //en la ultima fila
                usuariosJugando.get(quienPulso).setHizo_primeromovimiento(true);
            } else if ((posiciony + 1 == totalDeColumnas) && tableroDePartida[posicionx][posiciony].esClickeable && quienPulso.equals("rojo")) {
                //en la ultima columna
                usuariosJugando.get(quienPulso).setHizo_primeromovimiento(true);
            }
    }

    public void esmina(int cordenadax, int cordenaday, String quienPulso) {

        if (!tableroDePartida[cordenadax][cordenaday].tieneBandera()) {
            minasDescubiertas++;
        }
        //10,10,nombreusuario_bomba.jpg
        coordenadasAfectados = cordenadax + "," + cordenaday + "," + quienPulso + "_bomba.jpg";
        servidorDelTablero.muertos.add(quienPulso);
    }

    public void ponerBandera(int posicionx, int posiciony, String quienPulso) {

        if (!tableroDePartida[posicionx][posiciony].tieneBandera() && usuariosJugando.get(quienPulso).banderas_puestas != totalDeBombas) {
            coordenadasAfectados = posicionx + "," + posiciony + "," + quienPulso + "_bandera.jpg";
            //le decimos quien tiene bandera en esa poscion
            tableroDePartida[posicionx][posiciony].setBandera_de_quien(quienPulso);
            tableroDePartida[posicionx][posiciony].setTieneBandera(true);
            
            //usuariosJugando.get(quienPulso).banderas_puestas++;
            usuariosJugando.get(quienPulso).aumentarTotalBanderasPuestas();
            if (tableroDePartida[posicionx][posiciony].esMina()) {
                minasDescubiertas++;
                //usuariosJugando.get(quienPulso).puntos++;
                usuariosJugando.get(quienPulso).sumarPuntos();
            }
            usuariosJugando.get(quienPulso).getEscritor_usuario().println((totalDeBombas - usuariosJugando.get(quienPulso).banderas_puestas) + "*");
        } else if (tableroDePartida[posicionx][posiciony].getBandera_de_quien().equals(quienPulso)) {
            //quita bandera
            quitaBandera(posicionx, posiciony, quienPulso);
        }
    }

    public void quitaBandera(int posicionx, int posiciony, String quien) {
        if (tableroDePartida[posicionx][posiciony].seDescubre()) {
            if(tableroDePartida[posicionx][posiciony].getValor()!=0){
             coordenadasAfectados = posicionx + "," + posiciony + "," + tableroDePartida[posicionx][posiciony].getValor() + "-";   
            }
            else{
                coordenadasAfectados = posicionx + "," + posiciony + "," + "" + "-";  
            }
            
            tableroDePartida[posicionx][posiciony].setEs_clickeable(false);
        } else {
            coordenadasAfectados = posicionx + "," + posiciony;
        }
        tableroDePartida[posicionx][posiciony].setBandera_de_quien(null);
        tableroDePartida[posicionx][posiciony].setTieneBandera(false);
        //usuariosJugando.get(quien).banderas_puestas--; 
        usuariosJugando.get(quien).reducirTotalBanderasPuestas();
        if (tableroDePartida[posicionx][posiciony].esMina()) {
            minasDescubiertas--;
            if (usuariosJugando.containsKey(quien)) {
                usuariosJugando.get(quien).restarPuntos();
            }
        }
        usuariosJugando.get(quien).getEscritor_usuario().println((totalDeBombas - usuariosJugando.get(quien).banderas_puestas) + "*");
    }

    public void colocarminas(int mina, int fila, int columna) {
       
        Random r = new Random();
        while (mina > 0) {
            int a = r.nextInt(fila);
            int b = r.nextInt(columna);
            if (!tableroDePartida[a][b].esMina()) {
                tableroDePartida[a][b].setEs_mina(true);
                tableroDePartida[a][b].setTieneBandera(false);
                tableroDePartida[a][b].setValor(-1);
                coordenadasDeLasMinas.add(tableroDePartida[a][b]);
                mina--;
            }
        }
        numeroAlrededor(fila, columna);
    }

    public void numeroAlrededor(int fila, int columnas) {
        //Verificaremos todo el tablero
        for (int x = 0; x < fila; x++) {
            for (int y = 0; y < columnas; y++) {
                //y si hay minas alrededor de posicion k 
                int contador = 0;
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int k = y - 1; k <= y + 1; k++) {
                        if (i >= 0 && k >= 0 && i < fila && k < columnas) {
                            if (tableroDePartida[i][k].esMina() && !tableroDePartida[x][y].esMina()) {
                                contador++;
                            }
                        }
                    }
                    tableroDePartida[x][y].setValor(contador);
                }
            }
        }
    }

    public void despejar(int fila, int columnas) {

        if (tableroDePartida[fila][columnas].EsClickeable()) {

            if (tableroDePartida[fila][columnas].tieneBandera()) {
                tableroDePartida[fila][columnas].hacerDescubrible();
                return;
            }
            tableroDePartida[fila][columnas].setEs_clickeable(false);
            if (tableroDePartida[fila][columnas].getValor() != 0) {
                coordenadasAfectados += fila + "," + columnas + "," + tableroDePartida[fila][columnas].getValor() + "-";
            } else {
                coordenadasAfectados += fila + "," + columnas + "," + "" + "-";
            }

            if (tableroDePartida[fila][columnas].getValor() == 0) {
                for (int i = fila - 1; i <= fila + 1; i++) {
                    for (int k = columnas - 1; k <= columnas + 1; k++) {
                        if (i >= 0 && k >= 0 && i < totalDeFilas && k < totalDeColumnas) {
                            despejar(i, k);
                        }
                    }
                }
            }

        }

    }
    public boolean sitieneBanderaEnCampoVacio(int fila, int columna){
        return tableroDePartida[fila][columna].tieneBandera() && tableroDePartida[fila][columna].EsClickeable();
    }
    public int casillasDescubiertas(){
        int descuebiertas=0;
        for (int fila = 0; fila < totalDeFilas; fila++) {
                for (int columna = 0; columna < totalDeColumnas; columna++) {
                    if (!tableroDePartida[fila][columna].EsClickeable() && !tableroDePartida[fila][columna].esMina() || tableroDePartida[fila][columna].seDescubre()){
                            descuebiertas++;
                    }
                }
            }
        return descuebiertas;
    }

    public void terminar() {
        /*
         1.- Solo queda un jugador conectado.
         2.- Se mueren todos
         3.- Se destapa todo el mapa y se ponen banderas en todas las minas
        
         */
        //System.out.println(totalDeFilas * totalDeColumnas);
        //System.out.println(minasDescubiertas);
        if (ganada == false) {
            
            int descuebiertas = casillasDescubiertas();
            
            //System.out.println(descuebiertas + " / " + minasDescubiertas);
            if (servidorDelTablero.usuariosConectados.size() == 1
                    || servidorDelTablero.muertos.size() == servidorDelTablero.usuariosConectados.size()
                    || (descuebiertas == (totalDeFilas * totalDeColumnas) - totalDeBombas) 
                    && (minasDescubiertas == totalDeBombas)) {
                
                String nombreGanador = "nadie";
                int puntosGanador = 0;
                for (Entry<String, Usuario> jugador : usuariosJugando.entrySet()) {
                    if (jugador.getValue().puntos > puntosGanador) {
                        nombreGanador = jugador.getValue().nombre_usuario;
                        puntosGanador = jugador.getValue().puntos;
                    } else if (jugador.getValue().puntos == puntosGanador) {
                        if (puntosGanador != 0) {
                            nombreGanador = nombreGanador + " y " + jugador.getValue().nombre_usuario;
                        }
                    }
                }

                if (servidorDelTablero.usuariosConectados.size() == 1) {
                    nombreGanador = servidorDelTablero.usuariosConectados.toString();
                    nombreGanador = nombreGanador.substring(1, nombreGanador.length() - 1);
                }
                ganada = true;
                String mensajGanador = "Ganador: " + nombreGanador + " -> " + puntosGanador + ".";
                enviarMensajeAlCliente(mensajGanador,ganada);
                // servidorDelTablero.actualizar("Ganador: " + nombreGanador + " -> " + puntosGanador + ".");
            }
            else{
                //System.out.println("No Termina la PARTIDA AUN *!!*!*!*!*!*!");
            }
        }
    }

    public void activarBombas() {
        //System.out.println("Debe entrar a explotar una bombaaa!!!!!!");
        boolean explo_mina = false;
        int a, b;
        if (coordenadasDeLasMinas.size() > 0 && minasDescubiertas != totalDeBombas) {
            Random r = new Random();
            while (!explo_mina) {
                int n_random = r.nextInt(coordenadasDeLasMinas.size());
                //System.out.println("Numero aleatorio: " + n_random);          

                a = coordenadasDeLasMinas.get(n_random).cordenadaX;
                b = coordenadasDeLasMinas.get(n_random).cordenadaY;
                if (tableroDePartida[a][b].esMina()) {
                    //System.out.println("Encontro una bombitaa!*!*!*!*!*!*!");
                    if (tableroDePartida[a][b].EsClickeable()) {
                        
                        if (!tableroDePartida[a][b].tieneBandera()) {
                            
                            coordenadasDeLasMinas.remove(n_random);
                            coordenadasAfectados = "";
                            despejar_bomba(a, b);
                            explo_mina = true;
                            break;                        
                        }
                    }
                }

            }
            //System.out.println("exploto la bombita");
            enviarMensajeAlCliente(coordenadasAfectados,ganada);
            if (minasDescubiertas == totalDeBombas) {
                terminar();
            }
        } else {
            //System.out.println("+++++++++++si entro a terminar la partida++++++++++++++");
            terminar();
            servidorDelTablero.tiempo.cancel();
        }
    }

    public void despejar_bomba(int fila, int columnas) {
        
        if (tableroDePartida[fila][columnas].EsClickeable()) {
            if (tableroDePartida[fila][columnas].tieneBandera()) {
                tableroDePartida[fila][columnas].hacerDescubrible();//
                return;
            }
            tableroDePartida[fila][columnas].setEs_clickeable(false);           
            if (tableroDePartida[fila][columnas].esMina()){
               coordenadasAfectados += fila + "," + columnas + "," + "bom" + "-";
               ++minasDescubiertas;
            } else {
                coordenadasAfectados += fila + "," + columnas + "," + tableroDePartida[fila][columnas].getValor() + "-";
            }

            if (tableroDePartida[fila][columnas].esMina()) {
                for (int i = fila - 1; i <= fila + 1; i++) {
                    for (int k = columnas - 1; k <= columnas + 1; k++) {
                        if (i >= 0 && k >= 0 && i < totalDeFilas && k < totalDeColumnas) {
                            despejar_bomba(i, k);
                        }
                    }
                }
            }

        }
        //System.out.println("*/*//*" + coordenadasAfectados);
    }
}
