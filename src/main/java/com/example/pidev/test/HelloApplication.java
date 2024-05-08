package com.example.pidev.test;

import controllers.EmployeController;
import controllers.vieController;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class HelloApplication  {



    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        vieController controller = loader.getController();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello Application");
        primaryStage.show();}







        public static void main(String[] args) {
            launch();
        }
    }