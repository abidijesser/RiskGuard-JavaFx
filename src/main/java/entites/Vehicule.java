package entites;

public class Vehicule {
    private int id;
    private String nom;
    private String prenom;
    private String cin;
    private String typeVehicule;
    private String matricule;
    private String lieu;
    private String date;
    private String description;
    private String image;

    // Constructor
    public Vehicule(String nom, String prenom, String cin, String typeVehicule, String matricule, String lieu, String date, String description, String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.typeVehicule = typeVehicule;
        this.matricule = matricule;
        this.lieu = lieu;
        this.date = date;
        this.description = description;
        this.image = image;
    }

    public Vehicule() {

    }

    // Getters and setters
    public int getId() {
        return id;
    }
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

    public String getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(String typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

