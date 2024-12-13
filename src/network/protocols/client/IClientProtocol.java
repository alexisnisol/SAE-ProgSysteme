package network.protocols.client;

import network.Player;

public interface IClientProtocol {
    String execute(String[] args, Player player);
}
