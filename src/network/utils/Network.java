package network.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Network {

    private Network() {
    }

    public static String receive(Socket socket) {
        String message = "";
        try {
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(stream);
            message = reader.readLine();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static void send(String message, Socket socket) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(message);
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
