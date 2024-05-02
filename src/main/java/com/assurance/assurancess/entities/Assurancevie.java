package com.assurance.assurancess.entities;

import java.util.Date;

public class Assurancevie {


    private Long id;
    private Date datedebut;
    private String periodevalidation;
    private float salaireclient;
    private String fichedepaie;
    private String reponse;
    private Assurance assurance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public String getPeriodevalidation() {
        return periodevalidation;
    }

    public void setPeriodevalidation(String periodevalidation) {
        this.periodevalidation = periodevalidation;
    }

    public float getSalaireclient() {
        return salaireclient;
    }

    public void setSalaireclient(float salaireclient) {
        this.salaireclient = salaireclient;
    }

    public String getFichedepaie() {
        return fichedepaie;
    }

    public void setFichedepaie(String fichedepaie) {
        this.fichedepaie = fichedepaie;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }
    @Override
    public String toString() {
        return "Assurancevie{" +
                "\n datedebut=" + datedebut +
                ",\n periodevalidation=" + periodevalidation +
                ",\n salaireclient=" + salaireclient +
                ",\n fichedepaie=" + fichedepaie +
                ",\n reponse=" + reponse +
               // ",\n assurance=" + assurance +
                '}';
    }








}
