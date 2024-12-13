package network;

public class Player {

    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean isValidPlayerName(String playerName) {
        return playerName != null && playerName.matches("[a-zA-Z0-9]{3,10}");
    }

    @Override
    public String toString() {
        return name;
    }
}
