package com.riskguard.crudjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBConnexion {
    static String user = "root";
    static String password = "";
    static String url = "jdbc:mysql://localhost/riskg";
    static String driver = "com.mysql.cj.jdbc.Driver";

    public static Connection getCon() {
        Connection con = null;
        try {
            Class.forName(driver);
            try{
                con = DriverManager.getConnection(url,user,password);
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return con;
    }



}

