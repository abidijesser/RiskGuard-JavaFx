package controllers;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.marketing;
import services.marketingService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class marketingCRUD{

    @FXML
    private TextField titreField;

    @FXML
    private TextField objectifField;

    @FXML
    private TextField budgetField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML private TableView<marketing> marketingTableView;
    @FXML private TableColumn<marketing, String> titreColumn;
    @FXML private TableColumn<marketing, String> objectifColumn;
    @FXML private TableColumn<marketing, Number> budgetColumn;
    @FXML private TableColumn<marketing, Date> startDateColumn;
    @FXML private TableColumn<marketing, Date> endDateColumn;



    @FXML
    private Button addMarketingButton;
    private marketingService marketingService;

    // ObservableList to hold marketing data
    private ObservableList<marketing> marketingList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up the columns in the table
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        objectifColumn.setCellValueFactory(new PropertyValueFactory<>("objectif"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("budget"));
        startDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(new Date(cellData.getValue().getDateDebut().getTime())));
        endDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(new Date(cellData.getValue().getDateFin().getTime())));

        // Load marketing data
        loadMarketingData();
        setupRowSelection();
    }

    private void setupRowSelection() {
        marketingTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                marketing selectedMarketing = marketingTableView.getSelectionModel().getSelectedItem();
                updateInputFields(selectedMarketing);
            }
        });
    }

    private void updateInputFields(marketing m) {
        titreField.setText(m.getTitre());
        objectifField.setText(m.getObjectif());
        budgetField.setText(String.valueOf(m.getBudget())); // Ensure conversion to String
        dateDebutPicker.setValue(m.getDateDebut().toLocalDate()); // Convert java.sql.Date to LocalDate
        dateFinPicker.setValue(m.getDateFin().toLocalDate()); // Convert java.sql.Date to LocalDate
    }


    private void loadMarketingData() {
        try {
            ObservableList<marketing> marketingData = FXCollections.observableArrayList(marketingService.getAll());
            marketingTableView.setItems(marketingData);
        } catch (SQLException e) {
            System.err.println("Error fetching marketing data: " + e.getMessage());
        }
    }

    public marketingCRUD() {
        // Initialize marketingService in the constructor
        this.marketingService = new marketingService();
    }

    private void fetchMarketingData() {
        try {
            // Call getAll() method from your service class to fetch marketing data
            List<marketing> marketingData = marketingService.getAll();

            // Clear existing data in the TableView
            marketingList.clear();

            // Add fetched marketing data to the ObservableList
            marketingList.addAll(marketingData);
        } catch (SQLException e) {
            System.err.println("Error fetching marketing data: " + e.getMessage());
        }
    }

    // You need to define a method to handle the button click event
    @FXML
    private void handleUpdateMarketingButton() {
        marketing selectedMarketing = marketingTableView.getSelectionModel().getSelectedItem();
        if (selectedMarketing != null) {
            selectedMarketing.setTitre(titreField.getText());
            selectedMarketing.setObjectif(objectifField.getText());
            try {
                selectedMarketing.setBudget(Double.parseDouble(budgetField.getText()));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid budget format");
                alert.showAndWait();
                return;
            }
            selectedMarketing.setDateDebut(Date.valueOf(dateDebutPicker.getValue()));
            selectedMarketing.setDateFin(Date.valueOf(dateFinPicker.getValue()));

            try {
                marketingService.update(selectedMarketing);
                System.out.println("Marketing updated successfully!");
                loadMarketingData(); // Refresh data in the table
            } catch (SQLException e) {
                System.err.println("Error updating marketing: " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleDeleteMarketingButton() {
        marketing selectedMarketing = marketingTableView.getSelectionModel().getSelectedItem();
        if (selectedMarketing != null) {
            try {
                marketingService.delete(selectedMarketing.getId());
                marketingTableView.getItems().remove(selectedMarketing); // Remove from table view
                System.out.println("Marketing deleted successfully!");
            } catch (SQLException e) {
                System.err.println("Error deleting marketing: " + e.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No marketing selected for deletion.");
            alert.showAndWait();
        }
        if (confirmDeletion()) {
            try {
                marketingService.delete(selectedMarketing.getId());
                marketingTableView.getItems().remove(selectedMarketing); // Remove from table view
                System.out.println("Marketing deleted successfully!");
            } catch (SQLException e) {
                System.err.println("Error deleting marketing: " + e.getMessage());
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error occurred while deleting marketing entry: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }
    private boolean confirmDeletion() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this marketing entry?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }



    @FXML
    private void handleAddMarketingButton() {
        System.out.println("Adding marketing");
        String titre = titreField.getText();
        String objectif = objectifField.getText();
        double budget = Double.parseDouble(budgetField.getText());
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        System.out.println("Data: " + titre + ", " + objectif + ", " + budget + ", " + dateDebut + ", " + dateFin);

        marketing marketing = new marketing(titre, objectif, budget, dateDebut, dateFin);

        try {
            marketingService.add(marketing);
        } catch (SQLException e) {
            System.err.println("Error adding marketing: " + e.getMessage());
        }
    }

}
