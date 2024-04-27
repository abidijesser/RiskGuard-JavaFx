package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurancevehicule;
import com.assurance.assurancess.service.AssuranceVehiculeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateAssVehiculeController {

    @FXML
    private TextField marqueField;

    @FXML
    private TextField modeleField;

    @FXML
    private TextField matriculeField;

    @FXML
    private TextField datedebutField;

    @FXML
    private TextField periodeValidationField;

    @FXML
    private TextField imageField;

    private AssuranceVehiculeService assuranceVehiculeService;

    private Assurancevehicule selectedAssuranceVehicule;

    public UpdateAssVehiculeController() {
        this.assuranceVehiculeService = new AssuranceVehiculeService();
    }

    @FXML
    public void updateAssuranceVehicule(ActionEvent actionEvent) {
        if (selectedAssuranceVehicule != null) {
            String marque = marqueField.getText();
            String modele = modeleField.getText();
            String matricule = matriculeField.getText();
            String datedebutString = datedebutField.getText();
            String periodeValidation = periodeValidationField.getText();
            String image = imageField.getText();

            Long id = selectedAssuranceVehicule.getId() != null ? selectedAssuranceVehicule.getId().longValue() : null;

            Date datedebut = null;
            try {
                datedebut = new SimpleDateFormat("dd/MM/yyyy").parse(datedebutString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Assurancevehicule updatedAssuranceVehicule = new Assurancevehicule();
            updatedAssuranceVehicule.setId(id);
            updatedAssuranceVehicule.setMarque(marque);
            updatedAssuranceVehicule.setModele(modele);
            updatedAssuranceVehicule.setMatricule(matricule);
            updatedAssuranceVehicule.setDatedebut(datedebut);
            updatedAssuranceVehicule.setPeriodedevalidation(periodeValidation);
            updatedAssuranceVehicule.setImage(image);

            assuranceVehiculeService.updateAssuranceVehicule(updatedAssuranceVehicule);
        }
    }

    public void initData(Assurancevehicule selectedAssuranceVehicule) {
        this.selectedAssuranceVehicule = selectedAssuranceVehicule;
        if (selectedAssuranceVehicule != null) {
            marqueField.setText(selectedAssuranceVehicule.getMarque());
            modeleField.setText(selectedAssuranceVehicule.getModele());
            matriculeField.setText(selectedAssuranceVehicule.getMatricule());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String datedebutString = dateFormat.format(selectedAssuranceVehicule.getDatedebut());
            datedebutField.setText(datedebutString);
            periodeValidationField.setText(selectedAssuranceVehicule.getPeriodedevalidation());
            imageField.setText(selectedAssuranceVehicule.getImage());
        }
    }

    public void returnToPreviousScene(ActionEvent actionEvent) {
        try {
            // Charger la vue précédente depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listassveh.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue précédente
            Scene scene = new Scene(root);

            // Fermer la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();

            // Afficher la nouvelle scène
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
