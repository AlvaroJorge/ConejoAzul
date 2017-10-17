/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;
import es.upv.dsic.gti_ia.core.SingleAgent;
import practica.Reconocimiento;
import practica.Repostaje;
import practica.Vehiculo;
import static java.lang.Thread.sleep;

/**
 *
 * @author alex
 */
public class Practica_2_DBA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //HOLA
        // TODO code application logic here
        SingleAgent vehiculo;
        SingleAgent reconocimiento;
        SingleAgent repostaje;
        AgentsConnection.connect("isg2.ugr.es",6000,"Achernar","Leon","Matute",false);
        vehiculo = new Vehiculo(new AgentID("vehiculo"));
        reconocimiento = new Reconocimiento(new AgentID("reconocimineto"));
        repostaje = new Repostaje(new AgentID("repostaje"));
        System.out.println("\n");
        vehiculo.start();
        repostaje.start();
        reconocimiento.start();
    }
    
}
