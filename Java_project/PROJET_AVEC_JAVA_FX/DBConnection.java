import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/applicationetab?serverTimezone=UTC";
        String username = "root";
        String password = "m1a2m3a4";

        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection(url, username, password);

            // Création de la requête SQL
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM utilisateurs"; // Récupérer toutes les lignes de la table 'utilisateurs'

            // Exécution de la requête et récupération des résultats
            ResultSet rs = stmt.executeQuery(query);

            // Parcourir les résultats
            while (rs.next()) {
                int id = rs.getInt("id");  // Récupérer l'ID de l'utilisateur
                String nom = rs.getString("nom");  // Récupérer le nom de l'utilisateur
                System.out.println("ID: " + id + ", Nom: " + nom);
            }

            // Fermer la connexion
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
