package controllers;

import entites.Vehicule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import org.w3c.dom.Document;
import services.VehiculeService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import entites.Vie;

public class VehiculeEmployeController {
    @FXML
    private TableView<Vehicule> tableve;
    @FXML
    private TableColumn<Vehicule, String> cinveh;

    @FXML
    private TableColumn<Vehicule, String> dateveh;

    @FXML
    private Button deleteve;

    @FXML
    private TableColumn<Vehicule, String> descveh;

    @FXML
    private TableColumn<Vehicule, String> imageveh;

    @FXML
    private TableColumn<Vehicule, String> lieuveh;

    @FXML
    private ImageView logo;

    @FXML
    private TableColumn<Vehicule, String> matriculeveh;

    @FXML
    private TableColumn<Vehicule, String> nomveh;

    @FXML
    private TableColumn<Vehicule, String> prenomveh;

    @FXML
    private TableView<Vehicule> liste;

    @FXML
    private Label titre;

    @FXML
    private Text titree;

    @FXML
    private TableColumn<Vehicule, String> typeveh;

    @FXML
    private Button updateve;
    @FXML
    private Button navigatetostatistic;

    private VehiculeService vehiculeService;

    public VehiculeEmployeController() {
        this.vehiculeService = new VehiculeService();
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        nomveh.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomveh.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        cinveh.setCellValueFactory(new PropertyValueFactory<>("cin"));
        typeveh.setCellValueFactory(new PropertyValueFactory<>("typeVehicule"));
        matriculeveh.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        lieuveh.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        dateveh.setCellValueFactory(new PropertyValueFactory<>("date"));
        descveh.setCellValueFactory(new PropertyValueFactory<>("description"));
        imageveh.setCellFactory(new Callback<TableColumn<Vehicule, String>, TableCell<Vehicule, String>>() {
            @Override
            public TableCell<Vehicule, String> call(TableColumn<Vehicule, String> param) {
                return new TableCell<Vehicule, String>() {
                    private final ImageView imageView = new ImageView();

                    {
                        // Preserve the aspect ratio of the image
                        imageView.setPreserveRatio(true);
                        // Automatically adjust the fit width to fit the image
                        imageView.setFitWidth(100); // You can adjust this value as needed
                    }

                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if (empty || imageUrl == null) {
                            imageView.setImage(null);
                        } else {
                            try {
                                Image image = new Image(imageUrl);
                                imageView.setImage(image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        setGraphic(imageView);
                    }
                };
            }
        });
        navigatetostatistic.setOnAction(this::navigateTostatistic);
        // Load data into the table view
        try {
            loadData();
        } catch (SQLException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws SQLException, MalformedURLException {
        List<Vehicule> vehicules = vehiculeService.getAllConstatVehicules();
        ObservableList<Vehicule> vehiculeObservableList = FXCollections.observableArrayList(vehicules);
        tableve.setItems(vehiculeObservableList);
    }


    public void updateveh() {
        // Get the selected vehicle from the table
        Vehicule selectedVehicule = tableve.getSelectionModel().getSelectedItem();
        if (selectedVehicule == null) {
            // No vehicle selected, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a vehicle to update.");
            alert.showAndWait();
            return;
        }

        Dialog<Vehicule> dialog = new Dialog<>();
        dialog.setTitle("Update Vehicle Information");
        dialog.setHeaderText("Enter the updated information:");


        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the grid for user input
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        // Add input fields to the grid
        TextField nomvehField = new TextField(selectedVehicule.getNom());
        TextField prenomvehField = new TextField(selectedVehicule.getPrenom());
        TextField cinvehField = new TextField(selectedVehicule.getCin());
        TextField typevehField = new TextField(selectedVehicule.getTypeVehicule());
        TextField matriculevehField = new TextField(selectedVehicule.getMatricule());
        TextField lieuvehField = new TextField(selectedVehicule.getLieu());
        TextField datevehField = new TextField(selectedVehicule.getDate());
        TextField descvehField = new TextField(selectedVehicule.getDescription());
        TextField imagevehField = new TextField(selectedVehicule.getImage());

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomvehField, 1, 0);
        grid.add(new Label("Prenom:"), 0, 1);
        grid.add(prenomvehField, 1, 1);
        grid.add(new Label("CIN:"), 0, 2);
        grid.add(cinvehField, 1, 2);
        grid.add(new Label("Type Véhicule:"), 0, 3);
        grid.add(typevehField, 1, 3);
        grid.add(new Label("Matricule:"), 0, 4);
        grid.add(matriculevehField, 1, 4);
        grid.add(new Label("Lieu:"), 0, 5);
        grid.add(lieuvehField, 1, 5);
        grid.add(new Label("Date:"), 0, 6);
        grid.add(datevehField, 1, 6);
        grid.add(new Label("Description:"), 0, 7);
        grid.add(descvehField, 1, 7);
        grid.add(new Label("Image:"), 0, 8);
        grid.add(imagevehField, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a vehicle when the update button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                selectedVehicule.setNom(nomvehField.getText());
                selectedVehicule.setPrenom(prenomvehField.getText());
                selectedVehicule.setCin(cinvehField.getText());
                selectedVehicule.setTypeVehicule(typevehField.getText());
                selectedVehicule.setMatricule(matriculevehField.getText());
                selectedVehicule.setLieu(lieuvehField.getText());
                selectedVehicule.setDate(datevehField.getText());
                selectedVehicule.setDescription(descvehField.getText());
                selectedVehicule.setImage(imagevehField.getText());
                return selectedVehicule;
            }
            return null;
        });

        // Show the dialog and wait for user input
        Optional<Vehicule> result = dialog.showAndWait();

        // If the user clicked the update button, update the vehicle
        result.ifPresent(updatedVehicule -> {
            try {
                vehiculeService.updateConstatVehicule(updatedVehicule);
                // Reload data after update
                loadData();
            } catch (SQLException | MalformedURLException e) {
                e.printStackTrace();
                // Display an error message if update fails
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update vehicle information.");
                alert.showAndWait();
            }
        });

    }
    public void deleteveh() {
        // Get the selected vehicle from the table
        Vehicule selectedVehicule = tableve.getSelectionModel().getSelectedItem();
        if (selectedVehicule == null) {
            // No vehicle selected, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a vehicle to delete.");
            alert.showAndWait();
            return;
        }

        // Confirm deletion with the user
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Confirm Deletion");
        confirmAlert.setContentText("Are you sure you want to delete the selected vehicle?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        // If the user confirms deletion, proceed with the deletion operation
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Call the deleteConstatVehicule method to delete the selected vehicle
                vehiculeService.deleteConstatVehicule(selectedVehicule.getMatricule());
                // Reload data after deletion
                loadData();
            } catch (SQLException | MalformedURLException e) {
                e.printStackTrace();
                // Display an error message if deletion fails
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to delete the vehicle.");
                errorAlert.showAndWait();
            }
        }
    }

    public void navigateTostatistic(ActionEvent event) {
        try {
            // Load the FXML file for VehiculeEmploye
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pidev/statistic.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) navigatetostatistic.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }}

}