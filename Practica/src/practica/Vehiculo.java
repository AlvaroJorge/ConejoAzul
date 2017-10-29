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
    private boolean finalizar;
    
    public Vehiculo(AgentID aid) throws Exception{
        super(aid);
        outbox = null;
        inbox = null;
        repostaje=false;
        finalizar = false;
        
        conexion();   
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
    
    public void recibir_mensaje() throws InterruptedException, JSONException{
        inbox = receiveACLMessage();
        recepcion = new JSONObject(inbox.getContent());
        recepcion_plano = recepcion.toString();
        System.out.println("Vehiculo: " + recepcion_plano);
    }
    
    @Override
    public void init(){
        System.out.println("Vehiculo: vivo");
    }
    
    @Override
    public void execute(){
        boolean primero = true;
        while(!finalizar){
            try {
                if(primero)
                    recibir_mensaje();
                recibir_mensaje();
                actuar();
                primero = false;
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
                enviar_mensaje(envio.toString(),"Achernar");
                envio = new JSONObject();
                envio.put("vehiculo","cerrar");
                enviar_mensaje(envio.toString(),"Repostaje");
                enviar_mensaje(envio.toString(),"Reconocimiento");
            } catch (JSONException ex) {
            Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Vehiculo terminado");
        } finally {
            super.finalize();
        }
    }
    
    public void actuar() throws JSONException, InterruptedException{
        if(recepcion.has("repostaje")){
            recepcion_plano = recepcion.getString("mensaje");
            if(recepcion_plano.equals("Repostar")){
                repostaje = true;
                envio = new JSONObject();
                envio.put("command","refuel");
                envio.put("key",key);
                enviar_mensaje("Achernar",envio.toString());
            }
            else
                finalizar = true;
        }else if(recepcion.has("pensamiento")){
            if(!repostaje){
                //Comprobar el mensaje de movimiento de reconocimiento
                recepcion_plano = recepcion.getString("mensaje");
                //Enviar el movimiento al servidor
                envio = new JSONObject();
                envio.put("command",recepcion_plano);
                envio.put("key",key);
                enviar_mensaje("Achernar",envio.toString());
            }else //Si se acaba de repostar no hacemos nada
                repostaje = false;
        }else{
            if(!recepcion.toString().equals("CRASHED")){
                if(recepcion.has("result")){
                    recepcion_plano = recepcion.getString("result");
                    if(!recepcion_plano.equals("BAD_MAP")&&!recepcion_plano.equals("BAD_PROTOCOL")&&!recepcion_plano.equals("BAD_KEY")
                            &&!recepcion_plano.equals("BAD_COMMAND")&&!recepcion_plano.equals("OK"))
                        key = recepcion.getString("result");    //Recibimos la key
                    else if(!recepcion_plano.equals("OK"))
                        finalizar = true;
                }
            }else
                finalizar = true;
        }
    }
}
