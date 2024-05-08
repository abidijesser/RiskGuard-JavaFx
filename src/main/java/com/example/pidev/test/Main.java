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
           // FXMLLoader loader = new FXMLLoader(getClass().getResource("/resetPasswordEmailVerification.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/employe.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pidev/hello-view.fxml"));
           // FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Front.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Back.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/listass.fxml"));

            Parent root = loader.load();


            primaryStage.setTitle("pidev");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }}