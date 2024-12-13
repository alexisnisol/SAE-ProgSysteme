package network;

import network.protocols.Command;
import network.protocols.server.ServerProtocolRegistry;
import network.utils.Constant;
import network.utils.Network;

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
        while ((message = Network.getMessage(this.socket)) != null) {
            Command command = Command.parse(message);

            ServerProtocolRegistry.TypeProtocol typeProtocol = ServerProtocolRegistry.TypeProtocol.getProtocol(command.getName());

            String response = ServerProtocolRegistry.execute(typeProtocol, command.getArgs(), this.player, this.server);

            //TODO : Improve this
            if (typeProtocol == ServerProtocolRegistry.TypeProtocol.CONNECT && Constant.STATUS_OK.equalsIgnoreCase(response)) {
                this.player = this.server.getPlayer(command.getArgs()[0]);
            }

            Network.sendMessage(response, this.socket);
        }
    }
}
