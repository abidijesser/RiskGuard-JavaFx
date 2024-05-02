package com.assurance.assurancess.service;






import com.assurance.assurancess.entities.Assurancevehicule;
import com.assurance.assurancess.utilities.Myconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssuranceVehiculeService {

    private Connection cnx;

    public AssuranceVehiculeService() {
        cnx = Myconnection.getInstance().getCnx();
    }

    public void addAssuranceVehicule(Assurancevehicule assurancevehicule) {
        String query = "INSERT INTO Assurancevehicule (marque, modele, matricule, datedebut, periodedevalidation, image, assurance_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, assurancevehicule.getMarque());
            pst.setString(2, assurancevehicule.getModele());
            pst.setString(3, assurancevehicule.getMatricule());
            pst.setDate(4, new java.sql.Date(assurancevehicule.getDatedebut().getTime()));
            pst.setString(5, assurancevehicule.getPeriodedevalidation());
            pst.setString(6, assurancevehicule.getImage());
            pst.setLong(7, assurancevehicule.getAssurance().getId());

            pst.executeUpdate();
            System.out.println("Assurance véhicule ajoutée avec succès");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'assurance véhicule : " + ex.getMessage());
        }
    }

    public void updateAssuranceVehicule(Assurancevehicule assurancevehicule) {
        if (assurancevehicule != null) {
            String query = "UPDATE Assurancevehicule SET marque=?, modele=?, matricule=?, datedebut=?, periodedevalidation=?, image=? WHERE id=?";
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, assurancevehicule.getMarque());
                pst.setString(2, assurancevehicule.getModele());
                pst.setString(3, assurancevehicule.getMatricule());
                pst.setDate(4, new java.sql.Date(assurancevehicule.getDatedebut().getTime()));
                pst.setString(5, assurancevehicule.getPeriodedevalidation());
                pst.setString(6, assurancevehicule.getImage());
                pst.setLong(7, assurancevehicule.getId());

                pst.executeUpdate();
                System.out.println("Assurance véhicule mise à jour avec succès");
            } catch (SQLException ex) {
                System.err.println("Erreur lors de la mise à jour de l'assurance véhicule : " + ex.getMessage());
            }
        } else {
            System.err.println("assuranceVehicule is null.");
        }
    }

    public void deleteAssuranceVehicule(long assuranceVehiculeId) {
        String query = "DELETE FROM Assurancevehicule WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setLong(1, assuranceVehiculeId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Assurance véhicule supprimée avec succès");
            } else {
                System.out.println("Aucune assurance véhicule trouvée avec l'ID spécifié.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'assurance véhicule : " + ex.getMessage());
        }
    }
    public List<Assurancevehicule> getAllAssuranceVehicules() {
        List<Assurancevehicule> assuranceVehicules = new ArrayList<>();
        String query = "SELECT * FROM Assurancevehicule";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Assurancevehicule assurancevehicule = new Assurancevehicule();
                    assurancevehicule.setId(rs.getLong("id"));
                    assurancevehicule.setMarque(rs.getString("marque"));
                    assurancevehicule.setModele(rs.getString("modele"));
                    assurancevehicule.setMatricule(rs.getString("matricule"));
                    assurancevehicule.setDatedebut(rs.getDate("datedebut"));
                    assurancevehicule.setPeriodedevalidation(rs.getString("periodedevalidation"));
                    assurancevehicule.setImage(rs.getString("image"));

                    // You may also need to set the associated assurance object here

                    assuranceVehicules.add(assurancevehicule);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des assurances véhicules : " + ex.getMessage());
        }
        return assuranceVehicules;
    }

    public Assurancevehicule getAssuranceVehiculeById(long assuranceVehiculeId) {
        String query = "SELECT * FROM Assurancevehicule WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setLong(1, assuranceVehiculeId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Assurancevehicule assurancevehicule = new Assurancevehicule();
                    assurancevehicule.setId(rs.getLong("id"));
                    assurancevehicule.setMarque(rs.getString("marque"));
                    assurancevehicule.setModele(rs.getString("modele"));
                    assurancevehicule.setMatricule(rs.getString("matricule"));
                    assurancevehicule.setDatedebut(rs.getDate("datedebut"));
                    assurancevehicule.setPeriodedevalidation(rs.getString("periodedevalidation"));
                    assurancevehicule.setImage(rs.getString("image"));

                    // You may also need to set the associated assurance object here

                    return assurancevehicule;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération de l'assurance véhicule : " + ex.getMessage());
        }
        return null;
    }
}
