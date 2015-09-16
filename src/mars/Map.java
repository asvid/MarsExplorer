package mars;

import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by Adam on 2015-09-10.
 */
public class Map {

    private static final Random random = new Random();
    private static Fields[][] mapArray = new Fields[41][41];
    private static boolean[][] mineralArray = new boolean[41][41];
    private static boolean init = true;
    private static int middle = mapArray.length / 2;
    private static String styles = "<style type=\"text/css\">\n" +
            ".tg  {border-collapse:collapse;border-spacing:0;}\n" +
            ".tg td{padding:5px;border-style:solid;border-width:1px;}\n" +
            ".EMPTY{\n" +
            "\tbackground: #FF9900;\n" +
            "}\n" +
            ".MOTHERSHIP{\n" +
            "\tbackground: #0A0A0A;\n" +
            "}\n" +
            ".EXPLORER{\n" +
            "\tbackground: #666666;\n" +
            "}\n" +
            ".MINERAL{\n" +
            "\tbackground: #00FFFF;\n" +
            "}\n" +
            "</style>";

    public Map() {
        for (int i = 0, l = mapArray.length; i < l; i++) {
            for (int j = 0; j < l; j++) {
                mapArray[i][j] = Fields.EMPTY;
            }
        }

        mapArray[middle][middle] = Fields.MOTHERSHIP;
        randomMinerals();

    }

    public static String getHtml() {
        String html = styles + "<table class=\"tg\">\n";

        for (int i = 0, l = mapArray.length; i < l; i++) {
            html += "<tr>";
            for (int j = 0; j < l; j++) {
                html += "<td class='" + mapArray[i][j] + "'>";
                html += "</td>";
            }
            html += "</tr>";
        }

        return html + "</table>";
    }

    public static void generate() {
        for (int i = 0, l = mapArray.length; i < l; i++) {
            for (int j = 0; j < l; j++) {
                if (mapArray[i][j] == Fields.EMPTY || mapArray[i][j] == Fields.EXPLORER)
                    mapArray[i][j] = Fields.EMPTY;
            }
        }
        drawExplorers();
        drawMinerals();
        mapArray[middle][middle] = Fields.MOTHERSHIP;
    }

    private static void drawExplorers() {
        for (int[] h : Controller.agentMapList.values())
            mapArray[h[1]][h[0]] = Fields.EXPLORER;
    }

    private static void drawMinerals() {
        for (int i = 0, l = mapArray.length; i < l; i++) {
            for (int j = 0; j < l; j++) {
                if (mineralArray[i][j])
                    mapArray[i][j] = Fields.MINERAL;
            }
        }
    }

    private static void randomMinerals() {
        for (int i = 0, l = mineralArray.length; i < l; i++) {
            for (int j = 0; j < l; j++) {
                if (mapArray[i][j] == Fields.EMPTY) {
                    if (random.nextInt(100) < 10 && init) {
                        mineralArray[i][j] = true;
                    } else {
                        mineralArray[i][j] = false;
                    }
                }
            }
        }
        init = false;
    }

    public static ExplorerInfo move(String name, Direction d) {
        int[] curPos = Controller.agentMapList.get(name);
        switch (d) {
            case E:
                if (curPos[0] < mapArray.length)
                    curPos[0] += 1;
                break;
            case W:
                if (curPos[0] > 0)
                    curPos[0] -= 1;
                break;
            case N:
                if (curPos[1] > 0)
                    curPos[1] -= 1;
                break;
            case S:
                if (curPos[1] < mapArray.length)
                    curPos[1] += 1;
                break;
        }
        ;

        Double angle = Math.toDegrees(Math.atan2(curPos[1] - middle, curPos[0] - middle));
        angle += 90;
        if (angle < 0) {
            angle += 360;
        }
        Double dist = sqrt(pow((curPos[0] - middle), 2) + pow((curPos[1] - middle), 2));
        ExplorerInfo explorerInfo = new ExplorerInfo(foundSample(name), dist, angle);
        generate();
        return explorerInfo;
    }

    public static boolean foundSample(String name) {
        boolean founded = false;
        int[] curPos = Controller.agentMapList.get(name);
        founded = mapArray[curPos[1]][curPos[0]] == Fields.MINERAL;
        System.out.println("Sample: " + founded + " / " + curPos + " / " + mapArray[curPos[1]][curPos[0]]);
        return founded;
    }

    public static void collectSample(String name) {
        int[] pos = Controller.agentMapList.get(name);
        mineralArray[pos[1]][pos[0]] = false;
    }

    private enum Fields {EMPTY, MOTHERSHIP, EXPLORER, MINERAL}

    public static enum Direction {N, E, S, direction, W}
}
