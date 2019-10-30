/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author ITLM
 */
public class temporizador_bombas {
    public Sala servidor;
    public Timer t = new Timer();
    
    public void activar_temporizador() {
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                //System.out.print(t);
                servidor.tableroDeLaSala.activarBombas();
            }
        };
        t.schedule(tarea, 30000, 30000);
    }
}
