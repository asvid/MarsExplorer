package mars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static String map = "ja jebie";
    private Controller ctrl;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Mars Explorer");
        ctrl = (Controller) fxmlLoader.getController();
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();

        new Map();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        ctrl.destroy();
    }
}
