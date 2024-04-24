package services;

import models.Client;
import utils.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class clientService implements IService<Client> {

    private Connection connection;

    public clientService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Client client) throws SQLException {
        String sql =
            "INSERT INTO client (nom, prenom, email, mot_de_passe, telephone, date_de_naissance, cin, adresse_domicile) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getNom());
            preparedStatement.setString(2, client.getPrenom());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setString(4, client.getMotDePasse()); // Ensure this is hashed if necessary
            preparedStatement.setString(5, client.getTelephone());
            preparedStatement.setDate(6, java.sql.Date.valueOf(client.getDateDeNaissance()));
            preparedStatement.setString(7, client.getCin());
            preparedStatement.setString(8, client.getAdresseDomicile());

            preparedStatement.executeUpdate();
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
