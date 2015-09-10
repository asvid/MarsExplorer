package mars;

import jade.core.Agent;

public class SimpleAgent extends Agent {

    @Override
    protected void setup() {
        System.out.printf("Hello world, from %s\n", getAID().getName());
        System.out.printf("map1: " + Main.map);
        Main.setMapString("jaki inny string");
        System.out.printf("map2: " + Main.map);
    }

    @Override
    protected void takeDown() {
        System.out.printf("Taking down agent, from %s\n", getAID().getName());
    }
}
