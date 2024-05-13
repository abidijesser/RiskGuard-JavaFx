package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.MyDatabase;

public class StatistiquesClientsController {

    @FXML
    private PieChart addressChart;

    public void showAlert(String title, String content) {
        // Créez ici une alerte avec JavaFX
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void loadChartData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Connection con = MyDatabase.getInstance().getConnection();
        String query = "SELECT adresse_domicile, COUNT(*) as count FROM client GROUP BY adresse_domicile";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String address = rs.getString("adresse_domicile");
                int count = rs.getInt("count");
                pieChartData.add(new PieChart.Data(address, count));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error Loading Data", "Failed to load address data for chart: " + e.getMessage());
        }

        addressChart.setData(pieChartData);
        addressChart.setTitle("Répartition des clients par \"Adresse domicile\" ");
    }

    public void initialize() {
        loadChartData();
    }

    public void navigateToListClients(ActionEvent event) {
        try {

            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminDashboard.fxml"));
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
