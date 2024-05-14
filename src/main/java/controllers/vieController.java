package controllers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import entites.Vie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import services.VieService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;




public class vieController  {
     @FXML
     private TextField nom;

     @FXML
     private TextField prenom;

     @FXML
     private TextField cin;

     @FXML
     private TextField causededeces;

     @FXML
     private TextField datededeces;

     @FXML
     private TextField identifiantdelinformant;

     @FXML
     private Button addButton;
    @FXML
    private Button constatvehicule;

    @FXML
    private VBox VBOX1;

    @FXML
    private VBox VBOX2;

    @FXML
    private TextFlow II;

    @FXML
    private TextArea LL;

     private VieService vieService;



    public vieController() {
        // Initialize your service class here
        this.vieService = new VieService();
    }

    @FXML
    public void initialize() {
        // Add action event handler to the button
        addButton.setOnAction(event -> vaddVie());
    }

    public void vaddVie() {
        // Retrieve values from text fields
        String nomValue = nom.getText();
        String prenomValue = prenom.getText();
        String cinValue = cin.getText();
        String causededecesValue = causededeces.getText();
        String datededecesValue = datededeces.getText();
        String identifiantdelinformantValue = identifiantdelinformant.getText();

        // Validate fields
        if (nomValue.isEmpty()) {
            showAlert(nom, "Nom cannot be empty.");
            return;
        }
        if (nomValue.matches(".*\\d.*")) {
            showAlert(nom, "Nom cannot contain numbers.");
            return;
        }

        if (prenomValue.isEmpty()) {
            showAlert(prenom, "Prenom cannot be empty.");
            return;
        }
        if (causededecesValue.isEmpty()) {
            showAlert(causededeces, "cause de deces cannot be empty.");
            return;
        }
        if (datededecesValue.isEmpty()) {
            showAlert(datededeces, "date de deces cannot be empty.");
            return;
        }
        if (prenomValue.matches(".*\\d.*")) {
            showAlert(prenom, "Prenom cannot contain numbers.");
            return;
        }
        if (cinValue.isEmpty()) {
            showAlert(cin, "CIN cannot be empty.");
            return;
        }

        if (cinValue.length() != 8) {
            showAlert(cin, "CIN must have exactly 8 digits.");
            return;
        }

        int cinInt;
        try {
            cinInt = Integer.parseInt(cinValue);
        } catch (NumberFormatException e) {
            showAlert(cin, "CIN must be a valid integer.");
            return;
        }

        // Create Vie object with the retrieved values
        Vie vie = new Vie(nomValue, prenomValue, cinValue, causededecesValue, datededecesValue, identifiantdelinformantValue);

        try {
            // Add Vie object to the database
            vieService.vaddVie(vie);
            System.out.println("Vie added successfully!");

            // Generate QR code containing the Vie information
            generateQRCode(vie);
        } catch (SQLException e) {
            System.err.println("Failed to add Vie: " + e.getMessage());
        }
    }

    private void generateQRCode(Vie vie) {
        try {
            // Construct the text for the QR code
            String qrText = "Nom: " + vie.getNom() + "\n"
                    + "Prenom: " + vie.getPrenom() + "\n"
                    + "CIN: " + vie.getCin() + "\n"
                    + "Cause de décès: " + vie.getCausededecess() + "\n"
                    + "Date de décès: " + vie.getDateDeces() + "\n"
                    + "Identifiant de l'informant: " + vie.getIdentifiantDeLinformantt();

            // Create QR code writer
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // Set QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Convert bit matrix to image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            // Convert image to JavaFX image
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Image qrImage = new Image(inputStream);

            // Display QR code image
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("QR Code");
            alert.setHeaderText(null);
            ImageView imageView = new ImageView(qrImage);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            alert.getDialogPane().setContent(imageView);
            alert.showAndWait();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            // Display an error message if QR code generation fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to generate QR code.");
            alert.showAndWait();
        }
    }

    private void showAlert(TextField field, String message) {
        // Create a Tooltip with the error message
        Tooltip tooltip = new Tooltip(message);

        // Convert the coordinates relative to the text field's parent to screen coordinates
        double screenX = field.localToScreen(field.getBoundsInLocal()).getMinX();
        double screenY = field.localToScreen(field.getBoundsInLocal()).getMinY();

        // Set the Tooltip above the text field
        tooltip.setAutoHide(true);
        tooltip.show(field, screenX, screenY - tooltip.getHeight());
    }

    public void constatvehicule(ActionEvent event) {
        try {
            // Load the FXML file for VehiculeEmploye
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pidev/vehicule.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) constatvehicule.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void navigateBack(ActionEvent event) {
        try {

            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionemp.fxml"));
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