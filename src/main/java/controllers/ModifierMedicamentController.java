package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.Medicament;
import services.MedicamentService;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static org.apache.http.client.utils.DateUtils.parseDate;

public class ModifierMedicamentController implements Initializable {
    @FXML
    private TextField dateExp;

    @FXML
    private TextField dateProd;

    @FXML
    private TextField descriptionMed;

    @FXML
    private TextField disponibiliteMed;
    @FXML
    private TextField nomMedicament;

    @FXML
    private Label errorDateExp;

    @FXML
    private Label errorDateProd;

    @FXML
    private Label errorDesc;

    @FXML
    private Label errorDispo;

    @FXML
    private Label errorNom;

    @FXML
    private Label errorPrix;

    @FXML
    private TextField prixMed;

    @FXML
    private TextField Hrexp;

    @FXML
    private TextField Hrprod;

    @FXML
    private TextField ID;

    @FXML
    private TextField Mnexp;

    @FXML
    private TextField Mnprod;

    @FXML
    private HBox Profile;

    @FXML
    private HBox afficheAnalyse;

    @FXML
    private HBox afficheLaboratoire;

    @FXML
    private Button btnupdate;

    @FXML
    private Circle circle;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image ("/img/woman.png");
        circle.setFill(new ImagePattern(img));
    }

    @FXML
    void UpdateMedicament(ActionEvent event) throws IOException {
        int idPharmacien = 1;
        Date dateP = null;
        Date dateE = null;
        String nomMed = nomMedicament.getText();
        String datePString = dateProd.getText();
        String dateEString = dateExp.getText();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateP = dateFormat.parse(datePString);
            dateE = dateFormat.parse(dateEString);
        } catch (ParseException e) {
            e.printStackTrace(); // Gérer l'erreur de parsing de la date
        }

        int prixMedicament = Integer.parseInt(prixMed.getText());
        String descriptionMedicament = descriptionMed.getText();
        Boolean dispoMedicament = Boolean.valueOf(disponibiliteMed.getText());

        // Vérification de nullité des dates
        if (dateP != null && dateE != null) {
            MedicamentService medicamentService = new MedicamentService();
            Medicament medicament = new Medicament(nomMed, dateP, dateE, prixMedicament, descriptionMedicament, dispoMedicament);
            medicamentService.update(medicament, idPharmacien);

            // Affichage de l'alerte pour indiquer que la modification a été effectuée
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Modification réussie");
            alert.setHeaderText(null);
            alert.setContentText("La modification du médicament a été effectuée avec succès.");
            alert.showAndWait();
            switchToScene5(event);
        } else {
            System.err.println("Les dates ne peuvent pas être null.");
        }
    }




    @FXML
    void deconnecter(MouseEvent event) {

    }

    @FXML
    void medicament(MouseEvent event) {

    }

    @FXML
    void pharmacie(MouseEvent event) {

    }

    @FXML
    void profil(MouseEvent event) {

    }

    @FXML
    void switchToScene5(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AfficherMedicament.fxml")));
        circle.getScene().setRoot(root);
    }


    public void initData(String nom, Date dateP, Date dateE, int prix, String description, Boolean disponibilite, int idMedicamentToUpdate) {
        nomMedicament.setText(nom);

        // Formatter les dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datePFormatted = dateFormat.format(dateP);
        String dateEFormatted = dateFormat.format(dateE);

        dateProd.setText(datePFormatted);
        dateExp.setText(dateEFormatted);
        prixMed.setText(String.valueOf(prix));
        descriptionMed.setText(description);
        disponibiliteMed.setText(String.valueOf(disponibilite));
    }

}