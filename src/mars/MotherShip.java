package mars;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by adam on 17.09.15.
 */
public class MotherShip extends Agent {

    @Override
    protected void setup() {
        System.out.printf("Hello world, from mothership: %s\n", getAID().getLocalName());
// Register the book-selling service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("sample-delivery");
        sd.setName("MotherShip");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new TakeMineral());
        addBehaviour(new SendSignal());

    }

    @Override
    protected void takeDown() {
        System.out.printf("Taking down agent, from %s\n", getAID().getName());
    }

    class TakeMineral extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();
                System.out.println("mothership: "+title + " / " + reply);
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                myAgent.send(reply);
                Logger.log(getAID().getLocalName() + " pobiera minerał od: " + msg.getSender().getLocalName());
            }
            else {
                block();
            }
        }
    }
    class SendSignal extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();
                System.out.println("mothership: "+title + " / " + reply);
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }

}
