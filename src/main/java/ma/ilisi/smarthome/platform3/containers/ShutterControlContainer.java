package ma.ilisi.smarthome.platform3.containers;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.ilisi.smarthome.platform3.agents.ShutterControlAgent;

public class ShutterControlContainer extends Application {
    public ShutterControlAgent shutterControlAgent;
    protected ObservableList<String> observableList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        startContainer();

        stage.setTitle("Shutter Control");

        BorderPane borderPane = new BorderPane();

        observableList = FXCollections.observableArrayList();

        Label lightIntensityLabel = new Label("Light Intensity (lux):");
        TextField lightIntensityField = new TextField();
        Label temperatureLabel = new Label("Temperature (°C):");
        TextField temperatureField = new TextField();
        Button sendButton = new Button("Send");

        ListView<String> listView = new ListView<>(observableList);

        VBox inputBox = new VBox();
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(lightIntensityLabel, lightIntensityField, temperatureLabel, temperatureField, sendButton);
        vbox.getChildren().add(listView);

        borderPane.setTop(inputBox);
        borderPane.setCenter(vbox);

        sendButton.setOnAction(evt -> {
            String lightIntensity = lightIntensityField.getText();
            String temperature = temperatureField.getText();

            // You can use lightIntensity and temperature in your HvacAgent logic

            GuiEvent event = new GuiEvent(evt, 1);
            event.addParameter(lightIntensity);
            event.addParameter(temperature);
            shutterControlAgent.onGuiEvent(event);
        });

        Scene scene = new Scene(borderPane, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    public void startContainer() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        AgentContainer agentContainer = rt.createAgentContainer(p1);

        try {
            AgentController agentController = agentContainer.createNewAgent(
                    "shutter_control", "ma.ilisi.smarthome.platform3.agents.ShutterControlAgent", new Object[]{this}
            );
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String msg) {
        Platform.runLater(() -> {
            observableList.add(msg);
        });
    }
}
