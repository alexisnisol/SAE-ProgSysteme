package network.protocols.client;

import network.Client;

/**
 * Interface représentant un protocole client pour l'exécution de commandes.
 * Cette interface définit la méthode à implémenter pour l'exécution d'une commande
 * spécifique par un client, avec des arguments passés sous forme de tableau de chaînes.
 */
public interface IClientProtocol {

    /**
     * Exécute une commande spécifique pour un client donné.
     * 
     * @param args Le tableau de chaînes contenant les arguments nécessaires à l'exécution de la commande.
     * @param client L'instance de client sur laquelle la commande sera exécutée.
     * @return La réponse ou le résultat de l'exécution de la commande sous forme de chaîne.
     */
    String execute(String[] args, Client client);
}
