package ma.ilisi.smarthome.platform2.containers;

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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.ilisi.smarthome.platform2.agents.HvacAgent;

public class HvacContainer extends Application {
    public HvacAgent hvacAgent;
    protected ObservableList<String> observableList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        startContainer();

        stage.setTitle("Hvac");

        BorderPane borderPane = new BorderPane();

        observableList = FXCollections.observableArrayList();

        Label temperatureLabel = new Label("Temperature (°C):");
        TextField temperatureField = new TextField();
        Label humidityLabel = new Label("Humidity (%):");
        TextField humidityField = new TextField();
        ChoiceBox<String> windChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("low", "medium", "high"));
        windChoiceBox.setValue("low");  // Set a default value
        ChoiceBox<String> outlookChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("sunny", "overcast", "rainy"));
        outlookChoiceBox.setValue("sunny");  // Set a default value
        Button sendButton = new Button("Send");

        ListView<String> listView = new ListView<>(observableList);

        VBox inputBox = new VBox();
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(temperatureLabel, temperatureField, humidityLabel, humidityField, windChoiceBox, outlookChoiceBox, sendButton);
        vbox.getChildren().add(listView);


        borderPane.setTop(inputBox);
        borderPane.setCenter(vbox);

        sendButton.setOnAction(evt -> {
            String temperature = temperatureField.getText();
            String humidity = humidityField.getText();
            String wind = windChoiceBox.getValue();
            String outlook = outlookChoiceBox.getValue();

            System.out.println("Temperature: " + temperature);
            System.out.println("Humidity: " + humidity);
            System.out.println("Wind: " + wind);
            System.out.println("Outlook: " + outlook);

            // You can use lightIntensity and temperature in your HvacAgent logic

            GuiEvent event = new GuiEvent(evt, 1);
            event.addParameter(temperature);
            event.addParameter(humidity);
            event.addParameter(wind);
            event.addParameter(outlook);
            hvacAgent.onGuiEvent(event);
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
                    "hvac", "ma.ilisi.smarthome.platform2.agents.HvacAgent", new Object[]{this}
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
