package server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        //tamaño del tablero valor de fila y columna
        int x=20,y=20;
        //numero de bombas
        int bom=70;
        servidor sala = new servidor(x, x, bom);
        ServerSocket servidor = new ServerSocket(69);
        while (true) {
            System.out.println("esperando cliente");
            Socket cliente = servidor.accept();
            System.out.println("ya entro cliente");
            if (!sala.agregarCliente(cliente)) {
                System.out.println("Nuevo servidor");
                sala = new servidor(x, x, bom);
                sala.agregarCliente(cliente);
            }
        }
    }

}
