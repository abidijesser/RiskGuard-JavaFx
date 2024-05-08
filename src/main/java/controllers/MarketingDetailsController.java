package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.commentaire;
import models.marketing;
import services.CommentaireService;

import java.util.List;

public class MarketingDetailsController {

    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label objectiveLabel;
    @FXML
    private Label budgetLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private ListView<String> commentListView; // ListView to display comments
    @FXML
    private TextArea commentTextArea;
    @FXML
    private TextField authorTextField;

    private marketing selectedMarketing;
    private CommentaireService commentaireService = new CommentaireService();

    // Method to set marketing data
    public void setMarketingData(marketing campaign) {
        titleLabel.setText(campaign.getTitre());
        objectiveLabel.setText(campaign.getObjectif());
        budgetLabel.setText("Budget: $" + campaign.getBudget());
        startDateLabel.setText("Start Date: " + campaign.getDateDebut().toString());
        endDateLabel.setText("End Date: " + campaign.getDateFin().toString());
        this.selectedMarketing = campaign;

        // Load the image if the path is not null or empty
        if (campaign.getImagePath() != null && !campaign.getImagePath().isEmpty()) {
            Image image = new Image("file:" + campaign.getImagePath()); // Ensure the path is correctly formatted
            imageView.setImage(image);
        }

        // Load comments for the selected marketing
        loadComments();
    }

    // Method to load comments for the selected marketing
    // Method to load comments for the selected marketing
    private void loadComments() {
        // Verify selected marketing ID
        System.out.println("Selected Marketing ID: " + selectedMarketing.getId());

        // Retrieve comments related to the selected marketing campaign
        List<commentaire> comments = commentaireService.getCommentsByMarketingId(selectedMarketing.getId());

        System.out.println("Number of comments retrieved: " + comments.size()); // Print the number of retrieved comments

        // Display comments in the ListView
        commentListView.getItems().clear(); // Clear existing items
        for (commentaire comment : comments) {
            System.out.println("Adding comment: " + comment.getContent()); // Print each comment being added
            commentListView.getItems().add(comment.getAuthor() + ": " + comment.getContent() + " - " + comment.getTimeStamp()); // Use the correct getter method for time_stamp
        }
    }




    @FXML
    private void handleSubmitComment() {
        String content = commentTextArea.getText();
        String author = authorTextField.getText(); // Assuming there's a TextField named authorTextField for entering author name

        if (!content.isEmpty()) {
            commentaire newComment = new commentaire();
            newComment.setContent(content);

            // Check if author field is empty, if so, set it to "anonymous"
            if (!author.isEmpty()) {
                newComment.setAuthor(author);
            } else {
                newComment.setAuthor("anonymous");
            }

            newComment.setTimeStamp(new java.sql.Timestamp(new java.util.Date().getTime()));
            newComment.setMarketing_id(selectedMarketing.getId());

            commentaireService.addComment(newComment);

            // Reload comments after adding a new comment
            loadComments();

            // Clear input fields after submission
            commentTextArea.clear();
            authorTextField.clear();
        } else {
            // Optionally handle empty comment case, show an error message or disable submit button
        }
    }
}
