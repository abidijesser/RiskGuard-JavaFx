package controllers;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.marketing;
import services.categorieService;
import services.marketingService;
import models.categorie;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import services.marketingService;

import java.sql.SQLException;
import controllers.StatisticsController;
import utils.SmsService;

public class marketingCRUD{

    public Button showCategorieButton;
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
    @FXML private TableColumn<marketing, String> categorieColumn;
    @FXML private TableColumn<marketing, String> statusColumn;


    @FXML
    private ComboBox<categorie> categorieComboBox;
    private categorieService categoryService;
    @FXML
    private RadioButton activeRadioButton;
    @FXML
    private RadioButton endedRadioButton;

    @FXML
    private ToggleGroup statusToggleGroup;



    @FXML
    private Button addMarketingButton;
    private marketingService marketingService;


    // ObservableList to hold marketing data
    private ObservableList<marketing> marketingList = FXCollections.observableArrayList();
    @FXML
    private ComboBox<categorie> filterCategoryComboBox;



    @FXML
    private void handleShowStatistics() {
        try {
            // Load the FXML file for the statistics interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statistics.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the loaded FXML
            StatisticsController controller = loader.getController();

            if (controller == null) {
                System.out.println("Controller is null");
            } else {
                System.out.println("Controller is not null");

                // Set data to the PieChart
                controller.setData(controller.getActiveMarketingCount(), controller.getEndedMarketingCount());
            }

            // Create a new stage for the statistics interface
            Stage stage = new Stage();
            stage.setTitle("Statistics");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleShowCategorieButton() {
        try {
            // Load the categorie FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorie.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) showCategorieButton.getScene().getWindow(); // Ensure showCategorieButton is the Button's fx:id
            stage.setTitle("Category Management");

            // Set the scene to the new layout
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions, perhaps show an error message
        }
    }


    private void loadCategoriesFilter() {
        try {
            List<categorie> categories = categoryService.getAll();
            categorie allCategories = new categorie(0, "All Categories");
            categories.add(0, allCategories);
            categorieComboBox.setItems(FXCollections.observableArrayList(categories));
            filterCategoryComboBox.setItems(FXCollections.observableArrayList(categories));
            filterCategoryComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }
    @FXML
    private void handleUploadImageButton() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            marketing selectedMarketing = marketingTableView.getSelectionModel().getSelectedItem();
            if (selectedMarketing != null) {
                selectedMarketing.setImagePath(file.getAbsolutePath());
                try {
                    marketingService.updateMarketingImage(selectedMarketing); // Update marketing with new image path
                    loadMarketingData(); // Optionally refresh data to reflect changes
                } catch (SQLException e) {
                    showAlert("Error", "Failed to update the marketing image: " + e.getMessage());
                }
            } else {
                showAlert("Error", "No marketing selected.");
            }
        }
    }


    private void setupCategoryFilter() {
        filterCategoryComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterMarketingList(newVal);
            }
        });
    }


    private void filterMarketingList(categorie selectedCategory) {
        try {
            ObservableList<marketing> filteredList;
            if (selectedCategory.getId() == 0) { // Assuming "All Categories" has an ID of 0
                filteredList = FXCollections.observableArrayList(marketingService.getAll());
            } else {
                filteredList = FXCollections.observableArrayList(marketingService.getByCategory(selectedCategory.getId()));
            }
            marketingTableView.setItems(filteredList);
        } catch (SQLException e) {
            System.err.println("Error filtering marketing data: " + e.getMessage());
            showAlert("Database Error", "Error filtering marketing data.");
        }
    }



    @FXML
    private void initialize() {

        // Initialize the ToggleGroup
        statusToggleGroup = new ToggleGroup();
        activeRadioButton.setToggleGroup(statusToggleGroup);
        endedRadioButton.setToggleGroup(statusToggleGroup);
        // Listen for changes in the radio button selection
        statusToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;  // Correctly cast the newValue to RadioButton
                filterMarketingByStatus(selectedRadioButton.getText());
            }
        });
        // Set up the columns in the table

        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        objectifColumn.setCellValueFactory(new PropertyValueFactory<>("objectif"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("budget"));
        startDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(new Date(cellData.getValue().getDateDebut().getTime())));
        endDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(new Date(cellData.getValue().getDateFin().getTime())));
        categorieColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));
        // Set up Status column
        statusColumn.setCellValueFactory(cellData -> {
            marketing marketing = cellData.getValue();
            String status = "";
            if (marketing.getDateFin().before(Date.valueOf(LocalDate.now()))) {
                status = "Ended";
            } else {
                status = "Active";
            }
            return new SimpleStringProperty(status);
        });

        // Load marketing data
        loadMarketingData();
        setupRowSelection();
        loadCategories();
        loadCategoriesFilter();
        setupCategoryFilter();
        setupCategoryFilter();
    }

    private void filterMarketingByStatus(String status) {
        ObservableList<marketing> filteredList = FXCollections.observableArrayList();
        if ("Active".equals(status)) {
            filteredList = marketingList.stream()
                    .filter(m -> !m.getDateFin().before(Date.valueOf(LocalDate.now())))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else if ("Ended".equals(status)) {
            filteredList = marketingList.stream()
                    .filter(m -> m.getDateFin().before(Date.valueOf(LocalDate.now())))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        marketingTableView.setItems(filteredList);
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
            // Call to fetch all marketing data from the database
            List<marketing> allMarketingData = marketingService.getAll();
            marketingList.clear(); // Clear existing data
            marketingList.addAll(allMarketingData); // Add all fetched data
            marketingTableView.setItems(marketingList); // Set items to TableView
            System.out.println("Data loaded successfully!");
        } catch (SQLException e) {
            System.err.println("Error fetching marketing data: " + e.getMessage());
        }
    }


    public marketingCRUD() {
        // Initialize marketingService in the constructor
        this.marketingService = new marketingService();
        this.categoryService = new categorieService();
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
            selectedMarketing.setCategory(categorieComboBox.getValue()); // Update the category

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
        if (selectedMarketing == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No marketing selected for deletion.");
            alert.showAndWait();
            return; // Stop further execution if no marketing is selected
        }

        if (!confirmDeletion()) {
            return; // Stop the deletion if the user does not confirm
        }

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




    private boolean confirmDeletion() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this marketing entry?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }



    @FXML
    private void handleAddMarketingButton() {
        if (!validateInputs()) {
            return;
        }

        String titre = titreField.getText();
        String objectif = objectifField.getText();
        double budget = Double.parseDouble(budgetField.getText());
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        categorie selectedCategory = categorieComboBox.getValue();

        marketing newMarketing = new marketing(titre, objectif, budget, dateDebut, dateFin);
        newMarketing.setCategory(selectedCategory);
        try {
            marketingService.add(newMarketing);

            loadMarketingData();
            System.out.println("Marketing added successfully!");
            // Send an SMS notification
            String message = "New marketing campaign added: " + titre + ". Check it out!";
            SmsService.sendSms("+21654113122", message);  // Replace "+1234567890" with your phone number or the recipient's number

            // Show a success message in the GUI
            showAlert("Success", "Marketing added and notified via SMS!");
        } catch (SQLException e) {
            System.err.println("Error adding marketing: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (titreField.getText().length() > 250) {
            showAlert("Validation Error", "Title must not exceed 250 characters.");
            return false;
        }
        // Check if the objective field is empty
        if (objectifField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Objective must not be empty.");
            return false;
        }

        if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null || dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
            showAlert("Validation Error", "Start date must be before the end date.");
            return false;
        }
        try {
            double budget = Double.parseDouble(budgetField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Budget must be a numeric value.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    private void loadCategories() {
        try {
            categorieComboBox.setItems(FXCollections.observableArrayList(categoryService.getAll()));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    public void handleGeneratePdf(ActionEvent actionEvent) {
        marketingService service = new marketingService();
        try {
            // Define the path to the resources directory where the PDF should be saved
            String basePath = System.getProperty("user.dir"); // Gets the root directory of the project
            String outputPath = basePath + "/src/main/resources/pdf/output.pdf";

            service.generateMarketingReport(outputPath);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("PDF Generated Successfully in Resources Folder!");
            alert.showAndWait();
        } catch (Exception e) {
            // Show an error message if something goes wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Generate PDF");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }


}
