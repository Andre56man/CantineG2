import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ApplicationETABFXZY extends Application {
    private Stage stage;
    private Scene sceneConnexion, sceneMenuPrincipal, sceneGestionMenus, sceneGestionEleves, sceneGestionUtilisateurs;
    private TextField champIdentifiant;
    private PasswordField champMotDePasse;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private final double BUTTON_WIDTH = 150;
    private final double BUTTON_HEIGHT = 40;

    private ArrayList<String> menu = new ArrayList<>();
    private ArrayList<String> eleves = new ArrayList<>();
    private ArrayList<String> utilisateurs = new ArrayList<>();

    public Connection connection;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("ETAB v1.1");

        connectToDB();

        sceneConnexion = creerSceneConnexion();
        sceneMenuPrincipal = creerSceneMenuPrincipal();
        sceneGestionMenus = creerSceneGestion("GESTION DES MENUS", menu);
        sceneGestionEleves = creerSceneGestion("GESTION DES ÉLÈVES", eleves);
        sceneGestionUtilisateurs = creerSceneGestion("GESTION DES UTILISATEURS", utilisateurs);

        stage.setScene(sceneConnexion);
        stage.setWidth(600);
        stage.setHeight(400);
        stage.show();
    }

    private void connectToDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/applicationetab?serverTimezone=UTC";
            String username = "root";
            String password = "m1a2m3a4";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion à la BD réussie !");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion à la BD : " + ex.getMessage());
        }
    }

    private Scene creerSceneConnexion() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: beige; -fx-alignment: center;"); //

        Label titre = new Label("BIENVENUE DANS L'APPLICATION ETAB v1.1");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;"); //

        champIdentifiant = new TextField();
        champMotDePasse = new PasswordField();

        Button boutonConnexion = new Button("Se connecter");
        boutonConnexion.setStyle("-fx-background-color: red; -fx-text-fill: white;"); //
        boutonConnexion.setOnAction(e -> verifierConnexion());

        layout.getChildren().addAll(titre, 
                                    new Label("Identifiant:"), champIdentifiant, 
                                    new Label("Mot de passe:"), champMotDePasse, 
                                    boutonConnexion);
        return new Scene(layout, 1600, 1000);
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
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: beige; -fx-alignment: center;");

        Label titre = new Label("MENU PRINCIPAL");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: red;");

        Button btnEleves = new Button("Gestion des élèves");
        btnEleves.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnEleves.setOnAction(e -> stage.setScene(sceneGestionEleves));
        btnEleves.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT); //BUTTON_WIDTH BUTTON_HEIGHT

        Button btnMenus = new Button("Gestion des menus");
        btnMenus.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnMenus.setOnAction(e -> stage.setScene(sceneGestionMenus));
        btnMenus.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);  //BUTTON_WIDTH BUTTON_HEIGHT

        Button btnUtilisateurs = new Button("Gestion des utilisateurs");
        btnUtilisateurs.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnUtilisateurs.setOnAction(e -> stage.setScene(sceneGestionUtilisateurs));
        btnUtilisateurs.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);     //BUTTON_WIDTH BUTTON_HEIGHT


        Button btnQuitter = new Button("Quitter");
        btnQuitter.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnQuitter.setOnAction(e -> stage.close());
        btnQuitter.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);    //BUTTON_WIDTH BUTTON_HEIGHT

        layout.getChildren().addAll(titre, btnEleves, btnMenus, btnUtilisateurs, btnQuitter);
        return new Scene(layout, WIDTH, HEIGHT);    //WIDTH HEIGHT
    }

    private Scene creerSceneGestion(String titreMenu, ArrayList<String> liste) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: beige; -fx-alignment: center;");

        Label labelTitre = new Label(titreMenu);
        labelTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: red;");

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(liste);

        Button btnAjouter = creerBoutonAction("Ajouter", liste, listView);
        Button btnSupprimer = creerBoutonAction("Supprimer", liste, listView);
        Button btnModifier = creerBoutonAction("Modifier", liste, listView);
        Button btnLister = creerBoutonAction("Lister", liste, listView);
        Button btnDernierAjoute = creerBoutonAction("Obtenir le dernier ajouté", liste, listView);
        Button btnRetour = creerBouton("Retour", sceneMenuPrincipal);

        layout.getChildren().addAll(labelTitre, listView, btnAjouter, btnSupprimer, btnModifier, btnLister, btnDernierAjoute, btnRetour);
        return new Scene(layout, WIDTH, HEIGHT);    //WIDTH HEIGHT
    }

    private Button creerBouton(String texte, Scene cible) {
        Button bouton = new Button(texte);
        bouton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        bouton.setOnAction(e -> stage.setScene(cible));
        bouton.setPrefSize(150, 40); //Bouttons
        return bouton;
    }

    private Button creerBoutonAction(String action, ArrayList<String> liste, ListView<String> listView) {
        Button bouton = new Button(action);
        bouton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        bouton.setOnAction(e -> effectuerAction(action, liste, listView));
        bouton.setPrefSize(150, 40); // Bouttons
        return bouton;
    }

    private void effectuerAction(String action, ArrayList<String> liste, ListView<String> listView) {
        if (action.equals("Ajouter")) {
            String nom = showInputDialog("Entrez le nom :");
            if (nom != null && !nom.trim().isEmpty()) {
                liste.add(nom);
                listView.getItems().setAll(liste);
                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi !");
            }
        } else if (action.equals("Supprimer")) {
            String nom = showInputDialog("Entrez le nom à supprimer :");
            if (liste.remove(nom)) {
                listView.getItems().setAll(liste);
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Nom introuvable.");
            }
        } else if (action.equals("Modifier")) {
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
        } else if (action.equals("Lister")) {
            if (liste.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Aucun élément trouvé.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Liste : " + String.join(", ", liste));
            }
        } else if (action.equals("Obtenir le dernier ajouté")) {
            if (!liste.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Dernier ajouté : " + liste.get(liste.size() - 1));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Aucun élément enregistré.");
            }
        }
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
