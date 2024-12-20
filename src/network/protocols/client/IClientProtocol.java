package network.protocols.client;

import network.Client;

public interface IClientProtocol {
    String execute(String[] args, Client client);
}
