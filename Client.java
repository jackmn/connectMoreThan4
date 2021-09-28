package connectMoreThan4;

import java.io.*;  
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {  
        System.out.println("What is your name? "); 
        Scanner userName = new Scanner (System.in);
        String playerName = userName.nextLine();

        try{      
            Socket Server_info=new Socket("localhost",6666);  
            ObjectInputStream FromServerObject = new ObjectInputStream ((Server_info.getInputStream()));
            DataInputStream FromServerData = new DataInputStream ((Server_info.getInputStream()));
            DataOutputStream ToServer = new DataOutputStream(Server_info.getOutputStream()); 
            boolean loop = true;
            String[][] board = new String[8][19];

            ToServer.writeUTF(playerName);
            Integer gameStatus = FromServerData.readInt();
            
            if(gameStatus == 0){
                System.out.println("Waiting for a second player...");
            } else {
                System.out.println("You are moving second, wait for the other player to make their move...");
            }

            while(gameStatus == 0){
                gameStatus = FromServerData.readInt();
                //Wait for it to change
            }
            

            while(loop){
                try {
                    if(FromServerData.readInt() == 0){
                        String loseMessage = "Sorry "+playerName+", you lose.";
                        System.out.println(loseMessage);
                        board = (String[][]) FromServerObject.readObject();
                        printPattern(board);
                        break;
                    }
                    board = (String[][]) FromServerObject.readObject();
                    printPattern(board);
                    ToServer.writeInt(dropPiece()); 
                    board = (String[][]) FromServerObject.readObject();
                    printPattern(board);
                    String message = FromServerData.readUTF();  
                    System.out.println(message);
                    
                } catch (IOException e) {
                    return;
                }
                
                
            }
            System.out.println("Game Over!");
            ToServer.close();  
            Server_info.close();  
        }
        catch(Exception e){
            System.out.println(e);
        }  
    }  

    public static void printPattern(String[][] board){
        for (int i =0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++){
                System.out.print(board[i][j]);
            }
        System.out.println();
        }
    }

  public static int dropPiece() {
    System.out.println("Choose a column to make your move: "); 
    Scanner userInput = new Scanner (System.in);
    int userMove = 2*userInput.nextInt()+1;
    return userMove;
  }
}  
