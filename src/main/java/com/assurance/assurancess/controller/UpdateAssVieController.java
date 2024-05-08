package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurancevie;
import com.assurance.assurancess.service.AssuranceVieService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class UpdateAssVieController {

    private AssuranceVieService assuranceVieService;

    private Assurancevie selectedAssuranceVie;

    @FXML
    private TextField periodeValidationField;

    @FXML
    private TextField salaireClientField;

    @FXML
    private TextField ficheDePaieField;

    @FXML
    private TextField reponseField;

    public UpdateAssVieController() {
        this.assuranceVieService = new AssuranceVieService();
    }

    public void updateAssuranceVie(ActionEvent actionEvent) {
        if (selectedAssuranceVie != null) {
            // Récupérer les valeurs des champs de texte
            String periodeValidation = periodeValidationField.getText();
            float salaireClient = Float.parseFloat(salaireClientField.getText());
            String ficheDePaie = ficheDePaieField.getText();
            String reponse = reponseField.getText();

            // Mettre à jour les propriétés de l'assurance vie sélectionnée
            selectedAssuranceVie.setPeriodevalidation(periodeValidation);
            selectedAssuranceVie.setSalaireclient(salaireClient);
            selectedAssuranceVie.setFichedepaie(ficheDePaie);
            selectedAssuranceVie.setReponse(reponse);

            // Appeler le service pour mettre à jour l'assurance vie dans la base de données
            assuranceVieService.updateAssuranceVie(selectedAssuranceVie);
            showAlert("Mise à jour de l'assurance vie effectuée avec succès.");
        } else {
            showAlert("Sélectionnez d'abord une assurance vie.");
        }
    }

    public void initData(Assurancevie selectedAssuranceVie) {
        this.selectedAssuranceVie = selectedAssuranceVie;
        // Utilisez les données de l'assurance vie sélectionnée pour initialiser les champs de votre interface utilisateur
        periodeValidationField.setText(selectedAssuranceVie.getPeriodevalidation());
        salaireClientField.setText(String.valueOf(selectedAssuranceVie.getSalaireclient()));
        ficheDePaieField.setText(selectedAssuranceVie.getFichedepaie());
        reponseField.setText(selectedAssuranceVie.getReponse());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
