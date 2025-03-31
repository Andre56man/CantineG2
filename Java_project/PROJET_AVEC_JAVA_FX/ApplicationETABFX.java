import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class ApplicationETABFX extends Application {
    private Stage stage;
    private Scene sceneConnexion, sceneMenuPrincipal, sceneGestionMenus, sceneGestionEleves, sceneGestionUtilisateurs;
    private TextField champIdentifiant;
    private PasswordField champMotDePasse;
    
    private ArrayList<String> menu = new ArrayList<>();
    private ArrayList<String> eleves = new ArrayList<>();
    private ArrayList<String> utilisateurs = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("ETAB v1.1");

        sceneConnexion = creerSceneConnexion();
        sceneMenuPrincipal = creerSceneMenuPrincipal();
        sceneGestionMenus = creerSceneGestion("GESTION DES MENUS", menu);
        sceneGestionEleves = creerSceneGestion("GESTION DES ÉLÈVES", eleves);
        sceneGestionUtilisateurs = creerSceneGestion("GESTION DES UTILISATEURS", utilisateurs);

        stage.setScene(sceneConnexion);
        stage.show();
    }

    private Scene creerSceneConnexion() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        Label titre = new Label("BIENVENUE DANS L'APPLICATION ETAB v1.1");
        champIdentifiant = new TextField();
        champMotDePasse = new PasswordField();

        Button boutonConnexion = new Button("Se connecter");
        boutonConnexion.setOnAction(e -> verifierConnexion());

        layout.getChildren().addAll(titre, new Label("Identifiant:"), champIdentifiant, new Label("Mot de passe:"), champMotDePasse, boutonConnexion);
        return new Scene(layout, 400, 300);
    }

    private void verifierConnexion() {
        String identifiant = champIdentifiant.getText();
        String motDePasse = champMotDePasse.getText();

        if (identifiant.equals("admin") && motDePasse.equals("1234")) {
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie !");
            stage.setScene(sceneMenuPrincipal);
        } else {
            showAlert(Alert.AlertType.ERROR, "Identifiant ou mot de passe incorrect.");
        }
    }

    private Scene creerSceneMenuPrincipal() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        Label titre = new Label("MENU PRINCIPAL");
        Button btnEleves = new Button("Gestion des élèves");
        btnEleves.setOnAction(e -> stage.setScene(sceneGestionEleves));

        Button btnMenus = new Button("Gestion des menus");
        btnMenus.setOnAction(e -> stage.setScene(sceneGestionMenus));

        Button btnUtilisateurs = new Button("Gestion des utilisateurs");
        btnUtilisateurs.setOnAction(e -> stage.setScene(sceneGestionUtilisateurs));

        Button btnQuitter = new Button("Quitter");
        btnQuitter.setOnAction(e -> stage.close());

        layout.getChildren().addAll(titre, btnEleves, btnMenus, btnUtilisateurs, btnQuitter);
        return new Scene(layout, 400, 300);
    }

    private Scene creerSceneGestion(String titre, ArrayList<String> liste) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        Label labelTitre = new Label(titre);
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(liste);

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.setOnAction(e -> {
            String nom = showInputDialog("Entrez le nom :");
            if (nom != null && !nom.trim().isEmpty()) {
                liste.add(nom);
                listView.getItems().setAll(liste);
                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi !");
            }
        });

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> {
            String nom = showInputDialog("Entrez le nom à supprimer :");
            if (liste.remove(nom)) {
                listView.getItems().setAll(liste);
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Nom introuvable.");
            }
        });

        Button btnModifier = new Button("Modifier");
        btnModifier.setOnAction(e -> {
            String ancienNom = showInputDialog("Entrez le nom à modifier :");
            if (liste.contains(ancienNom)) {
                String nouveauNom = showInputDialog("Nouveau nom :");
                if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
                    liste.set(liste.indexOf(ancienNom), nouveauNom);
                    listView.getItems().setAll(liste);
                    showAlert(Alert.AlertType.INFORMATION, "Modification réussie !");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Nom introuvable.");
            }
        });

        Button btnLister = new Button("Lister");
        btnLister.setOnAction(e -> {
            if (liste.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Aucun élément trouvé.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Liste : " + String.join(", ", liste));
            }
        });

        Button btnDernierAjoute = new Button("Obtenir le dernier ajouté");
        btnDernierAjoute.setOnAction(e -> {
            if (!liste.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Dernier ajouté : " + liste.get(liste.size() - 1));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Aucun élément enregistré.");
            }
        });

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> stage.setScene(sceneMenuPrincipal));

        layout.getChildren().addAll(labelTitre, listView, btnAjouter, btnSupprimer, btnModifier, btnLister, btnDernierAjoute, btnRetour);
        return new Scene(layout, 400, 400);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait().orElse(null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
