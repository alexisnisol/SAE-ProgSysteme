package network;

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

    public static void main(String[] args) {
        new Server();
    }
}
