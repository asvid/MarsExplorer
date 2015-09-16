package mars;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.Random;

public class Exolorer extends Agent {

    private final Random random = new Random();
    public boolean hasSample = false;
    private Double motherShipDirection;
    private Double motherShipDistance;
    private Map.Direction prev;

    @Override
    protected void setup() {
        System.out.printf("Hello world, from %s\n", getAID().getName());

        addBehaviour(new TickerBehaviour(this, 200) {
            protected void onTick() {
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

            private void move(Map.Direction direction) {
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
            }

            private boolean goingBack(Map.Direction direction) {
                if (prev != null) {
                    int dirIndex = Map.Direction.valueOf(direction.toString()).ordinal();
                    int prevIndex = Map.Direction.valueOf(prev.toString()).ordinal();
                    System.out.println("indexes: " + dirIndex + " / " + prevIndex);
                    return dirIndex == 0 && prevIndex == 2 || dirIndex == 1 && prevIndex == 3 || dirIndex == 2 && prevIndex == 1 || dirIndex == 3 && prevIndex == 1;
                } else return false;

            }

            private Map.Direction goToMothership() {
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
        });
    }

    @Override
    protected void takeDown() {
        System.out.printf("Taking down agent, from %s\n", getAID().getName());
    }
}
