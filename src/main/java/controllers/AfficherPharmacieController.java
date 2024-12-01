package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.Pharmacien;
import services.PharamcieService;
import services.PharmacienService;
import utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class AfficherPharmacieController implements Initializable {
    @FXML
    private Button ajouterPharmacie;

    @FXML
    private Circle circle;

    @FXML
    private Button modifier;

    @FXML
    private HBox prelevement;

    @FXML
    private Button supprimer;

    @FXML
    private Label textlaberreur;

    @FXML
    private Label textpharmacieLocation;

    @FXML
    private Label textpharmacieNum;

    @FXML
    private Label textpharmacieType;

    @FXML
    private Label textpharmacieadresse;

    @FXML
    private Label textpharmacienom;
    private Parent parent;
    private PharmacienService pharmacienService = new PharmacienService();

    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image ("/img/woman.png");
        circle.setFill(new ImagePattern(img));
    }

    public void initData(String nom,  String ntel,String adresse, String type) {
        if (nom == null || ntel == null ||adresse == null ||  type == null ) {
            textlaberreur.setText("veuillez créer une pharmacie D'abord");
        } else {
            textpharmacienom.setText(nom);
            textpharmacieNum.setText(ntel);
            textpharmacieadresse.setText(adresse);
            textpharmacieType.setText(type);
        }
    }

    @FXML
    void addPharmacie(ActionEvent event) throws IOException {
        int idPharmacien = 1;
        Pharmacien pharmacien = pharmacienService.getById(idPharmacien);
        if (pharmacien.getId_pharmacie() == 0) {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AjouterPharmacie.fxml")));
            circle.getScene().setRoot(parent);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Vous ne pouvez avoir qu'une seule pharmacie associé à votre compte.");
            alert.showAndWait();
        }
    }

    public void DeletePharmacie(ActionEvent event) {
        int idPharmacien = 1;
        Pharmacien pharmacien2 = pharmacienService.getById(idPharmacien);
        int idPharmacie = pharmacien2.getId_pharmacie();
        if (idPharmacie == 0) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette pharmacie ?");

        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                try {
                    PharamcieService pharamcieService = new PharamcieService();
                    pharamcieService.delete(idPharmacie);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Suppression réussie");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("La pharmacie a été supprimé avec succès.");
                    successAlert.showAndWait();
                    switchToScene4(event);
                } catch (Exception e) {
                    System.out.println("Une erreur s'est produite lors de la suppression du pharmacie : " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void switchToScene4(ActionEvent event) throws IOException {
        int idPharmacien = 1;
        Pharmacien pharmacien = pharmacienService.getById(idPharmacien);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherPharmacie.fxml"));
        Parent parent = loader.load();
        AfficherPharmacieController afficherpharmacieController = loader.getController();

        int idPharmacie = pharmacien.getId_pharmacie();
        String select = "SELECT * FROM pharmacie WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idPharmacie);
            rs = st.executeQuery();
            if (rs.next()) {
                // Passer les valeurs récupérées depuis la base de données en tant que paramètres
                afficherpharmacieController.initData(rs.getString("nom"), rs.getString("ntel"),rs.getString("adresse"),  rs.getString("type"));
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                afficherpharmacieController.initData(null, null, null, null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        circle.getScene().setRoot(parent);
    }
    @FXML
    public void switchToScene2(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierPharmacie.fxml"));
        Parent parent = loader.load();
        ModifierPharmacieController modifierPharmacienController = loader.getController();

        int idPharmacien = 1;
        Pharmacien pharmacien = pharmacienService.getById(idPharmacien);
        int idPharmacie = pharmacien.getId_pharmacie();
        if (idPharmacie == 0) {
            return;
        }
        String select = "SELECT * FROM pharmacie WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idPharmacie);
            rs = st.executeQuery();
            // Passer les valeurs récupérées depuis la base de données en tant que paramètres
            if (rs.next()) {
                modifierPharmacienController.initData2(rs.getString("nom"), rs.getString("ntel"),rs.getString("adresse"), rs.getString("type"));
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                System.out.println("Aucune pharmacie trouvé pour l'ID " + idPharmacie);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        circle.getScene().setRoot(parent);
    }

    @FXML
    void medicament(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AfficherMedicament.fxml")));
        circle.getScene().setRoot(root);
    }

    @FXML
    void pharmacie(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AfficherPharmacie.fxml")));
        circle.getScene().setRoot(root);
    }

    @FXML
    void profil(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/homePhar.fxml")));
        circle.getScene().setRoot(root);
    }

}