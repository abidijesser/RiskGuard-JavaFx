package com.riskguard.crudjava;

import java.util.Date;

public class Reponse {
        private int id_reponse ;
        private String id_reclamation ;
        private String contenu ;
        private Date date;
    private String description; // Nouvelle propriété

    // Getter et Setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_reponse() {
        return id_reponse;
    }

    public void setId_reponse(int id_reponse) {
        this.id_reponse = id_reponse;
    }

    public String getId_reclamation() {
        return id_reclamation;
    }

    public void setId_reclamation(String id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

