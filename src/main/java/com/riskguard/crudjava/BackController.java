package com.riskguard.crudjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class BackController implements Initializable {

    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;


    @FXML
    private TableColumn<Reclamation, String> colnom;

    @FXML
    private TableColumn<Reclamation, Integer> colid;

    @FXML
    private TableColumn<Reclamation, String> coldesc;

    @FXML
    private TableColumn<Reclamation, String> colemail;

    @FXML
    private TableColumn<Reclamation, String> colnum;

    @FXML
    private TableColumn<Reclamation, String> coletat;

    @FXML
    private TableView<Reclamation> table;




    @FXML
    private Pane inner_pane;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_ankerpane;
    @FXML
    private TextField txt_serach;
    @FXML
    private Button btn_reclamation;
    @FXML
    private Button btn_reponse;
    @FXML
    private Button btn_statistique;
    @FXML
    private Button btn_reprec;

    public BackController() {
        // Initialise le client API de vérification des mots inappropriés
        String baseURL = "https://api.badwordservice.com/";
        String endpoint = "check";
        String apiKey = "VOTRE_CLE_API";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showReclamation();
        // Add listener to the search text field to trigger search on typing
        txt_serach.textProperty().addListener((observable, oldValue, newValue) -> {
            searchReclamation(newValue.trim().toLowerCase());
        });
    }

    private void searchReclamation(String searchQuery) {
        // Create a filtered list to hold the filtered reclamation objects
        ObservableList<Reclamation> filteredList = FXCollections.observableArrayList();

        // If search query is empty, show all reclamations
        if (searchQuery.isEmpty()) {
            table.setItems(getReclamations());
            return;
        }

        // Iterate through the original list of reclamations and add matching ones to the filtered list
        for (Reclamation reclamation : getReclamations()) {
            // Convert relevant fields to lowercase for case-insensitive search
            String description = reclamation.getDescription().toLowerCase();
            String nom_client = reclamation.getNom_client().toLowerCase();
            String email_client = reclamation.getEmail_client().toLowerCase();
            String num_tel = reclamation.getNum_tel().toLowerCase();
            String etat = reclamation.getEtat().toLowerCase();


            // Check if any field contains the search query
            if (description.contains(searchQuery) || nom_client.contains(searchQuery) || etat.contains(searchQuery) || email_client.contains(searchQuery) || num_tel.contains(searchQuery)) {
                filteredList.add(reclamation);
            }
        }

        // Update the table view with the filtered list
        table.setItems(filteredList);
    }


    public ObservableList<Reclamation> getReclamations() {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String query = "SELECT id_reclamation, nom_client, email_client, description, num_tel, etat FROM reclamation";
        con = DBConnexion.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setId_reclamation(rs.getInt("id_reclamation"));
                rec.setDescription(rs.getString("description"));
                rec.setNom_client(rs.getString("nom_client"));
                rec.setEmail_client(rs.getString("email_client"));
                rec.setNum_tel(rs.getString("num_tel"));
                rec.setEtat(rs.getString("etat"));
                reclamations.add(rec);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reclamations;
    }

    public void showReclamation() {
        ObservableList<Reclamation> list = getReclamations();
        table.setItems(list);
        colid.setCellValueFactory(new PropertyValueFactory<>("id_reclamation"));
        coldesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colnom.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email_client"));
        colnum.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        coletat.setCellValueFactory(new PropertyValueFactory<>("etat"));
    }

    @Deprecated
    public void sceneSwitch(ActionEvent actionEvent) {
    }

    @FXML
    public void reponseRec(ActionEvent actionEvent) {
        Reclamation selectedReclamation = table.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Reponse.fxml"));
                Parent root = loader.load();

                // Access the controller and set data
                ReponseController reponseController = loader.getController();
                reponseController.initData(selectedReclamation);

                // Access the current stage from any node in the scene
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Display an error message if no reclamation is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Aucune Reclamation n'est séléctionnée");
            alert.setContentText("Please select a reclamation before proceeding.");
            alert.showAndWait();
        }
    }


    @FXML
    public void goreponse(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Reponse.fxml"));
            Parent root = loader.load();

            // Access the current stage from any node in the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goreclamation(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Back.fxml"));
            Parent root = loader.load();

            // Access the current stage from any node in the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



