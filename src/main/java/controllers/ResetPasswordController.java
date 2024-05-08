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
import utils.EmailUtil;
import utils.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class ResetPasswordController {
    private int otp;

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
                int otp = new Random().nextInt(9000) + 1000; // Generate the OTP
                String emailBody = String.format(
                        "Hello,\n\n" +
                                "Thank you for using our service. Please use the following One-Time Password (OTP) to proceed with your authentication:\n\n" +
                                "OTP: %d\n\n" +
                                "This OTP is valid for the next 10 minutes. Please do not share this OTP with anyone.\n\n" +
                                "Best regards,\n" +
                                "RiskGuard", otp);

                try {
                    EmailUtil.sendEmail(email, "Demande de réinitialisation de mot de passe", emailBody);
                    showSuccessAlert("Email Sent Successfully", "An OTP has been sent to your email address.");
                    navigateToOTPVerification(); // Navigate only if email is successfully sent
                } catch (Exception e) {
                    showAlert("Email Sending Error", "Failed to send email: " + e.getMessage());
                }
            } else {
                emailVerficationErrorTF.setText("Email n'existe pas.");
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "A problem occurred while verifying the email: " + ex.getMessage());
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

    public void verifyOTP(ActionEvent e) {
        if (OTP1.getText().isBlank() || OTP2.getText().isBlank() || OTP3.getText().isBlank() || OTP4.getText().isBlank()) {
            OTPErrorMessageTF.setText("Tous les champs doivent être remplis.");
        } else if (OTP1.getText().length() > 1 || OTP2.getText().length() > 1 || OTP3.getText().length() > 1 || OTP4.getText().length() > 1) {
            OTPErrorMessageTF.setText("Chaque champ doit contenir un seul digit.");
        } else {
            String enteredOTP = OTP1.getText().trim() + OTP2.getText().trim() + OTP3.getText().trim() + OTP4.getText().trim();

            if (enteredOTP.equals(String.valueOf(otp))) {
                OTPErrorMessageTF.setText("");
                showSuccessAlert("Vérification réussie", "Le code OTP est correct.");
            } else {
                OTPErrorMessageTF.setText("OTP est incorrect, vérifiez à nouveau.");
            }
        }
    }



}

