/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author FERNANDO
 */
public class tablero extends JFrame {

    public JPanel superior;
    public JPanel inferior;
    public JLabel nombreusuario;
    public JLabel numerobanderas;
    public static JButton tablero_botones[][];
    private int px, py,bandera;
    private boolean puede=true;
    String usuario;
    //funcion nueva
    void construyePanelSuperior() {
        superior = new JPanel();
        superior.setLayout(new FlowLayout());
        //System.out.println("Usuario: "+usuario);
        nombreusuario = new JLabel();
        numerobanderas = new JLabel();
        nombreusuario.setText("Usuario: "+usuario);
        numerobanderas.setText("numero de banderas: "+bandera);
        superior.add(nombreusuario);
        superior.add(numerobanderas);
    }
    
    void construyePanelinferior() {
        inferior = new JPanel();
        inferior.setLayout(new GridLayout(20, 20, 0, 0));
        tablero_botones = new JButton[px][py];
        //setLayout(new GridLayout(py, px, 0, 0));
        for (int fila = 0; fila < px; fila++) {
            for (int colum = 0; colum < py; colum++) {
                final int cordex = fila;
                final int cordey = colum;
                
                tablero_botones[fila][colum] = new JButton("");
                tablero_botones[fila][colum].setPreferredSize(new Dimension(41, 20));
                tablero_botones[fila][colum].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        if (me.getButton() == MouseEvent.BUTTON1) {
                            clickeo(cordex,cordey,me.getButton(),usuario);
                        }
                        if (me.getButton() == MouseEvent.BUTTON3) {
                            clickeo(cordex,cordey,me.getButton(),usuario);
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent me) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                    }
                });
                inferior.add(tablero_botones[fila][colum]);
                tablero_botones[fila][colum].setEnabled(true);
            }
        }
    }
    
    public tablero(int px, int py, String u, int b) {
        this.px = px;
        this.py = py;
        this.usuario=u;
        this.bandera=b;
        
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        construyePanelSuperior();
        construyePanelinferior();
        this.add(superior);
        this.add(inferior);
        this.pack();
        //this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Todo normal
        
        /*System.out.println("valor de px: " + px + " valor de py: " + py);
        tablero_botones = new JButton[px][py];
        setLayout(new GridLayout(py, px, 0, 0));
        for (int fila = 0; fila < px; fila++) {
            for (int colum = 0; colum < py; colum++) {
                final int cordex = fila;
                final int cordey = colum;
                
                tablero_botones[fila][colum] = new JButton("");
                tablero_botones[fila][colum].setPreferredSize(new Dimension(41, 20));
                tablero_botones[fila][colum].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        if (me.getButton() == MouseEvent.BUTTON1) {
                            clickeo(cordex,cordey,me.getButton(),usuario);
                        }
                        if (me.getButton() == MouseEvent.BUTTON3) {
                            clickeo(cordex,cordey,me.getButton(),usuario);
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent me) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                    }
                });
                add(tablero_botones[fila][colum]);
                tablero_botones[fila][colum].setEnabled(true);
            }
        }*/
        pack();
    }

    public void meter_num(int x, int y, int n) {
        tablero_botones[x][y].setName(""+n);
        //tablero_botones[x][y].setText(""+n);
    }

    public void meterbomba(int x, int y) {
        tablero_botones[x][y].setName("-1");
        //tablero_botones[x][y].setText("-1");
    }
    public void clickeo(int cordex, int cordey , int boton_pulsado,String u){
        if(tablero_botones[cordex][cordey].isEnabled()){
        Cliente.out.println("presione " + cordex + "-" + cordey+"-"+boton_pulsado+"-"+u);
        System.out.println("//////////////////////////////////");
        }
    }
    public void actualizarcasilla(int x, int y){
        tablero_botones[x][y].setEnabled(false);
    }
    
}
