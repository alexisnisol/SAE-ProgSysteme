package network.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe utilitaire pour gérer les communications réseau.
 * Fournit des méthodes statiques pour envoyer et recevoir des messages via un socket.
 */
public class Network {

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private Network() {
    }

    /**
     * Reçoit un message du client ou du serveur via un socket.
     *
     * @param socket le socket utilisé pour la communication.
     * @return la ligne reçue sous forme de chaîne de caractères, ou null si la fin du flux est atteinte.
     * @throws IOException si une erreur d'entrée/sortie survient lors de la lecture du message.
     */
    public static String receive(Socket socket) throws IOException {
        InputStreamReader stream = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(stream);
        return reader.readLine();
    }

    /**
     * Envoie un message au client ou au serveur via un socket.
     *
     * @param message le message à envoyer.
     * @param socket le socket utilisé pour la communication.
     * @throws IOException si une erreur d'entrée/sortie survient lors de l'envoi du message.
     */
    public static void send(String message, Socket socket) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(message);
        writer.flush();
    }
}
