package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import entites.Vie;
import services.VieService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;

    public class EmployeController {

        @FXML
        private TableColumn<Vie, String> causededecesv; // Updated type parameter

        @FXML
        private TableColumn<Vie, String> cinv; // Updated type parameter

        @FXML
        private TableColumn<Vie, String> datededecesv; // Updated type parameter

        @FXML
        private TableColumn<Vie, String> idv; // Updated type parameter

        @FXML
        private Label label;

        @FXML
        private TableView<Vie> liste;

        @FXML
        private TableColumn<Vie, String> nomv; // Updated type parameter

        @FXML
        private TableColumn<Vie, String> prenomv; // Updated type parameter

        @FXML
        private Button remove;

        @FXML
        private Button update;

        private VieService vieService = new VieService();


        @FXML

        public void initialize() {
            // Fetch data and populate TableView
            loadData();
        }

        private void loadData() {
            try {
                // Set up columns in the TableView
                nomv.setCellValueFactory(new PropertyValueFactory<>("nom"));
                prenomv.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                cinv.setCellValueFactory(new PropertyValueFactory<>("cin"));
                causededecesv.setCellValueFactory(new PropertyValueFactory<>("causededecess"));
                datededecesv.setCellValueFactory(new PropertyValueFactory<>("dateDeces"));
                idv.setCellValueFactory(new PropertyValueFactory<>("identifiantDeLinformantt"));

                // Fetch data and populate TableView
                List<Vie> vies = vieService.getAllConstatVies();
                ObservableList<Vie> vieObservableList = FXCollections.observableArrayList(vies);
                liste.setItems(vieObservableList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void UpdateViee() {
            // Get the selected item from the TableView
            Vie selectedVie = liste.getSelectionModel().getSelectedItem();
            if (selectedVie != null) {
                // Create a dialog for editing the selected 'Vie' object
                Dialog<Vie> dialog = new Dialog<>();
                dialog.setTitle("Edit Vie");
                dialog.setHeaderText("Edit Vie Information");

                // Set the button types (OK and Cancel)
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                // Create the dialog pane content
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                // Add text fields for editing 'Vie' attributes
                TextField nomField = new TextField(selectedVie.getNom());
                TextField prenomField = new TextField(selectedVie.getPrenom());
                TextField cinField = new TextField(selectedVie.getCin());
                TextField causededecesField = new TextField(selectedVie.getCausededecess());
                TextField datededecesField = new TextField(selectedVie.getDateDeces());
                TextField identifiantdelinformantField = new TextField(selectedVie.getIdentifiantDeLinformantt());

                grid.add(new Label("Nom:"), 0, 0);
                grid.add(nomField, 1, 0);
                grid.add(new Label("Prenom:"), 0, 1);
                grid.add(prenomField, 1, 1);
                grid.add(new Label("CIN:"), 0, 2);
                grid.add(cinField, 1, 2);
                grid.add(new Label("Cause de deces:"), 0, 3);
                grid.add(causededecesField, 1, 3);
                grid.add(new Label("Date de deces:"), 0, 4);
                grid.add(datededecesField, 1, 4);
                grid.add(new Label("Identifiant de l'informant:"), 0, 5);
                grid.add(identifiantdelinformantField, 1, 5);

                // Set the dialog pane content
                dialog.getDialogPane().setContent(grid);

                // Convert the result to a 'Vie' object when the OK button is clicked
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        // Create a new 'Vie' object with the updated attributes
                        Vie editedVie = new Vie(
                                nomField.getText(),
                                prenomField.getText(),
                                cinField.getText(),
                                causededecesField.getText(),
                                datededecesField.getText(),
                                identifiantdelinformantField.getText()
                        );
                        return editedVie;
                    }
                    return null;
                });

                // Show the dialog and wait for the user's response
                Optional<Vie> result = dialog.showAndWait();

                // If the user clicked OK, update the 'Vie' object
                result.ifPresent(editedVie -> {
                    try {
                        // Update the selected 'Vie' object with the attributes of the edited 'Vie' object
                        selectedVie.setNom(editedVie.getNom());
                        selectedVie.setPrenom(editedVie.getPrenom());
                        selectedVie.setCin(editedVie.getCin());
                        selectedVie.setCausededecess(editedVie.getCausededecess());
                        selectedVie.setDateDeces(editedVie.getDateDeces());
                        selectedVie.setIdentifiantDeLinformantt(editedVie.getIdentifiantDeLinformantt());

                        // Call the updateVie method with the updated 'Vie' object
                        vieService.updateVie(selectedVie);

                        // Refresh the TableView after updating
                        loadData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Handle SQL exception
                    }
                });
            } else {
                // Handle case when no item is selected
            }
        }


        public void deleteVie() {
            // Get the selected item from the TableView
            Vie selectedVie = liste.getSelectionModel().getSelectedItem();
            if (selectedVie != null) {
                // Create a confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Vie");
                confirmationAlert.setHeaderText("Confirm Deletion");
                confirmationAlert.setContentText("Are you sure you want to delete this Vie?");

                // Show the confirmation dialog and wait for user response
                confirmationAlert.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.OK) {
                        try {
                            // Call the deleteVie method with the selected item
                            vieService.deleteVie(selectedVie);
                            // Remove the selected item from the TableView
                            liste.getItems().remove(selectedVie);
                        } catch (SQLException e) {
                            e.printStackTrace(); // Handle the exception as needed
                        }
                    }
                });
            } else {
                // Show a warning alert if no item is selected
                Alert noItemSelectedAlert = new Alert(Alert.AlertType.WARNING);
                noItemSelectedAlert.setTitle("No Selection");
                noItemSelectedAlert.setHeaderText("No Vie Selected");
                noItemSelectedAlert.setContentText("Please select a Vie to delete.");
                noItemSelectedAlert.showAndWait();}}}