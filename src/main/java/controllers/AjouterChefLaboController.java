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
import models.Chef_Lab;
import services.Chef_LabService;

import java.io.IOException;
import java.util.Objects;

public class AjouterChefLaboController {

    @FXML
    private TextField Nom;

    @FXML
    private TextField NumTel;

    @FXML
    private TextField Prenom;

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

    private Chef_LabService chefLabService = new Chef_LabService();

    @FXML
    void creer(ActionEvent event) throws IOException {
        String nom = Nom.getText();
        String prenom = Prenom.getText();
        int ntel = Integer.parseInt(NumTel.getText());
        String mail = email.getText();
        String pass = password.getText();
        String gendre = genre.getText();
        // Le rôle du chef de laboratoire peut être défini ici ou à partir de l'interface utilisateur, selon votre logique.
        String role = "Chef laboratoire";

        // Création d'un nouvel objet Chef_Lab
        Chef_Lab nouveauChefLab = new Chef_Lab(nom, prenom, ntel, mail, pass, gendre, role, false);

        // Ajout du chef de laboratoire à la base de données
        chefLabService.ajouterChefLab(nouveauChefLab);
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
