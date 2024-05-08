package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Department;
import services.departmentService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Gestiondep {

    @FXML
    private TextField searchTextField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button displayButton;

    @FXML
    private Button updateButton;

    @FXML
    private TableColumn<Department, String> local;

    @FXML
    private TableColumn<Department, String> chef_dep;

    @FXML
    private TableColumn<Department, Integer> code;

    @FXML
    private TableColumn<Department, Integer> idep;

    @FXML
    private TableView<Department> tableview;

    private final departmentService dep = new departmentService();

    @FXML
    void display(ActionEvent event) {
        local.setCellValueFactory(new PropertyValueFactory<>("local"));
        chef_dep.setCellValueFactory(new PropertyValueFactory<>("chef_dep"));
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        idep.setCellValueFactory(new PropertyValueFactory<>("idep"));



        try {
            // Fetch employee data from the service
            List<Department> departmentList = dep.getDepartmentList();

            // Populate the TableView with the retrieved data
            tableview.setItems(FXCollections.observableArrayList(departmentList));
        } catch (SQLException e) {
            // Handle any SQL exceptions by printing the stack trace
            e.printStackTrace();
            // You may also consider displaying an error message to the user
        }
    }

    @FXML
    void filterByLocal(ActionEvent event) {
        try {
            List<Department> departmentList = dep.getDepartmentList();

            Collections.sort(departmentList, Comparator.comparing(Department::getLocal));

            tableview.setItems(FXCollections.observableArrayList(departmentList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void filterDepartments(String query) {
        try {
            List<Department> departmentList = dep.getDepartmentList();
            List<Department> filteredList = new ArrayList<>();
            for (Department dep : departmentList) {
                if (depContainsQuery(dep, query)) {
                    filteredList.add(dep);
                }
            }
            tableview.setItems(FXCollections.observableArrayList(filteredList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean depContainsQuery(Department dep, String query) {
        return dep.getLocal().toLowerCase().contains(query.toLowerCase()) ||
                dep.getChef_dep().toLowerCase().contains(query.toLowerCase()) ||
                Integer.toString(dep.getCode()).toLowerCase().contains(query.toLowerCase()) ||
                Integer.toString(dep.getIdep()).toLowerCase().contains(query.toLowerCase());
    }

    @FXML
    void ajouterdep(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_dep.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AddDep controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.setTitle("Add Department");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Call the method to add employee in the AddEmployee controller
                controller.ajouterdep(new ActionEvent());

                // Optionally, you can refresh the display after adding the employee
                // display(new ActionEvent());
            }
        });
    }


    @FXML
    void delete(ActionEvent event) {
        Department selectedDepartment = tableview.getSelectionModel().getSelectedItem();

        if (selectedDepartment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete department");
            alert.setContentText("Are you sure you want to delete this department?");

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                try {
                    dep.delete(selectedDepartment.getIdep());
                    tableview.getItems().remove(selectedDepartment);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert noSelectionAlert = new Alert(Alert.AlertType.INFORMATION);
            noSelectionAlert.setTitle("No Selection");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Please select a department to delete.");
            noSelectionAlert.showAndWait();
        }
    }
    @FXML
    void updatedep(ActionEvent event) {
        Department selectedDepartment = tableview.getSelectionModel().getSelectedItem();

        if (selectedDepartment != null) {
            // Load the Edit_dep.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Edit_dep.fxml"));

            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Get the EditDep controller
            EditDep editController = loader.getController();

            // Pass the selected department to the EditDep controller
            editController.initData(selectedDepartment);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(root);
            dialog.setTitle("Update Department");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    tableview.refresh();
                }
            });
        } else {
            Alert noSelectionAlert = new Alert(Alert.AlertType.INFORMATION);
            noSelectionAlert.setTitle("No Selection");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Please select a department to update.");
            noSelectionAlert.showAndWait();
        }
    }




    public Department getSelectedDepartment() {
        return tableview.getSelectionModel().getSelectedItem();
    }

    @FXML
    void initialize() {
        //searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        // filterDepartments(newValue);
        //});
    }

    @FXML
    void retourneremp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionemp.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Optionally, you can get the controller of the "gestiondep.fxml" if needed
        Gestionemp controller = loader.getController();

        // Display the new interface
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
