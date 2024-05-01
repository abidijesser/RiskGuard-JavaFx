package controllers;

import entites.Vie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import services.VieService;


import java.sql.SQLException;


public class vieController  {
     @FXML
     private TextField nom;

     @FXML
     private TextField prenom;

     @FXML
     private TextField cin;

     @FXML
     private TextField causededeces;

     @FXML
     private TextField datededeces;

     @FXML
     private TextField identifiantdelinformant;

     @FXML
     private Button addButton;
    @FXML
    private Button constatvehicule;

     private VieService vieService;


     public vieController() {
         // Initialize your service class here
         this.vieService = new VieService();
     }

     @FXML
     public void initialize() {
         // Add action event handler to the button
         addButton.setOnAction(event -> vaddVie());
     }

    public void vaddVie() {
        // Retrieve values from text fields
        String nomValue = nom.getText();
        String prenomValue = prenom.getText();
        String cinValue = cin.getText();
        String causededecesValue = causededeces.getText();
        String datededecesValue = datededeces.getText();
        String identifiantdelinformantValue = identifiantdelinformant.getText();

        // Validate fields
        if (nomValue.isEmpty()) {
            showAlert(nom, "Nom cannot be empty.");
            return;
        }
        if (nomValue.matches(".*\\d.*")) {
            showAlert(nom, "Nom cannot contain numbers.");
            return;
        }

        if (prenomValue.isEmpty()) {
            showAlert(prenom, "Prenom cannot be empty.");
            return;
        }
        if (prenomValue.matches(".*\\d.*")) {
            showAlert(prenom, "Prenom cannot contain numbers.");
            return;
        }
        if (cinValue.isEmpty()) {
            showAlert(cin, "CIN cannot be empty.");
            return;
        }

        if (cinValue.length() != 8) {
            showAlert(cin, "CIN must have exactly 8 digits.");
            return;
        }

        int cinInt;
        try {
            cinInt = Integer.parseInt(cinValue);
        } catch (NumberFormatException e) {
            showAlert(cin, "CIN must be a valid integer.");
            return;
        }

        // Other validations for 'causededeces', 'datededeces', and 'identifiantdelinformant' fields if needed

        // Create Vie object with the retrieved values
        Vie vie = new Vie(nomValue, prenomValue, cinValue, causededecesValue, datededecesValue, identifiantdelinformantValue);

        try {
            // Add Vie object to the database
            vieService.vaddVie(vie);
            System.out.println("Vie added successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to add Vie: " + e.getMessage());
        }
    }

    private void showAlert(TextField field, String message) {
        // Create a Tooltip with the error message
        Tooltip tooltip = new Tooltip(message);

        // Convert the coordinates relative to the text field's parent to screen coordinates
        double screenX = field.localToScreen(field.getBoundsInLocal()).getMinX();
        double screenY = field.localToScreen(field.getBoundsInLocal()).getMinY();

        // Set the Tooltip above the text field
        tooltip.setAutoHide(true);
        tooltip.show(field, screenX, screenY - tooltip.getHeight());
    }

    public void constatvehicule(ActionEvent event) {
        try {
            // Load the FXML file for VehiculeEmploye
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pidev/vehicule.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) constatvehicule.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }}}