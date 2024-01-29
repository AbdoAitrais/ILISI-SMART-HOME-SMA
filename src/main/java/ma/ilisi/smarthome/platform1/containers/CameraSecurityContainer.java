package ma.ilisi.smarthome.platform1.containers;

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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ma.ilisi.smarthome.platform1.agents.CameraSecurityAgent;

import java.io.File;

public class CameraSecurityContainer extends Application {
    public CameraSecurityAgent cameraSecurityAgent;
    protected ObservableList<String> observableList;
    private ImageView imageView; // Déclarer ImageView ici
    private AgentContainer agentContainer; // Déclarez la variable ici

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        startContainer();
        stage.setTitle("Camera Security");
        BorderPane BP = new BorderPane();
        Scene scene = new Scene(BP, 400, 300);

        observableList = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(observableList);

        // Champ pour insérer le chemin de l'image
        TextField imagePathField = new TextField();
        imagePathField.setPromptText("Chemin de l'image");

        // Bouton pour parcourir et sélectionner une image
        Button browseButton = new Button("Parcourir...");
        browseButton.setOnAction(e -> browseImage(stage, imagePathField, imageView)); // Passer imageView ici

        // ImageView pour afficher l'image sélectionnée
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200); // Ajustez la largeur selon vos besoins
        imageView.setFitHeight(200); // Ajustez la hauteur selon vos besoins

        // Bouton pour déclencher la détection d'image
        Button detectButton = new Button("Détecter");
        detectButton.setOnAction(e -> {
            // Ajoutez ici la logique pour déclencher la détection d'image
            String imagePath = imagePathField.getText();
            // Vous pouvez appeler une méthode dans votre agent pour traiter la détection
            cameraSecurityAgent.detectImage(imagePath);
        });

        HBox inputBox = new HBox(imagePathField, browseButton, detectButton);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));

        VBox vBox = new VBox(inputBox, imageView, listView);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        BP.setCenter(vBox);
        stage.setScene(scene);
        stage.show();
    }
    public void startContainer() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        AgentContainer agentContainer = rt.createAgentContainer(p1);

        // Initialisez l'agentContainer avant de l'utiliser
        this.agentContainer = agentContainer;

        if (agentContainer != null) {
            try {
                AgentController agentController = agentContainer.createNewAgent(
                        "CameraSecurity", "ma.ilisi.smarthome.platform1.agents.CameraSecurity", new Object[]{this}
                );
                agentController.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("AgentContainer is null. Make sure it is initialized properly.");
        }
    }

    private void browseImage(Stage stage, TextField imagePathField, ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers d'images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            // Utilisez la référence imageView existante
            imageView.setImage(image);
        }
    }

    public void showMessage(String msg) {
        Platform.runLater(() -> {
            observableList.add(msg);
        });
    }
}