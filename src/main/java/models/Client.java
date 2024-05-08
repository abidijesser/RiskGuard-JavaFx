

package models;

final public class Client extends AbstractUtilisateur {
    public String cin;
    public String adresseDomicile;

    public Client(String nom, String prenom, String email, String motDePasse, String telephone ,
                  java.time.LocalDate dateDeNaissance, String adresseDomicile, String cin) {
        super(nom, prenom, email, motDePasse, telephone, dateDeNaissance);
        this.adresseDomicile = adresseDomicile;
        this.cin = cin;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAdresseDomicile() {
        return adresseDomicile;
    }

    public void setAdresseDomicile(String adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
    }
}









