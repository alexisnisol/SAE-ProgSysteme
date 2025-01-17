package com.network;

import com.model.Game;
import com.network.protocols.Command;
import com.network.protocols.client.ClientProtocolRegistry;
import com.network.protocols.server.ServerProtocolRegistry;
import com.network.utils.Constant;
import com.network.utils.Network;
import com.views.AppliClient;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private boolean quit;
    private Game game;
    private AppliClient appli;

    public Client(AppliClient appli) {
        quit = false;
        try {
            this.appli = appli;
            this.socket = new Socket(Constant.SERVER_HOST, Constant.PORT);

            Thread sendThread = null;
            if(appli == null) {
                sendThread = new Thread(() -> {
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

                sendThread.start();
            }


            Thread receiveThread = new Thread(() -> {
                try {
                    while(!quit) {
                        String serverResponse = Network.receive(socket);

                        Command command = Command.parse(serverResponse);

                        ClientProtocolRegistry.TypeProtocol typeProtocol = ClientProtocolRegistry.TypeProtocol.getProtocol(command.getName());

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

            receiveThread.start();

            if(sendThread != null) {
                sendThread.join();
            }

            receiveThread.join();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(ServerProtocolRegistry.TypeProtocol type, String... args) {
        try {
        Command command = Command.parse(type.name() + " " + String.join(" ", args));
        Network.send(command.toString(), socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendWithAck(ServerProtocolRegistry.TypeProtocol type, String... args) {
        try {
            this.send(type, args);
            String serverResponse = Network.receive(socket);

            Command command = Command.parse(serverResponse);

            ClientProtocolRegistry.TypeProtocol typeProtocol = ClientProtocolRegistry.TypeProtocol.getProtocol(command.getName());

            String response = ClientProtocolRegistry.execute(typeProtocol, command.getArgs(), this);
            if (response != null) {
                if (!response.isEmpty()) {
                    return response;
                }
            } else {
                return serverResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public static void main(String[] args) {
        new Client(null);
    }

}
