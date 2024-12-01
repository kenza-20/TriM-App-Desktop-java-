package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Chef_Lab;
import models.Pharmacien;
import services.Chef_LabService;
import services.MedicamentService;
import services.OrdonnanceService;
import services.PharmacienService;
import utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomePharController implements Initializable {

    @FXML
    private HBox Profile;

    @FXML
    private HBox afficheMedicaments;

    @FXML
    private HBox affichePharmacie;

    @FXML
    private Pane chart;

    @FXML
    private Circle circle;

    @FXML
    private Button generepdf;

    @FXML
    private Label nbr;

    @FXML
    private Label num;

    @FXML
    private Pane pane;

    @FXML
    private Pane pane1;

    @FXML
    private VBox vbox3;

    @FXML
    private VBox vbox5;
    private Parent parent;
    private PharmacienService pharmacienService = new PharmacienService();
    private MedicamentService medicamentService = new MedicamentService();

    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;

    @FXML
    void pdf(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image ("/img/woman.png");
        circle.setFill(new ImagePattern(img));
        int id_pharmacien_id = 1;
        int medicamentCount = medicamentService.getMaxMedicamentCount(id_pharmacien_id);
        num.setText(String.valueOf(medicamentCount));
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
    void Edit(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/ModifierPharmacien.fxml")));
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

}
