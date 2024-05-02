package com.riskguard.crudjava;
import com.riskguard.crudjava.EmailSender;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;




public class ReponseController implements Initializable {


    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btn_ajoutrep;

    @FXML
    private Button btn_annrep;

    @FXML
    private Button btn_modrep;

    @FXML
    private Button btn_reclamation;

    @FXML
    private Button btn_reponse;

    @FXML
    private Button btn_statistique;

    @FXML
    private Button btn_supprep;

    @FXML
    private TableColumn<Reponse, Integer> col_idrep;

    @FXML
    private TableColumn<Reponse, String> col_idrec;

    @FXML
    private TableColumn<Reponse, String> col_description;

    @FXML
    private TableColumn<Reponse, String> col_contenu;


    @FXML
    private TableColumn<Reponse, Date> col_date;

    @FXML
    private Pane inner_pane;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private TextField tf_descReponse;

    @FXML
    private TextField tf_idreclamation;

    @FXML
    private TextField txt_serach;
    @FXML
    private Button btn_reclamation1;
    @FXML
    private TableView table_reponse;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        showReponse();
        // Add listener to the search text field to trigger search on typing
        txt_serach.textProperty().addListener((observable, oldValue, newValue) -> {
            searchReponse(newValue.trim().toLowerCase());
        });
    }


    private void searchReponse(String searchQuery) {
        // Create a filtered list to hold the filtered reponse objects
        ObservableList<Reponse> filteredList = FXCollections.observableArrayList();

        // If search query is empty, show all reponses
        if (searchQuery.isEmpty()) {
            table_reponse.setItems(getReponses());
            return;
        }

        // Iterate through the original list of reponses and add matching ones to the filtered list
        for (Reponse reponse : getReponses()) {
            // Convert relevant fields to lowercase for case-insensitive search
            String contenu = reponse.getContenu().toLowerCase();
            String id_reclamation = reponse.getId_reclamation().toLowerCase();

            // Check if any field contains the search query
            if (contenu.contains(searchQuery) || id_reclamation.contains(searchQuery)) {
                filteredList.add(reponse);
            }
        }

        // Update the table view with the filtered list
        table_reponse.setItems(filteredList);
    }

    public ObservableList<Reponse> getReponses() {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();
        String query = "SELECT r.id_reponse, r.id_reclamation, r.date, r.contenu, rc.description " +
                "FROM reponse r " +
                "JOIN reclamation rc ON r.id_reclamation = rc.id_reclamation";
        con = DBConnexion.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Reponse rep = new Reponse();
                rep.setId_reponse(rs.getInt("id_reponse"));
                rep.setId_reclamation(rs.getString("id_reclamation"));
                rep.setDate(rs.getDate("date"));
                rep.setContenu(rs.getString("contenu"));
                rep.setDescription(rs.getString("description")); // Récupérer la description
                reponses.add(rep);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reponses;
    }


    public void initData(Reclamation selectedReclamation) {
        tf_idreclamation.setText(String.valueOf(selectedReclamation.getId_reclamation()));
        // You can initialize other fields if needed
    }


    @FXML
    void ajouterRep(ActionEvent event) {
        // Get the data from the input fields
        String contenu = tf_descReponse.getText();
        String id_reclamation = tf_idreclamation.getText(); // Assuming this is obtained from the selected reclamation

        // Validate the input fields
        if (contenu.isEmpty() || id_reclamation.isEmpty()) {
            // Display an error message if any of the fields are empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Champs manquant");
            alert.setContentText("Veuillez entrer votre réponse.");
            alert.showAndWait();
            return;
        }

        // Check if a response already exists for the specified reclamation
        String countQuery = "SELECT COUNT(*) FROM reponse WHERE id_reclamation = ?";
        con = DBConnexion.getCon();
        try {
            PreparedStatement countStatement = con.prepareStatement(countQuery);
            countStatement.setString(1, id_reclamation);
            ResultSet resultSet = countStatement.executeQuery();
            resultSet.next();
            int responseCount = resultSet.getInt(1);
            if (responseCount > 0) {
                // If a response already exists, show an error message and return
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Réponse dupliquée");
                alert.setContentText("Cette réclamation admet une réponse déja.");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            // Handle the SQL exception
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Database Error");
            errorAlert.setContentText("An error occurred while checking for existing responses. Please try again.");
            errorAlert.showAndWait();
            return;
        }

        // Insert the new Reponse object into the database
        String insertQuery = "INSERT INTO reponse(id_reclamation, date, contenu) VALUES (?, ?, ?)";
        String updateReclamation = "UPDATE reclamation SET etat = 'réponse effectué' WHERE id_reclamation = ?";
        try {
            st = con.prepareStatement(insertQuery);
            st.setString(1, id_reclamation);  // Fix the order of parameters
            st.setDate(2, new java.sql.Date(new Date().getTime())); // Use current date
            st.setString(3, contenu);
            st.executeUpdate();

            // Execute the update statement to change the reclamation status to 'réponse effectué'
            st = con.prepareStatement(updateReclamation);
            st.setString(1, id_reclamation);
            st.executeUpdate();

            showReponse();
            // Send email notification to the client
            sendEmailNotification(id_reclamation);
            // Show a success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Reponse Ajoutée ! ");
            successAlert.setContentText("The reponse has been added successfully.");
            successAlert.showAndWait();

            // Clear the input fields
            tf_descReponse.clear();
            tf_idreclamation.clear();
        } catch (SQLException e) {
            // Display an error message if the insertion fails
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Database Error");
            errorAlert.setContentText("An error occurred while adding the reponse to the database. Please try again.");
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }

    private void sendEmailNotification(String id_reclamation) {
        // Get client email, client name, response, and description from the database using id_reclamation
        String clientEmail = getClientEmail(id_reclamation);
        String clientName = getClientName(id_reclamation);
        String response = getResponseContent(id_reclamation);
        String description = getReclamationDescription(id_reclamation);

        // Prepare email content
        String subject = "Notification de réponse à votre réclamation";
        String content = "Cher " + clientName + ", \n\nUne réponse a été ajoutée à votre réclamation :\n\n" +
                "Description de la réclamation : " + description + "\n\nRéponse : " + response;

        // Send email
        EmailSender.sendEmail(clientEmail, subject, content);
    }



    private String getClientEmail(String id_reclamation) {
        // Query the database to get the client email using the id_reclamation
        String query = "SELECT email_client FROM reclamation WHERE id_reclamation = ?";
        try {
            st = con.prepareStatement(query);
            st.setString(1, id_reclamation);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("email_client");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private String getReclamationDescription(String id_reclamation) {
        // Query the database to get the description of the reclamation using the id_reclamation
        String query = "SELECT description FROM reclamation WHERE id_reclamation = ?";
        try {
            st = con.prepareStatement(query);
            st.setString(1, id_reclamation);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("description");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private String getClientName(String id_reclamation) {
        // Query the database to get the client name using the id_reclamation
        String query = "SELECT nom_client FROM reclamation WHERE id_reclamation = ?";
        try {
            st = con.prepareStatement(query);
            st.setString(1, id_reclamation);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("nom_client");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String getResponseContent(String id_reclamation) {
        // Query the database to get the response content using the id_reclamation
        String query = "SELECT contenu FROM reponse WHERE id_reclamation = ?";
        try {
            st = con.prepareStatement(query);
            st.setString(1, id_reclamation);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("contenu");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }





    @FXML
    void annulerRep(ActionEvent event) {

        // Réinitialiser les champs de saisie
        tf_descReponse.clear();
        tf_idreclamation.clear();
        btn_modrep.setDisable(false); // Activer le bouton modifier
        table_reponse.getSelectionModel().clearSelection(); // Désélectionner toute ligne dans la table

    }

    @FXML
    void getData(MouseEvent event) {

        Reponse reponse = (Reponse) table_reponse.getSelectionModel().getSelectedItem();
        if (reponse != null) {
            tf_descReponse.setText(reponse.getContenu());
            tf_idreclamation.setText(reponse.getId_reclamation());
        }
        btn_modrep.setDisable(false);

    }

    @FXML
    void modifierRep(ActionEvent event) {

        Reponse reponse = (Reponse) table_reponse.getSelectionModel().getSelectedItem();
        if (reponse != null) {
            String updateQuery = "UPDATE reponse SET contenu = ?, id_reclamation = ?, date = ? WHERE id_reponse = ?";
            con = DBConnexion.getCon();
            try {
                st = con.prepareStatement(updateQuery);
                st.setString(1, tf_descReponse.getText());
                st.setString(2, tf_idreclamation.getText());
                st.setDate(3, new java.sql.Date(new Date().getTime())); // Mettre à jour la date actuelle
                st.setInt(4, reponse.getId_reponse()); // Assurez-vous que l'ID de la réponse est correctement placé

                st.executeUpdate();
                showReponse();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    void supprimerRep(ActionEvent event) {

        Reponse reponse = (Reponse) table_reponse.getSelectionModel().getSelectedItem();
        if (reponse != null) {
            String deleteQuery = "DELETE FROM reponse WHERE id_reponse = ?";
            String updateReclamation = "UPDATE reclamation SET etat = 'en attente' WHERE id_reclamation = ?";
            con = DBConnexion.getCon();
            try {
                // Supprimer la réponse de la base de données
                st = con.prepareStatement(deleteQuery);
                st.setInt(1, reponse.getId_reponse());
                st.executeUpdate();

                // Mettre à jour l'état de la réclamation associée à "en attente"
                st = con.prepareStatement(updateReclamation);
                st.setString(1, reponse.getId_reclamation());
                st.executeUpdate();

                // Rafraîchir l'affichage des réponses
                showReponse();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void showReponse() {
        ObservableList<Reponse> list = getReponses();
        table_reponse.setItems(list);
        col_idrep.setCellValueFactory(new PropertyValueFactory<>("id_reponse"));
        col_idrec.setCellValueFactory(new PropertyValueFactory<>("id_reclamation"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
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

    @FXML
    public void sceneSwitch(ActionEvent actionEvent) {
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
    public void gostatistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/statistique.fxml"));
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



    private void generateResponsePDF(String clientName, String description, String responseContent) {
        Document document = new Document();
        try {
            // Spécifier le chemin du fichier PDF de sortie avec le nom du client
            String fileName = clientName + "_response_details.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            // Ouvrir le document
            document.open();

            // Ajouter une zone vide pour déplacer le contenu vers le centre de la page
            document.add(new Paragraph(" "));

            // Ajouter le logo (remplacez l'URL par le chemin de votre propre logo)
            Image logo = Image.getInstance("C:/Users/ghass/IdeaProjects/crudJava/src/main/resources/images/logo.png");
            logo.setAlignment(Image.ALIGN_CENTER); // Aligner le logo au centre
            logo.scaleToFit(200, 100); // Ajuster la taille du logo
            document.add(logo);

            // Ajouter les informations de réponse au document PDF
            Paragraph clientParagraph = new Paragraph("Nom du client: " + clientName);
            clientParagraph.setAlignment(Paragraph.ALIGN_CENTER); // Aligner le texte au centre
            document.add(clientParagraph);

            Paragraph descriptionParagraph = new Paragraph("La réclamation soumise : " + description);
            descriptionParagraph.setAlignment(Paragraph.ALIGN_CENTER); // Aligner le texte au centre
            document.add(descriptionParagraph);

            Paragraph responseParagraph = new Paragraph("Réponse à la réclamation: " + responseContent);
            responseParagraph.setAlignment(Paragraph.ALIGN_CENTER); // Aligner le texte au centre
            document.add(responseParagraph);

            // Fermer le document
            document.close();

            // Afficher un message de réussite
            System.out.println("Le fichier PDF a été généré avec succès.");

            // Ouvrir automatiquement le PDF après la génération
            Desktop.getDesktop().open(new File(fileName));

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void downloadResponsePDF(ActionEvent event) {
        // Vérifier si un élément est sélectionné dans le tableau
        if (table_reponse.getSelectionModel().isEmpty()) {
            // Afficher un message d'erreur si aucun élément n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Aucune réponse sélectionnée. Veuillez sélectionner une réponse à télécharger.");
            alert.showAndWait();
            return; // Sortir de la méthode
        }

        // Récupérer les informations de réponse
        String id_reclamation = tf_idreclamation.getText();
        String clientName = getClientName(id_reclamation);
        String response = getResponseContent(id_reclamation);
        String description = getReclamationDescription(id_reclamation);

        // Générer le PDF avec les informations de réponse
        generateResponsePDF(clientName, description, response);

        // Afficher un message indiquant que le téléchargement a réussi
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Téléchargement réussi");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Le fichier PDF a été téléchargé avec succès.");
        successAlert.showAndWait();
    }




}
