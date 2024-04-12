package lab03;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * Simple server implementation for handling client requests.
 */
public class Server {

    private static boolean stopServer = false;

    /**
     * Main method to start the server and handle client connections.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket srvSocket = new ServerSocket(4243);

        while (!stopServer) {
            System.out.println("Server is running!");
            Socket clientSocket = srvSocket.accept();
            if (stopServer) {
                clientSocket.close();
                break;
            }
            Thread serverThread = new ServerThread(clientSocket);
            serverThread.start();
        }
        System.out.println("Not accepting any more clients!");
        srvSocket.close();
    }

    /**
     * Thread handling communication with a client.
     */
    private static class ServerThread extends Thread {

        private Socket clientSocket;

        public void run() {
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                processRequests(rd, wr);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Constructs a new ServerThread instance for a given client socket.
         *
         * @param clientSocket Socket representing the connection to the client
         */
        public ServerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
    }

    /**
     * Processes client requests.
     *
     * @param rd            BufferedReader for reading client messages
     * @param wr            BufferedWriter for writing responses to the client
     * @throws IOException  Exception that may occur during I/O operations
     */
    private static void processRequests(BufferedReader rd, BufferedWriter wr) throws IOException {
        while (true) {
            String line = rd.readLine();
            if (line == null) break;
            System.out.println("Request: " + line);

            if (line.equalsIgnoreCase("quit")) {
                break;
            }
            if (line.equalsIgnoreCase("stopserver")) {
                setStopServer();
                break;
            }

            String[] lineParts = line.split(" ");
            if (lineParts.length == 2) {
                try {
                    int arraySize = Integer.parseInt(lineParts[0]);
                    int numberOfThreads = Integer.parseInt(lineParts[1]);

                    handleMergeSort(arraySize, numberOfThreads, wr);
                } catch (NumberFormatException e) {
                    sendMessage(wr, "undefined");
                }
            } else {
                sendMessage(wr, "undefined");
            }
        }
    }

    /**
     * Sends a message to the client.
     */
    private static void sendMessage(BufferedWriter wr, String message) throws IOException {
        wr.write(message + "\n");
        wr.flush();
    }

    /**
     * Sets the stopServer to true, indicating that the server should stop accepting new clients.
     */
    private static synchronized void setStopServer() {
        System.out.println("stopserver");
        stopServer = true;
    }

    /**
     * Handles the merge sort operation for a given array size and number of threads.
     * It generates an array, sorts it, and measures the sorting time.
     *
     * @param arraySize         The size of the array to be generated and sorted
     * @param numberOfThreads   The number of threads
     * @param wr                BufferedWriter for writing responses to the client
     * @throws IOException      Exception that may occur during I/O operations
     */
    private static void handleMergeSort(int arraySize, int numberOfThreads, BufferedWriter wr) throws IOException {

        int[] myArray = generateRandomArray(arraySize);
        System.out.println("Original array: " + Arrays.toString(myArray));

        ParallelMergeSort myParallMergeSort = new ParallelMergeSort(myArray);
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);

        long startTime = System.currentTimeMillis();
        forkJoinPool.invoke(myParallMergeSort);
        long endTime = System.currentTimeMillis();

        System.out.println("Sorted array: " + Arrays.toString(myParallMergeSort.join()));

        String response = (endTime - startTime) + "ms";
        sendMessage(wr, response);
    }

    /**
     * Generates a random integer array of a specified size.
     * Numbers ranging from -100 to 100.
     */
    public static int[] generateRandomArray(int size) {
        int[] myArray = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            myArray[i] = random.nextInt(201) - 100;
        }
        return myArray;
    }
}
