package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import models.categorie;
import services.categorieService;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class categorieController {

    public Button returnButton;
    @FXML
    private ComboBox<categorie> categorieComboBox;
    private ObservableList<categorie> categoriesList = FXCollections.observableArrayList();
    private categorieService categoryService = new categorieService();

    @FXML
    private TextField nameField;

    @FXML
    private TableView<categorie> categoryTableView;

    @FXML
    private TableColumn<categorie, Integer> idColumn;

    @FXML
    private TableColumn<categorie, String> nameColumn;


    @FXML
    private void initialize() {
        categoryService = new categorieService(); // Instantiate categorieService
        idColumn.setCellValueFactory(new PropertyValueFactory<categorie, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<categorie, String>("name"));

        loadCategories();
    }
    @FXML
    private void handleReturnButton() {
        try {
            // Load the marketingCRUD FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/marketingCRUD.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) returnButton.getScene().getWindow(); // Make sure returnButton is the Button's fx:id
            stage.setTitle("Marketing Management");

            // Set the scene to the new layout
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions, perhaps show an error message
        }
    }


    private void loadCategories() {
        try {
            categoriesList.setAll(categoryService.getAll());
            // Populate TableView
            categoryTableView.setItems(categoriesList);
            System.out.println("Categories fetched: " + categoriesList);


        } catch (SQLException e) {
            System.out.println("Error loading categories: " + e.getMessage());
            // Proper error handling should be implemented
        }
    }

    @FXML
    private void handleAddButton() {
        String categoryName = nameField.getText().trim();
        if (!categoryName.isEmpty()) {
            try {
                categorie newCategory = new categorie(categoryName);
                categoryService.add(newCategory); // Assume this method correctly inserts the category and sets its ID
                categoriesList.add(newCategory); // Add the new category directly to the list backing the TableView
                showAlert("Success", "Category added successfully!");
                clearFields();
            } catch (SQLException e) {
                showAlert("Error", "Failed to add category: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please enter a category name.");
        }
    }


    @FXML
    private void handleUpdateButton() {
        categorie selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        String updatedName = nameField.getText().trim();
        if (selectedCategory != null && !updatedName.isEmpty()) {
            selectedCategory.setName(updatedName);
            try {
                categoryService.update(selectedCategory);
                categoryTableView.refresh(); // Refresh the TableView to display the updated data
                showAlert("Success", "Category updated successfully!");
                clearFields();
            } catch (SQLException e) {
                showAlert("Error", "Failed to update category: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please select a category and enter a new name.");
        }
    }





    @FXML
    private void handleDeleteButton() {
        categorie selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                categoryService.delete(selectedCategory.getId());
                categoriesList.remove(selectedCategory); // Remove the category from the ObservableList
                showAlert("Success", "Category deleted successfully!");
                clearFields();
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete category: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please select a category to delete.");
        }
    }



    private void showAlert (String title, String message){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        private void clearFields () {
            nameField.clear();
        }

    }

