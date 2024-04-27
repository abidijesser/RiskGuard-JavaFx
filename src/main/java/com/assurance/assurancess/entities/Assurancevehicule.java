package com.assurance.assurancess.entities;
import java.util.Date;

public class Assurancevehicule {

    private Long id;
    private String marque;
    private String modele;
    private String matricule;
    private Date datedebut;
    private String periodedevalidation;
    private String image;
    private Assurance assurance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public String getPeriodedevalidation() {
        return periodedevalidation;
    }

    public void setPeriodedevalidation(String periodedevalidation) {
        this.periodedevalidation = periodedevalidation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    @Override
    public String toString() {
        return "Assurancevehicule{" +
                "id=" + id +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", matricule='" + matricule + '\'' +
                ", datedebut=" + datedebut +
                ", periodedevalidation='" + periodedevalidation + '\'' +
                ", image='" + image + '\'' +
                ", assurance=" + assurance +
                '}';
    }
}
