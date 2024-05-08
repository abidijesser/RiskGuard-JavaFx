package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import services.marketingService;

import java.sql.SQLException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

public class StatisticsController {

    @FXML
    private PieChart statusPieChart;

    marketingService marketingService;

    @FXML
    private void initialize()  {
        // Initialize your marketing service
        marketingService = new marketingService();

        // Populate the PieChart with data
        populatePieChart();
    }

    private void populatePieChart() {
        Map<Integer, Integer> budgetData = marketingService.getMonthlyBudget();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        budgetData.forEach((month, budget) -> {
            String monthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault());
            pieChartData.add(new PieChart.Data(monthName, budget));
        });

        statusPieChart.setData(pieChartData);
    }


    public void setChartData(Map<String, Integer> dataMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        dataMap.forEach((label, value) -> {
            pieChartData.add(new PieChart.Data(label, value));
        });
        statusPieChart.setData(pieChartData);
    }

    public int getActiveMarketingCount() throws SQLException {
        return (int) marketingService.getActiveMarketingCount();
    }

    public int getEndedMarketingCount() throws SQLException {
        return (int) marketingService.getEndedMarketingCount();
    }

}
