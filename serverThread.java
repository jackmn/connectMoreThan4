import java.io.*;  
import java.net.*; 

public class serverThread {
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
        gameState currentGame = new gameState();
        while (numConnections < 2) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            Thread newThread = new Thread(new server(socket, currentGame, numConnections));
            newThread.start();
            numConnections++;
            
        }
    }
}