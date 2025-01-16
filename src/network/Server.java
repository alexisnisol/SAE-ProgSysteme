package network;

import model.Game;
import model.Puissance4;
import model.exception.PoseImpossibleException;
import network.protocols.client.ClientProtocolRegistry;
import network.utils.Constant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Classe représentant le serveur du système de jeu.
 * Gère les connexions des joueurs, les parties en cours, et les interactions entre les joueurs et le serveur.
 */
public class Server {

    /** Liste des joueurs connectés, identifiés par leur nom. */
    private final ConcurrentMap<String, Player> playersList = new ConcurrentHashMap<>();

    /** Liste des parties en cours, identifiées par leur ID. */
    private final ConcurrentMap<String, Game> gamesList = new ConcurrentHashMap<>();

    /**
     * Constructeur de la classe Server.
     * Initialise le serveur, attend les connexions des clients et crée un thread pour chaque connexion.
     */
    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(Constant.PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(clientSocket, this));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connecte un joueur au serveur.
     *
     * @param player le joueur à connecter.
     * @return le statut de la connexion : "OK" si réussi, "ERR" si le nom est déjà utilisé.
     */
    public String connect(Player player) {
        if (this.playersList.containsKey(player.getName())) {
            return Constant.STATUS_ERR + " Le nom est déjà utilisé";
        }
        this.playersList.put(player.getName(), player);
        System.out.println("Client " + player.getName() + " connecté : " + this.playersList);
        return Constant.STATUS_OK;
    }

    /**
     * Récupère un joueur connecté par son nom.
     *
     * @param name le nom du joueur.
     * @return l'objet Player correspondant, ou null si le joueur n'est pas connecté.
     */
    public Player getPlayer(String name) {
        return this.playersList.get(name);
    }

    /**
     * Déconnecte un joueur du serveur.
     *
     * @param player le joueur à déconnecter.
     * @return le statut de la déconnexion : "OK".
     */
    public String disconnect(Player player) {
        this.playersList.remove(player.getName());
        System.out.println("Client " + player.getName() + " déconnecté : " + this.playersList);
        return Constant.STATUS_OK;
    }

    /**
     * Envoie une demande de jeu à un autre joueur.
     *
     * @param source le joueur à l'origine de la demande.
     * @param target le nom du joueur cible.
     * @return le statut de la demande : "OK" si réussie, "ERR" sinon.
     */
    public String playRequest(Player source, String target) {
        if (!source.isAvailable()) {
            return Constant.STATUS_ERR + " Vous ne pouvez faire une demande de jeu";
        }

        Player targetPlayer = this.playersList.get(target);
        if (targetPlayer == null) {
            return Constant.STATUS_ERR + " Le joueur " + target + " n'existe pas";
        }
        if (targetPlayer.equals(source)) {
            return Constant.STATUS_ERR + " Vous ne pouvez pas vous rejoindre vous-même";
        }
        if (!targetPlayer.isAvailable()) {
            return Constant.STATUS_ERR + " Le joueur " + target + " n'est pas disponible";
        }

        targetPlayer.setRequest(source);
        targetPlayer.getClientHandler().sendMessage("Demande de jeu de " + source.getName());

        return Constant.STATUS_OK;
    }

    /**
     * Accepte une demande de jeu et crée une nouvelle partie.
     *
     * @param player le joueur acceptant la demande.
     * @return le statut de l'opération : "OK" si réussie, "ERR" sinon.
     */
    public String acceptRequest(Player player) {
        String targetName = player.getRequest();
        player.clearRequest();

        if (targetName == null) {
            return Constant.STATUS_ERR + " Aucune demande de jeu en attente";
        }

        Player target = this.playersList.get(targetName);
        if (target == null || !target.isAvailable()) {
            return Constant.STATUS_ERR + " La demande de jeu n'est plus valide";
        }

        target.getClientHandler().sendMessage("Demande de jeu acceptée par " + player.getName());

        return createGame(player, target);
    }

    /**
     * Lance une partie entre deux joueurs.
     *
     * @param player1 le premier joueur.
     * @param player2 le deuxième joueur.
     * @return une chaîne vide indiquant le succès de l'opération.
     */
    private String createGame(Player player1, Player player2) {
        Game game = new Puissance4();
        if (player1.setInGame(game) && player2.setInGame(game)) {

            this.gamesList.put(game.getId(), game);

            System.out.println("Nouvelle partie créée : " + game.getId() + " entre " + player1.getName() + " et " + player2.getName());
            game.start();
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.CREATE_GAME);
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.GAME_STATUS);
        }
        return Constant.STATUS_EMPTY;
    }

    /**
     * Envoie une mise à jour du statut de la partie à tous les joueurs.
     *
     * @param game        la partie en cours.
     * @param typeProtocol le type de mise à jour.
     * @param args        arguments supplémentaires liés à la mise à jour.
     */
    private void sendGameStatus(Game game, ClientProtocolRegistry.TypeProtocol typeProtocol, String... args) {
        if (typeProtocol == ClientProtocolRegistry.TypeProtocol.END_GAMES_VICTORY) {
            for (Player player : game.getPlayers()) {
                if (game.getPlayer(game.getJoueurActuel()) == player) {
                    player.getClientHandler().send(ClientProtocolRegistry.TypeProtocol.END_GAMES_VICTORY, args);
                } else {
                    player.getClientHandler().send(ClientProtocolRegistry.TypeProtocol.END_GAMES_LOOSE, args);
                }
            }
        } else {
            for (Player player : game.getPlayers()) {
                player.getClientHandler().send(typeProtocol, args);
            }
        }
    }

    /**
     * Refuse une demande de jeu.
     *
     * @param player le joueur refusant la demande.
     * @return le statut de l'opération : "OK" si réussie, "ERR" sinon.
     */
    public String declineRequest(Player player) {
        String targetName = player.getRequest();
        player.clearRequest();

        if (targetName == null) {
            return Constant.STATUS_ERR + " Aucune demande de jeu en attente";
        }

        Player target = this.playersList.get(targetName);
        if (target == null || !target.isAvailable()) {
            return Constant.STATUS_ERR + " La demande de jeu n'est plus valide";
        }

        target.getClientHandler().sendMessage("Demande de jeu refusée par " + player.getName());

        return Constant.STATUS_OK;
    }

    /**
     * Récupère la liste des joueurs connectés.
     *
     * @return une chaîne contenant les noms des joueurs connectés.
     */
    public String getPlayerList() {
        StringBuilder response = new StringBuilder(Constant.STATUS_OK + " Liste des joueurs : ");
        for (String name : this.playersList.keySet()) {
            response.append(name).append(" ");
        }
        return response.toString();
    }

    /**
     * Point d'entrée principal du serveur.
     * Crée une nouvelle instance de Server.
     *
     * @param args arguments de ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        new Server();
    }
}
