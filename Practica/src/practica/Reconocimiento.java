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
import java.util.Random;
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
    private int camino_recorrido[][];
    private float scanner[][];
    private int radar[][];
    private JSONObject envio;
    private JSONObject recepcion;
    private ACLMessage outbox, inbox;
    private String recepcion_plano;
    private boolean finalizar;
    private int pasos;
    
    //public String movimiento;
    Random r;
    
    public Reconocimiento(AgentID aid) throws Exception {
        super(aid);
        
        radar = new int [5][5]; 
        scanner = new float[5][5];
        finalizar = false;
        pasos = 0;
        
        r = new Random();
        
        camino_recorrido = new int[1000][1000];
        
        for(int i=0; i<1000; i++){
            for(int j=0; j<1000; j++){
                camino_recorrido[i][j] = -1;
            }
        }
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
        
        System.out.print("Reconocimiento: actualizo radar ");
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                System.out.print(radar[i][j] + ", ");
    }
    
    public void actualizar_Scanner() throws JSONException{
        JSONArray scan = recepcion.getJSONArray("scanner");
        for(int i = 0; i < scan.length(); i+=5)
            for(int j = 0; j < 5; j++)
                scanner[i/5][j] = scan.getInt(i+j);
        
        System.out.print("Reconocimiento: actualizo scanner ");
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                System.out.print(scanner[i][j] + ", ");
    }
    
    public void actualizarMatrizAuxiliar(){
        camino_recorrido[1000/2 + posicion_y][1000/2 + posicion_x] = pasos; 
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
        if(!inbox.getContent().equals("\"CRASHED\"")){
            recepcion = new JSONObject(inbox.getContent());
            recepcion_plano = recepcion.toString();
            if(recepcion.has("scanner"))
                actualizar_Scanner();
            else if(recepcion.has("gps"))
                actualizar_GPS();
            else if(recepcion.has("radar"))
                actualizar_Radar();
            System.out.println("\n");
        }else
            finalizar = true;
    }
    
    @Override
    public void execute(){
        int contador = 0;
        while(!finalizar){
            try {
                recibir_mensaje();
                contador++;
                if(contador % 3 == 0){
                    actuar();
                }
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
    
    
    public void actuar() throws JSONException{
        
        if(recepcion.has("vehiculo")){
            if(recepcion.getString("vehiculo").equals("cerrar"))
                finalizar = true;
        }else{//IMPLEMENTAR
            if(radar[2][2] == 2){
                envio = new JSONObject();
                envio.put("pensamiento","llegada");
                System.out.println("Reconocimiento Actuar: objetivo encontrado con exito");
                enviar_mensaje(envio.toString(),"vehiculo15");
                finalizar = true;
            }
            else{
                
                actualizarMatrizAuxiliar();
                
                envio = new JSONObject();                          
                String movimiento = "";
                
                
                
                
                int menor_paso = 50000;
                float menor_distancia = 50000;
                
                if(radar[1][1] != 1 && camino_recorrido[1000/2 + posicion_y-1][1000/2 + posicion_x-1] <= menor_paso && scanner[1][1] <= menor_distancia){
                    movimiento = "moveNW";                      
                    menor_paso = camino_recorrido[1000/2 + posicion_y-1][1000/2 + posicion_x-1];
                    menor_distancia = scanner[1][1];
                }
                
                if(radar[1][2] != 1 && camino_recorrido[1000/2 + posicion_y-1][1000/2 + posicion_x] <= menor_paso && scanner[1][2] <= menor_distancia){
                        movimiento = "moveN";
                        menor_distancia = scanner[1][2];
                        menor_paso = camino_recorrido[1000/2 + posicion_y-1][1000/2 + posicion_x]; 
                }             
               
                if(radar[1][3] != 1 && camino_recorrido[1000/2 + posicion_y-1][1000/2 + posicion_x+1] <= menor_paso && scanner[1][3] <= menor_distancia){
                    movimiento = "moveNE";
                    menor_paso = camino_recorrido[1000/2 + posicion_y-1][1000/2 + posicion_x+1];
                    menor_distancia = scanner[1][3];
                }
                
               if(radar[2][1] != 1 && camino_recorrido[1000/2 + posicion_y][1000/2 + posicion_x-1] <= menor_paso && scanner[2][1] <= menor_distancia){
                    movimiento = "moveW";
                    menor_distancia = scanner[2][1];
                    menor_paso = camino_recorrido[1000/2 + posicion_y][1000/2 + posicion_x-1];
                }
                
                if(radar[2][3] != 1 && camino_recorrido[1000/2 + posicion_y][1000/2 + posicion_x+1] <= menor_paso && scanner[2][3] <= menor_distancia){
                    movimiento = "moveE";         
                    menor_distancia = scanner[2][3];
                    menor_paso = camino_recorrido[1000/2 + posicion_y][1000/2 + posicion_x+1];             
                }
                
                if(radar[3][1] != 1 && camino_recorrido[1000/2 + posicion_y+1][1000/2 + posicion_x-1] <= menor_paso && scanner[3][1] <= menor_distancia){
                    movimiento = "moveSW";
                    menor_paso = camino_recorrido[1000/2 + posicion_y+1][1000/2 + posicion_x-1];
                    menor_distancia = scanner[3][1];                 
                }
                
               if(radar[3][2] != 1 && camino_recorrido[1000/2 + posicion_y+1][1000/2 + posicion_x] <= menor_paso && scanner[3][2] <= menor_distancia){
                    movimiento = "moveS";
                    menor_paso = camino_recorrido[1000/2 + posicion_y+1][1000/2 + posicion_x];
                    menor_distancia = scanner[3][2];
                }
                
                if(radar[3][3] != 1 && camino_recorrido[1000/2 + posicion_y+1][1000/2 + posicion_x+1] <= menor_paso && scanner[3][3] <= menor_distancia){
                    movimiento = "moveSE";
                    menor_paso = camino_recorrido[1000/2 + posicion_y+1][1000/2 + posicion_x+1];
                    menor_distancia = scanner[3][3];
                }
                
                
                if(radar[1][1] == 2){
                    movimiento = "moveNW";
                }    
                
                if(radar[1][2] == 2){
                    movimiento = "moveN";
                }
                
                if(radar[1][3] == 2){
                    movimiento = "moveNE";
                }
                
                if(radar[2][1] == 2){
                    movimiento = "moveW";
                }
                
                if(radar[2][3] == 2){
                    movimiento = "moveE";
                }
                
                if(radar[3][1] == 2){
                    movimiento = "moveSW";
                }
                
                if(radar[3][2] == 2){
                    movimiento = "moveS";
                }
                
                if(radar[3][3] == 2){
                    movimiento = "moveSE";
                }
               
                //movimiento = "moveSW";
                envio.put("pensamiento", movimiento);
                System.out.println("Reconocimiento Actuar: moviendo");
                enviar_mensaje(envio.toString(),"vehiculo15");
                pasos++;
                System.out.println("Reconocimiento pasos:" + pasos);
            }
        }
    }
    
}
