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
public class Repostaje extends SingleAgent{
    
    private int bateria;
    private JSONObject envio;
    private JSONObject recepcion;
    private ACLMessage outbox, inbox;
    private String recepcion_plano;
    private boolean finalizar;
    
    public Repostaje(AgentID aid) throws Exception {
        super(aid);
        outbox = null;
        inbox = null;  
        finalizar = false;
    }
    
    public void enviar_mensaje(String mensaje, String receptor){
        outbox = new ACLMessage();
        outbox.setSender(getAid());
        outbox.setReceiver(new AgentID(receptor));
        outbox.setContent(mensaje);
        this.send(outbox);
    }
    
    public void recibir_mensaje() throws InterruptedException, JSONException{
        inbox = receiveACLMessage();
        recepcion = new JSONObject(inbox.getContent());
        recepcion_plano = recepcion.toString();
        System.out.println("Repostaje: " + recepcion_plano);
        actuar();
    }
    
    @Override
    public void execute(){
        while(true && !finalizar){
            try {
                recibir_mensaje();
            } catch (InterruptedException | JSONException ex) {
                Logger.getLogger(Repostaje.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void finalize(){
        try {
            System.out.println("Repostaje muerto");
        } finally {
            super.finalize();
        }
    }
    
    public void actuar() throws JSONException{
        
        if(recepcion.has("vehiculo")){
            if(recepcion.getString("vehiculo").equals("cerrar"))
                finalizar = true;
        }else{
            if(recepcion.has("battery")){
                //Actuar según niveles de batería
                recepcion_plano = recepcion.getString("battery");
                bateria = (int) Float.parseFloat(recepcion_plano);
                envio = new JSONObject();
                if(bateria <= 2)//mandar mensaje de repostaje a vehiculo
                    envio.put("repostaje","Repostaje");
                else//mandar mensaje ok a vehiculo
                    envio.put("repostaje","OK");
                enviar_mensaje(envio.toString(), "vehiculo");
            }
        }
    }
}
