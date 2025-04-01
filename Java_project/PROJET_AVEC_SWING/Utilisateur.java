public class Utilisateur {
    private String nom;
    private String prenom;
    private String login;

    public Utilisateur(String nom, String prenom, String login) {
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
    }

    public String toString() {
        return login + " - " + prenom + " " + nom;
    }

    public String getLogin() {
        return login;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String toFileFormat() {
        return nom + "|" + prenom + "|" + login;
    }

    public static Utilisateur fromFileFormat(String line) {
        String[] parts = line.split("\\|");
        return new Utilisateur(parts[0], parts[1], parts[2]);
    }
}