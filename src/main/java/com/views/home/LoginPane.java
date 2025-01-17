package com.views.home;

import com.network.protocols.server.ServerProtocolRegistry;
import com.views.AppliClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Cette classe est la vue de la page de connexion. Elle permet à l'utilisateur de se connecter à son compte.
 * Elle contient un formulaire de connexion avec un champ pour le pseudo et un champ pour le mot de passe.
 * Elle contient également un bouton pour se connecter et un bouton pour s'inscrire.
 */

public class LoginPane extends BorderPane {

    /**
     * main est le conteneur principal de l'application
     * modele est le modèle de l'application
     * pseudo est le champ de texte pour le pseudo
     */

    private AppliClient appli;
    private TextField pseudo;

    /**
     * Constructeur de la classe Login
     *
     * @param main conteneur principal de l'application
     */

    public LoginPane(AppliClient main) {
        this.appli = main;
        this.setStyle("-fx-background-color: white;");
        showCenter();
        showRight();
        showBottom();
    }


    /**
     * Méthode permettant d'afficher le centre de la page de connexion
     */

    public void showCenter() {

        // Création du formulaire de connexion
        VBox center = new VBox(50);
        center.setAlignment(Pos.CENTER);
        pseudo = new TextField();
        pseudo.setPrefSize(433, 59);
        pseudo.setId("accueil-tf");
        pseudo.setPromptText("Pseudo");

        center.getChildren().addAll(pseudo);
        this.setCenter(center);
    }


    /**
     * Méthode permettant d'afficher la partie droite de la page de connexion
     */
    public void showRight() {
        ImageView idCard = new ImageView("file:src/main/assets/images/id_card.png"); // Image de la carte d'identité
        BorderPane.setAlignment(idCard, Pos.CENTER);
        idCard.setFitWidth(217);
        idCard.setFitHeight(229);
        this.setRight(idCard);
    }

    /**
     * Méthode permettant de récupérer le pseudo
     *
     * @return le champ de texte pour le pseudo
     */

    public TextField getPseudo() {
        return this.pseudo;
    }

    /**
     * Méthode permettant d'afficher le bas de la page de connexion
     */

    public void showBottom() {
        Button bottom = new Button("Connexion"); // Bouton de connexion
        bottom.setCursor(Cursor.HAND);
        bottom.setOnAction(e -> {
            System.out.println(this.appli);
            System.out.println(this.appli.getClient());

            String resp = this.appli.getClient().sendWithAck(ServerProtocolRegistry.TypeProtocol.CONNECT, this.pseudo.getText());
            if (resp.equals("OK")) {
                //this.appli.showHome();
            } else {
                this.appli.showAlert("Erreur", resp).show();
            }
        });

        BorderPane.setAlignment(bottom, Pos.CENTER);
        BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
        bottom.setPrefSize(314, 70);
        bottom.setStyle(
                "-fx-text-fill: white; -fx-background-color: #0781FE; -fx-font-weight: bold; -fx-font-size: 18;");
        this.setBottom(bottom);
    }

    /**
     * Méthode permettant de réinitialiser les champs de texte
     */
    public void resetTF() {
        this.pseudo.setText("");
    }

    /**
     * Méthode permettant de récupérer le conteneur principal de l'application
     *
     * @return le conteneur principal de l'application
     */

    public AppliClient getAppli() {
        return this.appli;
    }
}