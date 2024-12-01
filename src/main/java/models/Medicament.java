package models;

import java.util.Date;

public class Medicament {
    private int id;
    private int id_pharmacien;
    private String nom;
    private Date dateProd;
    private Date dateExp;
    private int prix;
    private String description;
    private Boolean disponibilite;
    private Pharmacien pharmacien;

    public Medicament(String nom, Date dateProd, Date dateExp, int prix, String description, Boolean disponibilite , int id_pharmacien) {
        this.nom = nom;
        this.dateProd = dateProd;
        this.dateExp = dateExp;
        this.prix = prix;
        this.description = description;
        this.disponibilite = disponibilite;
        this.id_pharmacien= id_pharmacien;
    }
    public Medicament(int id,String nom, Date dateProd, Date dateExp, int prix, String description, Boolean disponibilite , int id_pharmacien) {
        this.id = id;
        this.nom = nom;
        this.dateProd = dateProd;
        this.dateExp = dateExp;
        this.prix = prix;
        this.description = description;
        this.disponibilite = disponibilite;
        this.id_pharmacien= id_pharmacien;
    }
    public Medicament(int id, int id_pharmacien, String nom, Date dateProd, Date dateExp, int prix, String description, Boolean disponibilite) {
        this.id = id;
        this.id_pharmacien = id_pharmacien;
        this.nom = nom;
        this.dateProd = dateProd;
        this.dateExp = dateExp;
        this.prix = prix;
        this.description = description;
        this.disponibilite = disponibilite;
    }

    public Medicament(int id_pharmacien, String nom, Date dateProd, Date dateExp, int prix, String description, Boolean disponibilite) {
        this.id_pharmacien = id_pharmacien;
        this.nom = nom;
        this.dateProd = dateProd;
        this.dateExp = dateExp;
        this.prix = prix;
        this.description = description;
        this.disponibilite = disponibilite;
    }
    public Medicament(String nom, Date dateProd, Date dateExp, int prix, String description, Boolean disponibilite) {
        this.nom = nom;
        this.dateProd = dateProd;
        this.dateExp = dateExp;
        this.prix = prix;
        this.description = description;
        this.disponibilite = disponibilite;
    }
    public Medicament() {
        this.pharmacien = new Pharmacien();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_pharmacien() {
        return id_pharmacien;
    }

    public void setId_pharmacien(int id_pharmacien) {
        this.id_pharmacien = id_pharmacien;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateProd() {
        return dateProd;
    }

    public void setDateProd(Date dateProd) {
        this.dateProd = dateProd;
    }

    public Date getDateExp() {
        return dateExp;
    }

    public void setDateExp(Date dateExp) {
        this.dateExp = dateExp;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", id_pharmacien=" + id_pharmacien +
                ", nom='" + nom + '\'' +
                ", dateProd=" + dateProd +
                ", dateExp=" + dateExp +
                ", prix=" + prix +
                ", description='" + description + '\'' +
                ", disponibilite=" + disponibilite +
                '}';
    }
}