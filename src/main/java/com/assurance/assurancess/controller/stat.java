package com.assurance.assurancess.controller;

import com.assurance.assurancess.service.AssuranceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class stat implements Initializable {

    @FXML
    private PieChart pieChart;

    @FXML
    private Label top3Label;

    private AssuranceService assuranceService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assuranceService = new AssuranceService();
        updatePieChart();
    }

    private void updatePieChart() {
        Map<String, Integer> top3AssurancePacks = assuranceService.countAndRetrieveTop3AssurancePacks();

        pieChart.getData().clear();

        top3AssurancePacks.forEach((type, count) -> {
            PieChart.Data slice = new PieChart.Data(type, count);
            pieChart.getData().add(slice);
        });

        StringBuilder top3Text = new StringBuilder("Top 3 Used Assurance Packs:\n");
        int i = 1;
        for (Map.Entry<String, Integer> entry : top3AssurancePacks.entrySet()) {
            top3Text.append(i).append(". ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            i++;
        }
        top3Label.setText(top3Text.toString());
    }

    @FXML
    private void gotoOtherStatPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/assurancestat.fxml"));
        Scene scene = new Scene(root);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
