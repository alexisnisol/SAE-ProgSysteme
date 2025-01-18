package network.protocols.server;

import network.ClientHandler;
import network.Player;
import network.utils.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe responsable de l'enregistrement et de l'exécution des protocoles serveur.
 * Gère les commandes reçues des clients et exécute les actions correspondantes.
 */
public class ServerProtocolRegistry {

    private static final Map<TypeProtocol, IServerProtocol> commandsList = new HashMap<>();

    static {
        // Commande CONNECT : permet à un client de se connecter
        ServerProtocolRegistry.commandsList.put(TypeProtocol.CONNECT, (args, player, clientHandler) -> {
            // TODO: Vérifier args[0] pour éviter des erreurs null pointer (amélioration nécessaire)
            String username = args[0];

            if (playerIsAuthenticated(player)) {
                return Constant.STATUS_ERR + " Vous êtes déjà connecté : " + player.getName();
            }

            if (Player.isValidPlayerName(username)) {
                return clientHandler.connect(username);
            }
            return Constant.STATUS_ERR + " Le nom doit contenir entre 3 et 10 caractères alphanumériques";
        });


        ServerProtocolRegistry.commandsList.put(TypeProtocol.QUIT, (args, player, clientHandler) -> clientHandler.getServer().disconnect(player));

        ServerProtocolRegistry.commandsList.put(TypeProtocol.ASK, (args, player, clientHandler) -> clientHandler.getServer().playRequest(player, args[0]));
        ServerProtocolRegistry.commandsList.put(TypeProtocol.ACCEPT, (args, player, clientHandler) -> clientHandler.getServer().acceptRequest(player));
        ServerProtocolRegistry.commandsList.put(TypeProtocol.DECLINE, (args, player, clientHandler) -> clientHandler.getServer().declineRequest(player));
        ServerProtocolRegistry.commandsList.put(TypeProtocol.PLAYERLIST, (args, player, clientHandler) -> clientHandler.getServer().getPlayerList());
        ServerProtocolRegistry.commandsList.put(TypeProtocol.HELP, (args, player, clientHandler) -> clientHandler.getServer().getHelp());
        ServerProtocolRegistry.commandsList.put(TypeProtocol.PLAY, (args, player, clientHandler) -> {
            if (args.length < 1) {
                return Constant.STATUS_ERR + " Veuillez fournir un numéro de colonne";
            }
            return clientHandler.getServer().play(player, args[0]);
        });
        ServerProtocolRegistry.commandsList.put(TypeProtocol.INFO, (args, player, clientHandler) -> clientHandler.getServer().infoPlayer(player, args[0]));
        ServerProtocolRegistry.commandsList.put(TypeProtocol.HISTORY, (args, player, clientHandler) -> clientHandler.getServer().historyPlayer(player, args[0]));

    }

    /**
     * Exécute une commande serveur en fonction du type de protocole.
     *
     * @param type le type de protocole à exécuter.
     * @param args les arguments de la commande.
     * @param player le joueur émettant la commande.
     * @param clientHandler le gestionnaire client associé.
     * @return une réponse sous forme de chaîne de caractères après exécution.
     */
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

    /**
     * Vérifie si un joueur est authentifié.
     *
     * @param player le joueur à vérifier.
     * @return true si le joueur est authentifié, false sinon.
     */
    private static boolean playerIsAuthenticated(Player player) {
        return player != null;
    }

    public static Set<TypeProtocol> getAllCommands() {
        return commandsList.keySet();
    }

    /**
     * Enumération des types de protocoles disponibles.
     * Chaque type peut nécessiter une authentification ou non.
     */
    public enum TypeProtocol {
        PING,
        CONNECT(false),
        QUIT,
        ASK,
        ACCEPT,
        DECLINE,
        PLAYERLIST,
        HELP(false),
        PLAY,
        INFO,
        HISTORY,
        DEFAULT;

        /** Indique si le protocole nécessite une authentification. */
        final boolean needAuthentification;

        /**
         * Constructeur par défaut (nécessite l'authentification).
         */
        TypeProtocol() {
            this(true);
        }

        /**
         * Constructeur avec spécification de l'authentification.
         *
         * @param needAuth true si l'authentification est requise, false sinon.
         */
        TypeProtocol(boolean needAuth) {
            this.needAuthentification = needAuth;
        }

        /**
         * Récupère le protocole correspondant à une commande donnée.
         *
         * @param command la commande à analyser.
         * @return le protocole correspondant ou DEFAULT si la commande est inconnue.
         */
        public static TypeProtocol getProtocol(String command) {
            try {
                return TypeProtocol.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                return TypeProtocol.DEFAULT;
            }
        }
    }
}
