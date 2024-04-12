package lab02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple server app.
 * It handles client connections, user authentication, and message communication.
 */
public class Server {

    private static String loginUser = null;
    private static boolean login = false;
    private static boolean stopServer = false;

    /**
     * Data structures for storing user records and messages
     */
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, List<String>> messages = new HashMap<>();

    /**
     * Main method to start the server.
     *
     * @throws IOException If an I/O error occurs while managing server connections.
     */
    public static void main(String[] args) throws IOException {

        addUsers();
        ServerSocket srvSocket = new ServerSocket(4243);

        while (!stopServer) {
            System.out.println("<server is running>");
            try (Socket clientSocket = srvSocket.accept();
                 BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
                processRequests(rd, wr);
            }
        }
        System.out.println("<server is not running>");
        srvSocket.close();
    }

    /**
     * Adds a users to the user map.
     */
    private static void addUsers() {
        users.put("David", "1234");
        users.put("Alice", "4321");
        users.put("Bob", "ab12cd");
        users.put("Klára", "0000");
        users.put("Aneta", "admin");
    }

    /**
     * Sends a message to the client.
     */
    private static void sendMessage(BufferedWriter wr, String message) throws IOException {
        wr.write(message + "\n");
        wr.flush();
    }

    /**
     * Checks if a client is logged in.
     * If not, sends a "NEED TO LOGIN" message to the client.
     */
    private static boolean checkLogin(BufferedWriter wr) throws IOException {
        if (!login) {
            sendMessage(wr, "NEED TO LOGIN");
            return false;
        }
        return true;
    }

    /**
     * Checks if the length of an array matches the expected size.
     * If not, sends an "ERR" message to the client.
     */
    private static boolean checkLength(String[] requestParts, int size, BufferedWriter wr) throws IOException {
        if (requestParts.length == size) {
            return true;
        }
        sendMessage(wr, "ERR");
        return false;
    }

    /**
     * A method ensuring communication with the client through a pair of objects of the Reader+Writer class.
     */
    private static void processRequests(BufferedReader rd, BufferedWriter wr) throws IOException {
        while (true) {
            String line = rd.readLine();
            if (line == null) break;

            System.out.println("Request: " + line);
            String[] requestParts = line.split(" ");

            if (requestParts.length >= 1) {
                switch (requestParts[0]) {
                    case "CONNECT":
                        handleConnect(requestParts, wr);
                        break;
                    case "READ":
                        handleRead(requestParts, wr);
                        break;
                    case "MSG":
                        handleSend(requestParts, wr);
                        break;
                    case "LOGOUT":
                        handleLogout(requestParts, wr);
                        break;
                    case "STOP":
                        handleStop(requestParts, wr);
                        break;
                    default:
                        sendMessage(wr, "ERR");

                }
            }
        }
    }

    /**
     * Handles the STOP command, stopping the server.
     */
    private static void handleStop(String[] requestParts, BufferedWriter wr) throws IOException {
        if (checkLogin(wr) && checkLength(requestParts, 1, wr)) {
            sendMessage(wr, "OK");
            stopServer = true;
        }
    }

    /**
     * Handles the LOGOUT command, logging out the current user.
     */
    private static void handleLogout(String[] requestParts, BufferedWriter wr) throws IOException {
        if (checkLogin(wr) && checkLength(requestParts, 1, wr)) {
            loginUser = null;
            login = false;
            sendMessage(wr, "OK");
        }
    }

    /**
     * Handles the CONNECT command, log in a user.
     */
    private static void handleConnect(String[] requestParts, BufferedWriter wr) throws IOException {
        if (checkLength(requestParts, 3, wr)) {
            String username = requestParts[1];
            String password = requestParts[2];

            if (users.containsKey(username.trim()) && users.get(username.trim()).equals(password.trim())) {
                loginUser = username.trim();
                login = true;
                sendMessage(wr, "OK");

            } else {
                sendMessage(wr, "ERR");
            }
        }
    }

    /**
     * Handles the READ command, sends messages to the client.
     */
    private static void handleRead(String[] requestParts, BufferedWriter wr) throws IOException {
        if (checkLogin(wr) && checkLength(requestParts, 1, wr)) {

            List<String> userMessages = messages.get(loginUser);
            StringBuilder response = new StringBuilder();

            if (userMessages == null || userMessages.isEmpty()) {
                response.append("No messages\n");
            } else {
                for (String message : userMessages) {
                    response.append(message).append("\n");
                }
                messages.get(loginUser).clear();
            }
            sendMessage(wr, response.toString());
            sendMessage(wr, "OK");
        }
    }

    /**
     * Handles the MSG command, sends a message to the specified recipient.
     */
    private static void handleSend(String[] requestParts, BufferedWriter wr) throws IOException {
        if (checkLogin(wr)) {

            if (checkSendMessageFormat(requestParts, wr)) {
                String[] recipientParts = requestParts[2].split(":");
                String recipient = recipientParts[0];

                if (users.containsKey(recipient)) {
                    String finalMessage = buildFinalMessage(requestParts);
                    addToUserMessages(recipient, finalMessage);
                    sendMessage(wr, "OK");
                } else {
                    sendMessage(wr, "ERR");
                }
            } else {
                sendMessage(wr, "ERR");
            }
        }
    }

    /**
     * Checks if the MSG command has the correct format.
     * Correct format: MSG FOR [name]: [message]
     */
    private static boolean checkSendMessageFormat(String[] requestParts, BufferedWriter wr) {
        return requestParts.length >= 4 && requestParts[1].equalsIgnoreCase("FOR") && requestParts[2].endsWith(":");
    }

    /**
     * Builds the final message to be sent.
     */
    private static String buildFinalMessage(String[] requestParts) {
        StringBuilder message = new StringBuilder();
        for (int i = 3; i < requestParts.length; i++) {
            message.append(requestParts[i]).append(" ");
        }
        return "FROM " + loginUser + ": " + message.toString().trim();
    }

    /**
     * Adds the final message to the recipient's message list.
     */
    private static void addToUserMessages(String recipient, String finalMessage) {
        if (!messages.containsKey(recipient)) {
            List<String> messageList = new ArrayList<>();
            messageList.add(finalMessage);
            messages.put(recipient, messageList);
        } else {
            List<String> existingMessages = messages.get(recipient);
            existingMessages.add(finalMessage);
            messages.put(recipient, existingMessages);
        }
    }
}


