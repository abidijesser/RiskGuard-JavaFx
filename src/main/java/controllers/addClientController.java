package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Client;
import services.clientService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class addClientController {

    private final clientService clientService = new clientService();

    @FXML
    private TextField nameTF;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField motDePasseTF;

    @FXML
    private TextField telephoneTF;

    @FXML
    private TextField dateDeNaissanceTF;

    @FXML
    private TextField cinTF;

    @FXML
    private TextField adresseDomicileTF;

    @FXML
    private Label nomError, prenomError, emailError, passwordError, telephoneError, dateError, cinError, domicileError;

    private void showAlert(String title, String content) {
        // Créez ici une alerte avec JavaFX
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void addClient(ActionEvent event) {
        try {

            if(validateFields()){

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateDeNaissance = LocalDate.of(2000, 1, 1);
                Client newClient = new Client(
                        nameTF.getText(),
                        prenomTF.getText(),
                        emailTF.getText(),
                        motDePasseTF.getText(),
                        telephoneTF.getText(),
                        dateDeNaissance,
                        adresseDomicileTF.getText(),
                        cinTF.getText()
                );

                clientService.add(newClient);
                showSuccessAlert("Succès", "Le client a été ajouté avec succès.");
                clearForm();
            }

        } catch (DateTimeParseException e) {
            System.err.println("The date of birth is in an invalid format.");
        } catch (SQLException e) {
            System.err.println("SQL error occurred while adding the client: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

    }

    private void clearForm() {
        nameTF.clear();
        prenomTF.clear();
        emailTF.clear();
        motDePasseTF.clear();
        telephoneTF.clear();
        dateDeNaissanceTF.clear();
        cinTF.clear();
        adresseDomicileTF.clear();
    }


    private boolean validateFields() {
        clearErrors(); // Clear previous error messages
        boolean valid = true;

        // Validate Name
        if (nameTF.getText().isEmpty()) {
            nomError.setText("Champs requis..");
            valid = false;
        }

        // Validate Prenom
        if (prenomTF.getText().isEmpty()) {
            prenomError.setText("Champs requis..");
            valid = false;
        }

        // Validate Email
        // Validate Email
        if (emailTF.getText().isEmpty()) {
            emailError.setText("Champs requis..");
            valid = false;
        } else if (!emailTF.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            emailError.setText("Format invalide.");
            valid = false;
        }


        // Validate Password
        if (motDePasseTF.getText().isEmpty()) {
            passwordError.setText("Champs requis.");
            valid = false;
        } else if (motDePasseTF.getText().length() < 8) {
            passwordError.setText("8 caractères au minimum.");
            valid = false;
        }

        // Validate Telephone
        if (telephoneTF.getText().isEmpty()) {
            telephoneError.setText("Champs requis.");
            valid = false;
        } else if (!telephoneTF.getText().matches("^[259]\\d{7}$")) {
            telephoneError.setText("8 chiffres et commence par 2,5 ou 9.");
            valid = false;
        }

        // Validate Date de Naissance
        if (dateDeNaissanceTF.getText().isEmpty()) {
            dateError.setText("Champs requis.");
            valid = false;
        }

        // Validate Adresse Domicile
        if (adresseDomicileTF.getText().isEmpty()) {
            domicileError.setText("Champs requis.");
            valid = false;
        }

        // Validate CIN
        if (cinTF.getText().isEmpty()) {
            cinError.setText("Champs requis.s.");
            valid = false;
        } else if (!cinTF.getText().matches("\\d{8}")) {
            cinError.setText("8 chiffres exactement.");
            valid = false;
        }

        return valid; // Return the validation status
    }


    private void clearErrors() {
        nomError.setText("");
        prenomError.setText("");
        emailError.setText("");
        passwordError.setText("");
        telephoneError.setText("");
        dateError.setText("");
        domicileError.setText("");
        cinError.setText("");
    }

    public void navigateToLogin(ActionEvent event) {
        try {

            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
