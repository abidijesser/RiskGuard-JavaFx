package services;

import models.categorie;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class categorieService {

    public List<categorie> getAll() throws SQLException {
        List<categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        try (Connection conn = MyDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorie category = new categorie(rs.getInt("id"), rs.getString("name"));
                categories.add(category);
            }
        }
        return categories;
    }
}
