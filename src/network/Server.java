package network;

import model.Game;
import model.Puissance4;
import model.exception.PoseImpossibleException;
import network.protocols.client.ClientProtocolRegistry;
import network.protocols.server.ServerProtocolRegistry;
import network.utils.Constant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import bdd.Requete;

/**
 * Class Server
 * Gère les connexions des clients et les parties en cours.
 * Créer un thread ClientHandler pour chaque client connecté.
 */
public class Server {

    private final ConcurrentMap<String, Player> playersList = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Game> gamesList = new ConcurrentHashMap<>();
    private Requete requete;

    public Server() {
        try {
            this.requete = new Requete();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ServerSocket serverSocket = new ServerSocket(Constant.PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(clientSocket, this));
                thread.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connecte un joueur au serveur
     * @param player Joueur à connecter
     * @return Statut de la connexion (OK ou ERR)
     */
    public String connect(Player player) {
        if (this.playersList.containsKey(player.getName())) {
            return Constant.STATUS_ERR + " Le nom est déjà utilisé";
        }
        if (!this.requete.playerExists(player.getName())) {
            this.requete.addPlayer(player.getName());
        }
        this.playersList.put(player.getName(), player);
        System.out.println("Client " + player.getName() + " connecté : " + this.playersList);
        return Constant.STATUS_OK;
    }

    /**
     * Déconnecte un joueur du serveur
     * @param player Joueur à déconnecter
     * @return Statut de la déconnexion (OK)
     */
    public String disconnect(Player player) {
        if (player != null && this.playersList.containsKey(player.getName())) {
            this.playersList.remove(player.getName());
            System.out.println("Client " + player.getName() + " déconnecté.");
            player.setAvailable(true);
            Game game = this.gamesList.get(player.getIdGame());
        if (game != null) {
            Player otherPlayer = null;
            for (Player p : game.getPlayers()) {
                if (!p.equals(player)) {
                    otherPlayer = p;
                    break;
                }
            }

            if (otherPlayer != null) {
                otherPlayer.getClientHandler().sendMessage("Votre adversaire s'est déconnecté. Vous avez gagné par forfait !");
                System.out.println("Message envoyé à " + otherPlayer.getName());
            }}
        } else {
            System.out.println("Déconnexion d'un joueur non enregistré.");
        }
        returnToLobby(this.gamesList.get(player.getIdGame()));

        return Constant.STATUS_OK;
    }


    /**
     * Récupère un joueur connecté par son nom
     * @param name Nom du joueur
     * @return Joueur connecté
     */
    public Player getPlayer(String name) {
        return this.playersList.get(name);
    }

    /**
     * Envoie une demande de jeu à un joueur
     * @param source Joueur demandant la partie
     * @param target Nom du joueur cible
     * @return Statut de la demande (OK ou ERR)
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
        if(!targetPlayer.isAvailable()) {
            return Constant.STATUS_ERR + " Le joueur " + target + " n'est pas disponible";
        }

        targetPlayer.setRequest(source);
        targetPlayer.getClientHandler().sendMessage("Demande de jeu de " + source.getName());

        return Constant.STATUS_OK;
    }

    /**
     * Accepte une demande de jeu
     * @param player Joueur acceptant la demande
     * @return Statut de l'acceptation (OK ou ERR)
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
     * Récupère les informations d'un joueur
     * @param source Joueur demandant les informations
     * @param target Nom du joueur cible
     * @return Informations du joueur
     */
    public String infoPlayer(Player source, String target) {
        if (!source.isAvailable()) {
            return Constant.STATUS_ERR + " Vous ne pouvez pas demander des informations sur un joueur";
        }

        return this.requete.getInfoPlayer(target);
    }

    public String historyPlayer(Player source, String target) {
        if (!source.isAvailable()) {
            return Constant.STATUS_ERR + " Vous ne pouvez pas demander l'historique d'un joueur";
        }

        return this.requete.getHistoriquePlayer(target);
    }

    /**
     * Joue dans une partie en cours.
     * Envoie une
     * @param player Joueur jouant le coup
     * @param column Colonne où jouer le coup
     * @return Statut du coup (EMPTY ou ERR)
     */
    public String play(Player player, String column) {
        Game game = this.gamesList.get(player.getIdGame());
        if (game == null) {
            return Constant.STATUS_ERR + " Vous n'êtes pas dans une partie";
        }
        try {

            if (column == null) {
                return Constant.STATUS_ERR + " Veuillez entrer une colonne";
            }
            
            if(game.getPlayer(game.getJoueurActuel()) != player) {
                return Constant.STATUS_ERR + " Ce n'est pas votre tour";
            }

            if (game.coupValide(Integer.parseInt(column)) == false) {
                return Constant.STATUS_ERR + " La colonne n'est pas valide";
            }

            Puissance4.Status status = game.poserPions(Integer.parseInt(column));
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.PLAY, column);

            if (status == Puissance4.Status.GAGNE) {
                sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.END_GAMES_VICTORY);
                String joueurGagnant = game.getPlayer(game.getJoueurActuel()).getName();
                System.out.println("Le joueur " + joueurGagnant + " a gagné la partie.");
                returnToLobby(game);
            } else if (status == Puissance4.Status.NULL) {
                sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.END_GAMES_DRAW);
                System.out.println("Match nul. Tous les joueurs retournent au lobby.");
                returnToLobby(game);
            }


        } catch (NumberFormatException e) {
            return Constant.STATUS_ERR + " La colonne doit être un nombre";
        } catch (IllegalArgumentException e) {
            return Constant.STATUS_ERR + e.getMessage();
        } catch (PoseImpossibleException e) {
            return Constant.STATUS_ERR + " La colonne est pleine";
        }
        return Constant.STATUS_EMPTY;
    }

    private String createGame(Player player1, Player player2) {
        Game game = new Puissance4();
        if (player1.setInGame(game) && player2.setInGame(game)) {

            this.gamesList.put(game.getId(), game);

            System.out.println("Nouvelle partie créée : " + game.getId() + " entre " + player1.getName() + " et " + player2.getName());
            game.start();
            player1.getClientHandler().sendMessage("C'est à votre tour !");
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.CREATE_GAME);
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.GAME_STATUS);
        }
        return Constant.STATUS_EMPTY;
    }

    /**
     * Envoie le statut de la partie à tous les joueurs de la partie.
     * Le statut est envoyé en fonction du type de protocole à envoyer.
     * Si le type de protocole est END_GAMES_VICTORY, envoie un message de victoire au joueur actuel et un message de défaite aux autres joueurs.
     * Sinon, envoie le statut de la partie à tous les joueurs.
     * @param game Partie en cours
     * @param typeProtocol Type de protocole à envoyer
     * @param args Arguments à envoyer
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

    public String getPlayerList() {
        StringBuilder response = new StringBuilder(Constant.STATUS_OK + " Liste des joueurs : ");
        for (Player player : this.playersList.values()) {
            if (player.isAvailable()) {
                response.append(player.getName()).append(" ");
            }
        }
        return response.toString();
    }

    public void returnToLobby(Game game) {
        try{

        for (Player player : game.getPlayers()) {
            if(!player.isAvailable()){ 
                player.setAvailable(true);
                this.playersList.put(player.getName(), player);

                player.getClientHandler().sendMessage("Vous êtes retourné au lobby.");
        }}
        this.gamesList.remove(game.getId());
     }
        catch (NullPointerException e) {
            System.out.println("Plus aucun joueur connecté.");
    }
    }


    public String getHelp() {
        StringBuilder response = new StringBuilder(Constant.STATUS_OK + " Liste des commandes : ");
        ServerProtocolRegistry.getAllCommands().forEach(name -> response.append(name).append(" - "));
        return response.toString();
    }


    public static void main(String[] args) {
        new Server();
    }
}
