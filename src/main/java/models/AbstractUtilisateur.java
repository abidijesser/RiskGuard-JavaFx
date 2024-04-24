package models;

sealed public abstract class AbstractUtilisateur permits Client, Admin {
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String telephone;
    private java.time.LocalDate dateDeNaissance;

    public AbstractUtilisateur( String nom, String prenom, String email,
        String motDePasse, String telephone, java.time.LocalDate dateDeNaissance) {

        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.dateDeNaissance = dateDeNaissance;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public java.time.LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(java.time.LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }
}

