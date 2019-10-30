package server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        //tamaño del tablero valor de fila y columna
        int numeroDeFilas=20;
        int numeroDeColumnas=20;
        //numero de bombas
        int numeroDeBombas=15;
        Sala sala = new Sala(numeroDeFilas, numeroDeColumnas, numeroDeBombas);
        ServerSocket servidor = new ServerSocket(69);
        while (true) {
            System.out.println("esperando cliente");
            Socket cliente = servidor.accept();
            System.out.println("ya entro cliente");
            if (!sala.agregarCliente(cliente)) {
                System.out.println("Nueva Sala");
                sala = new Sala(numeroDeFilas, numeroDeColumnas, numeroDeBombas);
                sala.agregarCliente(cliente);
            }
        }
    }

}
