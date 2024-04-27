package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurance;
import com.assurance.assurancess.entities.Assurancevie;
import com.assurance.assurancess.service.AssuranceService;
import com.assurance.assurancess.service.AssuranceVieService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddAssuranceVieController {

    @FXML
    private DatePicker datedebutPicker;

    @FXML
    private TextField periodeValidationField;

    @FXML
    private TextField salaireClientField;

    @FXML
    private TextField ficheDePaieField;

    @FXML
    private TextField reponseField;

    private AssuranceVieService assuranceVieService;

    private AssuranceService assuranceService;

    private Long selectedAssuranceId;

    public AddAssuranceVieController() {
        this.assuranceVieService = new AssuranceVieService();
        this.assuranceService = new AssuranceService();
    }

    public void addAssuranceVie(ActionEvent actionEvent) {
        LocalDate datedebutValue = datedebutPicker.getValue();
        if (datedebutValue == null) {
            showAlert("Veuillez sélectionner une date de début.");
            return;
        }

        Date datedebut = Date.from(datedebutValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String periodeValidation = periodeValidationField.getText();
        float salaireClient = Float.parseFloat(salaireClientField.getText());
        String ficheDePaie = ficheDePaieField.getText();
        String reponse = reponseField.getText();

        // Vérification de la saisie pour les champs obligatoires
        if (periodeValidation.isEmpty() || ficheDePaie.isEmpty() || reponse.isEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }
        if (!validateFields()) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Obtention de l'objet Assurance correspondant à l'ID
        Assurance selectedAssurance = assuranceService.getAssuranceById(selectedAssuranceId);

        // Création de l'objet Assurancevie
        Assurancevie assurancevie = new Assurancevie();
        assurancevie.setDatedebut(datedebut);
        assurancevie.setPeriodevalidation(periodeValidation);
        assurancevie.setSalaireclient(salaireClient);
        assurancevie.setFichedepaie(ficheDePaie);
        assurancevie.setReponse(reponse);

        // Liaison de l'objet Assurancevie avec l'objet Assurance
        assurancevie.setAssurance(selectedAssurance);

        // Ajout de l'assurance vie en utilisant le service
        assuranceVieService.addAssuranceVie(assurancevie);


    }
    private boolean validateFields() {
        return datedebutPicker.getValue() != null &&
                !periodeValidationField.getText().isEmpty() &&
                !salaireClientField.getText().isEmpty() &&
                !ficheDePaieField.getText().isEmpty() &&
                !reponseField.getText().isEmpty();
    }
    public void initData(Long id) {
        this.selectedAssuranceId = id;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAssuranceVie(ActionEvent actionEvent) {
        // Charger la vue précédente depuis le fichier FXML
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listassvie.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue précédente
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir de l'actionEvent
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Changer la scène actuelle pour la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
