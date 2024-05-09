package controllers;

import DTO.OTPUserId;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.*;
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
    private static int otp;
    private int userId ;
    OTPUserId otpUserId= new OTPUserId();



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

    @FXML
    private PasswordField passwordFieldTF;

    @FXML
    private PasswordField confirmPasswordFieldTF;

    @FXML
    private Label passwordErrorMessageTF;

    @FXML
    private Label confPasswordErrorMessageTF;

    @FXML
    private CheckBox toggleVisibility;

    @FXML
    private PasswordField displayPasswordTF;

    @FXML
    private PasswordField displayConfPasswordField;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

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

    public void verifyEmail(ActionEvent event){

        if(emailVerificationTF.getText().isBlank() ||  !pattern.matcher(emailVerificationTF.getText()).matches() ){

            if(emailVerificationTF.getText().isBlank()) {
                emailVerficationErrorTF.setText("Le champs Email est requis.");
            } else if (!pattern.matcher(emailVerificationTF.getText()).matches()) {
                emailVerficationErrorTF.setText("Format d'email invalide.");
            } else {
                emailVerficationErrorTF.setText("");
            }
        } else {
            checkEmailExistence(event);
        }

    }


    private void checkEmailExistence(ActionEvent event) {
        String email = emailVerificationTF.getText();
        String query = "SELECT id FROM abstract_utilisateur WHERE email = ?";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();


            if (rs.next()) {
                this.userId = rs.getInt("id");
                otpUserId.setUserId(userId);
                setUserId(userId);
                System.out.println(this.userId);
                this.otp = new Random().nextInt(9000) + 1000;  // Generate the OTP
                String emailBody = String.format(
                        "Hello,\n\n" +
                                "Thank you for using our service. Please use the following One-Time Password (OTP) to proceed with your authentication:\n\n" +
                                "OTP: %d\n\n" +
                                "This OTP is valid for the next 10 minutes. Please do not share this OTP with anyone.\n\n" +
                                "Best regards,\n" +
                                "RiskGuard", this.otp);

                try {
                    EmailUtil.sendEmail(email, "Demande de réinitialisation de mot de passe", emailBody);
                    showSuccessAlert("Email Sent Successfully", "An OTP has been sent to your email address.");
                    System.out.println(otpUserId.getUserId());
                    navigateToOTPVerification(event); // Navigate only if email is successfully sent
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



    public void navigateToOTPVerification(ActionEvent event) {
        try {

            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

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
        String enteredOTP = OTP1.getText().trim() + OTP2.getText().trim() + OTP3.getText().trim() + OTP4.getText().trim();
        System.out.println("Checking OTP: " + enteredOTP + " against stored: " + otp);

        if (enteredOTP.equals(String.valueOf(otp))) {
            OTPErrorMessageTF.setText("");
//            showSuccessAlert("Vérification réussie", "Le code OTP est correct.");
            navigateToRestPassword(e);
        } else {
            OTPErrorMessageTF.setText("OTP est incorrect, vérifiez à nouveau.");
        }
    }


    public void navigateToRestPassword(ActionEvent event) {
        try {

            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resetPassword.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifyPasswords(ActionEvent e) {
        if (passwordFieldTF.getText().isBlank() || confirmPasswordFieldTF.getText().isBlank()) {
            if (passwordFieldTF.getText().isBlank()) {
                passwordErrorMessageTF.setText("Le champs Mot de passe est requis.");
            } else if (passwordFieldTF.getText().length() < 8) {
                passwordErrorMessageTF.setText("8 caractéres au minimum.");
            } else {
                passwordErrorMessageTF.setText("");
            }

            if (confirmPasswordFieldTF.getText().isBlank()) {
                confPasswordErrorMessageTF.setText("Le champs Confirmer mot de passe est requis.");

            } else {
                confPasswordErrorMessageTF.setText("");
            }
        } else {

            if (!passwordFieldTF.getText().equals(confirmPasswordFieldTF.getText())) {
                confPasswordErrorMessageTF.setText("Mot de passe ne correspond pas");
                passwordErrorMessageTF.setText("");

            } else {
                passwordErrorMessageTF.setText("");
                confPasswordErrorMessageTF.setText("");
                updatePassword(passwordFieldTF.getText(), e);
            }
        }
    }

    public void initialize() {
        if (displayPasswordTF != null) {
            displayPasswordTF.textProperty().bindBidirectional(passwordFieldTF.textProperty());

        } else {
            System.out.println("displayPasswordTF is null");
        }
    }


    public void togglePasswordVisibility() {
        boolean visible = toggleVisibility.isSelected();

        // Manage visibility of password and plain text fields

        passwordFieldTF.setManaged(!visible);
        passwordFieldTF.setVisible(!visible);
        displayPasswordTF.setManaged(visible);
        displayPasswordTF.setVisible(visible);

        confirmPasswordFieldTF.setManaged(!visible);
        confirmPasswordFieldTF.setVisible(!visible);
        displayConfPasswordField.setManaged(visible);
        displayConfPasswordField.setVisible(visible);

        if (visible) {
            // Copy passwords to text fields to display them
            displayPasswordTF.setText(passwordFieldTF.getText());
            displayConfPasswordField.setText(confirmPasswordFieldTF.getText());
        } else {
            // Copy back the possibly changed text to the password fields when hidden
            passwordFieldTF.setText(displayPasswordTF.getText());
            confirmPasswordFieldTF.setText(displayConfPasswordField.getText());
        }
    }



    private void updatePassword(String newPassword, ActionEvent event) {
        // Placeholder for the database update logic
        String query = "UPDATE abstract_utilisateur SET mot_de_passe = ? WHERE id = ?";
        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPassword); // Assuming you hash the password before storing
            System.out.println(this.userId);
            System.out.println(otpUserId.getReserveId());

            pstmt.setInt(2, otpUserId.getReserveId()); // Assumes you have the user's email

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showSuccessAlert("Mise à jour réussie", "Votre mot de passe a été réinitialisé avec succès.");
                navigateBackToLogin(event);
            } else {
                showAlert("Erreur de mise à jour", "La mise à jour du mot de passe a échoué.");
                System.out.println(userId);
            }
        } catch (SQLException ex) {
            showAlert("Erreur de Base de Données", "Un problème est survenu lors de la mise à jour du mot de passe: " + ex.getMessage());
        }
    }


    public void navigateBackToLogin(ActionEvent event) {
        try {

            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));

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






