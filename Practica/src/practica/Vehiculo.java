/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author alex
 */
public class Vehiculo extends SingleAgent{
    
    private int mapa;
    private JSONObject conexion;
    
    public Vehiculo(AgentID aid) throws Exception {
        super(aid);
        conexion = new JSONObject();
        conexion.put("command","login");
        conexion.put("world","mundo1");
        conexion.put("radar","reconocimiento");
        conexion.put("scanner","reconocimiento");
        conexion.put("battery","repostaje");
        conexion.put("gps","reconocimiento");
        System.out.println(conexion.get("command"));
    }
    
    public void mover(JSONObject movimiento){
    }
    
    public void conexion(){
        
    }
    
    public void escuchar_Reconocimiento(){
        
    }
    
    public void solicitar_movimiento(){
        
    }
    
    @Override
    public void init(){
        
    }
    
    @Override
    public void execute(){
        
    }
    
    @Override
    public void finalize(){
        System.out.println("Vehiculo terminado");
    }
    
}
