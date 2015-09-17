package mars;

/**
 * Created by Adam on 2015-09-16.
 */
public class Logger {
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

    private static String log = new String("");

    public static void log(String str) {
        log = (str+"<br>") + log;
    }

    public static String getHtml() {
        String html = styles + log;

        return html;
    }

    public static void reset() {
        log = "";
    }
}
