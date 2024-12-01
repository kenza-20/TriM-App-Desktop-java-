package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Patient;
import services.PatientService;

import java.io.IOException;
import java.util.Objects;

public class AjouterPatientController {

    @FXML
    private TextField Adresse;

    @FXML
    private TextField Nom;

    @FXML
    private TextField NumTel;

    @FXML
    private TextField Prenom;

    @FXML
    private TextField age;

    @FXML
    private Button créer;

    @FXML
    private TextField email;

    @FXML
    private TextField genre;

    @FXML
    private PasswordField password;

    @FXML
    private Button retour;
    private final PatientService patientService = new PatientService();
    @FXML
    void creer(ActionEvent event) throws IOException {
        String nom = Nom.getText();
        String prenom = Prenom.getText();
        int age = Integer.parseInt(this.age.getText());
        int ntel = Integer.parseInt(NumTel.getText());
        String email = this.email.getText();
        String password = this.password.getText();
        String adresse = Adresse.getText();
        String genre = this.genre.getText();

        // Créer un objet Patient avec les données récupérées
        Patient patient = new Patient(nom, prenom, age, ntel, email, password, adresse, "Patient", genre, false);

        // Appeler la méthode add du service PatientService pour ajouter le patient à la base de données
        patientService.add(patient);

        showAlert("Votre compte a été créé avec succès !");
        retour(event);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Main.fxml")));
        Scene scene = new Scene(parent);
        Image image = new Image("/img/favicon.png");
        Stage oldStage = (Stage) email.getScene().getWindow();
        Stage newStage = new Stage();
        newStage.getIcons().add(image);
        newStage.setTitle("TriM");
        scene.setFill(Color.TRANSPARENT);
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setWidth(980);
        newStage.setHeight(680);
        newStage.setX(oldStage.getX());
        newStage.setY(oldStage.getY());
        newStage.setMaximized(oldStage.isMaximized());
        newStage.show();
        oldStage.close();
    }

}
