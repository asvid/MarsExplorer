package mars;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.Random;

public class Exolorer extends Agent {

    private final Random random = new Random();

    private Double motherShipDirection;
    private Double motherShipDistance;

    private boolean hasSample = false;

    @Override
    protected void setup() {
        System.out.printf("Hello world, from %s\n", getAID().getName());

        addBehaviour(new TickerBehaviour(this, 200) {
            protected void onTick() {
                System.out.println("explorer: " + hasSample + " dist: " + motherShipDistance + "angle: " + motherShipDirection);
                if (hasSample && motherShipDistance == 0) {
                    hasSample = false;
                    move(Map.Direction.values()[random.nextInt(Map.Direction.values().length)]);
                } else if (hasSample && motherShipDistance > 0) {
                    move(goToMothership());
                } else {
                    move(Map.Direction.values()[random.nextInt(Map.Direction.values().length)]);
                }
            }

            private void move(Map.Direction direction) {
                ExplorerInfo explorerInfo = Map.move(getAID().getName(), direction);
                motherShipDistance = explorerInfo.distance;
                motherShipDirection = explorerInfo.angle;
                if (!hasSample)
                    hasSample = explorerInfo.foundMineral;
                //System.out.println("mapinfo: " + getAID().getName() + " : " + mapInfo[0] + " / " + mapInfo[1]);
            }

            private Map.Direction goToMothership() {
                Map.Direction direction = null;
                if (motherShipDirection != null) {
                    System.out.println("direction: " + motherShipDirection);
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
        });
    }

    @Override
    protected void takeDown() {
        System.out.printf("Taking down agent, from %s\n", getAID().getName());
    }
}
