package network;

import network.protocols.Command;
import network.protocols.client.ClientProtocolRegistry;
import network.protocols.server.ServerProtocolRegistry;
import network.utils.Constant;
import network.utils.Network;

import java.io.IOException;
import java.net.Socket;

/**
 * Classe gérant la communication entre le serveur et un client.
 * Chaque client connecté au serveur est associé à une instance de cette classe.
 */
public class ClientHandler implements Runnable {

    /** Socket pour la connexion avec le client. */
    private Socket socket;

    /** Référence au serveur gérant les clients et les parties. */
    private Server server;

    /** Joueur associé à ce gestionnaire client. */
    private Player player;

    /**
     * Constructeur de la classe ClientHandler.
     *
     * @param socket le socket connecté au client.
     * @param server l'instance du serveur auquel le client est connecté.
     */
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * Exécute la logique de gestion des messages envoyés par le client.
     * Lit les commandes reçues, les interprète via le protocole du serveur,
     * et renvoie une réponse appropriée au client.
     */
    @Override
    public void run() {
        String message;
        try {
            while ((message = Network.receive(this.socket)) != null) {
                // Analyse de la commande reçue
                Command command = Command.parse(message);
                ServerProtocolRegistry.TypeProtocol typeProtocol =
                        ServerProtocolRegistry.TypeProtocol.getProtocol(command.getName());

                // Exécution de la commande et récupération de la réponse
                String response = ServerProtocolRegistry.execute(typeProtocol, command.getArgs(), this.player, this);

                // Envoi de la réponse au client
                sendMessage(response);
            }
        } catch (IOException e) {
            // Gestion de la déconnexion du client
            if (this.player != null) {
                this.server.disconnect(this.player);
            }
        }
    }

    /**
     * Récupère le serveur associé à ce gestionnaire client.
     *
     * @return l'instance du serveur.
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Connecte un joueur au serveur.
     * Crée une nouvelle instance de joueur, l'associe à ce gestionnaire client,
     * et informe le serveur de la connexion.
     *
     * @param username le nom du joueur.
     * @return le statut de la connexion : "OK" si réussi, "ERR" sinon.
     */
    public String connect(String username) {
        Player player = new Player(username);
        player.setClientHandler(this);
        String response = this.server.connect(player);
        if (response.equals(Constant.STATUS_OK)) {
            this.player = player;
        }
        return response;
    }

    /**
     * Envoie un message brut au client via le socket.
     *
     * @param message le message à envoyer au client.
     */
    public void sendMessage(String message) {
        try {
            Network.send("MSG " + message, this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie une commande au client en utilisant un protocole spécifique.
     *
     * @param protocol le type de protocole de la commande.
     * @param args les arguments de la commande.
     */
    public void send(ClientProtocolRegistry.TypeProtocol protocol, String[] args) {
        try {
            Network.send(protocol.name() + " " + String.join(" ", args), this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
