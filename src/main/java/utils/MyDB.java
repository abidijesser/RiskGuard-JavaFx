package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {

    private final String URL = "jdbc:mysql://localhost:3306/riskguard-pidev";
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection connection;
    private static MyDB instance;

    public MyDB() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDB getInstance() {
        if(instance == null)
            instance = new MyDB();
        return instance;
    }





    public Connection getConnection() {
        return connection;
    }


}