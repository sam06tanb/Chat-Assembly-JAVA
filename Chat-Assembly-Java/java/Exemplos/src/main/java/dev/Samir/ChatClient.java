package dev.Samir;

import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            System.out.println("Connected to the server");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            new Thread(() -> {
                String receivedMessages;
                try {
                    while ((receivedMessages = input.readLine()) != null) {
                        System.out.println("Server: " + receivedMessages);
                    }
                } catch (IOException e) {
                    System.out.println("Server disconnected.");
                }
            }).start();

            String message;
            while ((message = keyboard.readLine()) != null) {
                output.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
