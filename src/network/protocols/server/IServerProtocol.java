package network.protocols.server;

import network.Player;
import network.Server;

public interface IServerProtocol {
    String execute(String[] args, Player player, Server server);
}
