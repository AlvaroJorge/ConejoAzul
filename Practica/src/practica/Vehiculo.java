/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author alex
 */
public class Vehiculo extends SingleAgent{
    //prueba
    private int mapa;
    private JSONObject envio;
    private JSONObject recepcion;
    private ACLMessage outbox, inbox;
    private String key;
    private String recepcion_plano;
    private boolean repostaje;
    
    public Vehiculo(AgentID aid) throws Exception{
        super(aid);
        outbox = null;
        inbox = null;
        repostaje=false;
        
        conexion();   
    }
    
    public void mover(JSONObject movimiento){
    }
    
    public void conexion() throws JSONException{
        envio = new JSONObject();
        envio.put("command","login");
        envio.put("world","map1");
        envio.put("radar","reconocimiento");
        envio.put("scanner","reconocimiento");
        envio.put("battery","repostaje");
        envio.put("gps","reconocimiento");
        enviar_mensaje(envio.toString(), "Achernar");
    }
    
    public void enviar_mensaje(String mensaje, String receptor){
        outbox = new ACLMessage();
        outbox.setSender(getAid());
        outbox.setReceiver(new AgentID(receptor));
        outbox.setContent(mensaje);
        this.send(outbox);
    }
    
    public void escuchar_Reconocimiento(){
        
    }
    
    public void solicitar_movimiento(){
        
    }
    
    public void recibir_mensaje() throws InterruptedException, JSONException{
        inbox = receiveACLMessage();
        recepcion = new JSONObject(inbox.getContent());
        recepcion_plano = recepcion.toString();
        System.out.println("Vehiculo: " + recepcion_plano);
    }
    
    @Override
    public void init(){
    }
    
    @Override
    public void execute(){
        boolean primero = true;
        while(true){
            try {
                recibir_mensaje();
                actuar();
            } catch (InterruptedException | JSONException ex) {
                Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void finalize(){
        try {
            envio = new JSONObject();
            try {
                envio.put("command","logout");
                envio.put("key",key);
            } catch (JSONException ex) {
            Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
            enviar_mensaje(envio.toString(),"Achernar");
            System.out.println("Vehiculo terminado");
        } finally {
            super.finalize();
        }
    }
    
    public void actuar() throws JSONException, InterruptedException{
        /*if(mensajero.equals("Achernar")){
            if(!recepcion.toString().equals("CRASHED")){
                if(recepcion.has("result")){
                    recepcion_plano = recepcion.getString("result");
                    if(!recepcion_plano.equals("BAD_MAP")&&!recepcion_plano.equals("BAD_PROTOCOL")&&!recepcion_plano.equals("BAD_KEY")
                            &&!recepcion_plano.equals("BAD_COMMAND")&&!recepcion_plano.equals("OK"))
                        key = recepcion.getString("result");    //Recibimos la key
                    else if(!recepcion_plano.equals("OK")){
                        //FINALIZAR AGENTES
                        finalize();
                    }
                }
            }else{
                finalize();
            }
        }else if(mensajero.equals("repostaje")){
            recepcion_plano = recepcion.getString("mensaje");
            if(recepcion_plano.equals("Repostar")){
                repostaje = true;
                envio = new JSONObject();
                envio.put("command","refuel");
                envio.put("key",key);
                enviar_mensaje("Achernar",envio.toString());
            }
        }else if(mensajero.equals("reconocimiento")){
            if(!repostaje) {
                //Comprobar el mensaje de movimiento de reconocimiento
                recepcion_plano = recepcion.getString("mensaje");
                //Enviar el movimiento al servidor
                envio = new JSONObject();
                envio.put("command",recepcion_plano);
                envio.put("key",key);
                enviar_mensaje("Achernar",envio.toString());
            } else //Si se acaba de repostar no hacemos nada
                repostaje = false;
        }*/
    }
}
