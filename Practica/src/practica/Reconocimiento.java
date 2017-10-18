/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
    private JSONObject envio;
    private JSONObject recepcion;
    private ACLMessage outbox, inbox;
    private String recepcion_plano;
    
    public Reconocimiento(AgentID aid) throws Exception {
        super(aid);
        outbox = new ACLMessage();
        outbox.setSender(aid);
        inbox = new ACLMessage();
        inbox.setReceiver(aid);  
    }
    
    public void actualizar_GPS(){
        //posicion_x = ;
        //posicion_y = ;
    }
    
    public void actualizar_Radar(){
        /*for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                radar[i][j] = ;*/
    }
    
    public void actualizar_Scanner(){
        /*for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                scanner[i][j] = ;*/
    }
    
    public void enviar_movimiento(){
        
    }
    
    public void actualizar_Matriz_AUX(){
        
    }
    
    public void escuchar_Repostaje(){
        
    }
    
    public void escuchar_Vehiculo(){
    
    }
    
    public void enviar_mensaje(String mensaje, String receptor){
        outbox.setReceiver(new AgentID(receptor));
        outbox.setContent(mensaje);
        this.send(outbox);
    }
    
    public void recibir_mensaje(String mensajero) throws InterruptedException, JSONException{
        inbox.setSender(new AgentID(mensajero));
        inbox = receiveACLMessage();
        recepcion = new JSONObject(inbox.getContent());
        recepcion_plano = recepcion.toString();
        //actuar(mensajero);
        System.out.println("Reconocimiento: " + recepcion_plano);
        finalize();
    }
    
    @Override
    public void execute(){
        while(true){
            try {
                recibir_mensaje("Achernar");
            } catch (InterruptedException ex) {
                Logger.getLogger(Reconocimiento.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(Reconocimiento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @Override
    public void finalize(){
        try {
            System.out.println("Reconocimiento muerto");
        } finally {
            super.finalize();
        }
    }
    
    public void actuar(String mensajero) throws JSONException{
        
        if(mensajero.equals("Achernar")){
            if(recepcion_plano.equals("CRASHED")){
                //Finalizar agente
                finalize();
            }else
                System.out.println("Reconocimiento: " + recepcion_plano);
        }else
            if(!recepcion_plano.equals("OK"))
                finalize();
    }
}
