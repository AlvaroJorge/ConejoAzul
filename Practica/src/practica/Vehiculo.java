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
    private boolean reconocimiento;
    private String mensaje_repostar;
    private String mensaje_movimiento;
    
    public Vehiculo(AgentID aid) throws Exception{
        super(aid);
        outbox = null;
        inbox = null;
        repostaje=false;
        finalizar = false;
        reconocimiento = false;
        
        conexion();   
    }
    
    public void conexion() throws JSONException{
        envio = new JSONObject();
        envio.put("command","login");
        envio.put("world","map1");
        envio.put("radar","reconocimiento14");
        envio.put("scanner","reconocimiento14");
        envio.put("battery","repostaje14");
        envio.put("gps","reconocimiento14");
        enviar_mensaje(envio.toString(), "Achernar");
    }
    
    public void enviar_mensaje(String mensaje, String receptor){
        outbox = new ACLMessage();
        outbox.setSender(getAid());
        outbox.setReceiver(new AgentID(receptor));
        outbox.setContent(mensaje);
        this.send(outbox);
        System.out.println(mensaje);
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
        while(!finalizar){
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
                envio.put("vehiculo","cerrar");
                enviar_mensaje(envio.toString(),"repostaje14");
                enviar_mensaje(envio.toString(),"reconocimiento14");
                envio = new JSONObject();
                envio.put("command","logout");
                envio.put("key",key);
                enviar_mensaje(envio.toString(),"Achernar");
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
            recepcion_plano = recepcion.getString("repostaje");
            repostaje = true;
            mensaje_repostar = recepcion_plano;
        }else if(recepcion.has("pensamiento")){
            recepcion_plano = recepcion.getString("pensamiento");
            if(recepcion_plano.equals("llegada")){
                System.out.println("Vehiculo: llegue, finalizando agentes.");   
                finalizar = true;
            }
            else{
                reconocimiento = true;
                mensaje_movimiento = recepcion_plano;
            }
        }else{
            if(recepcion.has("result")){
                recepcion_plano = recepcion.getString("result");
                if(!recepcion_plano.equals("BAD_MAP")&&!recepcion_plano.equals("BAD_PROTOCOL")&&!recepcion_plano.equals("BAD_KEY")
                        &&!recepcion_plano.equals("BAD_COMMAND")&&!recepcion_plano.equals("OK")&&!recepcion_plano.equals("CRASHED"))
                    key = recepcion.getString("result");    //Recibimos la key
                else if(!recepcion_plano.equals("OK"))
                    finalizar = true;
            }
        }      
        
        if(reconocimiento && repostaje){
            if(mensaje_repostar.equals("Repostaje")){
                repostaje = true;
                envio = new JSONObject();
                envio.put("command","refuel");
                envio.put("key",key);
                enviar_mensaje(envio.toString(),"Achernar");
                System.out.println("Enviado al servidor repostar");
            }else{
                envio = new JSONObject();
                envio.put("command",mensaje_movimiento);
                envio.put("key",key);
                enviar_mensaje(envio.toString(),"Achernar");
                System.out.println("Enviado al servidor movimiento");     
            }
            reconocimiento = false;
            repostaje = false;
        }
    }
}
