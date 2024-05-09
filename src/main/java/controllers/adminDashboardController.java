package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import models.Client;
import utils.MyDatabase;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.util.Callback;

public class adminDashboardController {
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
    private TableColumn<Client, Void> actionColumn;

    private ObservableList<Client> clients = FXCollections.observableArrayList();

    private void addActionButtons() {
        actionColumn.setCellFactory(new Callback<TableColumn<Client, Void>, TableCell<Client, Void>>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                final TableCell<Client, Void> cell = new TableCell<Client, Void>() {
                    private final Button btnEdit = new Button("Modifier");
                    private final Button btnDelete = new Button("Supprimer");

                    {
                        btnEdit.setStyle("-fx-background-color: #002BAB; -fx-text-fill: white;");
                        btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                        btnDelete.setOnAction(event -> {
                            Client client = getTableView().getItems().get(getIndex());
                            if (client != null) {
                                confirmAndDelete(client);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsContainer = new HBox(10, btnEdit, btnDelete);
                            buttonsContainer.setStyle("-fx-alignment: center; -fx-padding: 0 10 0 10; -fx-spacing: 10;");
                            buttonsContainer.setSpacing(10);

                            HBox.setHgrow(btnEdit, Priority.ALWAYS);
                            HBox.setHgrow(btnDelete, Priority.ALWAYS);
                            btnEdit.setMaxWidth(Double.MAX_VALUE);
                            btnDelete.setMaxWidth(Double.MAX_VALUE);

                            setGraphic(new HBox(10, btnEdit, btnDelete));
                        }
                    }
                };
                return cell;
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


        addActionButtons();

        tableView.setItems(getClients());
    }

    private ObservableList<Client> getClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        Connection con = MyDatabase.getInstance().getConnection();
        String query = "SELECT u.id, u.nom, u.prenom, u.email, u.telephone, c.cin " +
                "FROM abstract_utilisateur u JOIN client c ON u.id = c.id " +
                "WHERE u.type = 'client'";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        null,  // mot_de_passe is typically not retrieved for security reasons
                        rs.getString("telephone"),
                        null,  // date_de_naissance is not used in the dashboard
                        null,  // adresseDomicile is not used in the dashboard
                        rs.getString("cin")
                );
                client.setId(rs.getInt("id"));  // Set the ID retrieved from the database
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    private void refreshClientTable() {
        tableView.setItems(getClients());  // Fetch new data and update TableView
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
                if (con != null) con.rollback();  // Roll back transaction on error
            } catch (Exception rollbackEx) {
                e.printStackTrace();
            }
            e.printStackTrace();
            showAlert("Erreur de suppression", "Impossible de supprimer le client : " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstmtClient != null) pstmtClient.close();
                if (pstmtUser != null) pstmtUser.close();
                if (con != null) {
                    con.setAutoCommit(true); // Reset auto-commit mode
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }







}
