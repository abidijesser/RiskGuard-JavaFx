package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import entites.Vie;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.VieService;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
    public class EmployeController {
        @FXML
        private TextField search;
        @FXML
        private ImageView loupe;
        @FXML
        private  Button  navigatee;

        @FXML
        private Button pdf;

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
        private ObservableList<Vie> originalList;

        @FXML
        public void initialize() {
            // Fetch data and populate TableView
            loadData();

            // Set up search functionality
            setUpSearch();
            navigatee.setOnAction(this::navigateToVehiculeEmploye);
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
                originalList = FXCollections.observableArrayList(vies);
                liste.setItems(originalList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void setUpSearch() {
            // Listen for changes in the search TextField
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                // Filter the original list based on the search query
                filterTableView(newValue);
            });
        }

        private void filterTableView(String query) {
            // Create a predicate to filter the TableView
            Predicate<Vie> filterPredicate = vie -> {
                if (query == null || query.isEmpty()) {
                    // If the search query is empty, display all items
                    return true;
                }
                // Filter based on the search query
                String lowerCaseQuery = query.toLowerCase();
                return vie.getNom().toLowerCase().contains(lowerCaseQuery)
                        || vie.getPrenom().toLowerCase().contains(lowerCaseQuery)
                        || vie.getCin().toLowerCase().contains(lowerCaseQuery)
                        || vie.getCausededecess().toLowerCase().contains(lowerCaseQuery)
                        || vie.getDateDeces().toLowerCase().contains(lowerCaseQuery)
                        || vie.getIdentifiantDeLinformantt().toLowerCase().contains(lowerCaseQuery);
            };

            // Filter the original list and update the TableView
            ObservableList<Vie> filteredList = originalList.filtered(filterPredicate);
            liste.setItems(filteredList);
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
                noItemSelectedAlert.showAndWait();}}
        public void navigateToVehiculeEmploye(ActionEvent event) {
            try {
                // Load the FXML file for VehiculeEmploye
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pidev/vehiculeEmploye.fxml"));
                Parent root = loader.load();

                // Create a new scene with the loaded FXML file
                Scene scene = new Scene(root);

                // Get the current stage
                Stage stage = (Stage) navigatee.getScene().getWindow();

                // Set the new scene
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception
            }
        }


        public void generatePDF(ActionEvent event) {
            // Get the selected item from the TableView
            Vie selectedVie = liste.getSelectionModel().getSelectedItem();
            if (selectedVie != null) {
                // Generate PDF for the selected Vie
                try {
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("VieDetails.pdf"));
                    document.open();

                    // Add title with custom font and color
                    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLUE); // Title color: Blue
                    Paragraph title = new Paragraph("Constat vie:", titleFont);
                    title.setAlignment(Paragraph.ALIGN_CENTER);
                    document.add(title);

                    // Add other information with formatting and color
                    Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK); // Text color: Black
                    Paragraph nom = new Paragraph("Nom: " + selectedVie.getNom(), normalFont);
                    Paragraph prenom = new Paragraph("Prenom: " + selectedVie.getPrenom(), normalFont);
                    Paragraph cin = new Paragraph("CIN: " + selectedVie.getCin(), normalFont);
                    Paragraph causeDeDeces = new Paragraph("Cause de deces: " + selectedVie.getCausededecess(), normalFont);
                    Paragraph dateDeDeces = new Paragraph("Date de deces: " + selectedVie.getDateDeces(), normalFont);
                    Paragraph identifiantInformant = new Paragraph("Identifiant de l'informant: " + selectedVie.getIdentifiantDeLinformantt(), normalFont);
                    document.add(nom);
                    document.add(prenom);
                    document.add(cin);
                    document.add(causeDeDeces);
                    document.add(dateDeDeces);
                    document.add(identifiantInformant);

                    document.close();

                    // Open the PDF file in the default PDF viewer
                    File file = new File("VieDetails.pdf");
                    Desktop.getDesktop().open(file);

                    // Show a success message or perform any other action
                    System.out.println("PDF generated successfully.");
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                    // Handle exception
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle exception
                }
            } else {
                // Show a warning if no item is selected
                Alert noItemSelectedAlert = new Alert(Alert.AlertType.WARNING);
                noItemSelectedAlert.setTitle("No Selection");
                noItemSelectedAlert.setHeaderText("No Vie Selected");
                noItemSelectedAlert.setContentText("Please select a Vie to generate PDF.");
                noItemSelectedAlert.showAndWait();
            }}}