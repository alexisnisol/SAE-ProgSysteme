package network;

import network.utils.Constant;
import network.utils.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    public Client() {
        try {
            this.socket = new Socket(Constant.SERVER_HOST, Constant.PORT);

            Scanner scanner = new Scanner(System.in);
            boolean quit = false;
            String message;
            while ((message = scanner.nextLine()) != null && !quit) {
                Network.send(message, socket);
                String serverResponse = Network.receive(socket);
                System.out.println(serverResponse);
                if(message.equalsIgnoreCase("QUIT") && serverResponse.equals(Constant.STATUS_OK)) {
                    quit = true;
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }

}
