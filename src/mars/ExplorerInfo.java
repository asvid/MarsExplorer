package mars;

/**
 * Created by Adam on 2015-09-15.
 */
public class ExplorerInfo {

    public boolean foundMineral;
    public Double distance;
    public Double angle;

    public ExplorerInfo(boolean foundMineral, Double distance, Double angle) {
        this.foundMineral = foundMineral;
        this.distance = distance;
        this.angle = angle;
    }
}
