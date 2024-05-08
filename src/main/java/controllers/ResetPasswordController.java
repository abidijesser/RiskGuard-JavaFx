package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;
import java.util.Random;
import utils.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ResetPasswordController {

//    Random random = new Random();
//    int OTP=1000 + random.nextInt(9000);
    int OTP=1234;

    @FXML
    private TextField emailVerificationTF;

    @FXML
    private Label emailVerficationErrorTF;

    @FXML
    private Label verifiedEmailTF;

    @FXML
    private TextField OTP1, OTP2, OTP3, OTP4;


    @FXML
    private Label OTPErrorMessageTF;

    @FXML
    private Label renvoyerOTPCodeTF;

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

    public void verifyEmail(ActionEvent e){

        if(emailVerificationTF.getText().isBlank() ||  !pattern.matcher(emailVerificationTF.getText()).matches() ){

            if(emailVerificationTF.getText().isBlank()) {
                emailVerficationErrorTF.setText("Le champs Email est requis.");
            } else if (!pattern.matcher(emailVerificationTF.getText()).matches()) {
                emailVerficationErrorTF.setText("Format d'email invalide.");
            } else {
                emailVerficationErrorTF.setText("");
            }
        } else {

            checkEmailExistence();
        }

    }


    private void checkEmailExistence() {
        String email = emailVerificationTF.getText();
        String query = "SELECT count(*) FROM abstract_utilisateur WHERE email = ?";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                emailVerficationErrorTF.setText("");
                showSuccessAlert("Vérification Réussie", "L'email existe dans notre base de données.");
                navigateToOTPVerification();
            } else {
                emailVerficationErrorTF.setText("Email n'existe pas.");
            }
        } catch (SQLException ex) {
            showAlert("Erreur de Base de Données", "Un problème est survenu lors de la vérification de l'email: " + ex.getMessage());
        }
    }

    private void navigateToOTPVerification() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OTPVerification.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifyOTP(ActionEvent e){

        if(OTP1.getText().isBlank() || OTP2.getText().isBlank() || OTP3.getText().isBlank() || OTP4.getText().isBlank() ){
            OTPErrorMessageTF.setText("Tous le champs doivent etre remplis.");

        } else if (OTP1.getText().length() > 1 || OTP2.getText().length() > 1 || OTP3.getText().length() > 1 || OTP4.getText().length() > 1){
            OTPErrorMessageTF.setText("Chaque champs doit contient un seul digit.");

        }else {
            String enteredOTP = OTP1.getText() + OTP2.getText() + OTP3.getText() + OTP4.getText();

            if (enteredOTP.equals(String.valueOf(OTP))) {
                OTPErrorMessageTF.setText("");
                showSuccessAlert("Vérification réussie", "Le code OTP est correct.");

            } else {
                OTPErrorMessageTF.setText("OTP est incorrect, vérifiez à nouveau.");
            }
        }

    }



}

