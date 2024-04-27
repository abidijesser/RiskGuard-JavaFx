package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurance;
import com.assurance.assurancess.service.AssuranceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UpdateAssController {

    @FXML
    private TextField nomPackField;

    @FXML
    private TextField promotionPackField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField typePackField;

    private Assurance selectedAssurance;

    private AssuranceService assuranceService;

    // Référence vers AssuranceController
    private AssuranceController assuranceController;

    public UpdateAssController() {
        this.assuranceService = new AssuranceService();
    }

    public void initData(Assurance assurance) {
        this.selectedAssurance = assurance;
        // Pré-remplissez les champs avec les données de l'assurance sélectionnée
        nomPackField.setText(assurance.getNomdupack());
        promotionPackField.setText(assurance.getPromotiondupack());
        descriptionField.setText(assurance.getDescription());
        typePackField.setText(assurance.getTypedupack());
    }

    @FXML
    private void modifieAssurance(ActionEvent event) {
        if (selectedAssurance != null) {
            String nomPack = nomPackField.getText();
            String promotionPack = promotionPackField.getText();
            String description = descriptionField.getText();
            String typePack = typePackField.getText();

            // Validez les champs et mettez à jour l'assurance sélectionnée
            // (Code similaire à ce que vous avez déjà dans votre code)
            selectedAssurance.setNomdupack(nomPack);
            selectedAssurance.setPromotiondupack(promotionPack);
            selectedAssurance.setDescription(description);
            selectedAssurance.setTypedupack(typePack);

            assuranceService.updateAssurance(selectedAssurance.getId(), selectedAssurance);

            // Appelez loadAssurances() de AssuranceController pour recharger les assurances
            if (assuranceController != null) {
                assuranceController.loadAssurances();
            }
        }
    }

    // Méthode pour définir la référence AssuranceController
    public void setAssuranceController(AssuranceController assuranceController) {
        this.assuranceController = assuranceController;
    }
}
