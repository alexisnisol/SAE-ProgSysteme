package network.protocols.server;

import network.ClientHandler;
import network.Player;
import network.utils.Constant;

import java.util.HashMap;
import java.util.Map;

public class ServerProtocolRegistry {

    private static final Map<TypeProtocol, IServerProtocol> commandsList = new HashMap<>();

    static {
        ServerProtocolRegistry.commandsList.put(TypeProtocol.CONNECT, (args, player, clientHandler) -> {

            //TODO Warning : args[0] is not checked for null => Improve the protocol
            String username = args[0];

            if(playerIsAuthenticated(player)) {
                return Constant.STATUS_ERR + " Vous êtes déjà connecté : " + player.getName();
            }

            if(Player.isValidPlayerName(username)) {
                return clientHandler.connect(username);
            }
            return Constant.STATUS_ERR + " Le nom doit contenir entre 3 et 10 caractères alphanumériques";
        });

        ServerProtocolRegistry.commandsList.put(TypeProtocol.QUIT, (args, player, clientHandler) -> clientHandler.getServer().disconnect(player));

        ServerProtocolRegistry.commandsList.put(TypeProtocol.ASK, (args, player, clientHandler) -> clientHandler.getServer().playRequest(player, args[0]));
        ServerProtocolRegistry.commandsList.put(TypeProtocol.ACCEPT, (args, player, clientHandler) -> clientHandler.getServer().acceptRequest(player));
        ServerProtocolRegistry.commandsList.put(TypeProtocol.DECLINE, (args, player, clientHandler) -> clientHandler.getServer().declineRequest(player));
        ServerProtocolRegistry.commandsList.put(TypeProtocol.PLAYERLIST, (args, player, clientHandler) -> clientHandler.getServer().getPlayerList());
    }

    public static String execute(TypeProtocol type, String[] args, Player player, ClientHandler clientHandler) {
        IServerProtocol command = ServerProtocolRegistry.commandsList.get(type);
        if (command == null) {
            return Constant.STATUS_ERR + " Commande inconnue";
        }
        if (type.needAuthentification && !playerIsAuthenticated(player)) {
            return Constant.STATUS_ERR + " Vous n'êtes pas connecté";
        }
        return command.execute(args, player, clientHandler);
    }

    private static boolean playerIsAuthenticated(Player player) {
        return player != null;
    }

    public enum TypeProtocol {
        PING,
        CONNECT(false),
        QUIT,
        ASK,
        ACCEPT,
        DECLINE,
        PLAYERLIST,
        DEFAULT;

        final boolean needAuthentification;

        TypeProtocol() {
            this(true);
        }

        TypeProtocol(boolean needAuth) {
            this.needAuthentification = needAuth;
        }

        public static TypeProtocol getProtocol(String command) {
            try {
                return TypeProtocol.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                return TypeProtocol.DEFAULT;
            }
        }
    }
}

