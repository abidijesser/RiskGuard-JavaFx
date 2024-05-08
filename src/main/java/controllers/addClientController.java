package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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

            if (nameTF.getText().trim().isEmpty() ||
                    prenomTF.getText().trim().isEmpty() ||
                    emailTF.getText().trim().isEmpty() ||
                    motDePasseTF.getText().trim().isEmpty() ||
                    telephoneTF.getText().trim().isEmpty() ||
                    dateDeNaissanceTF.getText().trim().isEmpty() ||
                    cinTF.getText().trim().isEmpty() ||
                    adresseDomicileTF.getText().trim().isEmpty() ) {

                showAlert("Erreur de saisie", "Tous les champs doivent être remplis.");
                return;
            }

            // Assuming the date format in the text field is yyyy-MM-dd
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
}
