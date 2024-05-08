package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    public static final String URL = "jdbc:mysql://127.0.0.1:3306/riskguard-pidev";
    public static final String USER = "root";
    public static final String PASSWORD = "";

    public static Connection connection;
    public static MyDatabase instance;

    public MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if(instance == null)
            instance = new MyDatabase();
        return instance;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                instance = new MyDatabase();
            }
        } catch (SQLException e) {
            System.err.println("Failed to check connection status: " + e.getMessage());
        }
        return connection;
    }
}
