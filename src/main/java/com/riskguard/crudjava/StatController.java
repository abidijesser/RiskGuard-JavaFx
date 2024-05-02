package com.riskguard.crudjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class StatController implements Initializable{

    @FXML
    private Button btn_reclamation;

    @FXML
    private Button btn_reponse;

    @FXML
    private Button btn_statistique;

    @FXML
    private Pane inner_pane;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private TextField txt_serach;
    @FXML
    private PieChart PieChart;

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
    void displayReclamationByEtatStatistics() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int totalReclamations = 0;
        try {
            Connection con = DBConnexion.getCon();
            Statement stmt = con.createStatement();
            String query = "SELECT etat, COUNT(*) FROM Reclamation GROUP BY etat";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String etat = rs.getString("etat");
                int count = rs.getInt(2);
                totalReclamations += count;
                pieChartData.add(new PieChart.Data(etat + " (" + count + ")", count));
            }
            // Ajouter les pourcentages aux étiquettes des secteurs
            for (PieChart.Data data : pieChartData) {
                double percentage = (data.getPieValue() / totalReclamations) * 100;
                data.setName(data.getName() + " - " + String.format("%.1f", percentage) + "%");
            }
            PieChart.setData(pieChartData);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayReclamationByEtatStatistics();

    }
}