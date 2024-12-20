package network.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Network {

    private Network() {
    }

    public static String receive(Socket socket) throws IOException {
        InputStreamReader stream = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(stream);
        return reader.readLine();
    }

    public static void send(String message, Socket socket) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(message);
        writer.flush();
    }
}
