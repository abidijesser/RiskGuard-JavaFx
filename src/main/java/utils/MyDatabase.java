package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL = "jdbc:mysql://localhost:3306/risk";
    private final String USER = "root";
    private final String PASSWORD = "";
    private static Connection connection;
    private static MyDatabase instance;

    private MyDatabase() {
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
