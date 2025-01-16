package model;

import model.exception.PoseImpossibleException;
import network.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe abstraite <code>Game</code> représente un jeu générique avec un identifiant unique
 * et une liste de joueurs. Elle définit des méthodes pour gérer les joueurs et leur interaction
 * avec le jeu.
 * Cette classe est destinée à être étendue par des jeux spécifiques, tels que le jeu de Puissance 4.
 */
public abstract class Game {

    /**
     * L'identifiant unique du jeu.
     */
    protected String id;

    /**
     * La liste des joueurs participants au jeu.
     */
    protected List<Player> players;

    /**
     * Constructeur de la classe <code>Game</code>.
     * Initialise un identifiant unique pour le jeu et une liste vide de joueurs.
     */
    public Game() {
        this.id = java.util.UUID.randomUUID().toString();
        this.players = new ArrayList<>();
    }

    /**
     * Ajoute un joueur à la liste des joueurs.
     * 
     * @param player Le joueur à ajouter au jeu.
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * Récupère l'identifiant unique du jeu.
     * 
     * @return L'identifiant unique du jeu.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Démarre le jeu. Cette méthode doit être implémentée dans les sous-classes pour
     * lancer un jeu spécifique.
     */
    public abstract void start();

    /**
     * Récupère la liste des joueurs participant au jeu.
     * 
     * @return La liste des joueurs.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Récupère un joueur en fonction de la valeur de son pion.
     * 
     * @param pions Le pion du joueur recherché (JOUEUR1 ou JOUEUR2).
     * @return Le joueur correspondant à ce pion.
     */
    public abstract Player getPlayer(Pions pions);

    /**
     * Récupère le joueur actuellement en train de jouer.
     * 
     * @return Le joueur actuel.
     */
    public abstract Pions getJoueurActuel();

    /**
     * Permet de poser un pion à un indice donné sur le plateau.
     * Si l'action est impossible, une exception est lancée.
     * 
     * @param indice L'indice où poser le pion.
     * @return Le statut du jeu après avoir posé le pion.
     * @throws PoseImpossibleException Si le placement du pion est impossible.
     */
    public abstract Puissance4.Status poserPions(int indice) throws PoseImpossibleException;
}
