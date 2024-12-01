package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.MedecinService;
import models.Medecin;

import java.io.IOException;
import java.sql.Time;
import java.util.Objects;

public class AjouterMedecinController {

    private MedecinService medecinService;

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
    private ComboBox<String> comboSpecialite; // Changez le type de ComboBox pour String

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

    @FXML
    private Text textlabhd;

    @FXML
    private Text textlabhd1;

    @FXML
    void creer(ActionEvent event) throws IOException {
        String hrdebut = Hrdebut.getText();
        String hrfin = Hrfin.getText();
        String mndebut = Mndebut.getText();
        String mnfin = Mnfin.getText();
        Time hdebut = Time.valueOf(hrdebut + ":" + mndebut + ":00");
        Time hfin = Time.valueOf(hrfin + ":" + mnfin + ":00");

        Medecin medecin = new Medecin(Nom.getText(), Prenom.getText(), Integer.parseInt(NumTel.getText()),
                email.getText(), password.getText(), Adresse.getText(),
                comboSpecialite.getValue(), hdebut, hfin, "Medecin", genre.getText(), false);

        // Ajout du médecin
        medecinService.add(medecin);

        // Affichage de l'alerte
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
        Stage oldStage = (Stage) textlabhd.getScene().getWindow();
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

    // Méthode appelée lorsque le contrôleur est initialisé
    public void initialize() {
        // Initialiser la ComboBox avec des valeurs de spécialité
        comboSpecialite.getItems().addAll("Dentiste", "Generaliste", "Pediatre");
        // Initialiser le service MedecinService
        medecinService = new MedecinService();
    }
}
