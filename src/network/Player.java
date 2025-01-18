package network;

import model.Game;

/**
 * Classe représentant un joueur dans le système de jeu.
 * Un joueur peut être connecté, avoir une demande de jeu en attente, ou participer à une partie.
 */
public class Player {

    /** Nom du joueur. */
    private String name;

    /** Indique si le joueur est actuellement dans une partie. */
    private boolean inGame;

    /** Nom du joueur qui a envoyé une demande de jeu. */
    private String pendingRequest;

    /** Identifiant de la partie en cours. */
    private String idGame;

    /** Gestionnaire client associé au joueur pour la communication avec le serveur. */
    private ClientHandler clientHandler;

    /**
     * Constructeur de la classe Player.
     *
     * @param name le nom du joueur.
     */
    public Player(String name) {
        this.name = name;
        this.inGame = false;
    }

    /**
     * Définit le gestionnaire client associé au joueur.
     *
     * @param clientHandler l'objet ClientHandler à associer.
     */
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Récupère le gestionnaire client associé au joueur.
     *
     * @return l'objet ClientHandler associé.
     */
    public ClientHandler getClientHandler() {
        return this.clientHandler;
    }

    /**
     * Récupère le nom du joueur.
     *
     * @return le nom du joueur.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Vérifie si un nom de joueur est valide.
     * Un nom est considéré comme valide s'il contient entre 3 et 10 caractères alphanumériques.
     *
     * @param playerName le nom du joueur à valider.
     * @return true si le nom est valide, false sinon.
     */
    public static boolean isValidPlayerName(String playerName) {
        return playerName != null && playerName.matches("[a-zA-Z0-9]{3,10}");
    }

    /**
     * Vérifie si le joueur est disponible pour jouer.
     *
     * @return true si le joueur n'est pas en partie et n'a pas de demande en attente, false sinon.
     */
    public boolean isAvailable() {
        return !this.inGame && this.pendingRequest == null;
    }


    public void setAvailable(boolean available) {
    this.inGame = !available;  
}



    /**
     * Définit une demande de jeu pour ce joueur.
     *
     * @param playerSource le joueur ayant envoyé la demande.
     */

    public void setRequest(Player playerSource) {
        this.pendingRequest = playerSource.getName();
    }

    /**
     * Récupère le nom du joueur ayant envoyé une demande.
     *
     * @return le nom du joueur ayant envoyé une demande, ou null s'il n'y en a pas.
     */
    public String getRequest() {
        return this.pendingRequest;
    }

    /**
     * Efface la demande de jeu en attente.
     */
    public void clearRequest() {
        this.pendingRequest = null;
    }

    /**
     * Met le joueur en partie.
     * Si le joueur est disponible, il est ajouté à la partie et son état est mis à jour.
     *
     * @param game la partie à laquelle le joueur participe.
     * @return true si le joueur a été ajouté à la partie, false sinon.
     */
    public boolean setInGame(Game game) {
        if (this.isAvailable()) {
            this.inGame = true;
            this.idGame = game.getId();
            game.addPlayer(this);
            return true;
        }
        return false;
    }

    /**
     * Récupère l'identifiant de la partie en cours.
     *
     * @return l'ID de la partie en cours, ou null si le joueur n'est pas en partie.
     */
    public String getIdGame() {
        return this.idGame;
    }

    /**
     * Retourne une représentation textuelle du joueur (son nom).
     *
     * @return le nom du joueur.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
