package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    @FXML
    void addClient(ActionEvent event) {
        try {
            // Assuming the date format in the text field is yyyy-MM-dd
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateDeNaissance = LocalDate.of(2000, 1, 1);
            Client newClient = new Client(
                    nameTF.getText(),
                    prenomTF.getText(),
                    "gamma@gmail.com",
                    "22222222",
                    "25399797",
                    dateDeNaissance,
                    cinTF.getText(),
                    "ariana soghra"
            );
//            newClient.setNom(nameTF.getText());
//            newClient.setPrenom(prenomTF.getText());
//            newClient.setEmail("gamma@gmail.com");
//            newClient.setMotDePasse("22222222"); // Consider hashing the password
//            newClient.setTelephone("25399797");
//            newClient.setDateDeNaissance(dateDeNaissance);
//            newClient.setCin(cinTF.getText());
//            newClient.setAdresseDomicile("ariana soghra");

            // Call the service to add the new client
            clientService.add(newClient);
            System.out.println("Client added successfully!");

            // Optionally clear the text fields after successful addition
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
