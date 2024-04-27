package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurance;
import com.assurance.assurancess.entities.Assurancevehicule;
import com.assurance.assurancess.service.AssuranceService;
import com.assurance.assurancess.service.AssuranceVehiculeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AjoutAssuranceVehiculeController {

    @FXML
    private TextField marqueField;

    @FXML
    private TextField modeleField;

    @FXML
    private TextField matriculeField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private TextField periodeValidationField;

    @FXML
    private TextField imageField;

    private AssuranceVehiculeService assuranceVehiculeService;
    private AssuranceService assuranceService;
    private Long selectedAssuranceId;

    public AjoutAssuranceVehiculeController() {
        this.assuranceVehiculeService = new AssuranceVehiculeService();
        this.assuranceService = new AssuranceService();
    }

    @FXML
    private void ajouterAssuranceVehicule() {
        String marque = marqueField.getText();
        String modele = modeleField.getText();
        String matricule = matriculeField.getText();
        LocalDate dateDebutValue = dateDebutPicker.getValue();
        String periodeValidation = periodeValidationField.getText();
        String image = imageField.getText();

        // Vérification de la saisie pour les champs obligatoires
        if (marque.isEmpty() || modele.isEmpty() || matricule.isEmpty() || dateDebutValue == null || periodeValidation.isEmpty() || image.isEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Conversion de LocalDate en Date
        Date dateDebut = Date.from(((LocalDate) dateDebutValue).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Assurance selectedAssurance = assuranceService.getAssuranceById(selectedAssuranceId);

        if (selectedAssurance == null) {
            // Gérer le cas où l'objet Assurance est introuvable
            // Afficher un message d'erreur ou effectuer une action appropriée
            return;
        }

        Assurancevehicule assuranceVehicule = new Assurancevehicule();
        assuranceVehicule.setMarque(marque);
        assuranceVehicule.setModele(modele);
        assuranceVehicule.setMatricule(matricule);
        assuranceVehicule.setDatedebut(dateDebut);
        assuranceVehicule.setPeriodedevalidation(periodeValidation);
        assuranceVehicule.setImage(image);
        assuranceVehicule.setAssurance(selectedAssurance);

        assuranceVehiculeService.addAssuranceVehicule(assuranceVehicule);

        // Vous pouvez ajouter des actions supplémentaires ici, comme fermer la fenêtre, afficher un message, etc.
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void initData(Long id) {
        this.selectedAssuranceId = id;
    }

    public void showDetails(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listassveh.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir de l'actionEvent
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnToPreviousScene(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/listass.fxml"));

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle à partir de l'actionEvent
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Fermer la fenêtre actuelle
            stage.close();

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
