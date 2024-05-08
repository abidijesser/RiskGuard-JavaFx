package services;
import entites.Vehicule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehiculeService {
    private Connection connection;

    public VehiculeService() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/riskguard-pidev", "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  // Handle the case where the JDBC driver class is not found
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the case where there is an error in establishing the connection
        }
    }

    // Create operation
    public void addConstatVehicule(Vehicule constatVehicule) throws SQLException {
        // Creating a Statement
        Statement statement = connection.createStatement();

        // Requête SQL d'ajout
        String req = "INSERT INTO constatvehicule (nom, prenom, cin, `type vehicule`, matricule, lieu, date, description, image) " +
                "VALUES ('" + constatVehicule.getNom() + "', '" + constatVehicule.getPrenom() + "', " +
                constatVehicule.getCin() + ", '" + constatVehicule.getTypeVehicule() + "', '" +
                constatVehicule.getMatricule() + "', '" + constatVehicule.getLieu() + "', '" +
                constatVehicule.getDate() + "', '" + constatVehicule.getDescription() + "', '" +
                constatVehicule.getImage() + "')";

        // Exécuter la Requête
        statement.executeUpdate(req);
    }

    // Read operation
    public List<Vehicule> getAllConstatVehicules() throws SQLException {
        List<Vehicule> constatVehicules = new ArrayList<>();
        String query = "SELECT * FROM constatvehicule";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Vehicule constatVehicule = new Vehicule(
                    resultSet.getString("nom"),
                    resultSet.getString("prenom"),
                    resultSet.getString("cin"),
                    resultSet.getString("type vehicule"),
                    resultSet.getString("matricule"),
                    resultSet.getString("lieu"),
                    resultSet.getString("date"),
                    resultSet.getString("description"),
                    resultSet.getString("image")
            );
            constatVehicules.add(constatVehicule);
        }
        return constatVehicules;
    }

    // Update operation
    public void updateConstatVehicule(Vehicule updatedConstatVehicule) throws SQLException {

        // Creating a Statement
        Statement statement = connection.createStatement();

        // Requête SQL de mise à jour
        String req = "UPDATE constatvehicule SET nom = '" + updatedConstatVehicule.getNom() +
                "', prenom = '" + updatedConstatVehicule.getPrenom() +
                "', cin = " + updatedConstatVehicule.getCin() +
                ", `type vehicule` = '" + updatedConstatVehicule.getTypeVehicule() +
                "', matricule = '" + updatedConstatVehicule.getMatricule() +
                "', lieu = '" + updatedConstatVehicule.getLieu() +
                "', date = '" + updatedConstatVehicule.getDate() +
                "', description = '" + updatedConstatVehicule.getDescription() +
                "', image = '" + updatedConstatVehicule.getImage() +
                "' WHERE matricule = '" + updatedConstatVehicule.getMatricule() + "'";


        // Exécuter la Requête
        statement.executeUpdate(req);
    }

    // Delete operation
    public void deleteConstatVehicule(String matricule) throws SQLException {
        String query = "DELETE FROM constatvehicule WHERE matricule = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, matricule);
        statement.executeUpdate();
    }



    public int getVehiculeCount() throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) AS count FROM constatvehicule   ";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehiculeService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return count;
    }
}








