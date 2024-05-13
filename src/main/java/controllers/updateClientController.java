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
import services.RefreshTableCallback;
import utils.MyDatabase;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;

import javafx.util.Callback;



public class updateClientController {
    @FXML
    public Button cancelTF, submitTF;

    @FXML
    private Label nomErrorTF,prenomErrorTF, emailErrorTF, telephoneErrorTF, cinErrorTF ;

    @FXML
    private TextField cinTF, nomTF, emailTF, prenomTF, telephoneTF;


    private RefreshTableCallback refreshCallback;

    @FXML
    void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

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

    private boolean validateInputs() {
        boolean isValid = true;

        // Clear previous error messages
        nomErrorTF.setText("");
        prenomErrorTF.setText("");
        emailErrorTF.setText("");
        telephoneErrorTF.setText("");
        cinErrorTF.setText("");

        if (!telephoneTF.getText().matches("^[259]\\d{7}$")) {
            telephoneErrorTF.setText("Doit commencer par 2, 5, ou 9 et contenir 8 chiffres.");
            isValid = false;
        }

        if (!cinTF.getText().matches("\\d{8}")) {
            cinErrorTF.setText("Doit contenir exactement 8 chiffres.");
            isValid = false;
        }

        return isValid;
    }


    private void updateUserDetails(ActionEvent event) {

        String sqlAbstractUser = "UPDATE abstract_utilisateur SET nom = ?, prenom = ?, email = ?, telephone = ? WHERE id = ?";
        String sqlClient = "UPDATE client SET cin = ? WHERE id = ?";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement pstmtAbstractUser = conn.prepareStatement(sqlAbstractUser);
             PreparedStatement pstmtClient = conn.prepareStatement(sqlClient)) {

            int absIndex=1;
            if (!nomTF.getText().isEmpty()) pstmtAbstractUser.setString(absIndex++, nomTF.getText());
            if (!prenomTF.getText().isEmpty()) pstmtAbstractUser.setString(absIndex++, prenomTF.getText());
            if (!emailTF.getText().isEmpty()) pstmtAbstractUser.setString(absIndex++, emailTF.getText());
            if (!telephoneTF.getText().isEmpty()) pstmtAbstractUser.setString(absIndex++, telephoneTF.getText());
            pstmtAbstractUser.setInt(absIndex, OTPUserId.reserveId);
            int affectedRowsUser = pstmtAbstractUser.executeUpdate();

            int clientIndex=1;
            if (!cinTF.getText().isEmpty()) pstmtClient.setString(clientIndex++, cinTF.getText());
            pstmtClient.setInt(clientIndex, OTPUserId.reserveId);
            int affectedRowsClient = pstmtClient.executeUpdate();

            // Check if updates were successful
            if (affectedRowsUser > 0 && affectedRowsClient > 0) {
                showSuccessAlert("Mise à jour réussie", "Vos informations ont été mises à jour avec succès.");

            } else {
                showAlert("Erreur de mise à jour", "La mise à jour des informations a échoué.");
            }

        } catch (SQLException ex) {
            showAlert("Erreur de Base de Données", "Un problème est survenu lors de la mise à jour des informations: " + ex.getMessage());
        }
    }

    @FXML
    void handleSubmitAction(ActionEvent event) {
        if (validateInputs()) {
            updateUserDetails(event);
            if (refreshCallback != null) {
                refreshCallback.refresh();
            }
            handleCancelAction(event);

        } else {
            showAlert("Erreur de Validation", "Veuillez corriger les erreurs.");
        }
    }


    public void setRefreshCallback(RefreshTableCallback callback) {
        this.refreshCallback = callback;
    }



}
