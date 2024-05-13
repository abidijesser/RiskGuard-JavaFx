package com.assurance.assurancess.controller;

import com.assurance.assurancess.entities.Assurance;
import com.assurance.assurancess.service.AssuranceService;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import org.controlsfx.control.Notifications;

public class AssuranceController {


    @FXML
    private TextField nomPackField;

    @FXML
    private TextField promotionPackField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField typePackField;

    @FXML
    private ListView<Assurance> assuranceListView;

    @FXML
    private Button modifierButton;
    @FXML
    private TextField rechercheField;

    private AssuranceService assuranceService;

    private ContextMenu contextMenu;

    private Assurance selectedAssurance;

    public AssuranceController() {
        this.assuranceService = new AssuranceService();
    }

    @FXML
    public void initialize() {
        loadAssurances();

        // Créez les éléments du menu
        MenuItem modifierMenuItem = new MenuItem("Modifier");
        modifierMenuItem.setOnAction(this::modifierAssurance);
        MenuItem supprimerMenuItem = new MenuItem("Supprimer");
        supprimerMenuItem.setOnAction(this::supprimerAssurance);

        // Créez le context menu et ajoutez les éléments du menu
        ContextMenu contextMenu = new ContextMenu(modifierMenuItem, supprimerMenuItem);

        // Liez le context menu à la ListView
        assuranceListView.setContextMenu(contextMenu);

        assuranceListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldAssurance, newAssurance) -> selectedAssurance = newAssurance
        );


    }

    void loadAssurances() {
        List<Assurance> assurances = assuranceService.getAllAssurances();
        ObservableList<Assurance> observableAssurances = FXCollections.observableArrayList(assurances);
        assuranceListView.setItems(observableAssurances);
    }

    @FXML
    private void addAssurance() {
        String nomPack = nomPackField.getText();
        String promotionPack = promotionPackField.getText();
        String description = descriptionField.getText();
        String typePack = typePackField.getText();

        if (nomPack.isEmpty() || promotionPack.isEmpty() || description.isEmpty() || typePack.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        // Check if promotionPack contains only numbers
        if (!promotionPack.matches("\\d+")) {
            showAlert("Promotion du pack doit contenir uniquement des chiffres.");
            return;
        }
        if (!nomPack.matches("\\d+")) {
            showAlert("nom pack du pack doit contenir uniquement des chiffres.");
            return;
        }
        if (!description.matches("\\d+")) {
            showAlert("Promotion du pack doit contenir uniquement des chiffres.");
            return;
        }
        if (!typePack.matches("\\d+")) {
            showAlert("type pack du pack doit contenir uniquement des chiffres.");
            return;
        }

        Assurance newAssurance = new Assurance(nomPack, promotionPack, description, typePack);
        assuranceService.addAssurance(newAssurance);

        showAlert("Assurance ajoutée avec succès.");

        clearFields();
        loadAssurances();
    }


    @FXML
    public void modifierAssurance(ActionEvent event) {
        if (selectedAssurance != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UpdateAss.fxml"));
                Parent root = loader.load();

                // Obtenez le contrôleur de la vue de mise à jour
                UpdateAssController updateAssController = loader.getController();

                // Initialisez les données de l'assurance sélectionnée dans le contrôleur de la vue de mise à jour
                updateAssController.initData(selectedAssurance);

                // Créez une nouvelle fenêtre pour la vue de mise à jour
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier une assurance");
                stage.showAndWait(); // Attendre la fermeture de la fenêtre de mise à jour

                // Rafraîchir la liste des assurances après la modification
                loadAssurances();

                // Afficher une alerte pour indiquer que l'assurance a été modifiée avec succès
                showAlert("Assurance modifiée avec succès.");

                // Rétablir la sélection de l'assurance précédemment sélectionnée
                if (selectedAssurance != null) {
                    assuranceListView.getSelectionModel().select(selectedAssurance);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Gérez l'erreur lors du chargement de la vue de mise à jour
            }
        } else {
            // Affichez un message d'erreur si aucune assurance n'est sélectionnée
            showAlert("Veuillez sélectionner une assurance à modifier.");
        }
    }


    @FXML
    private void supprimerAssurance(ActionEvent event) {
        if (selectedAssurance != null) {
            assuranceService.deleteAssurance(selectedAssurance.getId());
            loadAssurances();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomPackField.clear();
        promotionPackField.clear();
        descriptionField.clear();
        typePackField.clear();
    }

    @FXML
    private void ajouterAssuranceVehicule(ActionEvent actionEvent) {
        // Vérifiez d'abord si une assurance est sélectionnée
        if (selectedAssurance != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutAssuranceVehicule.fxml"));
                Parent root = loader.load();

                // Obtenez le contrôleur de la vue d'ajout d'assurance véhicule
                AjoutAssuranceVehiculeController ajoutController = loader.getController();

                // Passez l'ID de l'assurance sélectionnée au contrôleur
                ajoutController.initData(selectedAssurance.getId());

                // Créez une nouvelle fenêtre pour la vue d'ajout d'assurance véhicule
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajouter Assurance Véhicule");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Gérez l'erreur lors du chargement de la vue d'ajout d'assurance véhicule
            }
        } else {
            // Affichez un message d'erreur si aucune assurance n'est sélectionnée
            showAlert("Veuillez sélectionner une assurance avant d'ajouter une assurance véhicule.");
        }
    }

    @FXML
    public void ajouterAssurance(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddAss.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre pour afficher le contenu du fichier FXML "AddAss.fxml"
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une assurance");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement du fichier FXML
        }
    }

    @FXML
    public void returnToPreviousScene(ActionEvent actionEvent) throws IOException {
        // Charger le fichier FXML de la nouvelle scène
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listass.fxml"));
        Parent root = loader.load();

        // Créer une nouvelle scène
        Scene scene = new Scene(root);

        // Obtenir la fenêtre principale
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Fermer la fenêtre actuelle
        stage.close();

        // Afficher la nouvelle scène
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void ajouterAssuranceVie(ActionEvent actionEvent) {
        // Vérifiez d'abord si une assurance est sélectionnée
        if (selectedAssurance != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddAssVie.fxml"));
                Parent root = loader.load();

                // Obtenez le contrôleur de la vue d'ajout d'assurance vie
                AddAssuranceVieController addAssuranceVieController = loader.getController();

                // Passez l'ID de l'assurance sélectionnée au contrôleur
                addAssuranceVieController.initData(selectedAssurance.getId());

                // Créez une nouvelle fenêtre pour la vue d'ajout d'assurance vie
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajouter Assurance Vie");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Gérez l'erreur lors du chargement de la vue d'ajout d'assurance vie
            }
        } else {
            // Affichez un message d'erreur si aucune assurance n'est sélectionnée
            showAlert("Veuillez sélectionner une assurance avant d'ajouter une assurance vie.");
        }
    }


    @FXML
    public void recherche(ActionEvent actionEvent) {
        String input = rechercheField.getText();
        if (!input.isEmpty()) {
            List<Assurance> results = assuranceService.searchAssurances(input);
            if (!results.isEmpty()) {
                assuranceListView.setItems(FXCollections.observableArrayList(results));

                Notifications.create()
                        .title("Success")
                        .text("Assurance(s) found for the search: " + input)
                        .showInformation();
            } else {
                Notifications.create()
                        .title("No Results")
                        .text("Aucune assurance trouvée pour la recherche : " + input)
                        .showWarning();
            }
        } else {
            Notifications.create()
                    .title("Empty Search")
                    .text("Veuillez saisir un critère de recherche.")
                    .showWarning();
        }
    }



    @FXML
   public void reloadList(ActionEvent event) {
        loadAssurances(); // Appel à la méthode pour recharger les assurances dans la ListView
    }

    @FXML
    private void gotoStatPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Stat.fxml"));
        Scene scene = new Scene(root);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
