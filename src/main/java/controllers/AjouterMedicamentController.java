package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.Medicament;

import services.MedicamentService;

import services.PharmacienService;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AjouterMedicamentController implements Initializable {
    @FXML
    private Circle circle;

    @FXML
    private Label errorDesc;

    @FXML
    private Label errorDispo;

    @FXML
    private Label errorNom;

    @FXML
    private Label errorPrix;

    @FXML
    private Label errordateExp;

    @FXML
    private Label errordateProd;

    @FXML
    private TextField nomMedicament;

    @FXML
    private TextField prixMed;
    @FXML
    private TextField dispoMed;

    @FXML
    private TextField dateExp;

    @FXML
    private TextField dateProd;

    @FXML
    private TextField descriptionMed;

    private PharmacienService pharmacienService = new PharmacienService();
    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image ("/img/woman.png");
        circle.setFill(new ImagePattern(img));
    }
    @FXML
    void createMedicament(ActionEvent event) throws IOException {
        int idPharmacien = 1;
        String nom = nomMedicament.getText();
        String datePString = dateProd.getText();
        String dateEString = dateExp.getText();
        int prix = Integer.parseInt(prixMed.getText());
        String description = descriptionMed.getText();
        String disponibilite = dispoMed.getText();

        if (nom.isEmpty() || datePString.isEmpty() || dateEString.isEmpty() || prix == 0 || description.isEmpty() || disponibilite.isEmpty()) {
            errorNom.setText("Ce champ est obligatoire.");
            errordateProd.setText("Ce champ est obligatoire.");
            errordateExp.setText("Ce champ est obligatoire.");
            errorPrix.setText("Ce champ est obligatoire.");
            errorDesc.setText("Ce champ est obligatoire.");
            errorDispo.setText("Ce champ est obligatoire.");
            return;
        }

        if (nom.length() < 4) {
            errorNom.setText("Le nom de médicament doit contenir au moins 4 caractères.");
            return;
        }

        if (datePString.length() < 4) {
            errorNom.setText("");
            errordateProd.setText("La date doit contenir au moins 4 caractères.");
            return;
        }

        if (dateEString.length() < 4) {
            errordateExp.setText("");
            errordateExp.setText("La date d'expiration doit contenir au moins 4 caractères.");
            return;
        }

        if (prix < 4) {
            errorPrix.setText("");
            errorPrix.setText("Le prix doit contenir au moins 4 caractères.");
            return;
        }

        Date dateP = null;
        Date dateE = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateP = dateFormat.parse(datePString);
            dateE = dateFormat.parse(dateEString);

            MedicamentService medicamentService = new MedicamentService();
            Medicament medicament = new Medicament(nom, dateP, dateE, prix, description, Boolean.valueOf(disponibilite), idPharmacien);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        MedicamentService medicamentService = new MedicamentService();
        Medicament medicament = new Medicament(nom, dateP, dateE, prix, description, Boolean.valueOf(disponibilite), idPharmacien);
        medicamentService.addMedicament(medicament,idPharmacien);

        errorNom.setText("");
        errordateProd.setText("");
        errordateExp.setText("");
        errorPrix.setText("");
        errorDesc.setText("");
        errorDispo.setText("");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout réussi");
        alert.setHeaderText(null);
        alert.setContentText("Medicament a été ajouté avec succès.");
        alert.showAndWait();
        switchToScene5(event);
    }

    @FXML
    void switchToScene5(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherMedicament.fxml"));
        circle.getScene().setRoot(root);
    }

}