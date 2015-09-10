package mars;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.Random;

public class SimpleAgent extends Agent {

    private final Random random = new Random();

    @Override
    protected void setup() {
        System.out.printf("Hello world, from %s\n", getAID().getName());
        System.out.printf("map1: " + Main.map);
        Main.setMapString("jaki inny string");
        System.out.printf("map2: " + Main.map);

        addBehaviour(new TickerBehaviour(this, 1000) {
            protected void onTick() {
                Map.move(Map.Direction.values()[random.nextInt(Map.Direction.values().length)]);
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.printf("Taking down agent, from %s\n", getAID().getName());
    }
}
