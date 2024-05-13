package controllers;

import DTO.OTPUserId;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import models.Client;
import utils.MyDatabase;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.util.Callback;

public class adminDashboardController {
    OTPUserId otpUserId= new OTPUserId();
    @FXML
    private TableView<Client> tableView;
    @FXML
    private TableColumn<Client, String> nomColumn;
    @FXML
    private TableColumn<Client, String> prenomColumn;
    @FXML
    private TableColumn<Client, String> emailColumn;
    @FXML
    private TableColumn<Client, String> telephoneColumn;
    @FXML
    private TableColumn<Client, String> cinColumn;
    @FXML
    private TableColumn<Client, String> adresseColumn;

    @FXML
    private TableColumn<Client, Void> actionColumn;

    @FXML
    private TextField searchBarTF;

    private ObservableList<Client> clients = FXCollections.observableArrayList();

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<Client, Void>() {
            private final Button btnEdit = new Button("Modifier");
            private final Button btnDelete = new Button("Supprimer");
            private final HBox buttonsContainer = new HBox(10, btnEdit, btnDelete);

            {
                btnEdit.setStyle("-fx-background-color:  #5946FA; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: #E80000; -fx-text-fill: white;");

                btnDelete.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    if (client != null) {
                        confirmAndDelete(client);
                    }
                });

                btnEdit.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    if (client != null) {
                        handleUserId(client);
                        openModelToModify(event);
                    }
                });

                buttonsContainer.setStyle("-fx-alignment: center; -fx-padding: 0 10 0 10;");
                HBox.setHgrow(btnEdit, Priority.ALWAYS);
                HBox.setHgrow(btnDelete, Priority.ALWAYS);
                btnEdit.setMaxWidth(Double.MAX_VALUE);
                btnDelete.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null); // No action buttons on empty rows
                } else {
                    setGraphic(buttonsContainer); // Add action buttons only on non-empty rows
                }
            }
        });
    }


    public void showAlert(String title, String content) {
        // Créez ici une alerte avec JavaFX
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseDomicile"));

        addActionButtons();
        tableView.setItems(clients);
        loadClients();

        searchBarTF.textProperty().addListener((observable, oldValue, newValue) -> filterClients(newValue));
    }


    private ObservableList<Client> getClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        Connection con = MyDatabase.getInstance().getConnection();
        String query = "SELECT u.id, u.nom, u.prenom, u.email, u.telephone, c.cin, c.adresse_domicile " +
                "FROM abstract_utilisateur u JOIN client c ON u.id = c.id " +
                "WHERE u.type = 'client'";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        null,
                        rs.getString("telephone"),
                        null,
                        rs.getString("adresse_domicile"),
                        rs.getString("cin")
                );
                client.setId(rs.getInt("id"));
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    public void refreshClientTable() {
        tableView.setItems(getClients());
    }

    private void confirmAndDelete(Client client) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression du client");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce client : " + client.getNom() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteClient(client);
                clients.remove(client);
                refreshClientTable();
            }
        });
    }

    private void deleteClient(Client client) {
        Connection con = null;
        PreparedStatement pstmtClient = null;
        PreparedStatement pstmtUser = null;

        try {
            con = MyDatabase.getInstance().getConnection();
            con.setAutoCommit(false);

            String queryClient = "DELETE FROM client WHERE id = ?";
            pstmtClient = con.prepareStatement(queryClient);
            pstmtClient.setInt(1, client.getId());
            pstmtClient.executeUpdate();

            String queryUser = "DELETE FROM abstract_utilisateur WHERE id = ?";
            pstmtUser = con.prepareStatement(queryUser);
            pstmtUser.setInt(1, client.getId());
            pstmtUser.executeUpdate();

            con.commit();
            showSuccessAlert("Succes ", "Suppression faite avec succes.");

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception rollbackEx) {
                e.printStackTrace();
            }
            e.printStackTrace();
            showAlert("Erreur de suppression", "Impossible de supprimer le client : " + e.getMessage());
        } finally {

            try {
                if (pstmtClient != null) pstmtClient.close();
                if (pstmtUser != null) pstmtUser.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    public void openModelToModify(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientUpdate.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("The specified FXML file was not found.");
            }
            Parent root = loader.load();
            updateClientController controller = loader.getController();
            controller.setRefreshCallback(this::refreshClientTable);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Load Error", "Failed to load the update form: " + e.getMessage());
        }
    }

    public void handleUserId(Client client){
        otpUserId.setUserId(client.getId());
        System.out.println(otpUserId.getReserveId());
    }

    private void loadClients() {
        clients.clear();
        clients.addAll(getClients());
    }

    private void filterClients(String searchText) {
        if (searchText.isEmpty()) {
            tableView.setItems(clients);
        } else {
            ObservableList<Client> filteredList = FXCollections.observableArrayList();
            for (Client client : clients) {
                if (client.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        client.getPrenom().toLowerCase().contains(searchText.toLowerCase()) ||
                        client.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        client.getTelephone().toLowerCase().contains(searchText.toLowerCase()) ||
                        client.getCin().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(client);
                }
            }
            tableView.setItems(filteredList);
        }
    }

    public void navigateToStatistiques(ActionEvent event) {
        try {

            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistiques.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
