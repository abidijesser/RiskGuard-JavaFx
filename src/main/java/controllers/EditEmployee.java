package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Employee;
import services.employeeService;

import java.sql.SQLException;

public class EditEmployee {

    @FXML
    private TextField nom;

    @FXML
    private TextField email;

    @FXML
    private TextField salaire;

    @FXML
    private TextField idep;

    @FXML
    private Button updateButton; // Rename to updateButton to avoid naming conflicts

    private Employee employ;
    private final employeeService empservice = new employeeService();

    public void initData(Employee employ) {
        this.employ = employ;
        populateFields();
    }

    private void populateFields() {
        if (employ != null) {
            nom.setText(employ.getNom());
            email.setText(employ.getEmail());
            salaire.setText(String.valueOf(employ.getSalaire()));
            idep.setText(String.valueOf(employ.getIdep()));

        }
    }

    @FXML
    void updateemp(ActionEvent event) {
        if (employ != null) {
            // Retrieve updated data from text fields
            String updatedNom = nom.getText();
            String updatedEmail = email.getText();
            int updatedSalaire;





            try {
                updatedSalaire = Integer.parseInt(salaire.getText());


            } catch (NumberFormatException e) {
                // Handle invalid input for code field
                // You may display an error message or handle it in a way suitable for your application
                return;
            }

            // Update the department object with the new data
            employ.setNom(updatedNom);
            employ.setEmail(updatedEmail);
            employ.setSalaire(updatedSalaire);


            try {
                // Update department in the database
                empservice.update(employ);

                // Close the dialog or navigate back to the previous screen
                Stage stage = (Stage) updateButton.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Handle the case where no department is selected
            // You may display an error message or handle it in a way suitable for your application
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
