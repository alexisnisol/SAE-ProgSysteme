package network.protocols.client;

import model.Puissance4;
import model.exception.PoseImpossibleException;
import network.Client;
import network.utils.Constant;

import java.util.HashMap;
import java.util.Map;

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
            //clear the console
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

        // Crée une nouvelle partie de Puissance 4, n'attend pas de valeur de retour
        ClientProtocolRegistry.commandsList.put(ClientProtocolRegistry.TypeProtocol.CREATE_GAME, (args, client) -> {
            client.setGame(new Puissance4());
            return "";
        });

        ClientProtocolRegistry.commandsList.put(TypeProtocol.MSG, (args, client) -> {
            if (args.length == 0) {
                return "";
            }
            return String.join(" ", args);
        });

        // Commande par défaut, ne fait rien
        ClientProtocolRegistry.commandsList.put(TypeProtocol.DEFAULT, (args, client) -> null);

    }

    public static String execute(TypeProtocol type, String[] args, Client client) {
        IClientProtocol command = ClientProtocolRegistry.commandsList.get(type);
        if (command == null) {
            return Constant.STATUS_ERR + " Commande inconnue";
        }

        return command.execute(args, client);
    }

    public enum TypeProtocol {
        GAME_STATUS,
        CREATE_GAME,
        PLAY,
        END_GAMES_VICTORY,
        END_GAMES_LOOSE,
        END_GAMES_DRAW,
        MSG,
        DEFAULT;

        public static TypeProtocol getProtocol(String command) {
            try {
                return TypeProtocol.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                return TypeProtocol.DEFAULT;
            }
        }
    }
}

