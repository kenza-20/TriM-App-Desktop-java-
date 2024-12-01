package controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Maladie;
import services.MaladieService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MaladiesController implements Initializable {

    @FXML
    private GridPane grid;
    @FXML
    private TextField search;

    @FXML
    private Circle circle;
    private boolean triCroissant = true;
    @FXML
    private ImageView tri;

    private List<Maladie> maladies;
    private ObservableList<Maladie> filteredMaladies;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img2 = new Image("/img/down.png");
        tri.setImage(img2);

        MaladieService maladieService = new MaladieService();
        Image img = new Image("/img/amine.jpg");
        circle.setFill(new ImagePattern(img));

        try {
            maladies = maladieService.recupererParMedecin(1);
            maladies.sort((maladie1, maladie2) -> maladie2.getNom().compareToIgnoreCase(maladie1.getNom()));
            filteredMaladies = FXCollections.observableArrayList(maladies);
            afficherMaladiesDansGridPane();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        search.textProperty().addListener((observable, oldValue, newValue) -> filterMaladies(newValue));
    }
    private void afficherMaladiesDansGridPane() {
        int column = 0;
        int row = 1;
        grid.getChildren().clear();
        for (Maladie maladie : filteredMaladies) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladie.fxml"));
                VBox maladieBox = loader.load();
                MaladieController maladieController = loader.getController();
                maladieController.setData(maladie);

                Insets insets = new Insets(8);
                GridPane.setMargin(maladieBox, insets);

                grid.add(maladieBox, column++, row);
                if (column == 3) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @FXML
    void BackTo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ajoutMaladie.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
    void toProfile(MouseEvent event) {
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

    private void filterMaladies(String searchText) {
        filteredMaladies.clear();
        for (Maladie maladie : maladies) {
            if (maladie.getType().toLowerCase().contains(searchText.toLowerCase())) {
                filteredMaladies.add(maladie);
            }
        }
        if (filteredMaladies.isEmpty()) {

            grid.getChildren().clear();
            Text noDataText = new Text("Pas de maladie de ce type");
            noDataText.setFont(Font.font("Berlin Sans FB", 30));
            noDataText.setStyle("-fx-fill: #02027c;");
            Insets insets = new Insets(500, 0, 0, 500);
            GridPane.setMargin(noDataText   , insets);
            grid.add(noDataText, 0, 0);
        } else {
            afficherMaladiesDansGridPane();
        }
    }

    @FXML
    void toSocketServer(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/socketServer.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void tri(MouseEvent event) {
        if (triCroissant) {
            triCroissant = false;
            Image img = new Image("/img/up.png");
            tri.setImage(img);
            maladies.sort((maladie1, maladie2) -> maladie1.getNom().compareToIgnoreCase(maladie2.getNom()));
        } else {
            triCroissant = true;
            Image img = new Image("/img/down.png");
            tri.setImage(img);
            maladies.sort((maladie1, maladie2) -> maladie2.getNom().compareToIgnoreCase(maladie1.getNom()));
        }
        filteredMaladies.setAll(maladies);
        afficherMaladiesDansGridPane();
    }
}
