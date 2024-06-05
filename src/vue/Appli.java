package vue;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import modele.Pions;
import modele.Puissance4;
import modele.exception.PoseImpossibleException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.File;

/**
 * Vue du jeu du pendu
 */
public class Appli extends Application {
    /**
     * modèle du jeu
     **/
    private Puissance4 modele;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;

    private GridPane grille;


    private Label roundP1;
    private Label roundP2;

    /**
     * initialise les attributs (créer le modèle, charge les images)
     */
    @Override
    public void init() {
        this.modele = new Puissance4();
    }

    /**
     * @return le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        this.panelCentral = new BorderPane();
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * Redimensionne une image
     * @param img l'image à redimensionner
     * @param size la taille de l'image
     */
    public static void resizeImage(ImageView img, int size){
        img.setFitHeight(size);
        img.setFitWidth(size);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private Pane titre(){
        
        GridPane banniere = new GridPane();
        Label jeu = new Label("Puissance 4");
        jeu.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        banniere.add(jeu, 0, 0);
        banniere.setBackground(new Background(new BackgroundFill(Color.valueOf("#e6e6fa"), null, null)));
        return banniere;
    }

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private BorderPane fenetreJeu(){
        BorderPane pane = new BorderPane();

        VBox playerOne = new VBox();
        Label joueur = new Label("Joueur 1");
        Label nbPions = new Label("Nombre de pions : 21");

        roundP1 = new Label("A vous de jouer !");
        roundP1.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        playerOne.getChildren().addAll(joueur, nbPions, roundP1);


        VBox playerTwo = new VBox();
        joueur = new Label("Joueur 2");
        nbPions = new Label("Nombre de pions : 21");
        roundP2 = new Label("A vous de jouer !");
        roundP2.setVisible(false);
        roundP2.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        playerTwo.getChildren().addAll(joueur, nbPions, roundP2);

        StackPane plateau = new StackPane();

        this.grille = new GridPane();
        
        grille.setAlignment(Pos.BASELINE_CENTER);
        grille.setPadding(new Insets(10));
        grille.setHgap(10);
        grille.setVgap(10);

        for (int i = 0; i < modele.getLargeur(); i++) {
            for (int j = 0; j < modele.getHauteur(); j++) {
                Button bouton = new Button(i + "," + j);

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
            Button bouton = new Button(""+i);
            bouton.setOnAction(e -> {
                try {
                    modele.poserPions(Integer.parseInt(bouton.getText()));
                    majAffichage(Integer.parseInt(bouton.getText()));
                } catch (PoseImpossibleException poseImpossibleException) {
                    System.out.println("Impossible de poser le pion");
                }
            });
            bouton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-text-fill: white;");
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


    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire){
        /*for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File(repertoire+"/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }*/
    }

    /**
     * change le mode de la vue pour l'accueil
     */
    public void modeAccueil(){
        this.modele = new Puissance4();
        this.panelCentral.setCenter(this.fenetreAccueil());
    }
    
    /**
     * change le mode de la vue pour le jeu
     */
    public void modeJeu(){
        this.panelCentral.setCenter(this.fenetreJeu());
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
     * Affiche une fenêtre popup pour afficher les règles du jeu
     * @return la fenêtre popup
     */
    public Alert popUpReglesDuJeu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Jeu du Pendu");
        alert.setHeaderText("Règles du Jeu");
        alert.setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        return alert;
    }
    
    /**
     * Affiche une fenêtre popup pour indiquer que l'utilisateur a gagné
     * @return la fenêtre popup
     */
    public Alert popUpMessageGagne(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);        
        alert.setTitle("Jeu du Pendu");
        alert.setHeaderText("Vous avez gagné :)");
        alert.setContentText("Bravo vous avez gagné !");
        return alert;
    }
    
    /**
     * Affiche une fenêtre popup pour indiquer que l'utilisateur a perdu
     * @return la fenêtre popup
     */
    public Alert popUpMessagePerdu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Jeu du Pendu");
        alert.setHeaderText("Vous avez perdu :(");
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        
        stage.setScene(this.laScene());
        this.modeJeu();
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}