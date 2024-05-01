package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import services.VehiculeService;
import services.VieService;

import java.sql.SQLException;

public class StatisticsController {
    @FXML
    private PieChart pieChart; // Example of a chart for displaying statistics

    private VieService vieService = new VieService();
    private VehiculeService vehiculeService = new VehiculeService();

    @FXML
    public void initialize() {
        // Load and display statistics when the view is initialized
        loadStatistics();
    }

    private void loadStatistics() {
        try {
            // Calculate statistics for Constat Vie
            int constatVieCount = vieService.getVieCount();
            int vehiculeCount = vehiculeService.getVehiculeCount();

            // Populate UI elements with the calculated statistics
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Constat Vie (" + constatVieCount + ")", constatVieCount),
                    new PieChart.Data("Vehicule (" + vehiculeCount + ")", vehiculeCount)

            );
            pieChart.setData(pieChartData);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }
    }


}
