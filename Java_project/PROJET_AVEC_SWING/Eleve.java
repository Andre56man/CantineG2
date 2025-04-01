public class Eleve {
    private String nom;
    private String prenom;
    private String numeroEtudiant;

    public Eleve(String nom, String prenom, String numeroEtudiant) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroEtudiant = numeroEtudiant;
    }

    public String toString() {
        return numeroEtudiant + " - " + prenom + " " + nom;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public String toFileFormat() {
        return nom + "|" + prenom + "|" + numeroEtudiant;
    }

    public static Eleve fromFileFormat(String line) {
        String[] parts = line.split("\\|");
        return new Eleve(parts[0], parts[1], parts[2]);
    }
}