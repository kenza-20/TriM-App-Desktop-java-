package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Chef_Lab;
import models.Ordonnance;
import models.Patient;
import services.AnalyseService;
import services.Chef_LabService;
import services.OrdonnanceService;
import services.PatientService;
import utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierChefLabController implements Initializable {

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
    private HBox afficheAnalyse;

    @FXML
    private HBox afficheLaboratoire;

    @FXML
    private Circle circle;

    @FXML
    private Button créer;

    @FXML
    private TextField email;

    @FXML
    private TextField genre;

    @FXML
    private PasswordField passwordNouv;

    @FXML
    private PasswordField passwordancien;

    @FXML
    private HBox prelevement;

    @FXML
    private Button retour;

    @FXML
    private VBox vbox3;

    @FXML
    private VBox vbox5;
    private Parent parent;
    private Chef_LabService chefLabService = new Chef_LabService();
    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    private String nomInitial;
    private String prenomInitial;
    private String numtelInitial;
    private String emailInitial;
    private String passwordAncienInitial;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
    }

    public void initData(String nom, String prenom, String numtel, String Email) {
        Nom.setText(nom);
        Prenom.setText(prenom);
        NumTel.setText(numtel);
        email.setText(Email);
        nomInitial = Nom.getText();
        prenomInitial = Prenom.getText();
        numtelInitial = NumTel.getText();
        emailInitial = email.getText();
        passwordAncienInitial = passwordancien.getText();

    }

    @FXML
    void afficheLaboratoire(MouseEvent event) throws IOException {
        int idChefLab = 1;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherLab.fxml"));
        Parent parent = loader.load();
        AfficherLabController afficherLabController = loader.getController();

        int idLab = chefLab.getId_lab();
        String select = "SELECT * FROM lab WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idLab);
            rs = st.executeQuery();
            if (rs.next()) {
                // Passer les valeurs récupérées depuis la base de données en tant que paramètres
                afficherLabController.initData(rs.getString("nom"), rs.getString("adresse"), rs.getString("ntel"), rs.getString("hdebut"), rs.getString("hfin"));
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                afficherLabController.initData(null, null, null, null, null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Afficher la scène
        circle.getScene().setRoot(parent);
    }

    @FXML
    void afficheAnalyse(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherAnalyse.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficheprelevement(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/DemandePrelevement.fxml"));
        circle.getScene().setRoot(root);
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
    void creer(ActionEvent event) throws IOException {
        int idChefLab = 1; // ou l'ID du chef de laboratoire que vous souhaitez modifier
        String nom = Nom.getText();
        String prenom = Prenom.getText();
        int ntel = Integer.parseInt(NumTel.getText());
        String email = this.email.getText();
        String passwordAncien = passwordancien.getText();
        String passwordNouveau = passwordNouv.getText();

        // Vérification du mot de passe ancien
        if (!chefLabService.verifierMotDePasse(idChefLab, passwordAncien)) {
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

        // Créer un objet Chef_Lab avec les nouvelles informations
        Chef_Lab chefLab = new Chef_Lab(idChefLab, nom, prenom, ntel, email, passwordNouveau);

        // Appeler la méthode de modification dans Chef_LabService
        chefLabService.modifierChefLab(chefLab);

        // Afficher une confirmation de la modification
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modification réussie");
        alert.setHeaderText(null);
        alert.setContentText("Le Chef de Laboratoire a été modifié avec succès.");
        alert.showAndWait();
        retour2(event);
    }


    @FXML
    void retour(ActionEvent event) throws IOException {
        String nomActuel = Nom.getText();
        String prenomActuel = Prenom.getText();
        String numtelActuel = NumTel.getText();
        String emailActuel = email.getText();
        String passwordAncienActuel = passwordancien.getText();

        // Vérifiez si les champs texte ont été modifiés
        if (!nomActuel.equals(nomInitial) || !prenomActuel.equals(prenomInitial) || !numtelActuel.equals(numtelInitial) || !emailActuel.equals(emailInitial) || !passwordAncienActuel.equals(passwordAncienInitial)) {
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

        // Si aucune modification n'a été apportée ou si l'utilisateur a confirmé de revenir sans enregistrer les modifications, retournez à la page d'accueil
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/home.fxml")));
        circle.getScene().setRoot(root);
    }

    @FXML
    void retour2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/home.fxml")));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficherProfil(MouseEvent event) throws IOException {
        parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/home.fxml")));
        circle.getScene().setRoot(parent);
    }

}
