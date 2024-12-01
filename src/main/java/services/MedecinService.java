package services;

import models.Medecin;
import models.Patient;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinService implements IService<Medecin> {
    private Connection cnx;
    public MedecinService(){
        cnx= MyDataBase.getInstance().getConnection();
    }


    @Override
    public void add(Medecin medecin) {
        String query = "INSERT INTO medecin (nom, prenom, n_tel, email, password, adresse, specialite, hdebut, hfin, role, genre, is_blocked) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, medecin.getNom());
            ps.setString(2, medecin.getPrenom());
            ps.setInt(3, medecin.getN_tel());
            ps.setString(4, medecin.getEmail());
            ps.setString(5, medecin.getPassword());
            ps.setString(6, medecin.getAdresse());
            ps.setString(7, medecin.getSpecialite());
            ps.setTime(8, medecin.getHdebut());
            ps.setTime(9, medecin.getHfin());
            ps.setString(10, medecin.getRole());
            ps.setString(11, medecin.getGenre());
            ps.setBoolean(12, medecin.isIs_blocked());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le médecin a été ajouté avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout du médecin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Medecin medecin, int id) {

    }

    @Override
    public void delete(int id) {

    }
    @Override
    public Medecin getById(int id) {
        String req = "SELECT * FROM medecin WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Supposons que id_muni est le nom de la colonne contenant l'ID de Muni dans la table end_user
                int id_muni = rs.getInt("id_muni");
                // Vous devez récupérer les détails de Muni en utilisant son ID ici
                return new Medecin(id);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Medecin getById1(int id) {
        String req = "SELECT * FROM medecin WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Récupérer les attributs du médecin depuis la base de données
                // Vous pouvez également récupérer les attributs liés à l'objet Admin si nécessaire
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int n_tel = rs.getInt("n_tel");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String adresse = rs.getString("adresse");
                String specialite = rs.getString("specialite");
                Time hdebut = rs.getTime("hdebut");
                Time hfin = rs.getTime("hfin");
                String role = rs.getString("role");
                String genre = rs.getString("genre");
                boolean is_blocked = rs.getBoolean("is_blocked");

                // Créer un objet Medecin avec les données récupérées
                return new Medecin(nom, prenom, n_tel, email, password, adresse, specialite, hdebut, hfin, role, genre, is_blocked);
            } else {
                // Aucun médecin trouvé avec cet ID
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Medecin> getAll() {
        return null;
    }


    public Medecin getMedecinById2(int id) {
        Medecin medecin = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM medecin WHERE id = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les informations du médecin depuis le résultat de la requête
                String nom = resultSet.getString("nom");
                String email = resultSet.getString("email");

                // Créer une instance de l'entité Medecin
                medecin = new Medecin(nom, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources (ResultSet, PreparedStatement, etc.)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return medecin;
    }

    public Medecin getMedecinById(int id) {
        Medecin medecin = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM medecin WHERE id = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les informations du médecin depuis le résultat de la requête
                int medecinId = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String specialite = resultSet.getString("specialite");

                // Créer une instance de l'entité Medecin
                medecin = new Medecin(medecinId, nom, specialite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources (ResultSet, PreparedStatement, etc.)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return medecin;
    }

    // Méthode pour récupérer les noms des médecins par spécialité depuis la base de données
    public List<String> getMedecinsBySpecialite(String specialite) {
        List<String> medecins = new ArrayList<>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Requête SQL pour récupérer les noms des médecins par spécialité
        String query = "SELECT nom FROM medecin WHERE specialite = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setString(1, specialite);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nomMedecin = resultSet.getString("nom");
                medecins.add(nomMedecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources (ResultSet et PreparedStatement seulement)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return medecins;
    }

    public int getIdMedecinByNom(String nom) {
        int idMedecin = -1; // Valeur par défaut si le médecin n'est pas trouvé

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Requête SQL pour récupérer l'ID du médecin par son nom
        String query = "SELECT id FROM medecin WHERE nom = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setString(1, nom);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                idMedecin = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return idMedecin;
    }

    public String getMedecinNomById(int id) {
        String nom = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT nom FROM medecin WHERE id = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                // Récupérer le nom du médecin depuis le résultat de la requête
                nom = resultSet.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nom;
    }

    public boolean verifierUtilisateurMed(String email, String mdp) {
        boolean utilisateurValide = false;

        String sql = "SELECT * FROM medecin WHERE email = ?";
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
    public boolean verifierMotDePasse(int idMedecin, String password) {
        String sql = "SELECT * FROM medecin WHERE id=? AND password=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setInt(1, idMedecin);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            // Si le résultat de la requête est non vide, cela signifie que le mot de passe est correct
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifierMedecin(Medecin medecin) {
        String sql = "UPDATE medecin SET nom=?, prenom=?, n_tel=?, email=?, password=?, adresse=?, hdebut=?, hfin=? WHERE id=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setString(1, medecin.getNom());
            pst.setString(2, medecin.getPrenom());
            pst.setInt(3, medecin.getN_tel());
            pst.setString(4, medecin.getEmail());
            pst.setString(5, medecin.getPassword());
            pst.setString(6, medecin.getAdresse());
            pst.setString(7, String.valueOf(medecin.getHdebut()));
            pst.setString(8, String.valueOf(medecin.getHfin()));
            pst.setInt(9, medecin.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Échec de la modification du médecin.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}