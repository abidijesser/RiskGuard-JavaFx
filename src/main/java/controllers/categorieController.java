package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import models.categorie;
import services.categorieService;
import javafx.util.StringConverter;

import java.sql.SQLException;

public class categorieController {

    @FXML
    private ComboBox<categorie> categorieComboBox;

    // Assuming you have a CategoryService similar to marketingService for database operations
    private categorieService categoryService;

    public categorieController() {
        categorieService categorieService = new categorieService();
    }

    @FXML
    private void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        try {
            ObservableList<categorie> categories = FXCollections.observableArrayList(categoryService.getAll());
            categorieComboBox.setItems(categories);

            categorieComboBox.setConverter(new StringConverter<categorie>() {
                @Override
                public String toString(categorie category) {
                    return category == null ? "" : category.getName();
                }

                @Override
                public categorie fromString(String name) {
                    return categories.stream().filter(cat -> cat.getName().equals(name)).findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }
}
