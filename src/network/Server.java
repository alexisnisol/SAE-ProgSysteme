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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {

    private final ConcurrentMap<String, Player> playersList = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Game> gamesList = new ConcurrentHashMap<>();

    public Server() {
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

    public String connect(Player player) {
        if (this.playersList.containsKey(player.getName())) {
            return Constant.STATUS_ERR + " Le nom est déjà utilisé";
        }
        this.playersList.put(player.getName(), player);
        System.out.println("Client " + player.getName() + " connecté : " + this.playersList);
        return Constant.STATUS_OK;
    }

    public Player getPlayer(String name) {
        return this.playersList.get(name);
    }

    public String disconnect(Player player) {
        this.playersList.remove(player.getName());
        System.out.println("Client " + player.getName() + " déconnecté : " + this.playersList);
        return Constant.STATUS_OK;
    }

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

    public String play(Player player, String column) {
        Game game = this.gamesList.get(player.getIdGame());
        if (game == null) {
            return Constant.STATUS_ERR + " Vous n'êtes pas dans une partie";
        }
        try {
            if(game.getPlayer(game.getJoueurActuel()) != player) {
                return Constant.STATUS_ERR + " Ce n'est pas votre tour";
            }

            Puissance4.Status status = game.poserPions(Integer.parseInt(column));
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.PLAY, column);

            if (status == Puissance4.Status.GAGNE) {
                sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.END_GAMES_VICTORY);
            } else if (status == Puissance4.Status.NULL) {
                sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.END_GAMES_DRAW);
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
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.CREATE_GAME);
            sendGameStatus(game, ClientProtocolRegistry.TypeProtocol.GAME_STATUS);
        }
        return Constant.STATUS_EMPTY;
    }

    private void sendGameStatus(Game game, ClientProtocolRegistry.TypeProtocol typeProtocol, String... args) {
        if(typeProtocol == ClientProtocolRegistry.TypeProtocol.END_GAMES_VICTORY) {
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
        for (String name : this.playersList.keySet()) {
            response.append(name).append(" ");
        }
        return response.toString();
    }

    public static void main(String[] args) {
        new Server();
    }
}
