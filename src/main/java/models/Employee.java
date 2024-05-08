package models;

public class Employee {


        private int id;
        private int idep;
        private String nom;
        private String email;
        private int salaire;



        // Getters and setters for the attributes...











    public Employee() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getIdep() {
        return idep;
    }

    public void setIdep(int idep) {
        this.idep = idep;
    }

    public  int getSalaire() {
        return salaire;
    }

    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


/*
    public employee(int id, int idep, int salaire, String nom, String email) {
        this.id = id;
        this.idep = idep;
        this.salaire = salaire;
        this.nom = nom;
        this.email = email;
    }*/
    public Employee(int id, int idep, String nom, String email, int salaire) {
        this.id = id;
        this.idep = idep;
        this.nom = nom;
        this.email = email;
        this.salaire = salaire;
    }

    @Override
    public String toString() {
        return "employee{" +
                "id=" + id +
                ", idep=" + idep +
                ", salaire=" + salaire +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Employee(int idepemp, String nomemp, String emailemp, float salaireemp) {
        this.idep = idep;
        this.salaire = salaire;
        this.nom = nom;
        this.email= email;
    }
    private Department department;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
