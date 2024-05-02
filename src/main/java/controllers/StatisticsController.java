package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import services.marketingService;

import java.sql.SQLException;

public class StatisticsController {

    @FXML
    private PieChart statusPieChart;

    private marketingService marketingService;

    @FXML
    private void initialize()  {
        // Initialize your marketing service
        marketingService = new marketingService();

        // Populate the PieChart with data
        populatePieChart();
    }

    private void populatePieChart() {
        try {
            long activeCount = marketingService.getActiveMarketingCount();
            long endedCount = marketingService.getEndedMarketingCount();

            statusPieChart.getData().addAll(
                    new PieChart.Data("Active", activeCount),
                    new PieChart.Data("Ended", endedCount)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void setData(int activeCount, int endedCount) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Active", activeCount),
                new PieChart.Data("Ended", endedCount)
        );
        statusPieChart.setData(pieChartData);
    }

    public int getActiveMarketingCount() throws SQLException {
        return (int) marketingService.getActiveMarketingCount();
    }

    public int getEndedMarketingCount() throws SQLException {
        return (int) marketingService.getEndedMarketingCount();
    }

}
