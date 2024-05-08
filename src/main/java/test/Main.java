package test;



import models.marketing; // Import the Marketing class if not already imported
import services.marketingService; // Import the MarketingService class


import java.sql.SQLException;
import java.util.List;

public class Main extends Object {

    public static void main(String[] args) {


        marketingService marketingService = new marketingService(); // Create an instance of MarketingService
        try {
            // Retrieve all marketing data
            List<marketing> marketingList = marketingService.getAll();
            // Print marketing details
            for (marketing marketing : marketingList) {
                System.out.println(marketing);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching marketing data: " + e.getMessage());
        }

    }
}
