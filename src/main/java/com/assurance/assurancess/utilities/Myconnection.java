package com.assurance.assurancess.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Myconnection {
    private String url = "jdbc:mysql://localhost:3306/riskguard-pidev";
    private String login = "root";
    private String password = ""; // Assurez-vous de spécifier le mot de passe de votre utilisateur root ici

    Connection cnx;
    private static Myconnection instance;

    private Myconnection(){
        try {
            cnx = DriverManager.getConnection(url, login, password);
            System.out.println("Connected to DB ! ");
        }
        catch (SQLException ex) {
            System.out.println("Erreur de connexion à la base de données : " + ex.getMessage());
        }
    }

    public Connection getCnx(){
        return cnx;
    }

    public static Myconnection getInstance() {
        if (instance == null){
            instance = new Myconnection();
        }
        return instance;
    }
}
