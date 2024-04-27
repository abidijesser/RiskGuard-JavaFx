package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurancevehicule;
import com.assurance.assurancess.service.AssuranceVehiculeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListAssVehiculeController {

    @FXML
    private ListView<Assurancevehicule> assuranceListView;
    @FXML
    private TextField rechercheField;
    private AssuranceVehiculeService assuranceVehiculeService;

    public ListAssVehiculeController() {
        this.assuranceVehiculeService = new AssuranceVehiculeService();
    }

    @FXML
    public void initialize() {
        loadAssuranceVehicules();
    }

    private void loadAssuranceVehicules() {
        List<Assurancevehicule> assuranceVehicules = assuranceVehiculeService.getAllAssuranceVehicules();
        ObservableList<Assurancevehicule> observableList = FXCollections.observableArrayList(assuranceVehicules);
        assuranceListView.setItems(observableList);
    }

    @FXML
    public void supprimerAssuranceVehicule(ActionEvent actionEvent) {
        // Vérifiez d'abord si un élément est sélectionné dans la ListView
        Assurancevehicule selectedAssuranceVehicule = assuranceListView.getSelectionModel().getSelectedItem();
        if (selectedAssuranceVehicule != null) {
            // Appelez votre service pour supprimer l'assurance véhicule
            assuranceVehiculeService.deleteAssuranceVehicule(selectedAssuranceVehicule.getId());

            // Rafraîchissez la liste après la suppression
            loadAssuranceVehicules();
        }
    }

    public void returnToPreviousScene(ActionEvent actionEvent) {
        try {
            // Charger la vue précédente depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutAssuranceVehicule.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre actuelle à partir de l'événement d'action
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Fermer la fenêtre actuelle
            stage.close();

            // Afficher la nouvelle vue
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateAssuranceVehicule(ActionEvent actionEvent) {
        // Vérifiez d'abord si une assurance véhicule est sélectionnée
        Assurancevehicule selectedAssuranceVehicule = assuranceListView.getSelectionModel().getSelectedItem();
        if (selectedAssuranceVehicule != null) {
            try {
                // Chargez la vue de mise à jour des détails de l'assurance véhicule depuis le fichier FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updassveh.fxml"));
                Parent root = loader.load();

                // Obtenez le contrôleur de la vue de mise à jour
                UpdateAssVehiculeController updateController = loader.getController();

                // Initialisez les données de l'assurance véhicule sélectionnée dans le contrôleur de mise à jour
                updateController.initData(selectedAssuranceVehicule);

                // Obtenez la scène actuelle à partir de l'événement d'action
                Scene currentScene = ((Node) actionEvent.getSource()).getScene();

                // Créez une nouvelle scène pour afficher la vue de mise à jour
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Mettre à jour Assurance Véhicule");
                stage.initOwner(currentScene.getWindow()); // Définissez la fenêtre parente
                stage.initModality(Modality.WINDOW_MODAL); // Définissez la modalité pour bloquer les autres fenêtres
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Gérez les erreurs de chargement de la vue de mise à jour
            }
        }
    }

    @FXML
    public void reloadAssuranceVehicules(ActionEvent actionEvent) {
        loadAssuranceVehicules();
    }

    @FXML
    public void rechercherAssuranceVehicule(ActionEvent actionEvent) {
        String rechercheText = rechercheField.getText();
        if (!rechercheText.isEmpty()) {
            long id = Long.parseLong(rechercheText);
            Assurancevehicule assuranceVehicule = assuranceVehiculeService.getAssuranceVehiculeById(id);
            if (assuranceVehicule != null) {
                assuranceListView.getItems().clear();
                assuranceListView.getItems().add(assuranceVehicule);
            } else {
                // Afficher un message d'erreur si l'assurance véhicule n'est pas trouvée
                // Vous pouvez utiliser une alerte ou un autre moyen pour afficher le message
                System.out.println("Assurance Véhicule non trouvée pour l'ID : " + id);
            }
        } else {
            // Afficher un message d'erreur si le champ de recherche est vide
            // Vous pouvez utiliser une alerte ou un autre moyen pour afficher le message
            System.out.println("Veuillez entrer un ID de recherche.");
        }
    }
}
