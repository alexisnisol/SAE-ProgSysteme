package network.protocols.server;

import network.ClientHandler;
import network.Player;

public interface IServerProtocol {
    String execute(String[] args, Player player, ClientHandler clientHandler);
}
