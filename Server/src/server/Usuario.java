
package server;

import java.io.PrintWriter;

public class Usuario {
    String nombre_usuario;

    public Usuario(String nombre_usuario, boolean vivo, PrintWriter escritor_usuario,boolean click) {
        this.nombre_usuario = nombre_usuario;
        this.vivo = vivo;
        this.escritor_usuario = escritor_usuario;
        this.hizo_primeromovimiento = click;
        this.banderas_puestas=0;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public boolean isHizo_primeromovimiento() {
        return hizo_primeromovimiento;
    }

    public void setHizo_primeromovimiento(boolean hizo_primeromovimiento) {
        this.hizo_primeromovimiento = hizo_primeromovimiento;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    public void restarPuntos(){
        puntos--;
    }
    public void sumarPuntos(){
        puntos++;
    }

    public int getBanderas_puestas() {
        return banderas_puestas;
    }

    public void setBanderas_puestas(int banderas_puestas) {
        this.banderas_puestas = banderas_puestas;
    }
    public void aumentarTotalBanderasPuestas(){
        banderas_puestas++;
    }
    public void reducirTotalBanderasPuestas(){
        banderas_puestas--;
    }

    public PrintWriter getEscritor_usuario() {
        return escritor_usuario;
    }

    public void setEscritor_usuario(PrintWriter escritor_usuario) {
        this.escritor_usuario = escritor_usuario;
    }
    boolean hizo_primeromovimiento;
    boolean vivo;
    int puntos;
    int banderas_puestas;
    PrintWriter escritor_usuario;
}
