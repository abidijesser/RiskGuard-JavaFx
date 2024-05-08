package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurancevie;
import com.assurance.assurancess.service.AssuranceVieService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListAssVieController {

    @FXML
    private ListView<Assurancevie> assuranceListView;
    @FXML
    private TextField rechercheField;
    private AssuranceVieService assuranceVieService;

    public ListAssVieController() {
        this.assuranceVieService = new AssuranceVieService();
    }

    @FXML
    public void initialize() {
        loadAssuranceVies();
    }

    private void loadAssuranceVies() {
        List<Assurancevie> assuranceVies = assuranceVieService.getAllAssuranceVies();
        ObservableList<Assurancevie> observableList = FXCollections.observableArrayList(assuranceVies);
        assuranceListView.setItems(observableList);
    }

    public void deleteAssuranceVie(ActionEvent actionEvent) {
        // Récupérer l'élément sélectionné dans la ListView
        Assurancevie selectedAssuranceVie = assuranceListView.getSelectionModel().getSelectedItem();
        if (selectedAssuranceVie != null) {
            // Supprimer l'élément sélectionné de la liste
            assuranceVieService.deleteAssuranceVie(selectedAssuranceVie.getId());
            // Recharger les assurances vie dans la ListView
            loadAssuranceVies();
        }
    }

    public void updateAssuranceVie(ActionEvent actionEvent) {
        Assurancevie selectedAssuranceVie = assuranceListView.getSelectionModel().getSelectedItem();
        if (selectedAssuranceVie != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updassvie.fxml"));
                Parent root = loader.load();
                UpdateAssVieController controller = loader.getController();
                // Vous pouvez passer des données au contrôleur de la fenêtre de mise à jour si nécessaire
                controller.initData(selectedAssuranceVie);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Veuillez sélectionner une assurance vie à mettre à jour.");
        alert.showAndWait();
    }

    public void reloadAssurances(ActionEvent actionEvent) {
        loadAssuranceVies();
    }

    @FXML
    public void rechercherAssuranceVie(ActionEvent actionEvent) {
        String rechercheText = rechercheField.getText();
        if (!rechercheText.isEmpty()) {
            long id = Long.parseLong(rechercheText);
            Assurancevie searchedAssuranceVie = assuranceVieService.getAssuranceVieById(id);
            if (searchedAssuranceVie != null) {
                ObservableList<Assurancevie> observableList = FXCollections.observableArrayList(searchedAssuranceVie);
                assuranceListView.setItems(observableList);
            } else {
                showAlert("Aucune assurance vie trouvée avec cet ID.");
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void sortAssuranceVieBySalaire() {
        List<Assurancevie> assuranceVies = assuranceVieService.getAllAssuranceVies("salaire");
        assuranceListView.getItems().clear();
        assuranceListView.getItems().addAll(assuranceVies);
    }

    @FXML
    private void sortAssuranceVieByDateDebut() {
        List<Assurancevie> assuranceVies = assuranceVieService.getAllAssuranceVies("datedebut");
        assuranceListView.getItems().clear();
        assuranceListView.getItems().addAll(assuranceVies);
    }
}
