package network.protocols.server;

import network.utils.Constant;
import network.Player;
import network.Server;

import java.util.HashMap;
import java.util.Map;

public class ServerProtocolRegistry {

    private static final Map<TypeProtocol, IServerProtocol> commandsList = new HashMap<>();

    static {
        ServerProtocolRegistry.commandsList.put(TypeProtocol.CONNECT, (args, player, server) -> {

            //TODO Warning : args[0] is not checked for null => Improve the protocol
            String username = args[0];

            if(playerIsAuthenticated(player)) {
                return Constant.STATUS_ERR + " Vous êtes déjà connecté : " + player.getName();
            }

            if(Player.isValidPlayerName(username)) {
                return server.connect(new Player(username));
            }
            return Constant.STATUS_ERR + " Le nom doit contenir entre 3 et 10 caractères alphanumériques";
        });

        ServerProtocolRegistry.commandsList.put(TypeProtocol.QUIT, (args, player, server) -> server.disconnect(player));
    }

    public static String execute(TypeProtocol type, String[] args, Player player, Server server) {
        IServerProtocol command = ServerProtocolRegistry.commandsList.get(type);
        if (command == null) {
            return Constant.STATUS_ERR + " Commande inconnue";
        }
        if (type.needAuthentification && !playerIsAuthenticated(player)) {
            return Constant.STATUS_ERR + " Vous n'êtes pas connecté";
        }
        return command.execute(args, player, server);
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
        REFUSE,
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
