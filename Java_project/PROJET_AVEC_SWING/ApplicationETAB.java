import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ApplicationETAB extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContainer;
    private JTextField champIdentifiant;
    private JPasswordField champMotDePasse;
    private ArrayList<Menu> menus;
    private ArrayList<Eleve> eleves;
    private ArrayList<Utilisateur> utilisateurs;
    private double solde = 7000.0;

    private static final String MENU_FILE = "menus.txt";
    private static final String ELEVES_FILE = "eleves.txt";
    private static final String UTILISATEURS_FILE = "utilisateurs.txt";
    private static final String SOLDE_FILE = "solde.txt";

    public ApplicationETAB() {
        setTitle("ETAB v1.1");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menus = new ArrayList<>();
        eleves = new ArrayList<>();
        utilisateurs = new ArrayList<>();
        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);

        chargerDonnees();

        panelContainer.add(creerPanelConnexion(), "Connexion");
        panelContainer.add(creerMenuPrincipal(), "MenuPrincipal");
        panelContainer.add(creerMenuGestion("GESTION DES MENUS"), "GestionMenus");
        panelContainer.add(creerMenuGestion("GESTION DES ÉLÈVES"), "GestionEleves");
        panelContainer.add(creerMenuGestion("GESTION DES UTILISATEURS"), "GestionUtilisateurs");

        add(panelContainer);
        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                sauvegarderDonnees();
            }
        });
    }

    private void chargerDonnees() {
        try (BufferedReader br = new BufferedReader(new FileReader(SOLDE_FILE))) {
            String line = br.readLine();
            if (line != null && !line.isEmpty()) {
                solde = Double.parseDouble(line);
            }
        } catch (IOException | NumberFormatException e) {}

        try (BufferedReader br = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                menus.add(Menu.fromFileFormat(line));
            }
        } catch (IOException e) {}

        try (BufferedReader br = new BufferedReader(new FileReader(ELEVES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                eleves.add(Eleve.fromFileFormat(line));
            }
        } catch (IOException e) {}

        try (BufferedReader br = new BufferedReader(new FileReader(UTILISATEURS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                utilisateurs.add(Utilisateur.fromFileFormat(line));
            }
        } catch (IOException e) {}
    }

    private void sauvegarderDonnees() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SOLDE_FILE))) {
            bw.write(String.valueOf(solde));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde du solde", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (Menu menu : menus) {
                bw.write(menu.toFileFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde des menus", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ELEVES_FILE))) {
            for (Eleve eleve : eleves) {
                bw.write(eleve.toFileFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde des élèves", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(UTILISATEURS_FILE))) {
            for (Utilisateur user : utilisateurs) {
                bw.write(user.toFileFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde des utilisateurs", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel creerPanelConnexion() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JLabel titre = new JLabel("BIENVENUE DANS L'APPLICATION ETAB v1.1 (Solde: " + solde + ")", SwingConstants.CENTER);
        panel.add(titre);

        JPanel panelConnexion = new JPanel(new GridLayout(2, 2));
        panelConnexion.add(new JLabel("Identifiant :"));
        champIdentifiant = new JTextField();
        panelConnexion.add(champIdentifiant);
        panelConnexion.add(new JLabel("Mot de passe :"));
        champMotDePasse = new JPasswordField();
        panelConnexion.add(champMotDePasse);
        panel.add(panelConnexion);

        JButton boutonConnexion = new JButton("Se connecter");
        boutonConnexion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verifierConnexion();
            }
        });
        panel.add(boutonConnexion);

        return panel;
    }

    private void verifierConnexion() {
        String identifiant = champIdentifiant.getText();
        String motDePasse = new String(champMotDePasse.getPassword());

        if (identifiant.equals("admin") && motDePasse.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Connexion réussie ! Solde: " + solde);
            cardLayout.show(panelContainer, "MenuPrincipal");
        } else {
            JOptionPane.showMessageDialog(this, "Identifiant ou mot de passe incorrect.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel creerMenuPrincipal() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JLabel titre = new JLabel("MENU PRINCIPAL (Solde: " + solde + ")", SwingConstants.CENTER);
        panel.add(titre);

        panel.add(creerBouton("Gestion des élèves", "GestionEleves"));
        panel.add(creerBouton("Gestion des menus", "GestionMenus"));
        panel.add(creerBouton("Gestion des utilisateurs", "GestionUtilisateurs"));

        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sauvegarderDonnees();
                System.exit(0);
            }
        });
        panel.add(btnQuitter);

        return panel;
    }

    private JPanel creerMenuGestion(String titreMenu) {
        JPanel panel = new JPanel(new GridLayout(8, 1));
        JLabel titre = new JLabel(titreMenu + " (Solde: " + solde + ")", SwingConstants.CENTER);
        panel.add(titre);

        String[] options;
        if (titreMenu.equals("GESTION DES MENUS")) {
            options = new String[]{"Ajouter", "Acheter", "Modifier", "Lister", "Obtenir le dernier ajouté"};
        } else {
            options = new String[]{"Ajouter", "Supprimer", "Modifier", "Lister", "Obtenir le dernier ajouté"};
        }

        for (int i = 0; i < options.length; i++) {
            final String option = options[i];
            JButton bouton = new JButton(option);
            bouton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (option.equals("Ajouter")) {
                        if (titreMenu.equals("GESTION DES MENUS")) {
                            ajouterMenu();
                        } else if (titreMenu.equals("GESTION DES ÉLÈVES")) {
                            ajouterEleve();
                        } else if (titreMenu.equals("GESTION DES UTILISATEURS")) {
                            ajouterUtilisateur();
                        }
                    } else if (option.equals("Lister")) {
                        if (titreMenu.equals("GESTION DES MENUS")) {
                            listerMenus();
                        } else if (titreMenu.equals("GESTION DES ÉLÈVES")) {
                            listerEleves();
                        } else if (titreMenu.equals("GESTION DES UTILISATEURS")) {
                            listerUtilisateurs();
                        }
                    } else if (option.equals("Modifier")) {
                        if (titreMenu.equals("GESTION DES MENUS")) {
                            modifierMenu();
                        } else if (titreMenu.equals("GESTION DES ÉLÈVES")) {
                            modifierEleve();
                        } else if (titreMenu.equals("GESTION DES UTILISATEURS")) {
                            modifierUtilisateur();
                        }
                    } else if (option.equals("Acheter") && titreMenu.equals("GESTION DES MENUS")) {
                        acheterMenu();
                    } else {
                        JOptionPane.showMessageDialog(ApplicationETAB.this, 
                            "Action sélectionnée : " + option + " (non implémentée)", 
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            panel.add(bouton);
        }

        panel.add(creerBouton("Retour", "MenuPrincipal"));
        return panel;
    }

    private void ajouterMenu() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nomField = new JTextField(10);
        JTextField descField = new JTextField(10);
        JTextField prixField = new JTextField(10);

        inputPanel.add(new JLabel("Nom du plat:"));
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descField);
        inputPanel.add(new JLabel("Prix:"));
        inputPanel.add(prixField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Ajouter un menu", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nomPlat = nomField.getText().trim();
            String description = descField.getText().trim();
            String prixText = prixField.getText().trim();

            try {
                double prix = Double.parseDouble(prixText);
                if (!nomPlat.isEmpty() && !description.isEmpty() && prix > 0) {
                    Menu menu = new Menu(nomPlat, description, prix);
                    menus.add(menu);
                    sauvegarderDonnees();
                    JOptionPane.showMessageDialog(this, "Menu ajouté: " + menu.toString(), 
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis et le prix doit être positif", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void acheterMenu() {
        if (menus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun plat disponible à l'achat.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nomPlat = JOptionPane.showInputDialog(this, "Entrez le nom du plat à acheter:");
        if (nomPlat == null || nomPlat.trim().isEmpty()) return;

        Menu menuToBuy = null;
        for (Menu menu : menus) {
            if (menu.getNomPlat().equalsIgnoreCase(nomPlat)) {
                menuToBuy = menu;
                break;
            }
        }

        if (menuToBuy == null) {
            JOptionPane.showMessageDialog(this, "Plat non trouvé.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] supplements = {"Aucun", "Boisson (+500)", "Frites (+300)"};
        String supplementChoisi = (String) JOptionPane.showInputDialog(this, 
            "Voulez-vous un supplément avec " + menuToBuy.toString() + " ?", 
            "Supplément", JOptionPane.QUESTION_MESSAGE, null, supplements, supplements[0]);

        if (supplementChoisi == null) return;

        double coutSupplement = 0;
        if (supplementChoisi.equals("Boisson (+500)")) coutSupplement = 500;
        else if (supplementChoisi.equals("Frites (+300)")) coutSupplement = 300;

        double total = menuToBuy.getPrix() + coutSupplement;

        if (total > solde) {
            JOptionPane.showMessageDialog(this, "Solde insuffisant. Total: " + total + ", Solde: " + solde, 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] moyensPaiement = {"Wave", "Espèces"};
        String paiement = (String) JOptionPane.showInputDialog(this, 
            "Choisissez le mode de paiement (Total: " + total + "):", 
            "Paiement", JOptionPane.QUESTION_MESSAGE, null, moyensPaiement, moyensPaiement[0]);

        if (paiement == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Confirmer l'achat de : " + menuToBuy.toString() + 
            (coutSupplement > 0 ? " + " + supplementChoisi : "") + 
            "\nTotal: " + total + " via " + paiement + " ?", 
            "Confirmation d'achat", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            solde -= total;
            sauvegarderDonnees();
            JOptionPane.showMessageDialog(this, 
                "Achat réussi : " + menuToBuy.toString() + 
                (coutSupplement > 0 ? " + " + supplementChoisi : "") + 
                "\nPaiement: " + paiement + "\nNouveau solde: " + solde, 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            panelContainer.removeAll();
            panelContainer.add(creerPanelConnexion(), "Connexion");
            panelContainer.add(creerMenuPrincipal(), "MenuPrincipal");
            panelContainer.add(creerMenuGestion("GESTION DES MENUS"), "GestionMenus");
            panelContainer.add(creerMenuGestion("GESTION DES ÉLÈVES"), "GestionEleves");
            panelContainer.add(creerMenuGestion("GESTION DES UTILISATEURS"), "GestionUtilisateurs");
            cardLayout.show(panelContainer, "GestionMenus");
        }
    }

    private void listerMenus() {
        if (menus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun menu enregistré.", 
                "Liste", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder liste = new StringBuilder("Liste des menus:\n");
            for (Menu menu : menus) {
                liste.append(menu.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, liste.toString(), 
                "Liste des menus", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modifierMenu() {
        if (menus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun menu à modifier.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nomPlat = JOptionPane.showInputDialog(this, "Entrez le nom du plat à modifier:");
        if (nomPlat == null || nomPlat.trim().isEmpty()) return;

        Menu menuToModify = null;
        for (Menu menu : menus) {
            if (menu.getNomPlat().equalsIgnoreCase(nomPlat)) {
                menuToModify = menu;
                break;
            }
        }

        if (menuToModify == null) {
            JOptionPane.showMessageDialog(this, "Plat non trouvé.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nomField = new JTextField(menuToModify.getNomPlat(), 10);
        JTextField descField = new JTextField(menuToModify.toString().split(" - ")[1].split(" \\(")[0], 10);
        JTextField prixField = new JTextField(String.valueOf(menuToModify.getPrix()), 10);

        inputPanel.add(new JLabel("Nom du plat:"));
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descField);
        inputPanel.add(new JLabel("Prix:"));
        inputPanel.add(prixField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Modifier un menu", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String description = descField.getText().trim();
            String prixText = prixField.getText().trim();

            try {
                double prix = Double.parseDouble(prixText);
                if (!nom.isEmpty() && !description.isEmpty() && prix > 0) {
                    menuToModify.setNomPlat(nom);
                    menuToModify.setDescription(description);
                    menuToModify.setPrix(prix);
                    sauvegarderDonnees();
                    JOptionPane.showMessageDialog(this, "Menu modifié: " + menuToModify.toString(), 
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis et le prix doit être positif", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ajouterEleve() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nomField = new JTextField(10);
        JTextField prenomField = new JTextField(10);
        JTextField numeroField = new JTextField(10);

        inputPanel.add(new JLabel("Nom:"));
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Prénom:"));
        inputPanel.add(prenomField);
        inputPanel.add(new JLabel("Numéro Étudiant:"));
        inputPanel.add(numeroField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Ajouter un élève", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String numero = numeroField.getText().trim();

            if (!nom.isEmpty() && !prenom.isEmpty() && !numero.isEmpty()) {
                Eleve eleve = new Eleve(nom, prenom, numero);
                eleves.add(eleve);
                sauvegarderDonnees();
                JOptionPane.showMessageDialog(this, "Élève ajouté: " + eleve.toString(), 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listerEleves() {
        if (eleves.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun élève enregistré.", 
                "Liste", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder liste = new StringBuilder("Liste des élèves:\n");
            for (Eleve eleve : eleves) {
                liste.append(eleve.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, liste.toString(), 
                "Liste des élèves", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modifierEleve() {
        if (eleves.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun élève à modifier.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String numero = JOptionPane.showInputDialog(this, "Entrez le numéro étudiant de l'élève à modifier:");
        if (numero == null || numero.trim().isEmpty()) return;

        Eleve eleveToModify = null;
        for (Eleve eleve : eleves) {
            if (eleve.getNumeroEtudiant().equals(numero)) {
                eleveToModify = eleve;
                break;
            }
        }

        if (eleveToModify == null) {
            JOptionPane.showMessageDialog(this, "Élève non trouvé.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nomField = new JTextField(eleveToModify.toString().split(" - ")[1].split(" ")[1], 10);
        JTextField prenomField = new JTextField(eleveToModify.toString().split(" - ")[1].split(" ")[0], 10);
        JTextField numeroField = new JTextField(eleveToModify.getNumeroEtudiant(), 10);

        inputPanel.add(new JLabel("Nom:"));
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Prénom:"));
        inputPanel.add(prenomField);
        inputPanel.add(new JLabel("Numéro Étudiant:"));
        inputPanel.add(numeroField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Modifier un élève", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String newNumero = numeroField.getText().trim();

            if (!nom.isEmpty() && !prenom.isEmpty() && !newNumero.isEmpty()) {
                eleveToModify.setNom(nom);
                eleveToModify.setPrenom(prenom);
                eleveToModify.setNumeroEtudiant(newNumero);
                sauvegarderDonnees();
                JOptionPane.showMessageDialog(this, "Élève modifié: " + eleveToModify.toString(), 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ajouterUtilisateur() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nomField = new JTextField(10);
        JTextField prenomField = new JTextField(10);
        JTextField loginField = new JTextField(10);

        inputPanel.add(new JLabel("Nom:"));
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Prénom:"));
        inputPanel.add(prenomField);
        inputPanel.add(new JLabel("Login:"));
        inputPanel.add(loginField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Ajouter un utilisateur", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String login = loginField.getText().trim();

            if (!nom.isEmpty() && !prenom.isEmpty() && !login.isEmpty()) {
                Utilisateur user = new Utilisateur(nom, prenom, login);
                utilisateurs.add(user);
                sauvegarderDonnees();
                JOptionPane.showMessageDialog(this, "Utilisateur ajouté: " + user.toString(), 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listerUtilisateurs() {
        if (utilisateurs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun utilisateur enregistré.", 
                "Liste", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder liste = new StringBuilder("Liste des utilisateurs:\n");
            for (Utilisateur user : utilisateurs) {
                liste.append(user.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, liste.toString(), 
                "Liste des utilisateurs", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modifierUtilisateur() {
        if (utilisateurs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun utilisateur à modifier.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String login = JOptionPane.showInputDialog(this, "Entrez le login de l'utilisateur à modifier:");
        if (login == null || login.trim().isEmpty()) return;

        Utilisateur userToModify = null;
        for (Utilisateur user : utilisateurs) {
            if (user.getLogin().equals(login)) {
                userToModify = user;
                break;
            }
        }

        if (userToModify == null) {
            JOptionPane.showMessageDialog(this, "Utilisateur non trouvé.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nomField = new JTextField(userToModify.toString().split(" - ")[1].split(" ")[1], 10);
        JTextField prenomField = new JTextField(userToModify.toString().split(" - ")[1].split(" ")[0], 10);
        JTextField loginField = new JTextField(userToModify.getLogin(), 10);

        inputPanel.add(new JLabel("Nom:"));
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Prénom:"));
        inputPanel.add(prenomField);
        inputPanel.add(new JLabel("Login:"));
        inputPanel.add(loginField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Modifier un utilisateur", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String newLogin = loginField.getText().trim();

            if (!nom.isEmpty() && !prenom.isEmpty() && !newLogin.isEmpty()) {
                userToModify.setNom(nom);
                userToModify.setPrenom(prenom);
                userToModify.setLogin(newLogin);
                sauvegarderDonnees();
                JOptionPane.showMessageDialog(this, "Utilisateur modifié: " + userToModify.toString(), 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton creerBouton(String texte, final String cible) {
        JButton bouton = new JButton(texte);
        bouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContainer, cible);
            }
        });
        return bouton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ApplicationETAB();
            }
        });
    }
}