package network;

import network.utils.Constant;
import network.utils.Network;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket socket;

    public Client() {
        try {
            this.socket = new Socket(Constant.SERVER_HOST, Constant.PORT);
            Network.sendMessage("connect Player"+(int)(Math.random()*100), socket);
            System.out.println(Network.getMessage(socket));

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }

}
