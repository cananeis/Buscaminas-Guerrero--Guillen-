/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author FERNANDO
 */
public class tablero extends JFrame {

    servidor servidor;
    public JButton tablero_botones[][];
    private int tablero[][] = new int[20][20];
    int px;
    int py;
    int bom;
    public boolean ganada=false;
    public String coordenadas_bombas = "";
    public String coordenadas_numero = "";
    public String coordenadas_afectados = "";
    int minas_descubiertas = 0;
    HashMap<String, Integer> puntos = new HashMap<String, Integer>();
    HashMap<String, Boolean> saberprimero = new HashMap<String, Boolean>();
    HashMap<JButton, Boolean> saberdescubrir = new HashMap<JButton, Boolean>();
    HashMap<String, Integer> banderaspuestas = new HashMap<String, Integer>();

    public tablero(int x, int y, int minas, servidor tuServidor) {
        servidor = tuServidor;
        this.px = x;
        this.py = y;
        bom = minas;
        saberprimero.put("amarillo", false);
        saberprimero.put("cafe", false);
        saberprimero.put("rojo", false);
        saberprimero.put("verde", false);
        
        banderaspuestas.put("amarillo", 0);
        banderaspuestas.put("cafe", 0);
        banderaspuestas.put("rojo", 0);
        banderaspuestas.put("verde", 0);
        tablero_botones = new JButton[px][py];

        //public int tablero[][] = new int[10][10];
        setLayout(new GridLayout(py, px, 0, 0));
        for (int fila = 0; fila < px; fila++) {
            for (int colum = 0; colum < py; colum++) {
                final int cordex = fila;
                final int cordey = colum;

                tablero_botones[fila][colum] = new JButton("");
                tablero_botones[fila][colum].setPreferredSize(new Dimension(41, 20));
                tablero_botones[fila][colum].setName("");
                tablero_botones[fila][colum].setText("");
                saberdescubrir.put(tablero_botones[fila][colum], false);
                add(tablero_botones[fila][colum]);
                tablero_botones[fila][colum].setEnabled(true);
            }
        }
        pack();
        colocarminas(minas, x, y);
        for (int ax = 0; ax < px; ax++) {
            for (int ay = 0; ay < py; ay++) {
                System.out.print(tablero[ax][ay]);
            }
            System.out.println();
        }
    }

    synchronized public void dondeyqueclickeo(int posicionx, int posiciony, int botonpulsado, String quien_pul) {
        if (!saberprimero.get(quien_pul)) {

            if ((posicionx == 0 && posiciony <= py) && tablero_botones[posicionx][posiciony].isEnabled() && quien_pul.equals("cafe")) {
                //puso en la primera fila
                saberprimero.put(quien_pul, true);
            } else if ((posiciony == 0 && posicionx <= px) && tablero_botones[posicionx][posiciony].isEnabled() && quien_pul.equals("amarillo")) {
                //en la primera columna
                saberprimero.put(quien_pul, true);
            } else if ((posicionx + 1 == px) && tablero_botones[posicionx][posiciony].isEnabled() && quien_pul.equals("verde")) {
                //en la ultima fila
                saberprimero.put(quien_pul, true);
            } else if ((posiciony + 1 == py) && tablero_botones[posicionx][posiciony].isEnabled() && quien_pul.equals("rojo")) {
                //en la ultima columna
                saberprimero.put(quien_pul, true);
            }
        }
        if (saberprimero.get(quien_pul)) {
            if (tablero_botones[posicionx][posiciony].isEnabled()) {
                coordenadas_afectados = "";
                if (botonpulsado == 1 && tablero_botones[posicionx][posiciony].getText().equals("")) {
                    esmina(posicionx, posiciony, quien_pul);
                } else if (botonpulsado == 3 ) {
                    if(tablero_botones[posicionx][posiciony].isEnabled()){
                    ponbande(posicionx, posiciony, quien_pul);
                    }
                }
            }
            servidor.actualizar(coordenadas_afectados);
            terminar();
        }
    }

