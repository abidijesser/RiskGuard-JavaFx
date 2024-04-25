package com.riskguard.crudjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Charger l'icône à partir d'un fichier image
        Image icon = new Image(getClass().getResourceAsStream("/images/ass.png"));

        Parent parent = FXMLLoader.load(getClass().getResource("/Fxml/Front.fxml"));
        Scene scene = new Scene(parent);

        // Ajouter l'icône à la fenêtre principale
        stage.getIcons().add(icon);

        stage.setTitle("RiskGuard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
