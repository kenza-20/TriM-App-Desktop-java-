package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.Analyse;
import models.Medicament;
import models.Pharmacien;
import services.MedicamentService;
import services.PharmacienService;
import utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class AfficherMedicamentController implements Initializable {

    @FXML
    private Button AjouterAnalyse;
    @FXML
    private GridPane MedicamentContainer;
    @FXML
    private Circle circle;
    private boolean triCroissant = true;
    @FXML
    private TextField recherche;
    @FXML
    private ImageView tri;
    private List<Medicament> medicaments;
    private PharmacienService pharmacienService = new PharmacienService();


    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image ("/img/woman.png");
        circle.setFill(new ImagePattern(img));
        Image img2 = new Image("/img/down.png");
        tri.setImage(img2);
        int idPharmacien = 1;
        MedicamentService medicamentService = new MedicamentService();
        medicaments = medicamentService.getAllById(idPharmacien);

        int column = 0;
        int row = 1;
        try {
            for (Medicament medicament : medicaments) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medicament.fxml"));
                VBox analyseBox = loader.load();
                MedicamentController medicamentController = loader.getController();
                medicamentController.setData(medicament);

                // Ajouter la boîte VBox à la position correcte dans le GridPane
                MedicamentContainer.add(analyseBox, column++, row);
                GridPane.setMargin(analyseBox, new Insets(19));

                // Mettre à jour les colonnes et les lignes si nécessaire
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void afficherMedicamentsDansGridPane() {
        int column = 0;
        int row = 1;
        try {
            MedicamentContainer.getChildren().clear(); // Effacer d'abord le contenu existant du GridPane
            for (Medicament medicament : medicaments) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medicament.fxml"));
                VBox analyseBox = loader.load();
                MedicamentController medicamentController = loader.getController();
                medicamentController.setData(medicament);

                // Ajouter la boîte VBox à la position correcte dans le GridPane
                MedicamentContainer.add(analyseBox, column++, row);
                GridPane.setMargin(analyseBox, new Insets(19));

                // Mettre à jour les colonnes et les lignes si nécessaire
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ajoutmedicament(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AjouterMedicament.fxml")));
        circle.getScene().setRoot(root);
    }

    @FXML
    void deconnecter(MouseEvent event) {

    }

    @FXML
    void medicament(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AfficherMedicament.fxml")));
        circle.getScene().setRoot(root);
    }

    @FXML
    void pharmacie(MouseEvent event) throws IOException {
        int idPharmacie = 1; // Remplacez ceci par l'ID de la pharmacie que vous souhaitez afficher
        Pharmacien pharmacien = pharmacienService.getById(idPharmacie);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AfficherPharmacie.fxml"));
        Parent parent = loader.load();
        AfficherPharmacieController afficherPharmacieController = loader.getController();

        int idAdmin = pharmacien.getId_pharmacie(); // Supposons que cela récupère l'ID de l'administrateur associé
        String select = "SELECT * FROM pharmacie WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idAdmin);
            rs = st.executeQuery();
            if (rs.next()) {
                // Passer les valeurs récupérées depuis la base de données en tant que paramètres
                afficherPharmacieController.initData(rs.getString("nom"), rs.getString("ntel"), rs.getString("adresse"), rs.getString("type"));
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                afficherPharmacieController.initData(null, null, null, null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Afficher la scène
        circle.getScene().setRoot(parent);
    }

    @FXML
    void profil(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/homePhar.fxml")));
        circle.getScene().setRoot(root);
    }

    @FXML
    void recherche(KeyEvent event) {

    }

    @FXML
    void tri(MouseEvent event) {
        // Mettre à jour l'image en fonction de l'état du tri
        String imagePath = triCroissant ? "/img/up.png" : "/img/down.png";
        Image img2 = new Image(imagePath);
        tri.setImage(img2);

        // Mettre à jour l'état du tri
        triCroissant = !triCroissant;

        // Effectuer le tri
        if (triCroissant) {
            Collections.sort(medicaments, Comparator.comparing(Medicament::getNom));
        } else {
            Collections.sort(medicaments, Comparator.comparing(Medicament::getNom).reversed());
        }

        // Rafraîchir l'affichage des analyses dans le GridPane
        afficherMedicamentsDansGridPane();
    }

}