package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Department;
import services.departmentService;

import java.io.IOException;
import java.sql.SQLException;

public class AddDep {


    @FXML
    private TextField local;

    @FXML
    private TextField chef_dep;

    @FXML
    private TextField code;



    private int idep;

    @FXML
    void ajouterdep(ActionEvent event) {
        String localdep = local.getText();
        String chef = chef_dep.getText();
        int codedep;
        try {
            codedep = Integer.parseInt(code.getText());
        } catch (NumberFormatException e) {

            showErrorAlert("Invalid code format. Please enter a valid integer.");

            return;
        }

        if (isEmptyOrNull(localdep) || isEmptyOrNull(chef)) {
            showAlert("Please fill in all fields.");
            return;
        }
        if (!localdep.matches("[a-zA-Z]+")) {
            showAlert("Local ne peut contenir que des lettres.");
            return;
        }

        if (!chef.matches("[a-zA-Z]+")) {
            showAlert("Le nom  de chef ne peut contenir que des lettres.");
            return;
        }



        Department dep = new Department(localdep, chef, codedep);

        departmentService depService = new departmentService();
        try {
            depService.add(dep);
            showAlert("Department added successfully.");
        } catch (SQLException e) {
            showErrorAlert("Error adding department: " + e.getMessage());
        }
    }


  /*  @FXML
    void ajouteremp(ActionEvent event) {
        String nomemp = nom.getText();
        String emailemp = email.getText();
        float salaireemp = Float.parseFloat(salaire.getText());

        if (isEmptyOrNull(nomemp) || isEmptyOrNull(emailemp) || isEmptyOrNull(String.valueOf(salaireemp))) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (!isValidEmail(emailemp)) {
            showAlert("Invalid email format; please enter a valid email address.");
            return;
        }

        employee emp = new employee(2, 4, nomemp, emailemp, salaireemp);

        employeeService empService = new employeeService();
        try {
            empService.add(emp);
            showAlert("Employee added successfully.");
        } catch (SQLException e) {
            showErrorAlert("Error adding employee: " + e.getMessage());
        }
    }*/

    private boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email.matches(".+@.+\\..+");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    public void setIdep(int idep) {
        this.idep = idep;
    }
    @FXML
    void update(ActionEvent event) throws IOException {

    }


    public void setLocal(String local) {
    }

    public void setChef_dep(String chefDep) {
    }

    public void setCode(String s) {
    }
}
