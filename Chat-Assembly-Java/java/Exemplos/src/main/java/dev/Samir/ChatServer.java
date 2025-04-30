package dev.Samir;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected!");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message) {
        for (ClientHandler clientHandler : clients) {
            clientHandler.send(message);
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;
        private String nickname = "Anonymous";

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.writer = new PrintWriter(socket.getOutputStream(), true);
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String message) {
            writer.println(message);
        }

        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("/nickname ")) {
                        nickname = message.substring(10);
                        broadcast("*** " + nickname + " joined the chat ***");
                    } else {
                        broadcast(nickname + ": " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println(nickname + " disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
                broadcast("*** " + nickname + " left the chat ***");
            }
        }
    }
}
