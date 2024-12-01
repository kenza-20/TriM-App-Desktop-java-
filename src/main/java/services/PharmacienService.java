package services;

import models.Patient;
import models.Pharmacien;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PharmacienService implements IService<Pharmacien> {
    private Connection cnx;
    private PreparedStatement pst;

    public PharmacienService(){
        cnx= MyDataBase.getInstance().getConnection();
    }
    @Override
    public void add(Pharmacien pharmacien) {
        String query = "INSERT INTO pharmacien (nom, prenom, ntel, email, password, role, genre, is_blocked) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = cnx.prepareStatement(query);
            pst.setString(1, pharmacien.getNom());
            pst.setString(2, pharmacien.getPrenom());
            pst.setInt(3, pharmacien.getNtel());
            pst.setString(4, pharmacien.getEmail());
            pst.setString(5, pharmacien.getPassword());
            pst.setString(6, pharmacien.getRole());
            pst.setString(7, pharmacien.getGenre());
            pst.setBoolean(8, pharmacien.isIs_blocked());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions ici
        }
    }

    @Override
    public void update(Pharmacien pharmacien, int id) {



    }
    public void update(Pharmacien pharmacien) {
        String sql = "UPDATE pharmacien SET nom=?, prenom=?, ntel=?, email=? ,password=? ,id_pharmacie_id=? WHERE id=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, pharmacien.getNom());
            pst.setString(2, pharmacien.getPrenom());
            pst.setInt(3, pharmacien.getNtel());
            pst.setString(4, pharmacien.getEmail());
            pst.setString(5, pharmacien.getPassword());
            pst.setString(6, String.valueOf(pharmacien.getId_pharmacie()));
            pharmacien.setId(1);
            pst.setInt(7, pharmacien.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Pharmacien getById(int id) {
        String sql = "SELECT * FROM pharmacien WHERE id=?";
        Pharmacien pharmacien = null;
        // pharmacien
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id_admin = rs.getInt("id_admin_id");
                int id_pharmacie = rs.getInt("id_pharmacie_id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int ntel = rs.getInt("ntel");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String genre = rs.getString("genre");
                String role = rs.getString("role");
                Boolean is_blocked = rs.getBoolean("is_blocked");

                pharmacien = new Pharmacien(id,id_admin,id_pharmacie, nom, prenom, ntel, email, password , genre, role, is_blocked);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pharmacien;
    }

    public boolean verifierUtilisateurPha(String email, String mdp) {
        boolean utilisateurValide = false;

        String sql = "SELECT * FROM pharmacien WHERE email = ?";
        PreparedStatement statement = null;
        try{

            statement = cnx.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String mdpStocke = rs.getString("password");
                if (mdpStocke.equals(mdp)) {
                    utilisateurValide = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données
        }
        return utilisateurValide;
    }
    public Pharmacien getById3(int id) {
        String sql = "SELECT nom, prenom, ntel, email, password FROM pharmacien WHERE id=?";
        Pharmacien patient = null;

        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int ntel = rs.getInt("ntel");
                String email = rs.getString("email");
                String password = rs.getString("password");

                patient = new Pharmacien(id, nom, prenom, ntel, email, password);
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  patient;
    }
    public boolean verifierMotDePasse(int idChefLab, String password) {
        String sql = "SELECT * FROM pharmacien WHERE id=? AND password=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, idChefLab);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            // Si le résultat de la requête est non vide, cela signifie que le mot de passe est correct
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pharmacien> getAll() {
        return null;
    }
}
