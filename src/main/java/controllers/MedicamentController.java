package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Medicament;
import services.MedicamentService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class MedicamentController {

    @FXML
    private VBox analyseBox;

    @FXML
    private Label dateexp;

    @FXML
    private Label dateprod;

    @FXML
    private Label idAnalyse;

    @FXML
    private Button modifier;

    @FXML
    private Label nomMedicament;

    @FXML
    private Button supprimer;
    private Stage stage;
    private Parent parent;

    @FXML
    void DeleteMedicament(ActionEvent event) {
        // Obtenez l'identifiant du médicament à supprimer
        int idMedicamentToDelete = Integer.parseInt(idAnalyse.getText());
        // Afficher une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce médicament ?");
        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");
        confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                try {
                    // Utiliser le service de médicament pour supprimer le médicament
                    MedicamentService medicamentService = new MedicamentService();
                    medicamentService.delete(idMedicamentToDelete);

                    // Afficher une boîte de dialogue pour confirmer la suppression
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Médicament supprimé");
                    alert.setHeaderText(null);
                    alert.setContentText("Le médicament a été supprimé avec succès.");
                    alert.showAndWait();
                    afficheMedicament(event);

                } catch (Exception e) {
                    System.out.println("Une erreur s'est produite lors de la suppression du médicament : " + e.getMessage());
                }
            }
        });
    }


    @FXML
    void UpdateMedicament(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MdifierMedicament.fxml"));
        Parent parent = loader.load();
        int idMedicamentToUpdate = Integer.parseInt(idAnalyse.getText());
        MedicamentService medicamentService = new MedicamentService();
        Medicament medicamentToUpdate = medicamentService.getById(idMedicamentToUpdate);
        if (medicamentToUpdate != null) {
            ModifierMedicamentController modifierMedicamentController = loader.getController();
            // Passer les données d'analyse à la scène de modification
            modifierMedicamentController.initData(medicamentToUpdate.getNom(), medicamentToUpdate.getDateProd(), medicamentToUpdate.getDateExp(), medicamentToUpdate.getPrix(), medicamentToUpdate.getDescription(), medicamentToUpdate.getDisponibilite(), idMedicamentToUpdate);
        } else {
            // Gérer le cas où l'analyse n'est pas trouvée
            System.out.println("Aucune medicament trouvée pour l'ID " + idAnalyse);
        }
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(parent);
    }

    public void setData(Medicament medicament) {
        nomMedicament.setText(medicament.getNom());
        // Formatage de la date de production
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateProduction  = "Date de production : " + dateFormat.format(medicament.getDateProd());
        dateprod.setText(dateProduction );
        // Formatage de la date d'expiration
        String dateExpiration = "Date d'expiration : " + dateFormat.format(medicament.getDateExp());
        dateexp.setText(dateExpiration);
        idAnalyse.setText(String.valueOf(medicament.getId()));
    }

    void afficheMedicament(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AfficherMedicament.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(parent);
    }
}