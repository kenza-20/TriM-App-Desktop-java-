package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import models.Pharmacie;
import models.Pharmacien;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.PharamcieService;
import services.PharmacienService;
import utils.MyDataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifierPharmacieController implements Initializable {
    @FXML
    private Circle circle;
    @FXML
    private Pane map;
    @FXML
    private Label errorAdresse;
    @FXML
    private TextField adressepharmacie;

    @FXML
    private Label errorLocation;

    @FXML
    private Label errorNom;

    @FXML
    private Label errorNtel;

    @FXML
    private Label errorType;

    @FXML
    private TextField locationpharmacie;

    @FXML
    private TextField nompharmcie;

    @FXML
    private TextField telpharmacie;

    @FXML
    private TextField typepharmacie;
    private PharmacienService pharmacienService = new PharmacienService();
    private WebView webView;
    private WebEngine webEngine;
    private SimpleStringProperty selectedAddressProperty = new SimpleStringProperty();
    private String initialPharmacieName;
    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image img = new Image ("/img/woman.png");
            circle.setFill(new ImagePattern(img));

            webView = new javafx.scene.web.WebView();
            webEngine = webView.getEngine();

            // Charger la page HTML contenant la carte Leaflet
            String htmlFilePath = Objects.requireNonNull(getClass().getResource("/html/map2.html")).toExternalForm();
            webEngine.load(htmlFilePath);

            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    // Récupérer les coordonnées de l'adresse sélectionnée via une API de géocodage (par exemple, Nominatim)
                    String selectedAddress = adressepharmacie.getText();
                    System.out.println("Adresse récupérée : " + selectedAddress);
                    if (!selectedAddress.isEmpty()) {
                        try {
                            URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + URLEncoder.encode(selectedAddress, StandardCharsets.UTF_8));
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();
                            JSONArray jsonArray = new JSONArray(response.toString());
                            if (!jsonArray.isEmpty()) {
                                JSONObject firstResult = jsonArray.getJSONObject(0);
                                double latitude = firstResult.getDouble("lat");
                                double longitude = firstResult.getDouble("lon");
                                // Centrer la carte sur les coordonnées récupérées et ajouter un marqueur
                                String jsScript = String.format("map.setView([%f, %f], 13); currentMarker = L.marker([%f, %f]).addTo(map);", latitude, longitude, latitude, longitude);
                                webEngine.executeScript(jsScript);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // Set the dimensions of the map Pane
            map.setPrefWidth(600); // Desired width in pixels
            map.setPrefHeight(520); // Desired height in pixels
            webView.setPrefSize(map.getPrefWidth(), map.getPrefHeight());

            map.getChildren().add(webView); // Add the WebView to your map Pane
            System.out.println("Adresse sélectionnée : ");
            selet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void selet(){
        webView = new javafx.scene.web.WebView();
        webEngine = webView.getEngine();

        // Charger la page HTML contenant la carte Leaflet
        String htmlFilePath = Objects.requireNonNull(getClass().getResource("/html/map2.html")).toExternalForm();
        webEngine.load(htmlFilePath);

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("app", this);
            }
        });

        // Set the dimensions of the map Pane
        map.setPrefWidth(600); // Desired width in pixels
        map.setPrefHeight(520); // Desired height in pixels
        webView.setPrefSize(map.getPrefWidth(), map.getPrefHeight());

        map.getChildren().add(webView); // Add the WebView to your map Pane

        selectedAddressProperty.addListener((observable, oldValue, newValue) -> {
            System.out.println("Adresse sélectionnée : " + newValue);
            adressepharmacie.setText(newValue);
        });
    }

    public SimpleStringProperty getSelectedAddressProperty() {
        return selectedAddressProperty;
    }

    public String updateSelectedAddress(String address) {
        selectedAddressProperty.set(address);
        return address;
    }
    public void initData2(String nom,String ntel,String adresse, String type) {
        nompharmcie.setText(nom);
        telpharmacie.setText(ntel);
        adressepharmacie.setText(adresse);
        typepharmacie.setText(type);
        initialPharmacieName = nom;
    }



    @FXML
    void editPharmacie(ActionEvent event) throws IOException {
        String nom = nompharmcie.getText();
        String ntel = telpharmacie.getText();
        String adresse = adressepharmacie.getText();
        String type = typepharmacie.getText();
        if (nom.isEmpty() || ntel.isEmpty() || adresse.isEmpty()  || type.isEmpty() ) {
            errorNom.setText("Champ obligatoire");
            errorNtel.setText("Champ obligatoire");
            errorAdresse.setText("Champ obligatoire");
            errorType.setText("Champ obligatoire");
            return;
        }
        if (nom.length() < 4) {
            errorNom.setText("Le nom du pharmacie doit contenir au moins 4 caractères.");
            return;
        }
        if (!ntel.matches("\\d{8}")) {
            errorNtel.setText("");
            errorNtel.setText("Le numéro de téléphone doit contenir uniquement 8 chiffres.");
            return;
        }
        if (adresse.length() < 4) {
            errorAdresse.setText("");
            errorAdresse.setText("L'adresse du pharmacie doit contenir au moins 4 caractères.");
            return; // Sortir de la méthode si l'adresse est trop courte
        }
        if (type.length() < 4) {
            errorType.setText("");
            errorType.setText("Le type du pharmacie doit contenir au moins 4 caractères.");
            return; // Sortir de la méthode si l'adresse est trop courte
        }

        PharamcieService pharamcieService = new PharamcieService();
        int PharmacienId = pharamcieService.getPharmacieIdByName(initialPharmacieName);

        if (PharmacienId != 0) {
            errorNom.setText("");
            errorNtel.setText("");
            errorAdresse.setText("");
            errorType.setText("");
            Pharmacie pharmacie = new Pharmacie(nom,  Integer.parseInt(ntel),adresse,type);
            pharamcieService.update(pharmacie, PharmacienId);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification réussie");
            alert.setHeaderText(null);
            alert.setContentText("Les informations du pharmacie ont été mises à jour avec succès.");
            alert.showAndWait();
            switchToScene1(event);
        } else {
            System.out.println("Aucune pharmacie trouvé avec le nom : " + initialPharmacieName);
        }
    }

    @FXML
    void switchToScene1(ActionEvent event) throws IOException {
        int idPharmacien = 1;
        Pharmacien pharmacien = pharmacienService.getById(idPharmacien);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherPharmacie.fxml"));
        Parent parent = loader.load();
        AfficherPharmacieController afficherPharmacieController = loader.getController();

        int idPharmacie = pharmacien.getId_pharmacie();
        String select = "SELECT * FROM pharmacie WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idPharmacie);
            rs = st.executeQuery();
            if (rs.next()) {
                // Passer les valeurs récupérées depuis la base de données en tant que paramètres
                afficherPharmacieController.initData(rs.getString("nom"), rs.getString("ntel"),rs.getString("adresse"),  rs.getString("type"));
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                afficherPharmacieController.initData(null, null, null, null);
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