package ma.ilisi.smarthome.platform3.containers;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.ilisi.smarthome.platform3.agents.SmartHomeAgent;

public class SmartHomeContainer extends Application {
    public SmartHomeAgent smartHomeAgent;
    protected ObservableList<String> observableList;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        startContainer();
        stage.setTitle("Smart Home");
        BorderPane BP=new BorderPane();
        Scene scene=new Scene(BP,400,300);
        observableList= FXCollections.observableArrayList();
        ListView<String> listView= new ListView<>(observableList);



        VBox vBox=new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.getChildren().add(listView);
        BP.setCenter(vBox);
        stage.setScene(scene);
        stage.show();
    }
    public void startContainer()
    {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        AgentContainer agentContainer = rt.createAgentContainer(p1); // Initialize the agentContainer variable here

        try {
            // Create agents and perform other actions with the agentContainer
            AgentController agentController= agentContainer.createNewAgent(
                    "smart_home","ma.ilisi.smarthome.platform3.agents.SmartHomeAgent", new Object[]{this}
            );
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMessage(String msg){
        Platform.runLater(()->{
            observableList.add(msg);
        });
    }
}
