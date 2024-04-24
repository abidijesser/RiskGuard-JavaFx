package services;
import models.marketing;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class marketingService implements IService<marketing> {
    private static Connection connection;

    public marketingService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void add(marketing marketing) throws SQLException {
        Double budget = marketing.getBudget();
        if (budget != null) {
            String sql = "INSERT INTO marketing (titre, objectif, budget, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, marketing.getTitre());
                preparedStatement.setString(2, marketing.getObjectif());
                preparedStatement.setDouble(3, budget);
                preparedStatement.setDate(4, new java.sql.Date(marketing.getDateDebut().getTime()));
                preparedStatement.setDate(5, new java.sql.Date(marketing.getDateFin().getTime()));


                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    System.out.println("Marketing added successfully!");
                } else {
                    System.err.println("No marketing was added, please check your input data!");
                }
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
            }
        } else {
            System.err.println("Error adding marketing: Budget cannot be null");
        }
    }





    @Override
    public void update(marketing marketing) throws SQLException {
        String sql = "UPDATE marketing SET titre = ?, objectif = ?, budget = ?, date_debut = ?, date_fin = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, marketing.getTitre()); // Set titre
        preparedStatement.setString(2, marketing.getObjectif()); // Set objectif
        preparedStatement.setDouble(3, marketing.getBudget()); // Set budget
        preparedStatement.setDate(4, marketing.getDateDebut()); // Set date_debut (assuming it's a java.sql.Date)
        preparedStatement.setDate(5, marketing.getDateFin()); // Set date_fin (assuming it's a java.sql.Date)
        preparedStatement.setInt(6, marketing.getId()); // Set id
        preparedStatement.executeUpdate();
    }


    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from marketing where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }


    public List<marketing> getAll() throws SQLException {
        String sql = "SELECT * FROM marketing";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<marketing> marketings = new ArrayList<>();
        while (rs.next()) {
            marketing m = new marketing();
            m.setId((long) rs.getInt("id"));
            m.setTitre(rs.getString("titre"));
            m.setObjectif(rs.getString("objectif"));
            m.setBudget(rs.getDouble("budget"));
            m.setDateDebut(rs.getDate("date_debut"));
            m.setDateFin(rs.getDate("date_fin"));

            marketings.add(m);
        }
        return marketings;
    }


    @Override
    public marketing getById(int id) throws SQLException {
        String sql = "SELECT * FROM marketing WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            marketing marketing = new marketing();
            marketing.setId((long) resultSet.getInt("id"));
            marketing.setTitre(resultSet.getString("titre"));
            marketing.setObjectif(resultSet.getString("objectif"));
            marketing.setBudget(resultSet.getDouble("budget"));
            marketing.setDateDebut(resultSet.getDate("date_debut"));
            marketing.setDateFin(resultSet.getDate("date_fin"));

            return marketing;
        } else {
            // Handle the case when no matching record is found
            return null;
        }
    }

}


