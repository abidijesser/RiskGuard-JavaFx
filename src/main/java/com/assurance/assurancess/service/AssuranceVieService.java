package com.assurance.assurancess.service;






import com.assurance.assurancess.entities.Assurancevie;
import com.assurance.assurancess.utilities.Myconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssuranceVieService {

    private Connection cnx;

    public AssuranceVieService() {
        cnx = Myconnection.getInstance().getCnx();
    }

    public void addAssuranceVie(Assurancevie assurancevie) {
        String query = "INSERT INTO Assurancevie (datedebut, periodevalidation, salaireclient, fichedepaie, reponse, assurance_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setDate(1, new java.sql.Date(assurancevie.getDatedebut().getTime()));
            pst.setString(2, assurancevie.getPeriodevalidation());
            pst.setFloat(3, assurancevie.getSalaireclient());
            pst.setString(4, assurancevie.getFichedepaie());
            pst.setString(5, assurancevie.getReponse());
            pst.setLong(6, assurancevie.getAssurance().getId());

            pst.executeUpdate();
            System.out.println("Assurance vie ajoutée avec succès");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'assurance vie : " + ex.getMessage());
        }
    }

    public void updateAssuranceVie(Assurancevie assurancevie) {
        if (assurancevie != null) {
            String query = "UPDATE Assurancevie SET datedebut=?, periodevalidation=?, salaireclient=?, fichedepaie=?, reponse=? WHERE id=?";
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setDate(1, new java.sql.Date(assurancevie.getDatedebut().getTime()));
                pst.setString(2, assurancevie.getPeriodevalidation());
                pst.setFloat(3, assurancevie.getSalaireclient());
                pst.setString(4, assurancevie.getFichedepaie());
                pst.setString(5, assurancevie.getReponse());
                pst.setLong(6, assurancevie.getId());

                pst.executeUpdate();
                System.out.println("Assurance vie mise à jour avec succès");
            } catch (SQLException ex) {
                System.err.println("Erreur lors de la mise à jour de l'assurance vie : " + ex.getMessage());
            }
        } else {
            System.err.println("La mise à jour de l'assurance vie a échoué : Assurancevie est null.");
        }
    }


    public void deleteAssuranceVie(long assuranceVieId) {
        String query = "DELETE FROM Assurancevie WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setLong(1, assuranceVieId);

            pst.executeUpdate();
            System.out.println("Assurance vie supprimée avec succès");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'assurance vie : " + ex.getMessage());
        }
    }

    public List<Assurancevie> getAllAssuranceVies() {
        List<Assurancevie> assuranceVies = new ArrayList<>();
        String query = "SELECT * FROM Assurancevie";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Assurancevie assurancevie = new Assurancevie();
                    assurancevie.setId(rs.getLong("id"));
                    assurancevie.setDatedebut(rs.getDate("datedebut"));
                    assurancevie.setPeriodevalidation(rs.getString("periodevalidation"));
                    assurancevie.setSalaireclient(rs.getFloat("salaireclient"));
                    assurancevie.setFichedepaie(rs.getString("fichedepaie"));
                    assurancevie.setReponse(rs.getString("reponse"));

                    // You may also need to set the associated assurance object here

                    assuranceVies.add(assurancevie);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des assurances vie : " + ex.getMessage());
        }
        return assuranceVies;
    }

    public Assurancevie getAssuranceVieById(long assuranceVieId) {
        String query = "SELECT * FROM Assurancevie WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setLong(1, assuranceVieId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Assurancevie assurancevie = new Assurancevie();
                    assurancevie.setId(rs.getLong("id"));
                    assurancevie.setDatedebut(rs.getDate("datedebut"));
                    assurancevie.setPeriodevalidation(rs.getString("periodevalidation"));
                    assurancevie.setSalaireclient(rs.getFloat("salaireclient"));
                    assurancevie.setFichedepaie(rs.getString("fichedepaie"));
                    assurancevie.setReponse(rs.getString("reponse"));

                    // You may also need to set the associated assurance object here

                    return assurancevie;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération de l'assurance vie : " + ex.getMessage());
        }
        return null;
    }

    public List<Assurancevie> getAllAssuranceVies(String sortBy) {
        List<Assurancevie> assuranceVies = new ArrayList<>();
        String query = "SELECT * FROM Assurancevie ORDER BY ";
        switch(sortBy) {
            case "salaire":
                query += "salaireclient";
                break;
            case "datedebut":
                query += "datedebut";
                break;
            default:
                query += "id";
        }
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Assurancevie assurancevie = new Assurancevie();
                    assurancevie.setId(rs.getLong("id"));
                    assurancevie.setDatedebut(rs.getDate("datedebut"));
                    assurancevie.setPeriodevalidation(rs.getString("periodevalidation"));
                    assurancevie.setSalaireclient(rs.getFloat("salaireclient"));
                    assurancevie.setFichedepaie(rs.getString("fichedepaie"));
                    assurancevie.setReponse(rs.getString("reponse"));
                    assuranceVies.add(assurancevie);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des assurances vie : " + ex.getMessage());
        }
        return assuranceVies;
    }


    public List<Map<String, Object>> getTopThreeSalaireClients() {
        List<Map<String, Object>> topThreeSalaireClients = new ArrayList<>();
        String query = "SELECT av.assurance_id, a.nomdupack, av.salaireclient " +
                "FROM Assurancevie av " +
                "INNER JOIN Assurance a ON av.assurance_id = a.id " +
                "ORDER BY av.salaireclient DESC " +
                "LIMIT 3";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> topSalaireClient = new HashMap<>();
                    topSalaireClient.put("assurance_id", rs.getLong("assurance_id"));
                    topSalaireClient.put("assurance_nom", rs.getString("nomdupack"));
                    topSalaireClient.put("salaireclient", rs.getFloat("salaireclient"));
                    topThreeSalaireClients.add(topSalaireClient);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des trois salaires les plus élevés : " + ex.getMessage());
        }
        return topThreeSalaireClients;
    }

}
