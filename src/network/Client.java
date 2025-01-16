package network;

import model.Game;
import network.protocols.Command;
import network.protocols.client.ClientProtocolRegistry;
import network.utils.Constant;
import network.utils.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe représentant le client du système de jeu.
 * Cette classe gère la communication avec le serveur via des threads pour l'envoi et la réception de messages.
 */
public class Client {

    /** Socket pour la connexion au serveur. */
    private Socket socket;

    /** Indicateur pour suivre si le client doit quitter. */
    private boolean quit;

    /** Instance du jeu en cours associé au client. */
    private Game game;

    /**
     * Constructeur de la classe Client.
     * Initialise la connexion au serveur, et démarre deux threads :
     * - Un pour envoyer des messages au serveur.
     * - Un pour recevoir des messages du serveur.
     */
    public Client() {
        quit = false;
        try {
            this.socket = new Socket(Constant.SERVER_HOST, Constant.PORT);

            Thread sendThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                String message;
                try {
                    while ((message = scanner.nextLine()) != null && !quit) {
                        Network.send(message, socket);
                        if (message.equalsIgnoreCase("QUIT")) {
                            quit = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    quit = true;
                }
            });

            Thread receiveThread = new Thread(() -> {
                try {
                    while (!quit) {
                        String serverResponse = Network.receive(socket);

                        Command command = Command.parse(serverResponse);
                        ClientProtocolRegistry.TypeProtocol typeProtocol =
                                ClientProtocolRegistry.TypeProtocol.getProtocol(command.getName());

                        String response = ClientProtocolRegistry.execute(typeProtocol, command.getArgs(), this);
                        if (response != null) {
                            if (!response.isEmpty()) {
                                System.out.println(response);
                            }
                        } else {
                            System.out.println(serverResponse);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Server disconnected");
                    quit = true;
                }
            });

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();

            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère l'instance du jeu associée au client.
     *
     * @return l'instance du jeu en cours.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Définit l'instance du jeu associée au client.
     *
     * @param game l'instance du jeu à associer.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Point d'entrée principal du client.
     * Initialise une nouvelle instance de Client.
     *
     * @param args arguments de ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        new Client();
    }
}
