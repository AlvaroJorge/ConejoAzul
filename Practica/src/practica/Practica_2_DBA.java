/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;
import es.upv.dsic.gti_ia.core.SingleAgent;
/**
 *
 * @author alex
 */
public class Practica_2_DBA {

    /**
     * @author Alejandro
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        SingleAgent vehiculo;
        SingleAgent reconocimiento;
        SingleAgent repostaje;
        AgentsConnection.connect("isg2.ugr.es",6000,"Achernar","Leon","Matute",false);
        System.out.println("\n");
        reconocimiento = new Reconocimiento(new AgentID("reconocimiento15"));
        repostaje = new Repostaje(new AgentID("repostaje15"));
        repostaje.start(); 
        reconocimiento.start();
        vehiculo = new Vehiculo(new AgentID("vehiculo15"));
        vehiculo.start();
    }
    
}