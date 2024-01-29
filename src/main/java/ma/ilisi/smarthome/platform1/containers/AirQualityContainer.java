package ma.ilisi.smarthome.platform1.containers;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.ilisi.smarthome.platform1.agents.AirQualitySensorAgent;

public class AirQualityContainer extends Application {
    public AirQualitySensorAgent airQualitySensorAgent;
    protected ObservableList<String> observableList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        startContainer();

        stage.setTitle("Air Quality Sensor");

        BorderPane borderPane = new BorderPane();

        observableList = FXCollections.observableArrayList();

        Label pm25Label = new Label("PM2.5:");
        TextField pm25Field = new TextField();
        Label pm10Label = new Label("PM10:");
        TextField pm10Field = new TextField();
        Label noLabel = new Label("NO:");
        TextField noField = new TextField();
        Label no2Label = new Label("NO2:");
        TextField no2Field = new TextField();
        Label noxLabel = new Label("NOx:");
        TextField noxField = new TextField();
        Label nh3Label = new Label("NH3:");
        TextField nh3Field = new TextField();
        Label coLabel = new Label("CO:");
        TextField coField = new TextField();
        Label so2Label = new Label("SO2:");
        TextField so2Field = new TextField();
        Label o3Label = new Label("O3:");
        TextField o3Field = new TextField();
        Label benzeneLabel = new Label("Benzene:");
        TextField benzeneField = new TextField();
        Label tolueneLabel = new Label("Toluene:");
        TextField tolueneField = new TextField();
        Label xyleneLabel = new Label("Xylene:");
        TextField xyleneField = new TextField();

        Button sendButton = new Button("Send");

        HBox buttonBox = new HBox(sendButton);

        ListView<String> listView = new ListView<>(observableList);

        HBox inputBox = new HBox(10,
                new VBox(pm25Label, pm25Field),
                new VBox(pm10Label, pm10Field),
                new VBox(noLabel, noField),
                new VBox(no2Label, no2Field),
                new VBox(noxLabel, noxField),
                new VBox(nh3Label, nh3Field),
                new VBox(coLabel, coField),
                new VBox(so2Label, so2Field),
                new VBox(o3Label, o3Field),
                new VBox(benzeneLabel, benzeneField),
                new VBox(tolueneLabel, tolueneField),
                new VBox(xyleneLabel, xyleneField)
        );

        VBox vbox = new VBox(10, inputBox, buttonBox, listView);
        vbox.setPadding(new Insets(10));

        sendButton.setOnAction(evt -> {
            double pm25 = Double.parseDouble(pm25Field.getText());
            double pm10 = Double.parseDouble(pm10Field.getText());
            double no = Double.parseDouble(noField.getText());
            double no2 = Double.parseDouble(no2Field.getText());
            double nox = Double.parseDouble(noxField.getText());
            double nh3 = Double.parseDouble(nh3Field.getText());
            double co = Double.parseDouble(coField.getText());
            double so2 = Double.parseDouble(so2Field.getText());
            double o3 = Double.parseDouble(o3Field.getText());
            double benzene = Double.parseDouble(benzeneField.getText());
            double toluene = Double.parseDouble(tolueneField.getText());
            double xylene = Double.parseDouble(xyleneField.getText());

            GuiEvent event = new GuiEvent(evt, 1);
            event.addParameter(pm25);
            event.addParameter(pm10);
            event.addParameter(no);
            event.addParameter(no2);
            event.addParameter(nox);
            event.addParameter(nh3);
            event.addParameter(co);
            event.addParameter(so2);
            event.addParameter(o3);
            event.addParameter(benzene);
            event.addParameter(toluene);
            event.addParameter(xylene);
            airQualitySensorAgent.onGuiEvent(event);
        });

        borderPane.setTop(inputBox);
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void startContainer() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        AgentContainer agentContainer = rt.createAgentContainer(p1);

        try {
            AgentController agentController = agentContainer.createNewAgent(
                    "air_quality_sensor", "ma.ilisi.smarthome.platform1.agents.AirQualitySensorAgent", new Object[]{this}
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
