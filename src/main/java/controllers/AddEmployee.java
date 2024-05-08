package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Employee;
import services.employeeService;

import java.sql.SQLException;
import java.util.List;

public class AddEmployee {


    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private TextField salaire;
    @FXML
    private TextField idep;

    @FXML
    void ajouteremp(ActionEvent event) throws SQLException {
        String nomEmp = nom.getText();
        String emailEmp = email.getText();
        String salaireText = salaire.getText();
        int idepEmp;

        if (isEmptyOrNull(nomEmp) || isEmptyOrNull(emailEmp) || isEmptyOrNull(salaireText)) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (!nomEmp.matches("[a-zA-Z]+")) {
            showAlert("Le nom ne peut contenir que des lettres.");
            return;
        }





// Vérification que le champ de salaire contient uniquement des nombres
        if (!salaireText.matches("\\d+")) {
            showAlert("Le salaire ne peut contenir que des nombres.");
            return;
        }

        int salaireEmp;
        try {
            salaireEmp = Integer.parseInt(salaireText);
        } catch (NumberFormatException e) {
            showAlert("Invalid salary format; please enter a valid number.");
            return;
        }

        try {
            idepEmp = Integer.parseInt(idep.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid department ID format. Please enter a valid integer.");
            return;
        }

        if (!isValidEmail(emailEmp)) {
            showAlert("Invalid email format; please enter a valid email address.");
            return;
        }



        Employee employee = new Employee(0, idepEmp, nomEmp, emailEmp, salaireEmp); // Assuming id is auto-generated

        employeeService employeeService = new employeeService();
        try {
            employeeService.add(employee);
            showAlert("Employee added successfully.");
        } catch (SQLException e) {
            showAlert("Error adding employee: " + e.getMessage());
        }




    }


    private boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email.matches(".+@.+\\..+");
    }

    private boolean isExisting(int idepEmp,String nomEmp, String emailEmp, float salaireEmp) throws SQLException {
        employeeService empService = new employeeService();
        try {
            List<Employee> allEmployees = empService.getAll();
            if (allEmployees != null) {
                for (Employee existing : allEmployees) {
                    if (existing.getNom().equals(nomEmp)
                            && existing.getEmail().equals(emailEmp)
                            && existing.getSalaire() == salaireEmp) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void initialize() {
    }

    public void setIdep(int idep) {
    }


}
