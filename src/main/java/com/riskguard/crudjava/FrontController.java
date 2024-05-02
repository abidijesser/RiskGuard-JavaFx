package com.riskguard.crudjava;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class FrontController implements Initializable {
    Connection con = null;
    PreparedStatement rc = null;
    ResultSet rs = null;


    @FXML
    private TextField TNumTel;

    @FXML
    private Button btnMod;
    @FXML
    private Button btnTri;
    @FXML
    private Button btnRec;


    @FXML
    private Button btnSup;

    @FXML
    private Button btnVid;

    @FXML
    private TextField tEmail;

    @FXML
    private TextField tNomPrenom;

    @FXML
    private TextArea tRec;

    @FXML
    private TableColumn<Reclamation, String> colNom;

    @FXML
    private TableColumn<Reclamation, String> colRec;

    @FXML
    private TableColumn<Reclamation, String> coletat;

    @FXML
    private TableColumn<Reclamation, String> colemail;

    @FXML
    private TextField txt_serach;

    @FXML
    private TableView<Reclamation> table;
    int id=0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showReclamations();
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

        String query = "select * from reclamation";
        con = DBConnexion.getCon();
        try {
            rc = con.prepareStatement(query);
            rs = rc.executeQuery();
            while (rs.next()){
                Reclamation rec = new Reclamation();
                rec.setId_reclamation(rs.getInt("id_reclamation"));
                rec.setNom_client(rs.getString("nom_client"));
                rec.setEmail_client(rs.getString("email_client"));
                rec.setNum_tel(rs.getString("num_tel"));
                rec.setDescription(rs.getString("description"));
                rec.setEtat(rs.getString("etat"));
                reclamations.add(rec);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reclamations;
    }

    public void showReclamations(){
        ObservableList<Reclamation> list = getReclamations();
        table.setItems(list);
        colNom.setCellValueFactory(new PropertyValueFactory<Reclamation,String>("nom_client"));
        colRec.setCellValueFactory(new PropertyValueFactory<Reclamation,String>("description"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email_client"));
        coletat.setCellValueFactory(new PropertyValueFactory<>("etat"));

    }

    @FXML
    void clearField(ActionEvent event) {
    clear();
    }

    @FXML
    void createReclamation(ActionEvent event) {
        // Get the input values from the text fields
        String nom = tNomPrenom.getText();
        String email = tEmail.getText();
        String num_tel = TNumTel.getText();
        String description = tRec.getText();

        // Perform input validation
        if (nom.isEmpty() || email.isEmpty() || num_tel.isEmpty() || description.isEmpty()) {
            // Show an alert if any of the fields are empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }
        // Vérifier si le champ "nom" contient des chiffres
        if (containsDigits(nom)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format de nom invalide");
            alert.setContentText("Le nom ne doit pas contenir de chiffres.");
            alert.showAndWait();
            return;
        }
        // Vérifier le format de l'email
        if (!isValidEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format d'email invalide");
            alert.setContentText("Veuillez entrer une adresse email valide.");
            alert.showAndWait();
            return;
        }
        // Vérifier si le numéro de téléphone contient uniquement des chiffres
        if (!num_tel.matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format de numéro de téléphone invalide");
            alert.setContentText("Le numéro de téléphone ne doit contenir que des chiffres.");
            alert.showAndWait();
            return;
        }


        // List of forbidden words
        List<String> forbiddenWords = Arrays.asList("sale","merde","negro","shit","raciste", "fuck");

        // Check for bad words in the description
        for (String word : forbiddenWords) {
            if (description.toLowerCase().contains(word.toLowerCase())) {
                // Show an alert if a forbidden word is found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Description non valide");
                alert.setContentText("La description contient des mots inappropriés.");
                alert.showAndWait();
                return;
            }
        }

        // Insert the new record into the database
        String insert = "insert into reclamation(nom_client,email_client,num_tel,description,etat) values(?,?,?,?,?)";
        con = DBConnexion.getCon();
        try {
            rc = con.prepareStatement(insert);
            rc.setString(1,tNomPrenom.getText());
            rc.setString(2,tEmail.getText());
            rc.setString(3,TNumTel.getText());
            rc.setString(4,tRec.getText());
            rc.setString(5, "en attente");
            rc.executeUpdate();
            showReclamations();
            // Show a success alert if the insertion is successful
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText("Réclamation ajoutée avec succès");
            successAlert.setContentText("La réclamation a été ajoutée avec succès à la base de données.");
            successAlert.showAndWait();
            // Insertion réussie, affichage de la notification
            Notifications notifications = Notifications.create();
            notifications.text("Réclamation ajoutée avec succès");
            notifications.title("Succès");
            notifications.hideAfter(Duration.seconds(4));
            notifications.show();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean isValidEmail(String email) {
        // Expression régulière pour valider l'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private boolean containsDigits(String str) {
        // Vérifier si la chaîne contient des chiffres
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    void getData(MouseEvent event) {
        Reclamation reclamation = table.getSelectionModel().getSelectedItem();
        id=reclamation.getId_reclamation();
        tNomPrenom.setText(reclamation.getNom_client());
        tEmail.setText(reclamation.getEmail_client());
        TNumTel.setText(reclamation.getNum_tel());
        tRec.setText(reclamation.getDescription());
        btnRec.setDisable(false);

    }

    void clear(){
        tNomPrenom.setText(null);
        tEmail.setText(null);
        TNumTel.setText(null);
        tRec.setText(null);
        btnVid.setDisable(false);
    }

    @FXML
    void deleteReclamation(ActionEvent event) {
        String deleteReclamation = "DELETE FROM reclamation WHERE id_reclamation=?";
        String deleteReponses = "DELETE FROM reponse WHERE id_reclamation=?";
        con = DBConnexion.getCon();
        try {
            // Supprimer la réclamation
            rc = con.prepareStatement(deleteReclamation);
            rc.setInt(1, id);
            rc.executeUpdate();

            // Supprimer les réponses associées
            rc = con.prepareStatement(deleteReponses);
            rc.setInt(1, id);
            rc.executeUpdate();

            showReclamations();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateReclamation(ActionEvent event) {
        String nom = tNomPrenom.getText();
        String email = tEmail.getText();
        String num_tel = TNumTel.getText();
        String description = tRec.getText();
        // Vérifier si le champ "nom" contient des chiffres
        if (containsDigits(nom)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format de nom invalide");
            alert.setContentText("Le nom ne doit pas contenir de chiffres.");
            alert.showAndWait();
            return;
        }
        // Vérifier le format de l'email
        if (!isValidEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format d'email invalide");
            alert.setContentText("Veuillez entrer une adresse email valide.");
            alert.showAndWait();
            return;
        }
        // Vérifier si le numéro de téléphone contient uniquement des chiffres
        if (!num_tel.matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format de numéro de téléphone invalide");
            alert.setContentText("Le numéro de téléphone ne doit contenir que des chiffres.");
            alert.showAndWait();
            return;
        }


        // List of forbidden words
        List<String> forbiddenWords = Arrays.asList("sale","merde","negro","shit","raciste", "fuck");

        // Check for bad words in the description
        for (String word : forbiddenWords) {
            if (description.toLowerCase().contains(word.toLowerCase())) {
                // Show an alert if a forbidden word is found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Description non valide");
                alert.setContentText("La description contient des mots inappropriés.");
                alert.showAndWait();
                return;
            }
        }
        String update = "update reclamation set nom_client = ?,email_client = ?,description = ?,num_tel = ?,etat = ? where id_reclamation = ?";
        con = DBConnexion.getCon();
        try {
            rc = con.prepareStatement(update);
            rc.setString(1,tNomPrenom.getText());
            rc.setString(2,tEmail.getText());
            rc.setString(3,tRec.getText());
            rc.setString(4,TNumTel.getText());
            rc.setString(5, "en attente");
            rc.setInt(6,id);
            rc.executeUpdate();
            showReclamations();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void showReponses(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/Fxml/Back.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Reponses");
        stage.setScene(scene);
        stage.show();
    }

    // Déclarations des éléments de l'interface utilisateur
    @FXML
    private ComboBox<String> cmbCritereTri;

    @FXML
    private RadioButton radioCroissant;

    @FXML
    private RadioButton radioDecroissant;

    // Groupe pour les boutons radio
    @FXML
    private ToggleGroup toggleGroup;

    // Méthode appelée lors du clic sur le bouton de tri
    @FXML
    void trierReclamations(ActionEvent event) {
        // Récupérer le critère de tri sélectionné
        String critere = cmbCritereTri.getValue();

        // Récupérer l'ordre de tri sélectionné
        boolean croissant = radioCroissant.isSelected();

        // Trier les réclamations en fonction du critère et de l'ordre de tri sélectionnés
        trierReclamationsAvancee(critere, croissant);
    }

    // Méthode pour trier les réclamations en fonction du critère et de l'ordre de tri sélectionnés
    private void trierReclamationsAvancee(String critere, boolean croissant) {
        // Récupérer la liste des réclamations depuis la table
        ObservableList<Reclamation> reclamations = table.getItems();

        // Utiliser un comparateur personnalisé en fonction du critère sélectionné
        Comparator<Reclamation> comparator = null;
        switch (critere) {
            case "Nom du client":
                comparator = Comparator.comparing(Reclamation::getNom_client);
                break;
            case "Email":
                comparator = Comparator.comparing(Reclamation::getEmail_client);
                break;
            // Ajoutez d'autres cas selon vos critères de tri
        }

        // Inverser l'ordre du comparateur si l'ordre de tri est décroissant
        if (!croissant) {
            comparator = comparator.reversed();
        }

        // Trier les réclamations en utilisant le comparateur
        reclamations.sort(comparator);

        // Mettre à jour la table avec les réclamations triées
        table.setItems(reclamations);
    }




}