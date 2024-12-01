package models;

public class Pharmacien {
    private int id;
    private int id_admin;
    private int id_pharmacie;
    private String nom;
    private String prenom;
    private int ntel;
    private String email;
    private String password;
    private String role;
    private String genre;
    private boolean is_blocked;

    public Pharmacien() {
    }

    public Pharmacien(String nom, String prenom, int numTel, String email, String password, String role, String genre, boolean isBlocked) {
        this.nom = nom;
        this.prenom = prenom;
        this.ntel = numTel;
        this.email = email;
        this.password = password;
        this.role = role;
        this.genre = genre;
        this.is_blocked = isBlocked;
    }
    public Pharmacien(int id, int id_admin, int id_pharmacie, String nom, String prenom, int ntel, String email, String password, String role, String genre, Boolean is_blocked) {
        this.id = id;
        this.id_admin = id_admin;
        this.id_pharmacie = id_pharmacie;
        this.nom = nom;
        this.prenom = prenom;
        this.ntel = ntel;
        this.email = email;
        this.password = password;
        this.role = role;
        this.genre = genre;
        this.is_blocked = is_blocked;
    }

    public Pharmacien(int id_admin, int id_pharmacie, String nom, String prenom, int ntel, String email, String password, String role, String genre, Boolean is_blocked) {
        this.id_admin = id_admin;
        this.id_pharmacie = id_pharmacie;
        this.nom = nom;
        this.prenom = prenom;
        this.ntel = ntel;
        this.email = email;
        this.password = password;
        this.role = role;
        this.genre = genre;
        this.is_blocked = is_blocked;
    }

    public Pharmacien(int id, String nom, String prenom, int ntel, String email, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.ntel = ntel;
        this.email = email;
        this.password = password;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public int getId_pharmacie() {
        return id_pharmacie;
    }

    public void setId_pharmacie(int id_pharmacie) {
        this.id_pharmacie = id_pharmacie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNtel() {
        return ntel;
    }

    public void setNtel(int ntel) {
        this.ntel = ntel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }
}
