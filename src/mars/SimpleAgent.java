package mars;

import jade.core.Agent;

public class SimpleAgent extends Agent {
    @Override
    protected void setup() {
        System.out.printf("Hello world, from %s\n", getAID().getName());
    }

    @Override
    protected void takeDown() {
        System.out.printf("Taking down agent, from %s\n", getAID().getName());
    }
}
