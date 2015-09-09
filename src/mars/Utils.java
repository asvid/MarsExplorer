package mars;

import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.ContainerID;
import jade.core.ServiceException;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.security.JADEPrincipal;

/**
 * Created by Adam on 2015-09-07.
 */
public class Utils {

    /*****************************************************************
     * Very interesting function to start any agent of any class on any container
     * agent is the agent, from where you want to create another agent. The other variables are
     * should be self-explanatory.
     *****************************************************************/
    static void startAgent(Agent agent, String agentName, String containerName, String className, String arg[], String ownerName) {
        CreateAgent ca = new CreateAgent();

        if(containerName.equals(""))
            containerName = AgentContainer.MAIN_CONTAINER_NAME;

        // fill the create action with the intended agent owner
        jade.security.JADEPrincipal intendedOwner = null;
        jade.security.Credentials initialCredentials = null;

        if ((ownerName==null) || (ownerName.trim().length()==0)) {
            // it is requested the creation of an agent
            // with the same owner of the RMA
            try {
                jade.security.JADEPrincipal rmaOwner = null;
                jade.security.Credentials rmaCredentials = null;
                jade.security.CredentialsHelper ch = (jade.security.CredentialsHelper) agent.getHelper("jade.core.security.Security");
                // get RMA's owner
                if (ch!=null) {  rmaCredentials = ch.getCredentials();    }
                if (rmaCredentials!=null) {  rmaOwner = rmaCredentials.getOwner();    }
                intendedOwner = rmaOwner;
            }
            catch (ServiceException se) { // Security service not present. Owner is null.
                intendedOwner=null;
                initialCredentials=null;
            }

        } else {
            // it is requested the creation of an agent
            // with a specified owner name
            try
            {
                Class c = Class.forName("jade.security.impl.JADEPrincipalImpl");
                intendedOwner = (JADEPrincipal) c.newInstance();
                java.lang.reflect.Method setName = c.getDeclaredMethod("setName", new Class[]{ String.class });
                setName.invoke(intendedOwner, new Object[] {ownerName});
            } catch (Exception e)
            {
                //e.printStackTrace();
                // Security service not present. Owner is null.
                intendedOwner=null;
                initialCredentials=null;
            }
        }

        ca.setOwner( intendedOwner );
        ca.setInitialCredentials( initialCredentials );

        ca.setAgentName(agentName);
        ca.setClassName(className);
        ca.setContainer(new ContainerID(containerName, null));
        if (arg != null) {
            for(int i = 0; i<arg.length ; i++) {
                ca.addArguments((Object)arg[i]);
            }
        }

        try {

            Action a = new Action();
            a.setActor(agent.getAMS());
            a.setAction(ca);

            ACLMessage requestMsg = new ACLMessage(ACLMessage.REQUEST);
            requestMsg.setSender(agent.getAID());
            requestMsg.addReceiver(agent.getAMS());
            requestMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            requestMsg.setLanguage(FIPANames.ContentLanguage.FIPA_SL);

            requestMsg.setOntology(JADEManagementOntology.NAME);
            agent.getContentManager().fillContent(requestMsg, a);
            SimpleAchieveREInitiator initiator = new SimpleAchieveREInitiator(agent, requestMsg);
            agent.addBehaviour(initiator);



        }
        catch(Exception fe) {
            fe.printStackTrace();
        }

    }

}
