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
import ma.ilisi.smarthome.platform3.agents.SmokeSensorAgent;

public class SmokeSensorContainer extends Application {
    public SmokeSensorAgent smokeSensorAgent;
    protected ObservableList<String> observableList;
    @Override
    public void start(Stage stage) throws Exception {
        startContainer();

        stage.setTitle("Smoke Sensor");

        BorderPane borderPane = new BorderPane();

        observableList = FXCollections.observableArrayList();

        Label temperatureLabel = new Label("Temperature[C]:");
        TextField temperatureField = new TextField();

        Label humidityLabel = new Label("Humidity[%]:");
        TextField humidityField = new TextField();

        Label tvocLabel = new Label("TVOC[ppb]:");
        TextField tvocField = new TextField();

        Label eco2Label = new Label("eCO2[ppm]:");
        TextField eco2Field = new TextField();

        Label rawH2Label = new Label("Raw H2:");
        TextField rawH2Field = new TextField();

        Label rawEthanolLabel = new Label("Raw Ethanol:");
        TextField rawEthanolField = new TextField();

        Label pressureLabel = new Label("Pressure[hPa]:");
        TextField pressureField = new TextField();

        Button sendButton = new Button("Send");

        ListView<String> listView = new ListView<>(observableList);

        VBox inputBox = new VBox();
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(temperatureLabel, temperatureField, humidityLabel, humidityField, tvocLabel, tvocField, eco2Label, eco2Field, rawH2Label, rawH2Field, rawEthanolLabel, rawEthanolField, pressureLabel, pressureField,sendButton);
        vbox.getChildren().add(listView);


        borderPane.setTop(inputBox);
        borderPane.setCenter(vbox);

        sendButton.setOnAction(evt -> {
            String temperature = temperatureField.getText();
            String humidity = humidityField.getText();
            String tvoc = tvocField.getText();
            String eco2 = eco2Field.getText();
            String rawH2 = rawH2Field.getText();
            String rawEthanol = rawEthanolField.getText();
            String pressure = pressureField.getText();

            System.out.println("Temperature: " + temperature);
            System.out.println("Humidity: " + humidity);
            System.out.println("TVOC: " + tvoc);
            System.out.println("eCO2: " + eco2);
            System.out.println("Raw H2: " + rawH2);
            System.out.println("Raw Ethanol: " + rawEthanol);
            System.out.println("Pressure: " + pressure);

            GuiEvent event = new GuiEvent(evt, 1);
            event.addParameter(temperature);
            event.addParameter(humidity);
            event.addParameter(tvoc);
            event.addParameter(eco2);
            event.addParameter(rawH2);
            event.addParameter(rawEthanol);
            event.addParameter(pressure);
            smokeSensorAgent.onGuiEvent(event);
        });

        Scene scene = new Scene(borderPane, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void startContainer() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        AgentContainer agentContainer = rt.createAgentContainer(p1);

        try {
            AgentController agentController = agentContainer.createNewAgent(
                    "smoke_sensor", "ma.ilisi.smarthome.platform3.agents.SmokeSensorAgent", new Object[]{this}
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
