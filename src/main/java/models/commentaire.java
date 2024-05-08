package models;

import java.sql.Timestamp;

public class commentaire {
    private int id;
    private String content;
    private String author;
    private Timestamp timeStamp;
    private int marketing_id;

    // Constructor without id for creating new instances
    public commentaire(String content, String author, Timestamp timeStamp, int marketing_id) {
        this.content = content;
        this.author = author;
        this.timeStamp = timeStamp;
        this.marketing_id = marketing_id;
    }

    // Default constructor
    public commentaire() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getMarketing_id() {
        return marketing_id;
    }

    public void setMarketing_id(int marketing_id) {
        this.marketing_id = marketing_id;
    }
}
