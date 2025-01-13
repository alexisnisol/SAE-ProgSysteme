package network;

import model.Game;

public class Player {

    private String name;
    private boolean inGame;
    private String pendingRequest;
    private String idGame;
    private ClientHandler clientHandler;

    public Player(String name) {
        this.name = name;
        this.inGame = false;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return this.clientHandler;
    }

    public String getName() {
        return this.name;
    }

    public static boolean isValidPlayerName(String playerName) {
        return playerName != null && playerName.matches("[a-zA-Z0-9]{3,10}");
    }

    public boolean isAvailable() {
        return !this.inGame && this.pendingRequest == null;
    }

    public void setRequest(Player playerSource) {
        this.pendingRequest = playerSource.getName();
    }

    public String getRequest() {
        return this.pendingRequest;
    }

    public void clearRequest() {
        this.pendingRequest = null;
    }

    public boolean setInGame(Game game) {
        if(this.isAvailable()) {
            this.inGame = true;
            this.idGame = game.getId();
            game.addPlayer(this);
            return true;
        }
        return false;
    }

    public String getIdGame() {
        return this.idGame;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
