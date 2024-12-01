package controllers;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.FileOutputStream;
import com.itextpdf.text.BaseColor;
import java.io.File;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Ordonnance;
import models.RendezVous;
import services.OrdonnanceService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OrdController {

    @FXML
    private Label dateOrd;
    @FXML
    private Label descO;

    @FXML
    private Label etatOrd;

    @FXML
    private Label idOrd;

    @FXML
    private VBox maladieBox;
    @FXML
    private Label nomPatient;


    @FXML
    private Label typeOrd;
    private Ordonnance ordonnance;

    private final OrdonnanceService ordonnanceService = new OrdonnanceService();

    public void setData(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
        typeOrd.setText(String.valueOf(ordonnance.getType()));
        etatOrd.setText(String.valueOf(ordonnance.getEtat()));
        descO.setText((ordonnance.getDescription()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = ordonnance.getDate().format(formatter);
        dateOrd.setText(formattedDateTime);
        idOrd.setText(String.valueOf(ordonnance.getId()));
        try {
            String rendezVousId = String.valueOf(ordonnance.getIdRendezVous());
            String nomPrenomPatient = ordonnanceService.getPatientNomPrenomByRendezVousId(rendezVousId);
            nomPatient.setText(nomPrenomPatient);
        } catch (SQLException e) {
            System.err.println("Error fetching patient name: " + e.getMessage());
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierOrd.fxml"));
            Parent root = loader.load();
            ModifierOrdController modifierOrdController = loader.getController();
            modifierOrdController.setOrdonnance(this.ordonnance);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        try {
            int ordId = Integer.parseInt(idOrd.getText());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete this entry?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        ordonnanceService.supprimer(ordId);
                        Stage stage = (Stage) maladieBox.getScene().getWindow();
                        stage.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
                        Parent root = loader.load();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (SQLException | IOException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            });
        } catch (NumberFormatException e) {
            System.err.println("Error parsing maladie ID: " + e.getMessage());
        }
    }
    //generer PDF
    @FXML
    public void generatePDF() {
        if (this.ordonnance != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());

            if (selectedFile != null) {
                try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
                    Document document = new Document();
                    PdfWriter.getInstance(document, fos);
                    document.open();
                    com.itextpdf.text.Image plan = com.itextpdf.text.Image.getInstance(Objects.requireNonNull(getClass().getResource("/img/plan1.png")));
                    plan.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
                    plan.setAbsolutePosition(0, 0);
                    document.add(plan);

                    // Ajouter un espacement avant le titre
                    document.add(Chunk.NEWLINE);

                    // Titre
                    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
                    Paragraph title = new Paragraph("Ordonnance Details", titleFont);
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);

                    // Ajouter un espacement après le titre
                    document.add(Chunk.NEWLINE);

                    // Tableau pour les détails de l'ordonnance
                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setSpacingAfter(20); // Espacement après le tableau

                    // Ajouter les détails de l'ordonnance dans le tableau
                    Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
                    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

                    // Ajouter une couleur de fond pour les cellules de titre
                    BaseColor headerColor = new BaseColor(51, 153, 255); // Bleu clair
                    PdfPCell headerCell = new PdfPCell(new Phrase("Champs", headerFont));
                    headerCell.setBackgroundColor(headerColor);
                    table.addCell(headerCell);

                    headerCell = new PdfPCell(new Phrase("Données", headerFont));
                    headerCell.setBackgroundColor(headerColor);
                    table.addCell(headerCell);

                    addCell(table, "ID", String.valueOf(ordonnance.getId()), font);
                    addCell(table, "Type", String.valueOf(ordonnance.getType()), font);
                    addCell(table, "Etat", String.valueOf(ordonnance.getEtat()), font);
                    addCell(table, "Description", ordonnance.getDescription(), font);
                    addCell(table, "Date", ordonnance.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), font);

                    // Ajouter le tableau au document
                    document.add(table);

                    // Close document
                    document.close();

                    // Open the generated PDF file
                    Desktop.getDesktop().open(selectedFile);

                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                    // Error handling
                }
            }
        }
    }

    private void addCell(PdfPTable table, String label, String value, Font font) {
        table.addCell(new Phrase(label, font));
        table.addCell(new Phrase(value, font));
    }



}
