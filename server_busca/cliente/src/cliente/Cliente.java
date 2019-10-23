/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author FERNANDO
 */
public class Cliente {

    static String username = null;
    String serverAddress;
    Scanner in;
    static PrintWriter out = null;

    JButton tablero_botones[][];
    tablero tab;
    comenzar comenzar1 = new comenzar(0);

    public Cliente(String serverAddress) {
        this.serverAddress = serverAddress;

    }

    private void correr() throws IOException, ClassNotFoundException {
        try {
            Socket socket = new Socket(serverAddress, 69);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {

                    out.println("agregame");

                } else if (line.startsWith("NAMEACCEPTED")) {

                    System.out.println("acepto el nombre");
                    System.out.println(line.substring(13));
                    username = line.substring(13);

                } else if (line.startsWith("actu")) {
                    /*
                        actu 10,10,rojo_bomba.jpg
                        actu 10,10,naranja_bandera.jpg
                        actu 10,10,-10,5,0-5,3,10-
                        actu 10,10,8-
                     */
                    System.out.println("*******************************************");
                    System.out.println(line);
                    System.out.println("*******************************************");
                    //String lala1234 = "actu 0,6-0,8-1,6-1,7-1,8-0,7-0,8-1,6-1,7-1,8-";
                    String lala = line.substring(5);

                    if (lala.endsWith(".jpg")) {
                        String partido[] = lala.split(",");
                        tablero.tablero_botones[Integer.parseInt(partido[0])][Integer.parseInt(partido[1])].setIcon(new ImageIcon(getClass().getResource("/imagenes/" + partido[2])));
                        /*try {
                            tablero.tablero_botones[Integer.parseInt(partido[0])][Integer.parseInt(partido[1])].setText(partido[2].substring(0, partido[2].length()-12));
                        } catch (Exception e) {
                        }*/

                    } else if (lala.endsWith("-")) {
                        String partido[] = lala.split("-");
                        // [ "10,10," , "5,6,8" ]
                        for (String lala2 : partido) {
                            String partido2[] = lala2.split(",", -1);
                            tablero.tablero_botones[Integer.parseInt(partido2[0])][Integer.parseInt(partido2[1])].setIcon(null);
                            tablero.tablero_botones[Integer.parseInt(partido2[0])][Integer.parseInt(partido2[1])].setEnabled(false);
                            tablero.tablero_botones[Integer.parseInt(partido2[0])][Integer.parseInt(partido2[1])].setText(partido2[2]);
                        }
                    } else if (lala.endsWith(".")) {
                        JOptionPane.showMessageDialog(tab, lala);
                    } else {
                        System.out.println(lala);
                        String partido[] = lala.split(",");
                        tablero.tablero_botones[Integer.parseInt(partido[0])][Integer.parseInt(partido[1])].setIcon(null);
                    }

                } else if (line.startsWith("new")) {
                    String lala = line.substring(4);
                    String[] parts2 = lala.split(";", -1);
                    //parts2.length;
                    System.out.println(parts2.length);

                    for (int x = 0; x <= parts2.length - 1; x++) {
                        String prueba = parts2[x];
                        System.out.println("lo que tiene en la parte: " + x + " " + prueba);
                    }
                    //para saber el tamaño en la parts2parts2[0]
                    String tamaño_t = parts2[0];
                    //("new " + x + "-" + y + ";" + tab.coordenadas_bombas + ";" + tab.coordenadas_numero + ";20");
                    String[] valorx_y = tamaño_t.split("-", -1);
                    int tam_x = Integer.parseInt(valorx_y[0]);
                    int tam_y = Integer.parseInt(valorx_y[1]);
                    
                    String band = parts2[3];
                    int bandera = Integer.parseInt(band);
                    
                    tab = new tablero(tam_x, tam_y, username, bandera);
                   
                   
                    //para bombas busca en la parts2[1]
                    //5,5,*1-8,8,*1-1,3,*1
                    String nom = parts2[1];
                    String[] onon = nom.split("-", -1);
                    for (int y = 0; y <= onon.length - 2; y++) {
                        String bombap = onon[y];
                        //decimos en que posicion de x y y debe haber una bomba
                        String[] lugar_b = bombap.split(",", -1);
                        int posx = Integer.parseInt(lugar_b[0]);
                        int posy = Integer.parseInt(lugar_b[1]);
                        tab.meterbomba(posx, posy);

                    }
                    //saber donde hay numero
                    String arreglo_numero = parts2[2];
                    String[] numeritos = arreglo_numero.split("-", -1);
                    for (int y = 0; y <= numeritos.length - 2; y++) {
                        String bombap1 = numeritos[y];

                        String[] lugar_b = bombap1.split(",", -1);
                        int posx = Integer.parseInt(lugar_b[0]);
                        int posy = Integer.parseInt(lugar_b[1]);
                        int n_b = Integer.parseInt(lugar_b[2]);

                        tab.meter_num(posx, posy, n_b);
                    }

                    comenzar1.dispose();
                    tab.setTitle("cliente-" + username);
                    tab.setLocationRelativeTo(null);
                    tab.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    tab.setVisible(true);

                } else if (line.startsWith("usuario")) {
                    int cuantos = Integer.parseInt(line.substring(8));
                    System.out.println("usuarios conectados y cuando se conecta uno nuevo: " + cuantos);
                } else if (line.startsWith("puedes")) {
                    int numero_usuarios = Integer.parseInt(line.substring(7));
                    //mostrar una ventana de comenzar
                    /*if(numero_usuarios==1){
                       JOptionPane.showMessageDialog(null,"esperando jugadores");
                    }else{*/
                    comenzar1.dispose();
                    comenzar1 = new comenzar(numero_usuarios);
                    comenzar1.setTitle("Confirmacion");
                    comenzar1.pack();
                    comenzar1.setLocationRelativeTo(null);
                    if (numero_usuarios == 1) {
                        comenzar1.btn.setEnabled(false);
                    }
                    comenzar1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    comenzar1.setVisible(true);
                    // }
                }
                else if(line.endsWith("*")){
                    int numerodebanderasdiposnibles=Integer.parseInt(line.substring(0, line.length()-1));
                    tab.numerobanderas.setText("numero de banderas: "+numerodebanderasdiposnibles);
                }
            }
        } finally {
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String respuesta = JOptionPane.showInputDialog(null, "IP del servidor");
        Cliente client = new Cliente(respuesta);

        client.correr();

    }

}
