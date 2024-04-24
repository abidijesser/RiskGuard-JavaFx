package services;

import models.Client;
import models.AbstractUtilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.util.List;

public class clientService implements IService<Client> {

    private Connection connection;

    public clientService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Client client) throws SQLException {

        String sqlAbstractUser = "INSERT INTO abstract_utilisateur " +
            "(nom, prenom, email, mot_de_passe, telephone, date_de_naissance, type) VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlClient = "INSERT INTO client (id, cin, adresse_domicile) VALUES (?, ?, ?)";

        try (
                PreparedStatement psAbstractUser = connection.prepareStatement(sqlAbstractUser, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psClient = connection.prepareStatement(sqlClient);
                )
        {
            psAbstractUser.setString(1, client.getNom());
            psAbstractUser.setString(2, client.getPrenom());
            psAbstractUser.setString(3, client.getEmail());
            psAbstractUser.setString(4, client.getMotDePasse()); // Ensure this is hashed if necessary
            psAbstractUser.setString(5, client.getTelephone());
            psAbstractUser.setDate(6, java.sql.Date.valueOf(client.getDateDeNaissance()));
            psAbstractUser.setString(7, "client");

            psAbstractUser.executeUpdate();

            ResultSet rs = psAbstractUser.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);  // Retrieve the generated ID

                // Second part: Insert into client
                psClient.setInt(1, generatedId);
                psClient.setString(2, client.getCin());
                psClient.setString(3, client.getAdresseDomicile());
                psClient.executeUpdate();  // Don't forget to execute the update for client
            } else {
                throw new SQLException("Creating abstract_utilisateur failed, no ID obtained.");
            }
        }
    }

    @Override
    public void update(Client client) throws SQLException {
        // Implement the update logic
    }

    @Override
    public void delete(int id) throws SQLException {
        // Implement the delete logic
    }

    @Override
    public List<Client> getAll() throws SQLException {
        // Implement the logic to retrieve all clients
        return null;
    }

    @Override
    public Client getById(int id) throws SQLException {
        // Implement the logic to retrieve a client by their ID
        return null;
    }
}
