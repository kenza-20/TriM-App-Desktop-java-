package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import models.Patient;
import models.Reclamation;
import services.PatientService;
import services.ReclamationService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ModifierPatientController {
    @FXML
    private Circle circle;

    @FXML
    private TextField AdresseEdit;

    @FXML
    private TextField NomEdit;

    @FXML
    private TextField NumTelEdit;

    @FXML
    private TextField PrenomEdit;
    @FXML
    private PasswordField passwordEdit;
    @FXML
    private PasswordField AncienPasswordEdit;

    @FXML
    private TextField emailEdit;

    PatientService reclamationService=new PatientService();
    int idPatient = 1;
    Patient patient = reclamationService.getById3(idPatient);
    @FXML
    private Button edit;

    @FXML
    private Button retour;
    private String nomInitial;
    private String prenomInitial;
    private String numtelInitial;
    private String emailInitial;
    private String passwordAncienInitial;


    public void initialize() {
        Image img = new Image("/img/wided.png");
        circle.setFill(new ImagePattern(img));
        PatientService patientService = new PatientService();
        int idPatient = 1;
        Patient patient = patientService.getById3(idPatient);
        if (patient != null) {
            AdresseEdit.setText(patient.getAdresse());
            NomEdit.setText(patient.getNom());
            NumTelEdit.setText(String.valueOf(patient.getNtel())); // Conversion en String pour le numéro de téléphone
            PrenomEdit.setText(patient.getPrenom());
            emailEdit.setText(patient.getEmail());

            nomInitial = patient.getNom();
            prenomInitial = patient.getPrenom();
            numtelInitial = String.valueOf(patient.getNtel());
            emailInitial = patient.getEmail();
            passwordAncienInitial = patient.getPassword();
        }
    }

    @FXML
    void edit(ActionEvent event) {
        if (patient != null && reclamationService!= null)
        {
            int idChefLab=1;
            String nouvelleAdresse = AdresseEdit.getText();
            String nouveauNom = NomEdit.getText();
            int nouveauNumTel = Integer.parseInt(NumTelEdit.getText());
            String nouveauPrenom = PrenomEdit.getText();
            String passwordAncien = AncienPasswordEdit.getText();
            String passwordNouveau = passwordEdit.getText();

            patient.setAdresse(nouvelleAdresse);
            patient.setNom(nouveauNom);
            patient.setNtel(nouveauNumTel);
            patient.setPrenom(nouveauPrenom);
            // Vérification du mot de passe ancien
            if (!reclamationService.verifierMotDePasse(idChefLab, passwordAncien)) {
                // Affichage d'une alerte si le mot de passe ancien n'est pas correct
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Mot de passe incorrect.");
                alert.showAndWait();
                return; // Sortie de la méthode
            }

            // Vérification si le nouveau mot de passe est différent de l'ancien
            if (passwordAncien.equals(passwordNouveau)) {
                // Affichage d'une alerte si le nouveau mot de passe est identique à l'ancien
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le nouveau mot de passe doit être différent de l'ancien.");
                alert.showAndWait();
                return; // Sortie de la méthode
            }

            if (passwordNouveau.isEmpty()) {
                passwordNouveau = passwordAncien;
                patient.setPassword(passwordNouveau);
            }
            else {
                patient.setPassword(passwordNouveau);
            }

            try {
                reclamationService.update(patient);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Actualité modifiée avec succès !");
                successAlert.setTitle("Modification réussie");
                successAlert.show();

            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Erreur lors de la modification de l'actualité hhh: " + e.getMessage());
                errorAlert.setTitle("Erreur de modification");
                errorAlert.show();
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/StatistiqueGui.fxml"));
               PrenomEdit.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sorry");
                alert.setTitle("Error");
                alert.show();
            }
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de modifier l'actualité car aucune actualité n'est sélectionnée ");
            errorAlert.setTitle("Erreur de modification");
            errorAlert.show();
        }
    }





    @FXML
    void AjouterRendezVous(MouseEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowRendezVousGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }
    @FXML
    void AjouterReclamation(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowReclamationGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }
    @FXML
    void Statistique(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/StatistiqueGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }

    @FXML
    void Calendrier(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/CalenderGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }

    @FXML
    void deconnecter(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr de vouloir vous déconnecter ?");
        alert.setContentText("Cliquez sur OK pour vous déconnecter ou Annuler pour rester connecté.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Main.fxml")));
            Scene scene = new Scene(parent);
            Image image = new Image("/img/favicon.png");
            Stage oldStage = (Stage) circle.getScene().getWindow();
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

    @FXML
    void retour(ActionEvent event) {
        String nomActuel = NomEdit.getText();
        String prenomActuel = PrenomEdit.getText();
        String numtelActuel = NumTelEdit.getText();
        String emailActuel = emailEdit.getText();

        // Vérifiez si les champs texte ont été modifiés
        if (!nomActuel.equals(nomInitial) || !prenomActuel.equals(prenomInitial) || !numtelActuel.equals(numtelInitial) || !emailActuel.equals(emailInitial) ) {
            // Affichez une alerte pour avertir l'utilisateur qu'il y a des modifications non enregistrées
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Des modifications non enregistrées ont été détectées. Êtes-vous sûr de vouloir revenir sans enregistrer les modifications?");

            // Personnalisez les boutons de l'alerte
            ButtonType ouiButton = new ButtonType("Oui");
            ButtonType nonButton = new ButtonType("Non");

            confirmationAlert.getButtonTypes().setAll(ouiButton, nonButton);

            // Affichez l'alerte et attendez la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Si l'utilisateur a choisi "Non", retournez sans effectuer d'action
            if (result.isPresent() && result.get() == nonButton) {
                return;
            }
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/StatistiqueGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}