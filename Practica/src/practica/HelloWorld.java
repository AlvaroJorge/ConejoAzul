package practica;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        // Conectar a la plataforma de agentes
        AgentsConnection.connect("localhost",5672,"test","guest", "guest", false);
        Agente smith = new Agente(new AgentID("Smith"));
        smith.start();
        
    }
}
