package server;

public class Casilla {

    boolean esMina = false;
    int cordenadaX, cordenadaY;
    boolean tieneBandera;
    boolean seDescubre;
    boolean esClickeable;
    String banderaDeQuien;
    int valor = 0;

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Casilla(int cordenadaX, int cordenadaY, boolean seDescubre, boolean esClickeable) {
        this.cordenadaX = cordenadaX;
        this.cordenadaY = cordenadaY;
        this.seDescubre = seDescubre;
        this.esClickeable = esClickeable;
        valor = 0;
        tieneBandera = false;
        banderaDeQuien = "";
    }

    public boolean esMina() {
        return esMina;
    }

    public void setEs_mina(boolean esMina) {
        this.esMina = esMina;
    }

    public int getCordenadaX() {
        return cordenadaX;
    }

    public void setCordenadaX(int cordenadaX) {
        this.cordenadaX = cordenadaX;
    }

    public int getCordenadaY() {
        return cordenadaY;
    }

    public void setCordenadaY(int cordenadaY) {
        this.cordenadaY = cordenadaY;
    }

    public boolean tieneBandera() {
        return tieneBandera;
    }

    public void setTieneBandera(boolean tieneBandera) {
        this.tieneBandera = tieneBandera;
    }

    public boolean seDescubre() {
        return seDescubre;
    }

    public void hacerDescubrible() {
        seDescubre = true;
    }

    public boolean EsClickeable() {
        return esClickeable;
    }

    public void setEs_clickeable(boolean esClickeable) {
        this.esClickeable = esClickeable;
    }

    public String getBandera_de_quien() {
        return banderaDeQuien;
    }

    public void setBandera_de_quien(String bandera_de_quien) {
        this.banderaDeQuien = bandera_de_quien;
    }

}
