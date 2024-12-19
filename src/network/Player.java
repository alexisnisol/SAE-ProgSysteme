package network;

public class Player {

    private String name;
    private boolean inGame;
    private String pendingRequest;
    private String currentGame;
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
        return name;
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

    public void setInGame() {
        if(this.isAvailable()) {
            this.inGame = true;
            this.currentGame = "";
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
