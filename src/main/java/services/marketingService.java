package services;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import models.marketing;
import models.categorie;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;



public class marketingService implements IService<marketing> {
    private static Connection connection;
    private categorieService categoryService ;


    public void generateMarketingReport(String dest) throws FileNotFoundException, MalformedURLException {
        // Initialize PDF writer and document
        Document document = null;
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            document = new Document(pdf);

            // Add an image
            ImageData imageData = ImageDataFactory.create("src/main/resources/image/favicon.png");
            Image pdfImg = new Image(imageData);
            pdfImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
            pdfImg.scaleToFit(200, 100);  // Adjust the image size
            document.add(pdfImg);

            // Create a table with flexible column widths
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3, 2}));  // Adjust column ratios
            table.setWidth(UnitValue.createPercentValue(100));  // Set the table width to 100% of the page width

            // Add headers
            table.addHeaderCell("ID");
            table.addHeaderCell("Campaign Name");
            table.addHeaderCell("Status");

            // Fetch marketing campaigns and add rows
            List<marketing> campaigns = getCampaigns();
            for (marketing campaign : campaigns) {
                table.addCell(String.valueOf(campaign.getId()));
                table.addCell(new Cell().add(new Paragraph(campaign.getTitre())).setMinWidth(50)); // Example of setting minimum width
                table.addCell("Active".equals(campaign.getStatus()) ? "Active" : "Ended");
            }

            // Add table to document
            document.add(table);

            // Close the document
            document.close();
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
        } finally {
            if (document != null) {
                document.close(); // Ensure all resources are closed properly
            }
        }
    }



    // Example method to fetch marketing campaigns
    private List<marketing> getCampaigns() {
        List<marketing> campaigns = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/risk", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id, titre, status FROM marketing"); // Adjust SQL as needed

            while (resultSet.next()) {
                marketing mkt = new marketing();
                mkt.setId(resultSet.getLong("id"));
                mkt.setTitre(resultSet.getString("titre"));
                mkt.setStatus(resultSet.getString("status")); // Assuming these methods exist in your marketing class
                campaigns.add(mkt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return campaigns;
    }


    public marketingService() {
        connection = MyDatabase.getInstance().getConnection();
        this.categoryService = new categorieService();  // Initialize categorieService here
    }

    // Method to use categorieService for fetching category details
    private categorie getCategoryById(int categoryId) throws SQLException {
        return categoryService.fetchCategoryById(categoryId);  // Correctly use categorieService's method
    }

    public long getActiveMarketingCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM marketing WHERE status = 'Active'";
        try (Connection conn = MyDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0; // Return 0 if no count is retrieved
    }

    public long getEndedMarketingCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM marketing WHERE status = 'Ended'";
        try (Connection conn = MyDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0; // Return 0 if no count is retrieved
    }


    public void add(marketing marketing) throws SQLException {
        Double budget = marketing.getBudget();
        if (budget != null) {
            String sql = "INSERT INTO marketing (titre, objectif, budget, date_debut, date_fin, categorie_id, image, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, marketing.getTitre());
                preparedStatement.setString(2, marketing.getObjectif());
                preparedStatement.setDouble(3, budget);
                preparedStatement.setDate(4, new java.sql.Date(marketing.getDateDebut().getTime()));
                preparedStatement.setDate(5, new java.sql.Date(marketing.getDateFin().getTime()));
                preparedStatement.setInt(6, marketing.getCategory().getId());  // Assuming getCategory() returns a categorie object
                preparedStatement.setString(7, marketing.getImagePath());  // Set the image path
                // Set the initial status based on the dateFin
                if (marketing.getDateFin().before(new java.util.Date())) {
                    preparedStatement.setString(8, "Ended");
                } else {
                    preparedStatement.setString(8, "Active");
                }
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
        String sql = "UPDATE marketing SET titre = ?, objectif = ?, budget = ?, date_debut = ?, date_fin = ?, categorie_id = ?, image = ?, status = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, marketing.getTitre()); // Set titre
        preparedStatement.setString(2, marketing.getObjectif()); // Set objectif
        preparedStatement.setDouble(3, marketing.getBudget()); // Set budget
        preparedStatement.setDate(4, marketing.getDateDebut()); // Set date_debut (assuming it's a java.sql.Date)
        preparedStatement.setDate(5, marketing.getDateFin()); // Set date_fin (assuming it's a java.sql.Date)
        preparedStatement.setInt(6, marketing.getCategory().getId()); // Update the category ID
        preparedStatement.setString(7, marketing.getImagePath());  // Update the image path

        // Update the status based on the new dateFin
        if (marketing.getDateFin().before(new java.util.Date())) {
            preparedStatement.setString(8, "Ended");
        } else {
            preparedStatement.setString(8, "Active");
        }
        preparedStatement.setInt(9, marketing.getId()); // Set id
        preparedStatement.executeUpdate();
    }


    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from marketing where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }


    public  List<marketing> getAll() throws SQLException {
        String sql = "SELECT m.*, c.name as categorie_name, c.id as categorie_id FROM marketing m INNER JOIN categorie c ON m.categorie_id = c.id";
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

            categorie cat = new categorie();
            cat.setId(rs.getInt("categorie_id"));
            cat.setName(rs.getString("categorie_name"));
            m.setCategory(cat);

            marketings.add(m);
        }
        return marketings;

    }


    @Override
    public marketing getById(int id) throws SQLException {
        String sql = "SELECT m.*, c.name as categorie_name, c.id as categorie_id FROM marketing m INNER JOIN categorie c ON m.categorie_id = c.id WHERE m.id = ?";
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

            categorie cat = new categorie();
            cat.setId(resultSet.getInt("categorie_id"));
            cat.setName(resultSet.getString("categorie_name"));
            marketing.setCategory(cat);

            return marketing;
        } else {
            // Handle the case when no matching record is found
            return null;
        }
    }

    public List<marketing> getByCategory(int categoryId) throws SQLException {
        List<marketing> filteredList = new ArrayList<>();
        String sql = "SELECT * FROM marketing WHERE categorie_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                marketing m = new marketing();
                m.setId(rs.getLong("id"));
                m.setTitre(rs.getString("titre"));
                m.setObjectif(rs.getString("objectif"));
                m.setBudget(rs.getDouble("budget"));
                m.setDateDebut(rs.getDate("date_debut"));
                m.setDateFin(rs.getDate("date_fin"));
                m.setCategory(categoryService.fetchCategoryById(rs.getInt("categorie_id"))); // Correctly access the method
                filteredList.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching marketing by category: " + e.getMessage());
            throw e;
        }
        return filteredList;
    }
    public void updateMarketingImage(marketing marketing) throws SQLException {
        String sql = "UPDATE marketing SET image = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, marketing.getImagePath());
            preparedStatement.setLong(2, marketing.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating image failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating marketing image: " + e.getMessage());
            throw e;
        }
    }

}







