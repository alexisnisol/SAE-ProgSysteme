package network.protocols.server;

import java.net.Socket;

import network.Player;
import network.Server;

public interface IServerProtocol {
    String execute(String[] args, Player player, Server server, Socket socket);
}
