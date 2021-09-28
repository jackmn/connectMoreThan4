package connectMoreThan4;

import java.io.*;  
import java.net.*; 

public class ServerThread {
    static final int PORT = 6666;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        Integer numConnections = 0;
        GameState currentGame = new GameState();
        while (numConnections < 2) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            Thread newThread = new Thread(new Server(socket, currentGame, numConnections));
            newThread.start();
            numConnections++;
            
        }
    }
}