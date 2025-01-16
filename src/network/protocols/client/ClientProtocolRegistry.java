package network.protocols.client;

import model.Puissance4;
import model.exception.PoseImpossibleException;
import network.Client;
import network.utils.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsable de l'enregistrement et de l'exécution des protocoles client.
 * Cette classe maintient une liste de commandes associées à des protocoles et gère leur exécution
 * en fonction du type de protocole.
 */
public class ClientProtocolRegistry {

    private static final Map<TypeProtocol, IClientProtocol> commandsList = new HashMap<>();

    static {
        ClientProtocolRegistry.commandsList.put(ClientProtocolRegistry.TypeProtocol.GAME_STATUS, (args, client) -> {
            if (client.getGame() == null) {
                return Constant.STATUS_ERR + " Vous n'êtes pas dans une partie";
            }
            return client.getGame().toString();
        });

        ClientProtocolRegistry.commandsList.put(ClientProtocolRegistry.TypeProtocol.PLAY, (args, client) -> {
            if (client.getGame() == null) {
                return Constant.STATUS_ERR + " Vous n'êtes pas dans une partie";
            }
            try {
                client.getGame().poserPions(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                return Constant.STATUS_ERR + " Veuillez entrer un nombre";
            } catch (PoseImpossibleException e) {
                return Constant.STATUS_ERR + " La colonne est pleine";
            }
            return client.getGame().toString();
        });

        ClientProtocolRegistry.commandsList.put(TypeProtocol.END_GAMES_VICTORY, (args, client) -> {
            if (client.getGame() == null) {
                return Constant.STATUS_ERR + " Vous n'êtes pas dans une partie";
            }
            return "Vous avez gagné !";
        });

        ClientProtocolRegistry.commandsList.put(TypeProtocol.END_GAMES_LOOSE, (args, client) -> {
            if (client.getGame() == null) {
                return Constant.STATUS_ERR + " Vous n'êtes pas dans une partie";
            }
            return "Vous avez perdu !";
        });

        ClientProtocolRegistry.commandsList.put(TypeProtocol.END_GAMES_DRAW, (args, client) -> {
            if (client.getGame() == null) {
                return Constant.STATUS_ERR + " Vous n'êtes pas dans une partie";
            }
            return "Vous avez fait match nul !";
        });

        ClientProtocolRegistry.commandsList.put(ClientProtocolRegistry.TypeProtocol.CREATE_GAME, (args, client) -> {
            client.setGame(new Puissance4());
            return Constant.STATUS_EMPTY;
        });

        ClientProtocolRegistry.commandsList.put(TypeProtocol.MSG, (args, client) -> {
            if (args.length == 0) {
                return "";
            }
            return String.join(" ", args);
        });

        ClientProtocolRegistry.commandsList.put(TypeProtocol.DEFAULT, (args, client) -> null);
    }

    /**
     * Exécute une commande en fonction du type de protocole.
     * 
     * @param type Le type de protocole à exécuter.
     * @param args Les arguments nécessaires à l'exécution de la commande.
     * @param client Le client sur lequel la commande doit être exécutée.
     * @return Le résultat de l'exécution de la commande sous forme de chaîne.
     */
    public static String execute(TypeProtocol type, String[] args, Client client) {
        IClientProtocol command = ClientProtocolRegistry.commandsList.get(type);
        if (command == null) {
            return Constant.STATUS_ERR + " Commande inconnue";
        }

        return command.execute(args, client);
    }

    /**
     * Enumération représentant les types de protocoles disponibles pour le client.
     */
    public enum TypeProtocol {
        GAME_STATUS,
        CREATE_GAME,
        PLAY,
        END_GAMES_VICTORY,
        END_GAMES_LOOSE,
        END_GAMES_DRAW,
        MSG,
        DEFAULT;

        /**
         * Obtient le type de protocole correspondant à une chaîne de commande.
         * 
         * @param command La chaîne représentant le type de protocole.
         * @return Le type de protocole correspondant, ou {@link TypeProtocol#DEFAULT} si la commande est inconnue.
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
