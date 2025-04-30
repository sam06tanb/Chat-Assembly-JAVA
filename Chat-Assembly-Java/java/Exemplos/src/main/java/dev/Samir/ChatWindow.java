package dev.Samir;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatWindow extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String nickname;

    public ChatWindow(String serverIP, int port) {
        setTitle("Java Chat Test");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        sendButton = new JButton("Send");

        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);
        add(panel, BorderLayout.SOUTH);

        applyDarkMode();

        try {
            socket = new Socket(serverIP, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            nickname = JOptionPane.showInputDialog(this, "Enter your nickname:");
            writer.println("/nickname " + nickname);

            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = reader.readLine()) != null) {
                        chatArea.append(receivedMessage + "\n");
                    }
                } catch (IOException e) {
                    chatArea.append("Connection closed.\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to the server: " + e.getMessage());
            System.exit(0);
        }

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            inputField.setText("");
        }
    }

    private void applyDarkMode() {
        Color background = new Color(30, 30, 30);
        Color foreground = new Color(200, 200, 200);
        Color accent = new Color(50, 50, 50);

        chatArea.setBackground(background);
        chatArea.setForeground(foreground);

        inputField.setBackground(accent);
        inputField.setForeground(foreground);

        sendButton.setBackground(accent);
        sendButton.setForeground(foreground);

        getContentPane().setBackground(background);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String serverIP = JOptionPane.showInputDialog("Enter the Server IP:");
            new ChatWindow(serverIP, 12345).setVisible(true);
        });
    }
}
