package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Medecin;
import services.MedecinService;
import services.OrdonnanceService;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierMedecinController implements Initializable {

    @FXML
    private TextField Adresse;

    @FXML
    private TextField Hrdebut;

    @FXML
    private TextField Hrfin;

    @FXML
    private TextField Mndebut;

    @FXML
    private TextField Mnfin;

    @FXML
    private TextField Nom;

    @FXML
    private TextField NumTel;

    @FXML
    private TextField Prenom;

    @FXML
    private HBox Profile;

    @FXML
    private HBox Profile1;

    @FXML
    private HBox afficheMaladies;

    @FXML
    private Circle circle;

    @FXML
    private Label contact;

    @FXML
    private Button créer;

    @FXML
    private Label dec;

    @FXML
    private TextField email;

    @FXML
    private Label mals;

    @FXML
    private Label ord;

    @FXML
    private PasswordField passwordNouv;

    @FXML
    private PasswordField passwordancien;

    @FXML
    private Label profile;

    @FXML
    private Label profile1;

    @FXML
    private Button retour;

    @FXML
    private Text textlabhd;

    @FXML
    private Text textlabhd1;

    @FXML
    private VBox vbox5;

    private String nomInitial;
    private String prenomInitial;
    private String adreseInitial;
    private String numtelInitial;
    private String hdebutInitial;
    private String hfinInitial;
    private String emailInitial;
    private String passwordAncienInitial;
    private MedecinService medecinService = new MedecinService();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/amine.jpg");
        circle.setFill(new ImagePattern(img));
    }

    public void initData2(String nom,String prenom, String adresse, String Email,String ntel, String hdebut, String hfin) {
        Nom.setText(nom);
        Prenom.setText(prenom);
        Adresse.setText(adresse);
        NumTel.setText(ntel);
        email.setText(Email);
        String[] hdebutParts = hdebut.split(":");
        Hrdebut.setText(hdebutParts[0]);
        Mndebut.setText(hdebutParts[1]);
        String[] hfinParts = hfin.split(":");
        Hrfin.setText(hfinParts[0]);
        Mnfin.setText(hfinParts[1]);
        nomInitial = Nom.getText();
        prenomInitial = Prenom.getText();
        adreseInitial = Adresse.getText();
        numtelInitial = NumTel.getText();
        emailInitial = email.getText();
        hdebutInitial = Hrdebut.getText() + ":" + Mndebut.getText();
        hfinInitial = Hrfin.getText() + ":" + Mnfin.getText();
        passwordAncienInitial = passwordancien.getText();
    }

    @FXML
    void creer(ActionEvent event) throws IOException {
        int idMedecin = 1; // ou l'ID du médecin que vous souhaitez modifier
        String nom = Nom.getText();
        String prenom = Prenom.getText();
        int ntel = Integer.parseInt(NumTel.getText());
        String email = this.email.getText();
        String passwordAncien = passwordancien.getText();
        String passwordNouveau = passwordNouv.getText();
        String adresse = Adresse.getText();
        String hdebut = Hrdebut.getText() + ":" + Mndebut.getText();
        String hfin = Hrfin.getText() + ":" + Mnfin.getText();

        // Vérification du mot de passe ancien
        if (!medecinService.verifierMotDePasse(idMedecin, passwordAncien)) {
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
        }

        // Conversion des chaînes de caractères en objets java.sql.Time
        Time heureDebut = Time.valueOf(hdebut + ":00");
        Time heureFin = Time.valueOf(hfin + ":00");


        // Créer un objet Medecin avec les nouvelles informations
        Medecin medecin = new Medecin(idMedecin, nom, prenom, ntel, email, passwordNouveau, adresse, heureDebut, heureFin);

        // Appeler la méthode de modification dans MedecinService
        medecinService.modifierMedecin(medecin);

        // Afficher une confirmation de la modification
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modification réussie");
        alert.setHeaderText(null);
        alert.setContentText("Le médecin a été modifié avec succès.");
        alert.showAndWait();
        retour2(event);
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
    void modifierProfil(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierMedecin.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to medecinProfile: " + e.getMessage());
        }
    }

    @FXML
    void profile(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medecinProfile.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to medecinProfile: " + e.getMessage());
        }
    }
    @FXML
    void retour(ActionEvent event) throws IOException {
        String nomActuel = Nom.getText();
        String prenomActuel = Prenom.getText();
        String numtelActuel = NumTel.getText();
        String emailActuel = email.getText();
        String passwordAncienActuel = passwordancien.getText();
        String adreseActuel = Adresse.getText();
        String hdebutActuel = Hrdebut.getText() + ":" + Mndebut.getText();
        String hfinActuel = Hrfin.getText() + ":" + Mnfin.getText();

        // Vérifiez si les champs texte ont été modifiés
        if (!nomActuel.equals(nomInitial) || !prenomActuel.equals(prenomInitial) || !numtelActuel.equals(numtelInitial) || !emailActuel.equals(emailInitial) || !passwordAncienActuel.equals(passwordAncienInitial) || !adreseActuel.equals(adreseInitial) || !hdebutActuel.equals(hdebutInitial) || !hfinActuel.equals(hfinInitial)) {

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

        // Si aucune modification n'a été apportée ou si l'utilisateur a confirmé de revenir sans enregistrer les modifications, retournez à la page d'accueil
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medecinProfile.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }
    @FXML
    void retour2(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medecinProfile.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    void toMaladies(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladies.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void toOrdList(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to ordList: " + e.getMessage());
        }
    }

    @FXML
    void toSocket(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/socketServer.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to profile: " + e.getMessage());
        }
    }

}
