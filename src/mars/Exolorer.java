package mars;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.Random;

public class Exolorer extends Agent {

    private final Random random = new Random();
    public boolean hasSample = false;
    private Double motherShipDirection;
    private Double motherShipDistance;
    private Map.Direction prev;
    private AID[] motherShip;

    private Agent self = this;


    public void move(Map.Direction direction) {
        while (goingBack(direction)) {
            direction = Map.Direction.values()[random.nextInt(Map.Direction.values().length)];
        }
        ExplorerInfo explorerInfo = Map.move(getAID().getName(), direction);
        motherShipDistance = explorerInfo.distance;
        motherShipDirection = explorerInfo.angle;
        if (!hasSample && explorerInfo.foundMineral) {
            hasSample = true;
            Map.collectSample(getAID().getName());
            Logger.log(getAID().getLocalName() + " : zmalazł minerał");
        }
        prev = direction;

        ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
        order.addReceiver(motherShip[0]);
        order.setContent("ping");
        order.setConversationId("sample-delivery");
        order.setReplyWith("ping" + System.currentTimeMillis());
        self.send(order);
    }

    public boolean goingBack(Map.Direction direction) {
        if (prev != null) {
            int dirIndex = Map.Direction.valueOf(direction.toString()).ordinal();
            int prevIndex = Map.Direction.valueOf(prev.toString()).ordinal();
            return dirIndex == 0 && prevIndex == 2 || dirIndex == 1 && prevIndex == 3 || dirIndex == 2 && prevIndex == 1 || dirIndex == 3 && prevIndex == 1;
        } else return false;

    }

    public Map.Direction goToMothership() {
        Map.Direction direction = null;
        if (motherShipDirection != null) {
            if (motherShipDirection >= 315 || motherShipDirection < 45) {
                direction = Map.Direction.S;
            } else if (motherShipDirection >= 45 && motherShipDirection < 135) {
                direction = Map.Direction.W;
            } else if (motherShipDirection >= 135 && motherShipDirection < 225) {
                direction = Map.Direction.N;
            } else if (motherShipDirection >= 225 && motherShipDirection < 315) {
                direction = Map.Direction.E;
            }
        }
        return direction;
    }

    @Override
    protected void setup() {
        System.out.printf("Hello world, from %s\n", getAID().getLocalName());
        addBehaviour(new UnloadMineral());
        addBehaviour(new LookAround());
        addBehaviour(new Work(this, 200));
        addBehaviour(new Ping());
    }

    class UnloadMineral extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                myAgent.send(reply);
                Logger.log(getAID().getLocalName() + " rozładował minerał w: " + msg.getSender().getLocalName());
                hasSample = false;
            }
            else {
                block();
            }
        }
    }
    class Ping extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {

                try {
                    ExplorerInfo info = (ExplorerInfo) msg.getContentObject();
                    hasSample = info.foundMineral;
                    motherShipDirection = info.angle;
                    motherShipDistance = info.distance;
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }

                try {
                    System.out.println(getAID().getLocalName() + " ping:" + msg.getContentObject().toString());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
            else {
                block();
            }
        }
    }

    class Work extends TickerBehaviour{
        public Work(Agent a, long period) {
            super(a, period);
        }

        protected void onTick() {
            if (hasSample && motherShipDistance == 0) {

                move(Map.Direction.values()[random.nextInt(Map.Direction.values().length)]);
                Logger.log(getAID().getLocalName() + " : wrócił do bazy");

                ACLMessage order = new ACLMessage(ACLMessage.PROPOSE);
                order.addReceiver(motherShip[0]);
                order.setContent("mam minerał");
                order.setConversationId("sample-delivery");
                order.setReplyWith("delivery" + System.currentTimeMillis());
                myAgent.send(order);

            } else if (hasSample && motherShipDistance > 0) {
                move(goToMothership());
                Logger.log(getAID().getLocalName() + " : wraca do bazy z minerałem");
            } else {
                move(Map.Direction.values()[random.nextInt(Map.Direction.values().length)]);
                Logger.log(getAID().getLocalName() + " : szuka...");
            }
        }
    }

    class LookAround extends OneShotBehaviour{
        @Override
        public void action() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("sample-delivery");
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(self, template);
                System.out.println("Found the following seller agents:");
                motherShip = new AID[result.length];
                for (int i = 0; i < result.length; ++i) {
                    motherShip[i] = result[i].getName();
                    System.out.println(motherShip[i].getName());
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
        }
    }

    public void runStep(){
        addBehaviour(new Step());
    }

    @Override
    protected void takeDown() {
        System.out.printf("Taking down agent, from %s\n", getAID().getName());
    }

    class Step extends OneShotBehaviour {

        @Override
        public void action() {
            if (hasSample && motherShipDistance == 0) {
                hasSample = false;
                move(Map.Direction.values()[random.nextInt(Map.Direction.values().length)]);
                Logger.log(getAID().getLocalName() + " : wrócił do bazy");
            } else if (hasSample && motherShipDistance > 0) {
                move(goToMothership());
                Logger.log(getAID().getLocalName() + " : wracam do bazy z minerałem");
            } else {
                move(Map.Direction.values()[random.nextInt(Map.Direction.values().length)]);
                Logger.log(getAID().getLocalName() + " : random move");
            }
        }
    }
}
