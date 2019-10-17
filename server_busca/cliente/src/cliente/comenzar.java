/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 *
 * @author FERNANDO
 */
public class comenzar extends JFrame{
    JButton btn = new JButton("");
    //public static int jugadores=0;
    public comenzar(int jugadores){
        setLayout(new GridLayout(2, 1, 0, 0));
        JLabel texto= new JLabel();
        texto.setHorizontalAlignment( SwingConstants.CENTER );
        texto.setText("Jugadores "+jugadores+"/4");
        btn = new JButton("Comenzar");
        btn.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        if (me.getButton() == MouseEvent.BUTTON1) {
                            Cliente.out.println("comienza");
                        }
                    }
                    @Override public void mousePressed(MouseEvent me) {}
                    @Override public void mouseReleased(MouseEvent me) {}
                    @Override public void mouseEntered(MouseEvent me) {}
                    @Override public void mouseExited(MouseEvent me) {}
                });
        //pack();
        add(texto);
        add(btn);
        setPreferredSize( new Dimension( 150, 125 ) );
    }
}
