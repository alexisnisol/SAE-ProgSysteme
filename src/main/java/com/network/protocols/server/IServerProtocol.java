package com.network.protocols.server;

import com.network.ClientHandler;
import com.network.Player;

public interface IServerProtocol {
    String execute(String[] args, Player player, ClientHandler clientHandler);
}
