package models;

import java.time.LocalDate;
import java.util.Date;

public class marketing {
    private Long id;
    private String titre;
    private String objectif;
    private Double budget;
    private Date dateDebut;
    private Date dateFin;

    public marketing(){}

    // Constructor
    public marketing(String titre, String objectif, double budget, LocalDate dateDebut, LocalDate dateFin) {
        this.titre = titre;
        this.objectif = objectif;
        this.budget = budget;
        // Converting LocalDate to java.sql.Date
        this.dateDebut = java.sql.Date.valueOf(dateDebut);
        this.dateFin = java.sql.Date.valueOf(dateFin);

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

    public java.sql.Date getDateDebut() {
        return (java.sql.Date) dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public java.sql.Date getDateFin() {
        return (java.sql.Date) dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }




    // Override toString method for easy printing of Marketing object details
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

