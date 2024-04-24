package com.example.pidev.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import controllers.EmployeController;
import controllers.vieController;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public  class Main  extends Application {

    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/employe.fxml"));
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pidev/vehiculeEmploye.fxml"));
            Parent root = loader.load();

            // Set up the primary stage
            primaryStage.setTitle("pidev");
            primaryStage.setScene(new Scene(root, 800, 600));

            // Show the primary stage
            primaryStage.show();
        } catch (IOException e) {
            // Handle any exceptions that occur during FXML loading
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }}