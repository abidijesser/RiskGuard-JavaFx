package models;

final public class Admin extends AbstractUtilisateur {
    public Float salaire;

    public Admin(String nom, String prenom, String email, String motDePasse, String telephone ,
                  java.time.LocalDate dateDeNaissance, Float salaire) {
        super(nom, prenom, email, motDePasse, telephone, dateDeNaissance);
        this.salaire = salaire;

    }

    // Getters and Setters
    public Float getSalaire() {
        return salaire;
    }

    public void setSalaire(Float salaire) {
        this.salaire = salaire;
    }
}

