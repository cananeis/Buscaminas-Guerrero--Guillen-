
package cliente;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class comenzar extends JFrame{
    JButton btn = new JButton("");
  
    public comenzar(int jugadores){
        setLayout(new GridLayout(2, 1, 0, 0));
        JLabel texto= new JLabel();
        texto.setHorizontalAlignment( SwingConstants.CENTER );
        texto.setText("Jugadores "+jugadores+"/4");
        btn = new JButton("Comenzar");
        btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        if (me.getButton() == MouseEvent.BUTTON1) {
                            Cliente.out.println("comienza");
                        }
                    }
                });
      
        add(texto);
        add(btn);
        setPreferredSize( new Dimension( 150, 125 ) );
    }
}
