package network;

import java.net.Socket;

public class Player {

    private String name;
    private Socket socket;

    public Player(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public static boolean isValidPlayerName(String playerName) {
        return playerName != null && playerName.matches("[a-zA-Z0-9]{3,10}");
    }

    @Override
    public String toString() {
        return name;
    }
}
