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
import org.codehaus.jettison.json.JSONArray;
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
    private float scanner[][];
    private int radar[][];
    private JSONObject envio;
    private JSONObject recepcion;
    private ACLMessage outbox, inbox;
    private String recepcion_plano;
    
    public Reconocimiento(AgentID aid) throws Exception {
        super(aid);
        radar = new int [5][5]; 
        scanner = new float[5][5];
    }
    
    public void actualizar_GPS() throws JSONException{
        recepcion = recepcion.getJSONObject("gps"); 
        posicion_x = recepcion.getInt("x");
        posicion_y = recepcion.getInt("y");
        System.out.println("Reconocimiento: actualizo gps: x: " + posicion_x + " y: " + posicion_y);
    }
    
    public void actualizar_Radar() throws JSONException{
        JSONArray rad = recepcion.getJSONArray("radar");
        for(int i = 0; i < rad.length(); i+=5)
            for(int j = 0; j < 5; j++)
                radar[i/5][j] = rad.getInt(i+j);
        System.out.println("Reconocimiento: actualizo radar");
    }
    
    public void actualizar_Scanner() throws JSONException{
        JSONArray scan = recepcion.getJSONArray("scanner");
        for(int i = 0; i < scan.length(); i+=5)
            for(int j = 0; j < 5; j++)
                scanner[i/5][j] = scan.getInt(i+j);
        System.out.println("Reconocimiento: actualizo scanner");
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
        outbox = new ACLMessage();
        outbox.setSender(getAid());
        outbox.setReceiver(new AgentID(receptor));
        outbox.setContent(mensaje);
        this.send(outbox);
    }
    
    public void recibir_mensaje(String mensajero) throws InterruptedException, JSONException{
        inbox = receiveACLMessage();
        recepcion = new JSONObject(inbox.getContent());
        if(recepcion.has("scanner"))
            actualizar_Scanner();
        else if(recepcion.has("gps"))
            actualizar_GPS();
        else if(recepcion.has("radar"))
            actualizar_Radar();
        recepcion_plano = recepcion.toString();
        System.out.println("Reconocimiento: " + mensajero + ": " + recepcion_plano);
    }
    
    @Override
    public void execute(){
        while(true){
            try {
                recibir_mensaje("Achernar");
                recibir_mensaje("Achernar");
                recibir_mensaje("Achernar");
                actuar("Achernar");
            } catch (InterruptedException | JSONException ex) {
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
    
    @Override
    public void init(){
        System.out.println("Reconocimiento: vivo");
    }
    
    public void actuar(String mensajero) throws JSONException{
        
        if(mensajero.equals("Achernar")){
            System.out.println("Reconocimiento: " + recepcion_plano);
            if(recepcion_plano.equals("CRASHED")){
                //Finalizar agente
                finalize();
            }else
                /*envio = new JSONObject();
                if(bateria <= 2)//mandar mensaje de repostaje a vehiculo
                    envio.put("mensaje","Repostaje");
                else//mandar mensaje ok a vehiculo
                    envio.put("mensaje","OK");
                enviar_mensaje(envio.toString(), "vehiculo");*/
            System.out.println("Reconocimiento: " + recepcion_plano);
        }else
            if(!recepcion_plano.equals("OK"))
                finalize();
    }
}
