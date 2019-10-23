package server;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author FERNANDO
 */
public class servidor {

    public Set<String> usuarios = new HashSet<>();
    public Set<usuario> usuarios123 = new HashSet<>();
    public Set<PrintWriter> writers = new HashSet<>();
    public Set<ObjectOutputStream> escritor_tablero = new HashSet<>();
    public HashMap<String, PrintWriter> mapa = new HashMap<String, PrintWriter>();
    public Set<String> muertos = new HashSet<>();

    int x = 10, y = 10, b;
    tablero tab;
    int jugadores_conectados = 0;
    private boolean enpartida = false;
    String[] names = {"cafe", "amarillo", "verde", "rojo"};

    private ExecutorService hilosClientes;

    servidor(int xx, int yx, int bom) {
        hilosClientes = Executors.newFixedThreadPool(4);
        this.x = xx;
        this.y = yx;
        this.b = bom;
        tab = new tablero(x, y, b, this);
    }

    boolean agregarCliente(Socket cliente) throws IOException {
        if (usuarios.size() == 4 || enpartida == true) {
            System.out.println("estan en partida*-*-*-*-*--*-*-*");
            return false;
        }
        usuarios123.add(new usuario(cliente));
        hilosClientes.execute(new usuario(cliente));
        return true;
    }

    synchronized public void actualizar(String afectados) {
        try {
            if (!afectados.equals("")) {
                for (PrintWriter writer : writers) {
                    writer.println("actu " + afectados);
                }
            }
        } catch (Exception ex) {
        }

    }

    public class usuario implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        public PrintWriter out;
       
        public usuario(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    if (name.equals("agregame")) {
                        name = names[usuarios.size()];
                    }
                    synchronized (usuarios) {
                        if (!usuarios.contains(name)) {
                            usuarios.add(name);
                            break;
                        }
                    }
                }
                
                jugadores_conectados++;
                out.println("NAMEACCEPTED " + name);
                
                writers.add(out);
                
                for (PrintWriter writer : writers) {
                    writer.println("usuario " + usuarios.size());
                    System.out.println("si entro para enviar");
                }

                if (usuarios.size() == 4) {
                    enpartida = true;
                    System.out.println("lo envia la primera vez");
                    for (PrintWriter writer : writers) {
                        writer.println("new " + x + "-" + y + ";" + tab.coordenadas_bombas + ";" + tab.coordenadas_numero + ";"+b);
                        System.out.println("si entro para enviar");
                    }
                }

                if (usuarios.size() > 0 && usuarios.size() != 4) {
                    for (PrintWriter writer : writers) {
                        writer.println("puedes " + usuarios.size());
                    }
                }

                synchronized (mapa) {
                    if (!mapa.containsKey(name)) {
                        mapa.put(name, out);
                    }
                }
                while (true) {
                    String input = in.nextLine();
                    if (input.equals("comienza")) {
                        if (usuarios.size() > 1 && input.equals("comienza")) {
                            enpartida = true;
                            System.out.println("lo envia la primera vez");
                            for (PrintWriter writer : writers) {
                                writer.println("new " + x + "-" + y + ";" + tab.coordenadas_bombas + ";" + tab.coordenadas_numero + ";"+b);
                            }
                        }
                    }
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    } else {
                        
                        System.out.println(name + "dice: " + input);
                        if (input.toLowerCase().startsWith("presione")) {
                            System.out.println("mensajee:" + input.substring(9));
                            //1-1-1
                            String[] parts = input.substring(9).split("-");
                            int posicionx = Integer.parseInt(parts[0]); // primera coordenada
                            int posiciony = Integer.parseInt(parts[1]); // Segunda
                            int botonpulsado = Integer.parseInt(parts[2]);
                            String quien_pul = parts[3];
                            System.out.println("1: " + posicionx);
                            System.out.println("2: " + posiciony);
                            System.out.println("3: " + botonpulsado);
                            System.out.println("quien pulso: " + quien_pul);
                            if (!muertos.contains(quien_pul)) {
                                System.out.println("no esta muerto y dio click");
                                tab.dondeyqueclickeo(posicionx, posiciony, botonpulsado, quien_pul);
                            }
                        }
                        
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println("El usuario: " + name + " se a ido");
                    usuarios.remove(name);
                    //usuarios_iniciados.remove(name);
                    tab.terminar();
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " se a ido");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

    }

}
