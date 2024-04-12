package lab03;

import java.io.*;
import java.net.Socket;

/**
 * Simple client for demonstrating communication with a server.
 * Client connects to a server using a socket and allows the user to send messages
 * to the server and receive responses.
 */
public class Client1 {

    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 4243;

        try (Socket socket = new Socket(host, port);
             InputStream inpStream = socket.getInputStream();
             OutputStream outStream = socket.getOutputStream();
             BufferedReader rd = new BufferedReader(new InputStreamReader(inpStream));
             BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(outStream))) {

            System.out.println("Client is ready!");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                wr.write(line);
                wr.write('\n');
                wr.flush();

                String response = rd.readLine();
                if (response == null) {
                    break;
                }
                System.out.println(response);
            }
        }
        System.out.println("Client disconnected");
    }
}
