package practica;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;

public class Agente extends SingleAgent{
    
    public Agente(AgentID aid) throws Exception  {
        super(aid);
    }
    
    // public void init()

    @Override
    public void execute()  {
        System.out.println("\n\nHola mundo soy un agente\n");
    }
    
    //public void finalize()
}
