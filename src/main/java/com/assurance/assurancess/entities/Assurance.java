package com.assurance.assurancess.entities;
import java.util.ArrayList;
import java.util.List;
public class Assurance {
    public Assurance(String nomduPack, String promotionduPack, String description, String typeduPack) {
        this.nomdupack = nomduPack;
        this.promotiondupack = promotionduPack;
        this.description = description;
        this.typedupack = typeduPack;
    }
    public Assurance() {
    }

    @Override
    public String toString() {
        return "Assurance:" +
                ",\nnomdupack='" + nomdupack + '\'' +
                ",\npromotiondupack='" + promotiondupack + '\'' +
                ",\ndescription='" + description + '\'' +
                ",\ntypedupack='" + typedupack + '\''
                ;
    }


    private Long id;
    private String nomdupack;
    private String promotiondupack;
    private String description;
    private String typedupack;
    private List<Assurancevie> assurancevie = new ArrayList<>();
    private List<Assurancevehicule> assurancevehicule = new ArrayList<>();

    public Long getId() {
        return id;
    }


    public String getNomdupack() {
        return nomdupack;
    }

    public void setNomdupack(String nomdupack) {
        this.nomdupack = nomdupack;
    }

    public String getPromotiondupack() {
        return promotiondupack;
    }

    public void setPromotiondupack(String promotiondupack) {
        this.promotiondupack = promotiondupack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypedupack() {
        return typedupack;
    }

    public void setTypedupack(String typedupack) {
        this.typedupack = typedupack;
    }

    public List<Assurancevie> getAssurancevie() {
        return assurancevie;
    }

    public void setAssurancevie(List<Assurancevie> assurancevie) {
        this.assurancevie = assurancevie;
    }

    public List<Assurancevehicule> getAssurancevehicule() {
        return assurancevehicule;
    }

    public void setAssurancevehicule(List<Assurancevehicule> assurancevehicule) {
        this.assurancevehicule = assurancevehicule;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
