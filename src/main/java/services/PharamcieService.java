package services;

import models.Pharmacie;
import models.Pharmacien;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharamcieService implements IService<Pharmacie> {
    private Connection cnx;
    private PreparedStatement pst;
    private PharmacienService pharmacienService = new PharmacienService();

    public PharamcieService () {cnx= MyDataBase.getInstance().getConnection();}
    @Override
    public void add(Pharmacie pharmacie) {
        String insert = "INSERT INTO pharmacie(nom,ntel,adresse,type) VALUES (?,?,?,?)";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            pst = cnx.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, pharmacie.getNom());
            pst.setInt(2, pharmacie.getNtel());
            pst.setString(3, pharmacie.getAdresse());
            pst.setString(4, pharmacie.getType());

            pst.executeUpdate();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idPharmacie = generatedKeys.getInt(1);
                int idPharmacien = 1;
                Pharmacien pharmacien = pharmacienService.getById(idPharmacien);
                pharmacien.setId_pharmacie(idPharmacie);
                pharmacienService.update(pharmacien);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Pharmacie pharmacie, int id) {
        String updateQuery = "UPDATE pharmacie SET nom = ?,  ntel = ?, adresse = ?, type = ? WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            pst = cnx.prepareStatement(updateQuery);
            pst.setString(1, pharmacie.getNom());
            pst.setInt(2, pharmacie.getNtel());
            pst.setString(3, pharmacie.getAdresse());
            pst.setString(4, pharmacie.getType());
            pst.setInt(5, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM pharmacie WHERE id=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pharmacie getById(int id) {
        String sql = "SELECT * FROM pharmacie WHERE id=?";
        Pharmacie pharmacie = null;
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                int ntel = rs.getInt("ntel");
                String adresse = rs.getString("adresse");
                String type = rs.getString("type");

                pharmacie = new Pharmacie(nom,  ntel,adresse, type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pharmacie;
    }

    @Override
    public List<Pharmacie> getAll() {
        List<Pharmacie> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacie";
        try {
            pst = cnx.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                int ntel = rs.getInt("ntel");
                String type = rs.getString("type");
                String loc = rs.getString("loc");
                Pharmacie pharmacie = new Pharmacie(id, nom,ntel,adresse, type, loc);
                pharmacies.add(pharmacie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pharmacies;
    }

    public int getPharmacieIdByName(String nom) {
        String sql = "SELECT id FROM pharmacie WHERE nom = ?";
        int idPharmacie = 0;

        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, nom);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                idPharmacie = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return idPharmacie;
    }




}