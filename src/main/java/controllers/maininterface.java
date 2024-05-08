package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class maininterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionemp.fxml"));
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Dashboard");
            Scene sr = new Scene(root, 1200, 750);
            primaryStage.setScene(sr);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
