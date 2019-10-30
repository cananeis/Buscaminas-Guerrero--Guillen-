package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author FERNANDO
 */
public class Sala {

    public Set<String> usuariosConectados = new HashSet<>();
    public HashMap<String, Usuario> usuariosJugando = new HashMap<String, Usuario>();
    public Set<PrintWriter> writers = new HashSet<>();
    public Set<String> muertos = new HashSet<>();

    int numeroDeFilas;
    int numeroDeColumnas;
    int totalDeBombas;
    public tablero tableroDeLaSala;
    public boolean enpartida = false;
    String[] nombreDeUsuarios = {"cafe", "amarillo", "verde", "rojo"};
    public Timer tiempo = new Timer();

    private ExecutorService hilosClientes;

    Sala(int numeroDeFilas, int numeroDeColumnas, int totalDeBombas) {
        hilosClientes = Executors.newFixedThreadPool(4);
        this.numeroDeFilas = numeroDeFilas;
        this.numeroDeColumnas = numeroDeColumnas;
        this.totalDeBombas = totalDeBombas;
        tableroDeLaSala = new tablero(numeroDeFilas, numeroDeColumnas, totalDeBombas, this);
    }

    boolean agregarCliente(Socket cliente) throws IOException {
        if (usuariosConectados.size() == 4 || enpartida == true) {
            //System.out.println("estan en partida*-*-*-*-*--*-*-*");
            return false;
        }
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

        private String nombreDelUsuario;
        private Socket socket;
        private Scanner lectorDeMensajeRecibido;
        public PrintWriter escritorDeMensajeaCliente;
        public Usuario usuarioConectado = new Usuario(nombreDelUsuario, true, escritorDeMensajeaCliente, false);

        public usuario(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                lectorDeMensajeRecibido = new Scanner(socket.getInputStream());
                escritorDeMensajeaCliente = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    escritorDeMensajeaCliente.println("SUBMITNAME");
                    nombreDelUsuario = lectorDeMensajeRecibido.nextLine();
                    if (nombreDelUsuario == null) {
                        return;
                    }
                    if (nombreDelUsuario.equals("agregame")) {
                        nombreDelUsuario = nombreDeUsuarios[usuariosConectados.size()];
                    }
                    synchronized (usuariosConectados) {
                        if (!usuariosConectados.contains(nombreDelUsuario)) {
                            usuariosConectados.add(nombreDelUsuario);
                            break;
                        }
                    }
                }
                escritorDeMensajeaCliente.println("NAMEACCEPTED " + nombreDelUsuario);
                writers.add(escritorDeMensajeaCliente);

                for (PrintWriter writer : writers) {
                    writer.println("usuario " + usuariosConectados.size());
                    //System.out.println("si entro para enviar");
                }
                //para iniciar la partida cuando se llene, o mandarle un mensaje de comenzar cuando sean mayor a 1 
                //y no este llena
                if (usuariosConectados.size() == 4) {
                    enpartida = true;
                    //System.out.println("lo envia la primera vez");
                    enviarDatosDelTablero();
                } else if (usuariosConectados.size() > 0 && usuariosConectados.size() != 4) {
                    for (PrintWriter writer : writers) {
                        writer.println("puedes " + usuariosConectados.size());
                    }
                }

                usuarioConectado.setNombre_usuario(nombreDelUsuario);
                usuarioConectado.setEscritor_usuario(escritorDeMensajeaCliente);
                usuariosJugando.put(nombreDelUsuario, usuarioConectado);
                
                while (true) {
                    
                    String mensajeRecivido = lectorDeMensajeRecibido.nextLine();
                    
                    if (mensajeRecivido.equals("comienza")) {
                        if (usuariosConectados.size() > 1 && mensajeRecivido.equals("comienza")) {
                            enpartida = true;
                            enviarDatosDelTablero();
                            TimerTask tarea = new TimerTask() {
                                @Override
                                public void run() {
                                    tableroDeLaSala.activarBombas();
                                }
                            };
                            tiempo.schedule(tarea, 5000, 30000);
                        }
                    }
                    else if (mensajeRecivido.toLowerCase().startsWith("presione")) {
                            //1-1-1
                            String[] parts = mensajeRecivido.substring(9).split("-");
                            int posicionx = Integer.parseInt(parts[0]); // primera coordenada
                            int posiciony = Integer.parseInt(parts[1]); // Segunda
                            int botonpulsado = Integer.parseInt(parts[2]);
                            String quienPulso = parts[3];

                            if (!muertos.contains(quienPulso)) {
                                System.out.println("no esta muerto y dio click");
                                tableroDeLaSala.dondeyqueclickeo(posicionx, posiciony, botonpulsado, quienPulso);
                            }
                        }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (escritorDeMensajeaCliente != null) {
                    writers.remove(escritorDeMensajeaCliente);
                }
                if (nombreDelUsuario != null) {
                    //System.out.println("El usuario: " + nombreDelUsuario + " se a ido");
                    usuariosConectados.remove(nombreDelUsuario);
                    tableroDeLaSala.terminar();
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + nombreDelUsuario + " se a ido");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

    }

    public void enviarDatosDelTablero() {
        for (PrintWriter writer : writers) {
            writer.println("new " + numeroDeFilas + "-" + numeroDeColumnas + ";" + totalDeBombas);
        }
    }

}
