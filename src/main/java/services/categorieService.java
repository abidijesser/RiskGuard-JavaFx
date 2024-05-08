package services;

import models.categorie;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class categorieService {
    private final Connection connection;

    public categorieService() {
        connection = MyDatabase.getInstance().getConnection();

    }



    public List<categorie> getAll() throws SQLException {
        List<categorie> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM categorie"; // Adjust SQL as per your DB schema
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            categorie cat = new categorie(rs.getInt("id"), rs.getString("name"));
            categories.add(cat);
        }
        rs.close();
        stmt.close();

        return categories;
    }

    public categorie fetchCategoryById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM categorie WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                categorie cat = new categorie();
                cat.setId(resultSet.getInt("id"));
                cat.setName(resultSet.getString("name"));
                return cat;
            } else {
                return null;  // or throw an exception if a category should always exist for a given ID
            }
        } catch (SQLException e) {
            System.err.println("Error fetching category by ID: " + e.getMessage());
            throw e;
        }
    }

    public void add(categorie category) throws SQLException {
        String sql = "INSERT INTO categorie (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();
            System.out.println("Category added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
            throw e;
        }
    }

    public void update(categorie category) throws SQLException {
        String sql = "UPDATE categorie SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("No category found with ID: " + category.getId());
            } else {
                System.out.println("Category updated successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            throw e;
        }
    }

    public void delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("No category found with ID: " + categoryId);
            } else {
                System.out.println("Category deleted successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            throw e;
        }
    }


}
