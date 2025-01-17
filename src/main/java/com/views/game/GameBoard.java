package com.views.game;

import com.controller.BoutonHoverProperty;
import com.model.Pions;
import com.model.Puissance4;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;

public class GameBoard extends BorderPane {

    private GridPane grille;

    private Label pionsP1;
    private Label pionsP2;
    private Label roundP1;
    private Label roundP2;

    private Puissance4 modele;

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private BorderPane fenetreJeu(){
        BorderPane pane = new BorderPane();

        VBox playerOne = new VBox();
        Label joueur = new Label("Joueur 1");
        pionsP1 = new Label("Nombre de pions : 21");

        roundP1 = new Label("A vous de jouer !");
        roundP1.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        playerOne.getChildren().addAll(joueur, pionsP1, roundP1);


        VBox playerTwo = new VBox();
        joueur = new Label("Joueur 2");
        pionsP2 = new Label("Nombre de pions : 21");
        roundP2 = new Label("A vous de jouer !");
        roundP2.setVisible(false);
        roundP2.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        playerTwo.getChildren().addAll(joueur, pionsP2, roundP2);

        StackPane plateau = new StackPane();

        this.grille = new GridPane();

        grille.setAlignment(Pos.BASELINE_CENTER);
        grille.setPadding(new Insets(10));
        grille.setHgap(10);
        grille.setVgap(10);

        for (int i = 0; i < modele.getLargeur(); i++) {
            for (int j = 0; j < modele.getHauteur(); j++) {
                Button bouton = new Button();

                bouton.setStyle("-fx-background-color: #ffffff;");
                bouton.setMinSize(50, 50);
                bouton.setShape(new Circle(25));
                grille.add(bouton, i, j);
            }
        }

        plateau.getChildren().add(grille);

        ImageView img = new ImageView();
        img.setFitHeight(200);
        img.setFitWidth(200);
        img.setImage(new Image(new File("img/background.png").toURI().toString()));

        plateau.getChildren().add(img);

        HBox buttons = new HBox();
        buttons.setPadding(new Insets(10));
        buttons.setSpacing(10);
        for (int i = 0; i < modele.getLargeur(); i++) {
            Button bouton = new Button();
            bouton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");

            bouton.hoverProperty().addListener(new BoutonHoverProperty(bouton));

            //bouton.setOnAction(new BoutonControlleur(this, i));
            bouton.setMinSize(50, 50*(modele.getHauteur()+1));
            buttons.getChildren().add(bouton);
        }

        buttons.setAlignment(Pos.BASELINE_CENTER);

        plateau.getChildren().add(buttons);

        HBox bottom = new HBox();

        pane.setLeft(playerOne);
        pane.setCenter(plateau);
        pane.setRight(playerTwo);
        pane.setBottom(bottom);

        pane.setPadding(new Insets(5, 20, 5, 20));
        return pane;
    }

    /**
     * change le mode de la vue pour le jeu
     */
    public void modeJeu(){
        this.modele.reset();
        //this.pane.setCenter(this.fenetreJeu());
    }

    /**
     * lance une partie, réinitialise le modèle
     */
    public void lancePartie(){
        this.modeJeu();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(int y){
        pionsP1.setText("Nombre de pionss : " + this.modele.getNbPions().get(Pions.JOUEUR1));
        pionsP2.setText("Nombre de pions : " + this.modele.getNbPions().get(Pions.JOUEUR2));
        roundP1.setVisible(modele.getJoueurActuel() == Pions.JOUEUR1);
        roundP2.setVisible(modele.getJoueurActuel() == Pions.JOUEUR2);
        int x = modele.getPlateau().get(y).size()-1;
        Button bouton = (Button) this.grille.getChildren().get(y*modele.getHauteur() + modele.getHauteur()-1-x);
        bouton.setStyle("-fx-background-color: " + (modele.getPlateau().get(y).get(x) == Pions.JOUEUR1 ? "#ff0000" : "#ffff00"));
        /*this.motCrypte.setText(this.modele.getMotCrypte());
        this.dessin.setImage(this.lesImages.get(this.modele.getNbErreursMax()-this.modele.getNbErreursRestants()));
        this.pg.setProgress(1-(double)this.modele.getNbErreursRestants()/this.modele.getNbErreursMax());

        this.clavier.desactiveTouches(this.modele.getLettresEssayees());

        if(this.modele.gagne()){
            this.chrono.stop();
            Optional<ButtonType> reponse = this.popUpMessageGagne().showAndWait(); // on lance la fenêtre popup et on attends la réponse
            if (reponse.isPresent() && reponse.get().equals(ButtonType.OK)){
                lancePartie();
            }
        } else if(this.modele.perdu()){
            this.chrono.stop();
            Optional<ButtonType> reponse = this.popUpMessagePerdu().showAndWait(); // on lance la fenêtre popup et on attends la réponse
            if (reponse.isPresent() && reponse.get().equals(ButtonType.OK)){
                lancePartie();
            }
        }
        */
    }


    /**
     * Affiche une fenêtre popup pour demander si l'utilisateur veut vraiment quitter
     * @return la fenêtre popup
     */
    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    /**
     * Affiche une fenêtre popup
     * @return la fenêtre popup
     */
    public Alert popUpMessage(Puissance4.Status st){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Jeu du Puissance 4");
        switch (st) {
            case GAGNE:
                alert.setHeaderText("Vous avez gagné :)");
                alert.setContentText("Bravo "+ this.modele.getJoueurActuel() +", vous avez gagné !");
                break;
            case NULL:
                alert.setHeaderText("Vous avez fait match nul !");
                break;
            default:
                break;
        }
        return alert;
    }
}
