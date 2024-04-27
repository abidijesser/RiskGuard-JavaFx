package com.assurance.assurancess.service;

import com.assurance.assurancess.entities.Assurance;
import com.assurance.assurancess.utilities.Myconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssuranceService {
    private Connection connection;

    public AssuranceService() {
        this.connection = Myconnection.getInstance().getCnx();
    }

    // Create
    public void addAssurance(Assurance assurance) {
        String query = "INSERT INTO assurance (nomdupack, promotiondupack, description, typedupack) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, assurance.getNomdupack());
            statement.setString(2, assurance.getPromotiondupack());
            statement.setString(3, assurance.getDescription());
            statement.setString(4, assurance.getTypedupack());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read
    public List<Assurance> getAllAssurances() {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Assurance assurance = new Assurance();
                assurance.setId(resultSet.getLong("id"));
                assurance.setNomdupack(resultSet.getString("nomdupack"));
                assurance.setPromotiondupack(resultSet.getString("promotiondupack"));
                assurance.setDescription(resultSet.getString("description"));
                assurance.setTypedupack(resultSet.getString("typedupack"));
                assurances.add(assurance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assurances;
    }

    // Update
    public void updateAssurance(Long id, Assurance updatedAssurance) {
        String query = "UPDATE assurance SET nomdupack=?, promotiondupack=?, description=?, typedupack=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedAssurance.getNomdupack());
            statement.setString(2, updatedAssurance.getPromotiondupack());
            statement.setString(3, updatedAssurance.getDescription());
            statement.setString(4, updatedAssurance.getTypedupack());
            statement.setLong(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void deleteAssurance(Long id) {
        String query = "DELETE FROM assurance WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Assurance getAssuranceById(Long selectedAssuranceId) {
        String query = "SELECT * FROM assurance WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, selectedAssuranceId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Assurance assurance = new Assurance();
                assurance.setId(resultSet.getLong("id"));
                assurance.setNomdupack(resultSet.getString("nomdupack"));
                assurance.setPromotiondupack(resultSet.getString("promotiondupack"));
                assurance.setDescription(resultSet.getString("description"));
                assurance.setTypedupack(resultSet.getString("typedupack"));
                return assurance;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
