package lab02;

import java.io.*;
import java.net.Socket;

/**
 * Simple client app to connect to the server.
 */
public class Client {

    /**
     * Main method to run the client app.
     * Creates BufferedReader and BufferedWriter for reading from and writing to the server.
     * Creates BufferedReader for reading user input from the console.
     *
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 4243;

        try (Socket socket = new Socket(host, port);
             InputStream inpStream = socket.getInputStream();
             OutputStream outStream = socket.getOutputStream();

             BufferedReader rd = new BufferedReader(new InputStreamReader(inpStream));
             BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(outStream))) {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("<client connected>");
            handleCommunication(wr, rd, in);
        }
    }


    /**
     * Handles the communication between the client and the server.
     *
     * @param wr The BufferedWriter for writing to the server.
     * @param rd The BufferedReader for reading from the server.
     * @param in The BufferedReader for reading user input from the console.
     * @throws IOException If an I/O error occurs.
     */
    private static void handleCommunication(BufferedWriter wr, BufferedReader rd, BufferedReader in) throws IOException {

        while (true) {
            String line = in.readLine();
            wr.write(line);
            wr.newLine();
            wr.flush();

            String resp = rd.readLine();

            if (line.equalsIgnoreCase("READ") && !resp.equalsIgnoreCase("NEED TO LOGIN")) {
                while (!resp.equalsIgnoreCase("OK")) {
                    System.out.println(resp);
                    resp = rd.readLine();
                    if (resp == null) {
                        break;
                    }
                }
            }
            if ((line.equalsIgnoreCase("LOGOUT") || line.equalsIgnoreCase("STOP")) && (resp != null && resp.equalsIgnoreCase("OK"))) {
                break;
            }
            if (resp == null) {
                break;
            }
            System.out.println(resp);
        }
        System.out.println("<client disconnected>");
    }
}