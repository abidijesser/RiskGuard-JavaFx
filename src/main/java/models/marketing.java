package models;

import java.sql.Date; // Correct import for java.sql.Date
import java.time.LocalDate; // Used for conversions between LocalDate and java.sql.Date

public class marketing {
    private Long id;
    private String titre;
    private String objectif;
    private Double budget;
    private Date dateDebut; // Using java.sql.Date directly
    private Date dateFin; // Using java.sql.Date directly
    private categorie category;
    private String imagePath;  // Field to store the image path
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public categorie getCategory() {
        return category;
    }

    public void setCategory(categorie category) {
        this.category = category;
    }

    public marketing() {}

    // Constructor converting LocalDate to java.sql.Date
    public marketing(String titre, String objectif, double budget, LocalDate dateDebut, LocalDate dateFin) {
        this.titre = titre;
        this.objectif = objectif;
        this.budget = budget;
        this.dateDebut = Date.valueOf(dateDebut); // Convert LocalDate to java.sql.Date
        this.dateFin = Date.valueOf(dateFin);     // Convert LocalDate to java.sql.Date


    }

    // Getters and Setters
    public int getId() {
        return Math.toIntExact(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



    @Override
    public String toString() {
        return "marketing{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", objectif='" + objectif + '\'' +
                ", budget=" + budget +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +

                '}';
    }
}
