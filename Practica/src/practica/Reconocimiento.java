/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;

/**
 *
 * @author alex
 */
public class Reconocimiento extends SingleAgent{
    
    private int posicion_x;
    private int posicion_y;
    private int camino_recorrido[][] = new int[1000][1000];
    private float scanner[][] = new float[5][5];
    private int radar[][] = new int [5][5];
    
    public Reconocimiento(AgentID aid) throws Exception {
        super(aid);
    }
    
    public void actualizar_GPS(){
        
    }
    
    public void actualizar_Radar(){
        
    }
    
    public void actualizar_Scanner(){
    
    }
    
    public void enviar_movimiento(){
        
    }
    
    public void actualizar_Matriz_AUX(){
        
    }
    
    public void escuchar_Repostaje(){
        
    }
    
    public void escuchar_Vehiculo(){
    
    }
}
