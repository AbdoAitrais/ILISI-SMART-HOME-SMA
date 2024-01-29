package ma.ilisi.smarthome.platform1.containers;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private ImageView imageView; // Déclarer ImageView ici

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        startContainer();
        stage.setTitle("Camera Security");
        BorderPane BP = new BorderPane();
        Scene scene = new Scene(BP, 400, 300);

        // Champ pour insérer le chemin de l'image
        TextField imagePathField = new TextField();
        imagePathField.setPromptText("Chemin de l'image");

        // Bouton pour parcourir et sélectionner une image
        Button browseButton = new Button("Parcourir...");
        browseButton.setOnAction(e -> browseImage(stage, imagePathField, imageView));

        // ImageView pour afficher l'image sélectionnée
        imageView = new ImageView();
        imageView.setFitWidth(200); // Ajustez la largeur selon vos besoins
        imageView.setFitHeight(200); // Ajustez la hauteur selon vos besoins

        // Bouton pour déclencher la détection d'image
        Button detectButton = new Button("Détecter");
        detectButton.setOnAction(e -> {
            String imagePath = imagePathField.getText();
            cameraSecurityAgent.detectImage(imagePath);
        });

        HBox inputBox = new HBox(imagePathField, browseButton, detectButton);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));

        VBox vBox = new VBox(inputBox, imageView);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        BP.setCenter(vBox);
        stage.setScene(scene);
        stage.show();
    }

    public void processMessage(String imagePath) {
        showImage(imagePath);
        // Vous pouvez ajouter d'autres opérations nécessaires ici
    }

    public void startContainer() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        AgentContainer agentContainer = rt.createAgentContainer(p1);
        try {
            AgentController agentController = agentContainer.createNewAgent(
                    "CameraSecurity", "ma.ilisi.smarthome.platform1.agents.CameraSecurityAgent", new Object[]{this}
            );
            try {
                agentController.start();
            } catch (Exception e) {
                System.out.print("Error:" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            showImage(selectedFile.getAbsolutePath());
        }
    }

    public void showImage(String imagePath) {
        Platform.runLater(() -> {
            Image image = new Image(new File(imagePath).toURI().toString());
            imageView.setImage(image);
        });
    }
}
