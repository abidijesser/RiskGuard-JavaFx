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
import javafx.util.Callback;
import models.Employee;

import services.employeeService;

import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.util.Callback;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import models.Employee;
import services.employeeService;

import java.sql.SQLException;
import java.util.List;


import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Gestionemp {

    @FXML
    private TextField searchTextField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button displayButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button ajouteButton;

    @FXML
    private TableColumn<Employee, String> nom;

    @FXML
    private TableColumn<Employee, String> email;
    @FXML
    private TableColumn<Employee, String> salaire;

    @FXML
    private TableColumn<Employee, Integer> id;

    @FXML
    private TableColumn<Employee, Integer> idep;

    @FXML
    private TableView<Employee> tableview;

    @FXML
    private Button generatePDFButton;





    @FXML
    private Pagination pagination;



    private final employeeService emp = new employeeService();
    private final int itemsPerPage = 5 ;





    @FXML
    void display() {
        int pageIndex = pagination.getCurrentPageIndex();
        try {
            // Fetch employee data from the service for the current page
            List<Employee> employeeList = emp.getEmployeeForPage(pageIndex, itemsPerPage);

            // Populate the TableView with the retrieved data
            populateTable(employeeList);
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            // You may also consider displaying an error message to the user
        }
    }

    // Method to populate the TableView with employee data
    private void populateTable(List<Employee> data) {
        TableColumn<Employee, String> nom = new TableColumn<>("Nom");
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Employee, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Employee, String> salaire = new TableColumn<>("Salaire");
        salaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));

        tableview.getColumns().clear();
        tableview.getColumns().addAll(nom, email, salaire);

        tableview.setItems(FXCollections.observableArrayList(data));
    }

    @FXML
    void initialize() {
        // Set the page factory for pagination
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                // Call the display method when changing pages
                display();
                return tableview;
            }
        });
    }


    @FXML
    void aller(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestiondep.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Optionally, you can get the controller of the "gestiondep.fxml" if needed
        Gestiondep controller = loader.getController();

        // Display the new interface
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }






    @FXML
    void delete(ActionEvent event) {
        // Obtenez l'élément sélectionné dans la TableView
        Employee selectedEmployee = tableview.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                // Supprimez l'employé de la base de données en utilisant le service approprié
                emp.delete(selectedEmployee.getId());

                // Supprimez également l'employé de la liste affichée dans la TableView
                tableview.getItems().remove(selectedEmployee);
            } catch (SQLException e) {
                // Gérez les exceptions SQL, par exemple, affichez un message d'erreur
                e.printStackTrace();
                // Affichez un message d'erreur à l'utilisateur
                showAlert("Erreur de suppression", "Une erreur s'est produite lors de la suppression de l'employé.");
            }
        } else {
            // Affichez un message à l'utilisateur s'il n'y a pas d'employé sélectionné
            showAlert("Aucun employé sélectionné", "Veuillez sélectionner un employé à supprimer.");
        }
    }

    // Méthode utilitaire pour afficher une boîte de dialogue d'alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void update(ActionEvent event) {
        // Obtenez l'élément sélectionné dans la TableView
        Employee selectedEmployee = tableview.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            // Affichez une boîte de dialogue ou un formulaire pour mettre à jour les informations de l'employé
            // Vous pouvez implémenter une boîte de dialogue ou un formulaire en fonction de vos besoins

            // Supposons que vous ayez une méthode dans votre service qui met à jour un employé
            try {
                // Mettez à jour l'employé dans la base de données en utilisant le service approprié
                emp.update(selectedEmployee);

                // Mettez également à jour l'employé dans la liste affichée dans la TableView
                // Vous pouvez mettre à jour directement l'objet ou rafraîchir les données depuis la base de données
                tableview.refresh(); // Actualisez la TableView pour refléter les changements
            } catch (SQLException e) {
                // Gérez les exceptions SQL, par exemple, affichez un message d'erreur
                e.printStackTrace();
                // Affichez un message d'erreur à l'utilisateur
                showAlert("Erreur de mise à jour", "Une erreur s'est produite lors de la mise à jour de l'employé.");
            }
        } else {
            // Affichez un message à l'utilisateur s'il n'y a pas d'employé sélectionné
            showAlert("Aucun employé sélectionné", "Veuillez sélectionner un employé à mettre à jour.");
        }
    }




    @FXML
    void openAddEmployeeWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_employee.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AddEmployee controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.setTitle("Add employee");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Call the method to add employee in the AddEmployee controller
                try {
                    controller.ajouteremp(new ActionEvent());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Optionally, you can refresh the display after adding the employee
                // display(new ActionEvent());
            }
        });
    }

    @FXML
    void updateemp(ActionEvent event) {
        Employee selectedEmployee = tableview.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            // Load the Edit_dep.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Edit_employee.fxml"));

            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Get the EditDep controller
            EditEmployee editController = loader.getController();

            // Pass the selected department to the EditDep controller
            editController.initData(selectedEmployee);

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
            noSelectionAlert.setContentText("Please select an employee to update.");
            noSelectionAlert.showAndWait();
        }
    }


    @FXML
    void PDF() {

        List<Employee> employeeList = null;
        try {
            employeeList = emp.getAllEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Populate the TableView with the retrieved data
        tableview.setItems(FXCollections.observableArrayList(employeeList));
         // Get your employee data
        PDF.generateEmployeeListPDF(employeeList);

        }


    }


