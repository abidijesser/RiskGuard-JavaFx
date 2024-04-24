package controllers;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import entites.Vehicule;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.VehiculeService;
import javafx.fxml.Initializable;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class VehiculeController {
    @FXML
    private Button ajouter;

    @FXML
    private TextField cinn;

    @FXML
    private Text cinve;

    @FXML
    private TextField datee;

    @FXML
    private Text dateve;

    @FXML
    private TextField descc;

    @FXML
    private Text descve;

    @FXML
    private TextField imagee;

    @FXML
    private Text imageve;

    @FXML
    private TextField lieuu;

    @FXML
    private Text lieuve;

    @FXML
    private ImageView logove;

    @FXML
    private TextField matriculee;

    @FXML
    private Text matriculeve;

    @FXML
    private TextField nomm;

    @FXML
    private Text nomve;

    @FXML
    private TextField prenomm;

    @FXML
    private Text prenomve;

    @FXML
    private Label titre;

    @FXML
    private TextField typee;

    @FXML
    private Text typeve;
    @FXML
    private Text titreE;


    private VehiculeService VehiculeService; // Assuming you have a reference to your service

    // Method to set the service
    public VehiculeController() {
        this.VehiculeService = new VehiculeService();
    }
    @FXML
    public void initialize() {
        // Add action event handler to the button

        ajouter.setOnAction(event -> addConstatVehicule());
    }

    // Function to validate input and add vehicule to the database
    public void addConstatVehicule() {
        // Retrieve values from text fields
        String nomValue = nomm.getText();
        String prenomValue = prenomm.getText();
        String cinValue = cinn.getText();
        String typeValue = typee.getText();
        String matriculeValue = matriculee.getText();
        String lieuValue = lieuu.getText();
        String dateValue = datee.getText();
        String descValue = descc.getText();
        String imageValue = imagee.getText();

        // Validate fields
        if (nomValue.isEmpty() || prenomValue.isEmpty() || cinValue.isEmpty() || typeValue.isEmpty() ||
                matriculeValue.isEmpty() || lieuValue.isEmpty() || dateValue.isEmpty() || descValue.isEmpty() || imageValue.isEmpty()) {
            // Show alert if any field is empty
            showAlert("All fields are required.");
            return;
        }

        // Create Vehicule object with the retrieved values
        Vehicule vehicule = new Vehicule(nomValue, prenomValue, cinValue, typeValue, matriculeValue, lieuValue, dateValue, descValue, imageValue);

        try {
            // Add Vehicule object to the database
            VehiculeService.addConstatVehicule(vehicule);
            System.out.println("Constat vehicule added successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to add constat vehicule: " + e.getMessage());
        }
    }

    // Method to show an alert message
    private void showAlert(String message) {
        // Implement your alert logic here
        System.out.println("Alert: " + message);
    }



    public void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        Stage stage = (Stage) ajouter.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Set the selected image file path to the TextField
            imagee.setText(selectedFile.getPath());
        }
    }}
