package services;

import models.Chef_Lab;
import utils.MyDataBase;
import java.sql.*;

public class Chef_LabService {
    private Connection cnx;
    private PreparedStatement pst;

    public Chef_LabService(){
        cnx= MyDataBase.getInstance().getConnection();
    }
    public Chef_Lab getById(int id) {
        String sql = "SELECT * FROM chef_lab WHERE id=?";
        Chef_Lab chefLab = null;
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id_lab = rs.getInt("id_lab_id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int ntel = rs.getInt("ntel");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String genre = rs.getString("genre");
                String role = rs.getString("role");
                Boolean is_blocked = rs.getBoolean("is_blocked");

                chefLab = new Chef_Lab(id,id_lab, nom, prenom, ntel, email, password , genre, role, is_blocked);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chefLab;
    }

    public void update(Chef_Lab chefLab) {
        String sql = "UPDATE chef_lab SET id_lab_id = ? WHERE id = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, chefLab.getId_lab());
            pst.setInt(2, chefLab.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Chef_Lab getByIdLab(int idLab) {
        String sql = "SELECT * FROM chef_lab WHERE id_lab_id = ?";
        Chef_Lab chefLab = null;
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, idLab);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int ntel = rs.getInt("ntel");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String genre = rs.getString("genre");
                String role = rs.getString("role");
                Boolean is_blocked = rs.getBoolean("is_blocked");
                chefLab = new Chef_Lab(id, idLab, nom, prenom, ntel, email, password, genre, role, is_blocked);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chefLab;
    }

    public boolean verifierUtilisateur(String email, String mdp) {
        boolean utilisateurValide = false;

        String sql = "SELECT * FROM chef_lab WHERE email = ?";

        try{

            pst = cnx.prepareStatement(sql);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
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

    public void ajouterChefLab(Chef_Lab chefLab) {
        String query = "INSERT INTO chef_lab (nom, prenom, ntel, email, password, genre, role, is_blocked) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, chefLab.getNom());
            preparedStatement.setString(2, chefLab.getPrenom());
            preparedStatement.setInt(3, chefLab.getNtel());
            preparedStatement.setString(4, chefLab.getEmail());
            preparedStatement.setString(5, chefLab.getPassword());
            preparedStatement.setString(6, chefLab.getGenre());
            preparedStatement.setString(7, chefLab.getRole());
            preparedStatement.setBoolean(8, chefLab.getIs_blocked());

            // Exécuter la requête
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifierChefLab(Chef_Lab chefLab) {
        String sql = "UPDATE chef_lab SET nom = ?, prenom = ?, ntel = ?, email = ?, password = ? WHERE id = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, chefLab.getNom());
            pst.setString(2, chefLab.getPrenom());
            pst.setInt(3, chefLab.getNtel());
            pst.setString(4, chefLab.getEmail());
            pst.setString(5, chefLab.getPassword());
            pst.setInt(6, chefLab.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifierMotDePasse(int idChefLab, String password) {
        String sql = "SELECT * FROM chef_lab WHERE id=? AND password=?";
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




}
