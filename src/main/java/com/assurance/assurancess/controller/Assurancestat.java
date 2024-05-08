package com.assurance.assurancess.controller;

import com.assurance.assurancess.service.AssuranceVieService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Assurancestat {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private AssuranceVieService assuranceVieService;

    public Assurancestat() {
        assuranceVieService = new AssuranceVieService();
    }

    @FXML
    public void initialize() {
        List<Map<String, Object>> data = assuranceVieService.getTopThreeSalaireClients();
        ObservableList<BarChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();

        for (Map<String, Object> item : data) {
            String assuranceNom = (String) item.get("assurance_nom");
            float salaireClient = (float) item.get("salaireclient");

            BarChart.Series<String, Number> series = new BarChart.Series<>();
            series.setName(assuranceNom);
            series.getData().add(new BarChart.Data<>("Salaire Client", salaireClient));
            seriesList.add(series);
        }

        barChart.getData().addAll(seriesList);
        customizeBarChart();
    }

    private void customizeBarChart() {
        // Personnalisez la largeur des barres ici
        for (BarChart.Series<String, Number> series : barChart.getData()) {
            for (BarChart.Data<String, Number> data : series.getData()) {
                data.getNode().setStyle("-fx-bar-width: 20;");
            }
        }
    }

    public void goback(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/listass.fxml"));
        Scene scene = new Scene(root);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }   
}
