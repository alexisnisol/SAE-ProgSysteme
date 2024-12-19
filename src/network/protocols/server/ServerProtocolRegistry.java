package network.protocols.server;

import network.utils.Constant;
import network.utils.Network;
import network.Player;
import network.Server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerProtocolRegistry {

    private static final Map<TypeProtocol, IServerProtocol> commandsList = new HashMap<>();

    static {
        ServerProtocolRegistry.commandsList.put(TypeProtocol.CONNECT, (args, player, server, socket) -> {

            //TODO Warning : args[0] is not checked for null => Improve the protocol
            String username = args[0];

            if(playerIsAuthenticated(player)) {
                return Constant.STATUS_ERR + " Vous êtes déjà connecté : " + player.getName();
            }

            if(Player.isValidPlayerName(username)) {
                return server.connect(new Player(username, socket));
            }
            return Constant.STATUS_ERR + " Le nom doit contenir entre 3 et 10 caractères alphanumériques";
        });

        ServerProtocolRegistry.commandsList.put(TypeProtocol.QUIT, (args, player, server, socket) -> server.disconnect(player));

        ServerProtocolRegistry.commandsList.put(TypeProtocol.PLAYERS, (args, player, server, socket) -> {
            return server.getPlayersList();
        });

        ServerProtocolRegistry.commandsList.put(TypeProtocol.ASK, (args, player, server, socket) -> {
            if (args == null || args.length < 1) {
                return Constant.STATUS_ERR + " Vous devez spécifier un joueur à inviter.";
            }
        
            String nomCible = args[0];
            Player adversaireCible = server.getPlayer(nomCible);
        
            if (adversaireCible == null) {
                return Constant.STATUS_ERR + " Le joueur " + nomCible + " n'est pas connecté.";
            }
        
            if (player.getName().equals(nomCible)) {
                return Constant.STATUS_ERR + " Vous ne pouvez pas vous inviter vous-même.";
            }
        
            server.sendInvitation(player, adversaireCible);
            return Constant.STATUS_OK + " Invitation envoyée à " + nomCible;
        });

        ServerProtocolRegistry.commandsList.put(TypeProtocol.ACCEPT, (args, player, server, socket) -> {
            if (args == null || args.length < 1) {
                return Constant.STATUS_ERR + " Vous devez spécifier un joueur.";
            }
        
            String fromPlayerName = args[0];
            Player fromPlayer = server.getPlayer(fromPlayerName);
        
            if (fromPlayer == null) {
                return Constant.STATUS_ERR + " Le joueur " + fromPlayerName + " n'est pas connecté.";
            }

            String responseMessage = "Votre invitation a été acceptée par " + player.getName();
            Network.send(responseMessage, fromPlayer.getSocket()); 
        
            return Constant.STATUS_OK + " Vous avez accepté de jouer avec " + fromPlayerName;
        });
        
        ServerProtocolRegistry.commandsList.put(TypeProtocol.REFUSE, (args, player, server, socket) -> {
            if (args == null || args.length < 1) {
                return Constant.STATUS_ERR + " Vous devez spécifier un joueur.";
            }
        
            String fromPlayerName = args[0];
            Player fromPlayer = server.getPlayer(fromPlayerName);
        
            if (fromPlayer == null) {
                return Constant.STATUS_ERR + " Le joueur " + fromPlayerName + " n'est pas connecté.";
            }

            String responseMessage = "Votre invitation a été refusée par " + player.getName();
            Network.send(responseMessage, fromPlayer.getSocket()); 
        
            return Constant.STATUS_OK + " Vous avez refusé de jouer avec " + fromPlayerName;
        });
    }

    public static String execute(TypeProtocol type, String[] args, Player player, Server server, Socket socket) {
        IServerProtocol command = ServerProtocolRegistry.commandsList.get(type);
        if (command == null) {
            return Constant.STATUS_ERR + " Commande inconnue";
        }
        if (type.needAuthentification && !playerIsAuthenticated(player)) {
            return Constant.STATUS_ERR + " Vous n'êtes pas connecté";
        }
        return command.execute(args, player, server, socket);
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
        DEFAULT, 
        PLAYERS;

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
