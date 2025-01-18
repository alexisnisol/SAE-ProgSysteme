package network.protocols.server;

import network.ClientHandler;
import network.Player;

/**
 * Interface représentant un protocole côté serveur.
 * Chaque implémentation définit le comportement d'une commande réseau spécifique.
 */
public interface IServerProtocol {

    /**
     * Exécute une commande serveur en fonction des arguments fournis, du joueur et du gestionnaire client associés.
     *
     * @param args les arguments de la commande.
     * @param player le joueur qui a envoyé la commande.
     * @param clientHandler le gestionnaire client associé au joueur.
     * @return une chaîne de caractères représentant la réponse du serveur à la commande.
     */
    String execute(String[] args, Player player, ClientHandler clientHandler);
}
