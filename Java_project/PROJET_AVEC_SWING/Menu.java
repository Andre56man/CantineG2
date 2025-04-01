public class Menu {
    private String nomPlat;
    private String description;
    private double prix;

    public Menu(String nomPlat, String description, double prix) {
        this.nomPlat = nomPlat;
        this.description = description;
        this.prix = prix;
    }

    public String toString() {
        return nomPlat + " - " + description + " (" + prix + ")";
    }

    public String getNomPlat() {
        return nomPlat;
    }

    public double getPrix() {
        return prix;
    }

    public void setNomPlat(String nomPlat) {
        this.nomPlat = nomPlat;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String toFileFormat() {
        return nomPlat + "|" + description + "|" + prix;
    }

    public static Menu fromFileFormat(String line) {
        String[] parts = line.split("\\|");
        return new Menu(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}