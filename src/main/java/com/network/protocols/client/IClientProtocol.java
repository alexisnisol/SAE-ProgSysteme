package com.network.protocols.client;

import com.network.Client;

public interface IClientProtocol {
    String execute(String[] args, Client client);
}