    public void esmina(int cordex, int cordey, String quien) {

        if (tablero_botones[cordex][cordey].getName().equals("-1")) {
            if (tablero_botones[cordex][cordey].getText().equals("")) {
                minas_descubiertas++;
            }
            //10,10,nombreusuario_bomba.jpg
            coordenadas_afectados = cordex + "," + cordey + "," + quien + "_bomba.jpg";
            if (tablero_botones[cordex][cordey].getText().equals(quien)) {
                if (puntos.containsKey(quien)) {
                    int puntos_totales = puntos.get(quien);
                    puntos_totales--;
                    puntos.put(quien, puntos_totales);
                }
            }
            servidor.muertos.add(quien);

        } else if (tablero_botones[cordex][cordey].getName().equals("")) {
            System.out.println("esta vacio " + tablero_botones[cordex][cordey].getName());
            //10,10,8-3,2,0
            despejar(cordex, cordey);
        } else {
            tablero_botones[cordex][cordey].setText(tablero_botones[cordex][cordey].getName());
            tablero_botones[cordex][cordey].setEnabled(false);
            coordenadas_afectados = cordex + "," + cordey + "," + tablero_botones[cordex][cordey].getName() + "-";
        }
        tablero_botones[cordex][cordey].setEnabled(false);

    }

    public void ponbande(int posicionx, int posiciony, String quien) {
       
        if (tablero_botones[posicionx][posiciony].getText().equals("") && banderaspuestas.get(quien)!=bom) {
            coordenadas_afectados = posicionx + "," + posiciony + "," + quien + "_bandera.jpg";
            tablero_botones[posicionx][posiciony].setText(quien);
            
            
            if (tablero_botones[posicionx][posiciony].getName().equals("-1")) {
                minas_descubiertas++;
                if (puntos.containsKey(quien)) {
                    int puntos_totales = puntos.get(quien);
                    puntos_totales++;
                    puntos.put(quien, puntos_totales);
                } else {
                    puntos.put(quien, 1);
                }
            }
            if (banderaspuestas.containsKey(quien)) {
                    int banderas_totales = banderaspuestas.get(quien);
                    banderas_totales++;
                    banderaspuestas.put(quien, banderas_totales);
                } else {
                    banderaspuestas.put(quien, 1);
                }
           
            int a= banderaspuestas.get(quien);
            servidor.mapa.get(quien).println((bom-a)+"*");
        } else if (tablero_botones[posicionx][posiciony].getText().equals(quien)) {
            if (saberdescubrir.get(tablero_botones[posicionx][posiciony])) {
                coordenadas_afectados = posicionx + "," + posiciony + "," + tablero_botones[posicionx][posiciony].getName() + "-";
                tablero_botones[posicionx][posiciony].setEnabled(false);
            } else {
                coordenadas_afectados = posicionx + "," + posiciony;
            }
            tablero_botones[posicionx][posiciony].setText("");
            if (tablero_botones[posicionx][posiciony].getName().equals("-1")) {
                minas_descubiertas--;
                if (puntos.containsKey(quien)) {
                    int puntos_totales = puntos.get(quien);
                    puntos_totales--;
                    puntos.put(quien, puntos_totales);
                }
            }
            if (banderaspuestas.containsKey(quien)) {
                    int bandera_totales = banderaspuestas.get(quien);
                    bandera_totales--;
                    banderaspuestas.put(quien, bandera_totales);
                }
            int a= banderaspuestas.get(quien);
            servidor.mapa.get(quien).println((bom-a)+"*");
        }
    }

