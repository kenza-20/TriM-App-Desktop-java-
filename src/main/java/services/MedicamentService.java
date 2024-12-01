package services;

import models.Medicament;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicamentService implements IService<Medicament>{
    private Connection cnx;
    private PreparedStatement pst;
    public MedicamentService(){
        cnx= MyDataBase.getInstance().getConnection();
    }


    public void addMedicament(Medicament medicament , int id) {
        String sql="insert into medicament(id_pharmacien_id,nom,dateprod,dateexp,prix,description,disponibilite) values (?,?,?,?,?,?,?)";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1,id);
            pst.setString(2,medicament.getNom());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateProdString = dateFormat.format(medicament.getDateProd());
            String dateExpString = dateFormat.format(medicament.getDateExp());
            pst.setString(3, dateProdString);
            pst.setString(4, dateExpString);

            pst.setInt(5,medicament.getPrix());
            pst.setString(6,medicament.getDescription());
            pst.setBoolean(7,medicament.getDisponibilite());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Medicament medicament) {

    }

    @Override
    public void update(Medicament medicament, int id) {
        String sql = "UPDATE medicament SET nom=?, dateprod=?, dateexp=?, prix=?, description=?, disponibilite=? WHERE id_pharmacien_id=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, medicament.getNom());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateProdString = dateFormat.format(medicament.getDateProd());
            String dateExpString = dateFormat.format(medicament.getDateExp());
            pst.setString(2, dateProdString);
            pst.setString(3, dateExpString);
            pst.setInt(4, medicament.getPrix());
            pst.setString(5, medicament.getDescription());
            pst.setBoolean(6, medicament.getDisponibilite());
            pst.setInt(7, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM medicament WHERE id=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("La suppression du médicament a échoué, aucun enregistrement n'a été supprimé.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Une erreur s'est produite lors de la suppression du médicament : " + e.getMessage());
        }
    }


    @Override
    public Medicament getById(int id) {
        String sql = "SELECT * FROM medicament WHERE id=?";
        Medicament medicament = null;
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");

                String dateProdString = rs.getString("dateprod");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateprod = dateFormat.parse(dateProdString);

                String dateExpString = rs.getString("dateexp"); // Corrected
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                Date dateexp = dateFormat1.parse(dateExpString); // Corrected

                int prix = rs.getInt("prix");
                String description = rs.getString("description");
                Boolean disponibilite = Boolean.valueOf(rs.getString("disponibilite"));
                int idPharmacien = rs.getInt("id_pharmacien_id");

                medicament = new Medicament(nom, dateprod, dateexp, prix, description, Boolean.valueOf(disponibilite), idPharmacien);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return medicament;
    }

    @Override
    public List<Medicament> getAll() {
        return List.of();
    }


    public List<Medicament> getAllById(int idPharmacien) {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament where id_pharmacien_id = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, idPharmacien);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");

                String dateProdString = rs.getString("dateprod");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateprod = dateFormat.parse(dateProdString);

                String dateExpString = rs.getString("dateexp"); // Corrected
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                Date dateexp = dateFormat1.parse(dateExpString); // Corrected

                int prix = rs.getInt("prix");
                String description = rs.getString("description");
                boolean disponibilite = Boolean.parseBoolean(rs.getString("disponibilite"));
                Medicament medicament = new Medicament(id,nom, dateprod, dateexp, prix, description, Boolean.valueOf(disponibilite), idPharmacien);
                medicaments.add(medicament);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }

    public int getMaxMedicamentCount(int idPharmacien) {
        String sql = "SELECT COUNT(*) AS maxMedicamentCount FROM medicament WHERE id_pharmacien_id = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, idPharmacien);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("maxMedicamentCount");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération du nombre maximal de médicaments : " + e.getMessage());
        }
        return 0; // Retourne 0 si aucun résultat n'est trouvé
    }

}