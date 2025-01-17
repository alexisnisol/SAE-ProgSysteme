package network;

import network.protocols.Command;
import network.protocols.client.ClientProtocolRegistry;
import network.protocols.server.ServerProtocolRegistry;
import network.utils.Constant;
import network.utils.Network;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private Player player;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = Network.receive(this.socket)) != null) {
                Command command = Command.parse(message);

                ServerProtocolRegistry.TypeProtocol typeProtocol = ServerProtocolRegistry.TypeProtocol.getProtocol(command.getName());

                String response = ServerProtocolRegistry.execute(typeProtocol, command.getArgs(), this.player, this);

                sendMessage(response);
            }
        }  catch (IOException e) {
             System.out.println("Déconnexion détectée pour le client : " + (this.player != null ? this.player.getName() : "Inconnu"));
    } finally {
        if (this.player != null) {
            this.server.disconnect(this.player);
        }
        try {
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("Erreur lors de la fermeture du socket : " + ex.getMessage());
        }
    }
}

    public Server getServer() {
        return this.server;
    }

    public String connect(String username) {
        Player player = new Player(username);
        player.setClientHandler(this);
        String response = this.server.connect(player);
        if (response.equals(Constant.STATUS_OK)) {
            this.player = player;
        }
        return response;
    }

    public void sendMessage(String message) {
        try {
            Network.send("MSG " + message, this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void send(ClientProtocolRegistry.TypeProtocol protocol, String[] args) {
        try {
            Network.send(protocol.name() + " " + String.join(" ", args), this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
play 3