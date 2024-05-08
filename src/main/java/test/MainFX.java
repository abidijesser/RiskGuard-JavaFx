package test;

import com.riskguard.crudjava.*;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load and display the Marketing interface
        FXMLLoader marketingLoader = new FXMLLoader(getClass().getResource("/marketingCRUD.fxml"));
        Parent marketingRoot = marketingLoader.load();
        Scene marketingScene = new Scene(marketingRoot);
        primaryStage.setTitle("Marketing");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/favicon.png"))); // Set favicon
        primaryStage.setScene(marketingScene);
        primaryStage.show();

        // Create and display the Client View interface in a new window
        Stage clientStage = new Stage();
        FXMLLoader clientLoader = new FXMLLoader(getClass().getResource("/clientView.fxml"));
        Parent clientRoot = clientLoader.load();
        Scene clientScene = new Scene(clientRoot);
        clientStage.setTitle("Client View");
        clientStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/favicon.png"))); // Set favicon
        clientStage.setScene(clientScene);
        clientStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
