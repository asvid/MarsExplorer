package mars;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    public static ArrayList<AgentController> agentList = new ArrayList<>();
    public static HashMap<String, int[]> agentMapList = new HashMap<>();
    @FXML
    public WebView webView;
    @FXML
    private Button button1;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert button1 != null : "fx:id=\"myButton\" was not injected: check your FXML file 'simple.fxml'.";
        button1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                startAgents();
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                webView.getEngine().loadContent(Map.getHtml());
                            }
                        });
                    }
                }, 0, 100);
            }
        });

    }

    private void startAgents() {
        Runtime rt = Runtime.instance();
        rt.setCloseVM(true);
        Profile profile = new ProfileImpl(null, 1200, null);
        jade.wrapper.AgentContainer mainContainer = rt.createMainContainer(profile);
        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        try {
            agentList.add(mainContainer.createNewAgent("ex1", Exolorer.class.getName(), new Object[0]));
            agentList.add(mainContainer.createNewAgent("ex2", Exolorer.class.getName(), new Object[0]));
            agentList.add(mainContainer.createNewAgent("ex3", Exolorer.class.getName(), new Object[0]));
//          agentList.add(mainContainer.createNewAgent("b1", mars.BookBuyerAgent.class.getName(), new String[]{"a", "b"}));

            for (int i = 0, l = agentList.size(); i < l; i++) {
                agentList.get(i).start();

            }
            agentMapList.put(agentList.get(0).getName(), new int[]{15, 15});
            agentMapList.put(agentList.get(1).getName(), new int[]{20, 20});
            agentMapList.put(agentList.get(2).getName(), new int[]{10, 10});

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void destroy() throws StaleProxyException {
        System.out.println("papa");
        for (int i = 0, l = agentList.size(); i < l; i++) {
            agentList.get(i).kill();
        }
    }

    class SayHello extends TimerTask {
        public void run() {

        }
    }
}
