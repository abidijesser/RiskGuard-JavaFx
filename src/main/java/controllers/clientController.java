package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.marketing;
import services.marketingService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class clientController {

    @FXML
    private FlowPane marketingFlowPane;

    public void initialize() throws SQLException {
        loadMarketingCards();
    }

    private void loadMarketingCards() {
        marketingService marketingService = new marketingService(); // Initialize your marketing service
        try {
            // Fetch all marketing campaigns from the database
            List<marketing> marketingCampaigns = marketingService.getAll();

            // Clear any existing marketing cards in the container
            marketingFlowPane.getChildren().clear();

            // Loop through each marketing campaign and create a card for it
            for (marketing campaign : marketingCampaigns) {
                Node card = createMarketingCard(campaign);
                marketingFlowPane.getChildren().add(card);
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }

    private Node createMarketingCard(marketing campaign) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10)); // Insets for top, right, bottom, and left

        // Check if the image path is not null
        if (campaign.getImagePath() != null && !campaign.getImagePath().isEmpty()) {
            ImageView imageView = new ImageView(new Image(campaign.getImagePath()));
            card.getChildren().add(imageView);
        }

        Label titleLabel = new Label(campaign.getTitre());
        Label budgetLabel = new Label("Budget: $" + campaign.getBudget());
        card.getChildren().addAll(titleLabel, budgetLabel);
        card.setOnMouseClicked(e -> openMarketingDetails(campaign));
        return card;
    }


    private void openMarketingDetails(marketing campaign) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MarketingDetails.fxml"));
            Parent root = loader.load();
            MarketingDetailsController controller = loader.getController();
            controller.setMarketingData(campaign);

            Stage stage = new Stage();
            stage.setTitle("Marketing Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions, perhaps show an error message
        }
    }
}
