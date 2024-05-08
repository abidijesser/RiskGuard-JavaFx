package services;

import models.commentaire;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService {

    public void addComment(commentaire comment) {
        String sql = "INSERT INTO commentaire (content, author, time_stamp, marketing_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = MyDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, comment.getContent());
            pstmt.setString(2, comment.getAuthor());
            pstmt.setTimestamp(3, comment.getTimeStamp());
            pstmt.setInt(4, comment.getMarketing_id());
            pstmt.executeUpdate();
            System.out.println("Comment added successfully");
        } catch (SQLException e) {
            System.err.println("Error adding comment: " + e.getMessage());
        }
    }

    public List<commentaire> getCommentsByMarketingId(int marketingId) {
        List<commentaire> comments = new ArrayList<>();
        String sql = "SELECT * FROM commentaire WHERE marketing_id = ?";

        try (Connection conn = MyDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, marketingId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                commentaire comment = new commentaire();
                comment.setId(rs.getInt("id"));
                comment.setContent(rs.getString("content"));
                comment.setAuthor(rs.getString("author"));
                comment.setTimeStamp(rs.getTimestamp("time_stamp"));
                comment.setMarketing_id(rs.getInt("marketing_id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return comments;
    }
}