    public void colocarminas(int x, int f, int c) {
        System.out.println("*******************************Generando");
        int mina = x;
        int fila = f, columna = c;

        Random r = new Random();
        while (mina > 0) {
            int a = r.nextInt(fila);
            int b = r.nextInt(columna);
            if (!tablero_botones[a][b].getName().equals("-1")) {
                tablero_botones[a][b].setName("-1");
                coordenadas_bombas = coordenadas_bombas + a + "," + b + "-";
                tablero[a][b] = -1;
                mina--;
            }
        }

        numeroalrededor(f, c);
    }

    public void numeroalrededor(int f, int c) {
        int fila = f, columnas = c;
        //int tablero[][] = minas;
        //Verificaremos todo el tablero
        for (int x = 0; x < fila; x++) {
            for (int y = 0; y < columnas; y++) {
                //y si hay minas alrededor de posicion k 
                int contador = 0;
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int k = y - 1; k <= y + 1; k++) {
                        if (i >= 0 && k >= 0 && i < fila && k < columnas) {
                            if (tablero_botones[i][k].getName().equals("-1") && !tablero_botones[x][y].getName().equals("-1")) {

                                contador++;
                                String numero = String.valueOf(contador);
                                tablero_botones[x][y].setName(numero);
                                tablero[x][y] = contador;
                            }
                        }
                    }
                }
                if (contador != 0) {
                    coordenadas_numero = coordenadas_numero + x + "," + y + "," + contador + "-";
                }
            }
        }

    }

    public void despejar(int fila, int columnas) {

        if (tablero_botones[fila][columnas].isEnabled()) {

            if (!tablero_botones[fila][columnas].getText().equals("")) {
                saberdescubrir.put(tablero_botones[fila][columnas], true);
                return;
            }

            tablero_botones[fila][columnas].setEnabled(false);
            coordenadas_afectados += fila + "," + columnas + "," + tablero_botones[fila][columnas].getName() + "-";

            if (tablero_botones[fila][columnas].getName().equals("")) {

                for (int i = fila - 1; i <= fila + 1; i++) {
                    for (int k = columnas - 1; k <= columnas + 1; k++) {
                        if (i >= 0 && k >= 0 && i < px && k < py) {
                            despejar(i, k);
                        }
                    }
                }

            }

        }

    }

    public void terminar() {
        /*
                1.- Solo queda un jugador conectado.
                2.- Se mueren todos
                3.- Se destapa todo el mapa y se ponen banderas en todas las minas
        
         */
        System.out.println(px*py);
           System.out.println(minas_descubiertas);
        if(ganada==false){
        int descuebiertas = 0;
        for (int x = 0; x < px; x++) {
            for (int y = 0; y < py; y++) {
                if (!tablero_botones[x][y].isEnabled() && !tablero_botones[x][y].getName().equals("-1")) {
                    descuebiertas++;
                }
            }
        }
        //System.out.println(servidor.muertos.size() + " == " + servidor.usuarios.size());
        //System.out.println(descuebiertas + " / " + minas_descubiertas);
        if (servidor.usuarios.size() == 1
                || servidor.muertos.size() == servidor.usuarios.size()
                || (descuebiertas == (px * py) - bom) || (minas_descubiertas == bom)) {
            System.out.println("/**/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*//*/*/*/*/*/*//**/*/*///*");
            String nombreGanador = "nadie";
            int puntosGanador = 0;
            for (Entry<String, Integer> jugador : puntos.entrySet()) {
                if (jugador.getValue() > puntosGanador) {
                    nombreGanador = jugador.getKey();
                    puntosGanador = jugador.getValue();
                } else if (jugador.getValue() == puntosGanador) {
                    if (puntosGanador != 0) {
                        nombreGanador = nombreGanador + " y " + jugador.getKey();
                    }
                }
            }
            if (servidor.usuarios.size() == 1) {
                nombreGanador = servidor.usuarios.toString();
                nombreGanador = nombreGanador.substring(1, nombreGanador.length() - 1);
            }
            servidor.actualizar("Ganador: " + nombreGanador + " -> " + puntosGanador + ".");
            ganada=true;
        }}
    }
}
