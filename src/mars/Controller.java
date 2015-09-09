package mars;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button button1;
    @FXML
    private WebView webView;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert button1 != null : "fx:id=\"myButton\" was not injected: check your FXML file 'simple.fxml'.";
        button1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("That was easy, wasn't it?");
            }
        });
        startAgents();
        webView.getEngine().loadContent("<h1>witaj świecie</h1>");
    }

    private void startAgents() {
        Runtime rt = Runtime.instance();
        rt.setCloseVM(true);
        System.out.print("runtime created\n");
        Profile profile = new ProfileImpl(null, 1200, null);
        System.out.print("profile created\n");
        System.out.println("Launching a whole in-process platform..." + profile);
        jade.wrapper.AgentContainer mainContainer = rt.createMainContainer(profile);

// now set the default Profile to start a container
        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        System.out.println("Launching the agent container ..." + pContainer);

        jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
        System.out.println("Launching the agent container after ..." + pContainer);

        System.out.println("containers created");
        System.out.println("Launching the rma agent on the main container ...");

        try {
            ArrayList<AgentController> agentList = new ArrayList<AgentController>();
            agentList.add(mainContainer.createNewAgent("s1", mars.BookSellerAgent.class.getName(), new Object[0]));
            agentList.add(mainContainer.createNewAgent("s2", mars.BookSellerAgent.class.getName(), new Object[0]));
            agentList.add(mainContainer.createNewAgent("s3", mars.BookSellerAgent.class.getName(), new Object[0]));
            agentList.add(mainContainer.createNewAgent("b1", mars.BookBuyerAgent.class.getName(), new String[]{"a", "b"}));

            for (int i = 0, l = agentList.size(); i < l; i++) {
                agentList.get(i).start();
            }

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        System.out.println("papa");
    }
}
