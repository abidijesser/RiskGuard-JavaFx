package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {



    @Override
    public void start(Stage primaryStage)  throws  Exception{
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/resetPasswordEmailVerification.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/addClient.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/resetPassword.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminDashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("RiskGuard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
