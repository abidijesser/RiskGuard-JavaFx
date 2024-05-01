package ults;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/bdjava"; // Change this to your MySQL URL
    private static final String USERNAME = "root"; // Change this to your MySQL username
    private static final String PASSWORD = ""; // Change this to your MySQL password

    // Establish database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Close database connection
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Close database resources (ResultSet, Statement, PreparedStatement)
    public static void close(ResultSet rs, PreparedStatement ps, Connection connection) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Execute SQL query (SELECT)
    public static ResultSet executeQuery(String sql) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Execute SQL query with parameters (SELECT)
    public static ResultSet executeQuery(String sql, Object... params) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Execute SQL update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String sql) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Execute SQL update with parameters (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String sql, Object... params) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
