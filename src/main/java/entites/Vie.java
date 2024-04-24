package entites;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vie {

    // Attributes
    private int id;
    private String nom;
    private String prenom;
    private String cin;
    private String causededecess;
    private String dateDeces;
    private String identifiantDeLinformantt; // Updated attribute name

    // Constructors
    public Vie(String nom, String prenom, String cin, String causededecess, String dateDeces, String identifiantDeLinformantt) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.causededecess = causededecess;
        this.dateDeces = dateDeces;
        this.identifiantDeLinformantt = identifiantDeLinformantt;
    }




    // Getters and Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getCausededecess() {
        return causededecess;
    }

    public void setCausededecess(String causededecess) {
        this.causededecess = causededecess;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public String getIdentifiantDeLinformantt() {
        return identifiantDeLinformantt;
    }

    public void setIdentifiantDeLinformantt(String identifiantDeLinformantt) {
        this.identifiantDeLinformantt = identifiantDeLinformantt;
    }




}
