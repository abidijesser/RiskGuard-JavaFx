package services;
import entites.Vie;
import javafx.scene.control.TableView;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VieService {
    private static Connection connection;

    public VieService() {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/riskguard-pidev", "root", "");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  // Handle the case where the JDBC driver class is not found
            } catch (SQLException e) {
                e.printStackTrace();  // Handle the case where there is an error in establishing the connection
            }
        }

        // l ajout
        public void vaddVie(Vie vie) throws SQLException {
            String query = "INSERT INTO constatvie (nom, prenom, cin, `cause de decess`, `date deces`, `identifiant de linformantt`) VALUES (?, ?, ?, ?, ?, ?)";


            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, vie.getNom());
            statement.setString(2, vie.getPrenom());
            statement.setString(3, vie.getCin());
            statement.setString(4, vie.getCausededecess());
            statement.setString(5, vie.getDateDeces());
            statement.setString(6, vie. getIdentifiantDeLinformantt());
            statement.executeUpdate();
        }

        // affichage
        public static List<Vie> getAllConstatVies() throws SQLException {
            List<Vie> vies = new ArrayList<>();
            String query = "SELECT * FROM constatvie";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Vie vie = new Vie(
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("cin"),
                        resultSet.getString("cause de decess"),
                        resultSet.getString("date deces"),
                        resultSet.getString("identifiant de linformantt")
                );
                vies.add(vie);
            }
            return vies;
        }

        // Update
        public void updateVie(Vie vie) throws SQLException {
            String query = "UPDATE constatvie SET nom = ?, prenom = ?, cin = ?, `cause de decess` = ?, `date deces` = ?,`identifiant de linformantt` = ? WHERE cin= ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, vie.getNom());
            statement.setString(2, vie.getPrenom());
            statement.setString(3, vie.getCin());
            statement.setString(4, vie.getCausededecess());
            statement.setString(5, vie.getDateDeces());
            statement.setString(6, vie. getIdentifiantDeLinformantt());
            statement.setString(7, vie.getCin());

            statement.executeUpdate();
        }

        // Delete
        public void deleteVie(Vie selectedVie) throws SQLException {
            String query = "DELETE FROM constatvie WHERE cin = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedVie.getCin());
            statement.executeUpdate();
        }
    public int getVieCount() throws SQLException {
        // Implement the logic to retrieve the count of Vie instances from the database
        // For example:
        // Assuming you have a method named getAllVies() in your VieService to get all Vie instances,
        // you can get the count by calling size() on the returned list.

        List<Vie> allVies = getAllConstatVies();
        return allVies.size();
    }

}