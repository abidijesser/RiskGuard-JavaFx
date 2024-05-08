package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Department;
import services.departmentService;
import java.sql.SQLException;

public class EditDep {

    @FXML
    private TextField local;

    @FXML
    private TextField chef_dep;

    @FXML
    private TextField code;

    @FXML
    private Button updateButton; // Rename to updateButton to avoid naming conflicts

    private Department depart;
    private final departmentService depservice = new departmentService();

    public void initData(Department depart) {
        this.depart = depart;
        populateFields();
    }

    private void populateFields() {
        if (depart != null) {
            local.setText(depart.getLocal());
            chef_dep.setText(depart.getChef_dep());
            code.setText(String.valueOf(depart.getCode()));
        }
    }

    @FXML
    void updatedep(ActionEvent event) {
        if (depart != null) {
            // Retrieve updated data from text fields
            String updatedLocal = local.getText();
            String updatedChefDep = chef_dep.getText();
            int updatedCode;
            try {
                updatedCode = Integer.parseInt(code.getText());
            } catch (NumberFormatException e) {
                // Handle invalid input for code field
                // You may display an error message or handle it in a way suitable for your application
                return;
            }

            // Update the department object with the new data
            depart.setLocal(updatedLocal);
            depart.setChef_dep(updatedChefDep);
            depart.setCode(updatedCode);

            try {
                // Update department in the database
                depservice.update(depart);

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
}
