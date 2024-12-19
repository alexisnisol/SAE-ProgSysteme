package network;

import network.utils.Constant;
import network.utils.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    public String extractPlayerName(String serverResponse) {
        if (serverResponse.startsWith("ASK ")) {
            String[] parts = serverResponse.split(" ");
            if (parts.length >= 2) {
                return parts[1]; 
            }
        }
        return null; 
    }
    

    public Client() {
        try {
            this.socket = new Socket(Constant.SERVER_HOST, Constant.PORT);
            System.out.println("Connecté au serveur " + Constant.SERVER_HOST + ":" + Constant.PORT);

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

                if (serverResponse.startsWith("ASK ")) {
                    System.out.println("Voulez-vous accepter ? (ACCEPT/REFUSE + nom adversaire)");
            
                    String answer = scanner.nextLine();
                    if (answer.startsWith("ACCEPT")) {
                        Network.send("ACCEPT " + extractPlayerName(serverResponse), socket);
                    } else {
                        Network.send("REFUSE " + extractPlayerName(serverResponse), socket);
                    }
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
