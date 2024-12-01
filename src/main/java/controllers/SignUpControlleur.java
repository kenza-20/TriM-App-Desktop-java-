package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpControlleur implements Initializable {

    @FXML
    private ComboBox<String> roleCompte;
    @FXML
    private Button choixRole;
    @FXML
    private VBox vbox;
    private Parent fxml;

    public void initialize(URL location, ResourceBundle resources) {
        roleCompte.setItems(FXCollections.observableArrayList("Medecin", "Pharmacien", "Chef laboratoire", "Patient"));
    }
    @FXML
    void choixRole(ActionEvent event) throws IOException {
        String selectedRole = roleCompte.getValue();
        if (selectedRole == null) {
            // Afficher un message d'erreur
            showAlert("Veuillez sélectionner un rôle.");
            return; // Sortir de la méthode
        }
        String fxmlPath;

        // Déterminer le chemin du fichier FXML en fonction du rôle sélectionné
        switch (selectedRole) {
            case "Medecin":
                fxmlPath = "/views/AddMedecin.fxml";
                break;
            case "Pharmacien":
                // Ajouter le chemin FXML pour le pharmacien
                fxmlPath = "/views/AddPharmacien.fxml";
                break;
            case "Chef laboratoire":
                // Ajouter le chemin FXML pour le chef de laboratoire
                fxmlPath = "/views/AddChefLabo.fxml";
                break;
            case "Patient":
                // Ajouter le chemin FXML pour le patient
                fxmlPath = "/views/AddPatient.fxml";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedRole);
        }

        // Charger le fichier FXML correspondant
        fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));

        // Afficher la scène correspondante
        Scene scene = new Scene(fxml);
        Image image = new Image("/img/favicon.png");
        Stage oldStage = (Stage) choixRole.getScene().getWindow();
        Stage newStage = new Stage();
        newStage.getIcons().add(image);
        newStage.setTitle("TriM");
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setWidth(550);
        newStage.setHeight(700);
        newStage.setX(oldStage.getX());
        newStage.setY(oldStage.getY());
        newStage.setMaximized(oldStage.isMaximized());
        newStage.show();
        oldStage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
