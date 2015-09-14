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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {
    @FXML
    public WebView webView;
    ArrayList<AgentController> agentList = new ArrayList<>();
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
                                System.out.println("generate");
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

            agentList.add(mainContainer.createNewAgent("test", Exolorer.class.getName(), new Object[0]));
//            agentList.add(mainContainer.createNewAgent("s1", mars.BookSellerAgent.class.getName(), new Object[0]));
//            agentList.add(mainContainer.createNewAgent("s2", mars.BookSellerAgent.class.getName(), new Object[0]));
//            agentList.add(mainContainer.createNewAgent("s3", mars.BookSellerAgent.class.getName(), new Object[0]));
//            agentList.add(mainContainer.createNewAgent("b1", mars.BookBuyerAgent.class.getName(), new String[]{"a", "b"}));

            for (int i = 0, l = agentList.size(); i < l; i++) {
                agentList.get(i).start();
            }

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
