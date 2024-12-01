package controllers;

import javafx.stage.StageStyle;
import models.Analyse;
import models.Medecin;
import models.Reclamation;
import models.Rendez_Vous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.RendezVousService;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

public class ShowRendezVousController {

    @FXML
    private Button Ajouter_RendezVous;
    @FXML
    private ListView<Rendez_Vous> ListRendezVous;
    @FXML
    private Circle circle;
    @FXML
    private TextField RendezVousSearch;
    @FXML
    private Button AjouterRendezV;

    @FXML
    private ImageView tri;
    private ObservableList<Rendez_Vous> filteredList;
    private final RendezVousService rendezVousService = new RendezVousService();
    private boolean triCroissant = true;
    private ObservableList<Rendez_Vous> rendez_vous;

    public void initialize() {
        Image img = new Image ("/img/wided.png");
        circle.setFill(new ImagePattern(img));
        Image img2 = new Image("/img/down.png");
        tri.setImage(img2);
//******************************recherchee*************************************************
        filteredList = FXCollections.observableArrayList();
        filteredList.addAll(ListRendezVous.getItems()); // Utilisation de getItems() pour obtenir la liste actuelle
        RendezVousSearch.textProperty().addListener((Observable, oldValue, newValue) -> {
            // Si le nouveau texte est vide, cela signifie que du texte a été supprimé
            if (newValue.isEmpty()) {
                filteredList.clear();
                showAll();
            } else {
                // Sinon, il y a eu une entrée de texte, donc on filtre
                filterList(newValue);
            }
        });

//**********************************************************
        ListRendezVous.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Rendez_Vous rendezVous, boolean empty) {
                super.updateItem(rendezVous, empty);

                if (empty || rendezVous == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Créer une mise en page personnalisée pour chaque élément
                    VBox container = new VBox();
                    container.setSpacing(5);
                    container.setAlignment(Pos.CENTER);

                    // Afficher les détails du rendez-vous avec des icônes
                    Label dateLabel = createStyledLabel("Date: " + rendezVous.getDate());
                    Label statusLabel = createStyledLabel("Statut: " + rendezVous.getStatus());
                    Label motifLabel = createStyledLabel("Motif: " + rendezVous.getMotif());
                    motifLabel.setWrapText(true);
                    Label medecinLabel = createStyledLabel("Médecin: " + rendezVous.getMedecin().getNom());
                    // Créer des boutons pour chaque élément et les placer dans un conteneur HBox
                    Button editButton = new Button("Modifier");
                    Button deleteButton = new Button("Supprimer");
                    HBox buttonsBox = new HBox(editButton, deleteButton);
                    buttonsBox.setSpacing(10);
                    buttonsBox.setAlignment(Pos.CENTER);
                    editButton.setStyle("-fx-background-color: #87acb5FF; -fx-background-radius: 10px;-fx-text-fill: white; ");
                    deleteButton.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 10px;-fx-text-fill: white;");
                    String fontName = "Berlin Sans FB"; // Nom de la police
                    double fontSize = 14.0; // Taille de la police
                    Font font = Font.font(fontName, fontSize); // Créer un objet Font avec le nom et la taille de la police
                    editButton.setFont(font); // Définir la police pour le bouton "Modifier"
                    deleteButton.setFont(font); // Définir la police pour le bouton "Supprimer"
                    editButton.setTextFill(Color.web("#2f3e44")); // Définir la couleur du texte pour le bouton "Modifier"
                    deleteButton.setTextFill(Color.web("#2f3e44")); // Définir la couleur du texte pour le bouton "Supprimer"

                    // Gérer les actions des boutons
                    editButton.setOnAction(event -> editRendezVous(rendezVous));
                    deleteButton.setOnAction(event -> deleteRendezVous(rendezVous));

                    // Ajouter les éléments à la mise en page personnalisée
                    container.getChildren().addAll(dateLabel, statusLabel, motifLabel,medecinLabel,buttonsBox);

                    // Ajouter une séparation entre les éléments
                    Separator separator = new Separator();
                    container.getChildren().add(separator);

                    // Définir la mise en page personnalisée comme contenu de la cellule
                    setGraphic(container);
                }
            }
        });

        showAllRendezVous(); // Charger les données dans la ListView
    }


    private void showAllRendezVous() {
        ListRendezVous.getItems().addAll(rendezVousService.getAll());
    }
    // Méthode pour éditer un rendez-vous
    private void editRendezVous(Rendez_Vous rendezVous) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditRendezVousGui.fxml"));
            Parent root = loader.load();
            EditRendezVousController controller = loader.getController();
            controller.SetRendezVousService(rendezVousService);
            controller.SetDataRendezVous(rendezVous);
            ListRendezVous.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Une erreur s'est produite lors de l'édition du rendez-vous.");
            alert.setTitle("Erreur");
            alert.show();
        }
    }

    // Méthode pour supprimer un rendez-vous
    private void deleteRendezVous(Rendez_Vous rendezVous) {
        rendezVousService.delete(rendezVous.getId());
        ListRendezVous.getItems().remove(rendezVous);
    }


    // Méthode pour créer un label avec style
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;"); // Gras et couleur bleue
        return label;
    }


    private void filterList(String searchText) {
        // Vérifier si le texte de recherche est vide
        if (!searchText.isEmpty()) {
            // Si le texte de recherche n'est pas vide, filtrer la liste en fonction du nouveau texte
            filteredList.clear(); // Effacer la liste filtrée actuelle
            // Parcourir la liste originale (probablement ListReclamation.getItems()) et ajouter les éléments correspondants au texte de recherche
            for (Rendez_Vous reclamation : ListRendezVous.getItems()) {
                if (reclamationMatchesSearch(reclamation, searchText)) {
                    filteredList.add(reclamation);
                }
            }
            showFilteredList();
        } else {
            // Si le texte de recherche est vide, afficher tous les éléments
            showAll();
        }
    }

    private boolean reclamationMatchesSearch(Rendez_Vous reclamation, String searchText) {
        // Convertir le texte de recherche en minuscules pour l'ignorance de la casse
        String searchTextLowerCase = searchText.toLowerCase();
        // Vérifier si la description, le statut ou la date de la réclamation contient le texte de recherche
        return reclamation.getMotif().toLowerCase().contains(searchTextLowerCase)
                || reclamation.getStatus().toLowerCase().contains(searchTextLowerCase)
                || reclamation.getDate().toString().contains(searchTextLowerCase); // Assurez-vous que la méthode getDate() retourne une représentation textuelle de la date
    }

    private void showFilteredList() {
        ListRendezVous.getItems().clear();
        ListRendezVous.getItems().setAll(filteredList);
    }
    public void showAll() {
        ListRendezVous.getItems().clear();
        ListRendezVous.getItems().addAll(rendezVousService.getAll());
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
    void AjouterRendezV(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/AjouterRendezVousGui.fxml"));
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
    void tri(MouseEvent event) {
        // Mettre à jour l'image en fonction de l'état du tri
        String imagePath = triCroissant ? "/img/up.png" : "/img/down.png";
        Image img2 = new Image(imagePath);
        tri.setImage(img2);

        // Mettre à jour l'état du tri
        triCroissant = !triCroissant;
        rendez_vous= FXCollections.observableArrayList();
        rendez_vous.addAll(ListRendezVous.getItems());

        // Effectuer le tri
        if (triCroissant) {
            Collections.sort(rendez_vous, Comparator.comparing(Rendez_Vous::getMotif));
        } else {
            Collections.sort(rendez_vous, Comparator.comparing(Rendez_Vous::getMotif).reversed());
        }

        // Rafraîchir l'affichage des analyses dans le GridPane
        showsortedList();
    }
    private void showsortedList() {
        ListRendezVous.getItems().clear();
        ListRendezVous.getItems().setAll(rendez_vous);
    }
}