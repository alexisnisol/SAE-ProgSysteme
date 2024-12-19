package network;

import model.Puissance4;
import network.utils.Constant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {

    private final Map<String, Player> playersList = new HashMap<>();
    private final Map<String, String> currentGames = new HashMap<>();

    private final ReadWriteLock playersLock = new ReentrantReadWriteLock();

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
        playersLock.writeLock().lock();
        try {
            if (this.playersList.containsKey(player.getName())) {
                return Constant.STATUS_ERR + " Le nom est déjà utilisé";
            }
            this.playersList.put(player.getName(), player);
            System.out.println("Client " + player.getName() + " connecté : " + this.playersList);
            return Constant.STATUS_OK;
        } finally {
            playersLock.writeLock().unlock();
        }
    }

    public Player getPlayer(String name) {
        playersLock.readLock().lock();
        try {
            return this.playersList.get(name);
        } finally {
            playersLock.readLock().unlock();
        }
    }

    public String disconnect(Player player) {
        playersLock.writeLock().lock();
        try {
            this.playersList.remove(player.getName());
            System.out.println("Client " + player.getName() + " déconnecté : " + this.playersList);
            return Constant.STATUS_OK;
        } finally {
            playersLock.writeLock().unlock();
        }
    }

    public String playRequest(Player source, String target) {
        playersLock.readLock().lock();
        try {
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
        } finally {
            playersLock.readLock().unlock();
        }
    }

    public String acceptRequest(Player player) {
        playersLock.readLock().lock();
        try {
            String targetName = player.getRequest();
            player.clearRequest();

            if (targetName == null) {
                return Constant.STATUS_ERR + " Aucune demande de jeu en attente";
            }

            Player target = this.playersList.get(targetName);
            if (target == null || !target.isAvailable()) {
                return Constant.STATUS_ERR + " La demande de jeu n'est plus valide";
            }

            player.setInGame();
            target.setInGame();

            target.getClientHandler().sendMessage("Demande de jeu acceptée par " + player.getName());

            Puissance4 game = new Puissance4();

            return Constant.STATUS_OK;
        } finally {
            playersLock.readLock().unlock();
        }
    }

    public String declineRequest(Player player) {
        playersLock.readLock().lock();
        try {
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
        } finally {
            playersLock.readLock().unlock();
        }
    }

    public String getPlayerList() {
        playersLock.readLock().lock();
        try {
            StringBuilder response = new StringBuilder(Constant.STATUS_OK + " Liste des joueurs : ");
            for (String name : this.playersList.keySet()) {
                response.append(name).append(" ");
            }
            return response.toString();
        } finally {
            playersLock.readLock().unlock();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
