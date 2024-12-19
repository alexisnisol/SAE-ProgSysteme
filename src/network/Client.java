package network;

import network.utils.Constant;
import network.utils.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private boolean quit;

    public Client() {
        quit = false;
        try {
            this.socket = new Socket(Constant.SERVER_HOST, Constant.PORT);

            Thread sendThread = new Thread(() -> {
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

            Thread receiveThread = new Thread(() -> {
                try {
                    while(!quit) {
                        String serverResponse = Network.receive(socket);
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    System.out.println("Server disconnected");
                    quit = true;
                }
            });

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();

            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }

}
