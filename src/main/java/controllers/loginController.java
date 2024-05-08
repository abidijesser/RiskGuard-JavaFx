package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import utils.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Client;

import javafx.scene.control.Alert;

import javafx.event.ActionEvent;

public class loginController {

    @FXML
    private TextField userEmailTF;

    @FXML
    private TextField userPasswordTF;

    @FXML
    private Label emailErrorMessageTF;

    @FXML
    private Label passwordErrorMessageTF;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);

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

    public void action(ActionEvent e){

        if(userEmailTF.getText().isBlank() || userPasswordTF.getText().isBlank() || !pattern.matcher(userEmailTF.getText()).matches()){

            if(userEmailTF.getText().isBlank()) {
                emailErrorMessageTF.setText("Le champs Email est requis.");
            } else if (!pattern.matcher(userEmailTF.getText()).matches()) {
                emailErrorMessageTF.setText("Format d'email invalide.");
            } else {
                emailErrorMessageTF.setText("");
            }

            if(userPasswordTF.getText().isBlank()) {
                passwordErrorMessageTF.setText("Le champs Password est requis.");
            } else if (userPasswordTF.getText().length() < 8){
                passwordErrorMessageTF.setText("Le mot de passe doit contenir au moins 8 caractères");
            }
            else {
                passwordErrorMessageTF.setText("");
            }
        }
        else{
            int loginResult = isValidLogin(userEmailTF.getText(), userPasswordTF.getText());
            switch (loginResult) {
                case 0:
                    emailErrorMessageTF.setText("Email n'existe pas.");
                    passwordErrorMessageTF.setText("");
                    break;
                case 1:
                    passwordErrorMessageTF.setText("Mot de passe incorrect.");
                    emailErrorMessageTF.setText("");
                    break;
                case 2:
                    showSuccessAlert("Succès", "Connexion réussie !");
                    break;
                default:
                    showAlert("Échec de la connexion", "Une erreur inconnue est survenue.");
                    break;
            }
        }
    }

//    public void connection(){
//        MyDatabase connection= new MyDatabase();
//        Connection login = connection.getConnection();
//
//        String loginQuery = "SELECT count(1) FROM abstract_utilisateur WHERE email=? AND password=?";
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(loginQuery)) {
//            pstmt.setString(1, userEmailTF.getText());
//            pstmt.setString(2, userPasswordTF.getText());
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next() && rs.getInt(1) > 0) {
//                    // Valid login
//                } else {
//                    // Invalid login
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    private int isValidLogin(String email, String mot_de_passe) {
        String checkUser = "SELECT mot_de_passe FROM abstract_utilisateur WHERE email = ?";
        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkUser)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Email exists, now check password
                    String storedPassword = rs.getString("mot_de_passe");
                    if (storedPassword.equals(mot_de_passe)) {
                        return 2; // Login successful
                    } else {
                        return 1; // Password incorrect
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error accessing database: " + e.getMessage());
        }
        return 0; // User not found
    }



}
