package com.views;

import com.model.Puissance4;
import com.network.Client;
import com.views.home.LoginPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Vue du jeu du pendu
 */
public class AppliClient extends Application {
    /**
     * modèle du jeu
     **/
    private Puissance4 modele;

    private Scene scene;
    private BorderPane root;

    private LoginPane loginPane;
    private Stage stage;

    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    private Client client;

    public Client getClient() {
        return this.client;
    }

    public Puissance4 getModele() {
        return this.modele;
    }

    /**
     * initialise les attributs (créer le modèle, charge les images)
     */
    @Override
    public void init() {
        this.modele = new Puissance4();
    }



    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     */
    private VBox fenetreAccueil(){
        VBox centre = new VBox(20);

        Button start = new Button("Lancer la partie");

        TitledPane niveauPane;
        VBox choix = new VBox(5);
        niveauPane = new TitledPane("Niveau de difficulté", choix);
        niveauPane.setCollapsible(false);


        centre.getChildren().addAll(start, niveauPane);
        centre.setPadding(new Insets(20));
        return centre;
    }

    private void renderLoginPane() {
        this.scene.setRoot(this.loginPane);
    }

    public Alert showAlert(String name, String desc) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(name);
        alert.setContentText(desc);

        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.root = new BorderPane();

        this.scene = new Scene(root, 500, 600);

        this.client = new Client(this);
        this.loginPane = new LoginPane(this);

        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.scene);
        stage.show();
        renderLoginPane();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public void launchApp(String[] args) {
        launch(args);
    }

    public static void main(String[] args) {
        launch(args);
    }
}