package connectMoreThan4;

import java.io.*;  
import java.net.*;  
import java.util.Scanner;

public class Server implements Runnable{ 
    protected static Socket Client_info;
    protected static GameState currentGame;
    protected static Integer idNumber;

    public Server(Socket clientSocket, GameState globalGame, Integer playerNumber) {
        Server.Client_info = clientSocket;
        Server.currentGame = globalGame;
        Server.idNumber = playerNumber;
    }

    public void run() {
        try {
            DataInputStream FromClient = new DataInputStream(Client_info.getInputStream());
            DataOutputStream ToClientData = new DataOutputStream(Client_info.getOutputStream());
            ObjectOutputStream ToClientObject = new ObjectOutputStream ((Client_info.getOutputStream()));

            String userName = FromClient.readUTF();
            currentGame.players[idNumber] = userName;
            System.out.println("Initial idNumber");
            System.out.println(idNumber);

            boolean loop = true;
            Integer rowPosition;
            boolean firstPlayer = false;

            if(currentGame.players[1] == null){
              currentGame.gameReady = false;
              ToClientData.writeInt(0);
              firstPlayer = true;
            } else {
              currentGame.gameReady = true;
              ToClientData.writeInt(1);
            }
            
            while(currentGame.gameReady == false){
              Thread.sleep(3000);
              //Wait for second player to join
            }

            if(firstPlayer){
              ToClientData.writeInt(1);
            } 

            while(loop) {
              for(int i = 0; i < currentGame.players.length; i++){
                if(currentGame.players[i] == userName){
                  idNumber = i;
                }
              }
                if(idNumber == currentGame.playersTurn){
                    try {
                        if(GameState.gameOver == false){
                          ToClientData.writeInt(1);
                        } else {
                          ToClientData.writeInt(0);
                          ToClientObject.flush();
                          ToClientObject.reset();
                          ToClientObject.writeObject(currentGame.board);
                          break;
                        }
                        printPattern(currentGame.board);
                        ToClientObject.flush();
                        ToClientObject.reset();
                        ToClientObject.writeObject(currentGame.board);
                        Integer check = idNumber;
                        rowPosition=FromClient.readInt();  

                        if (check == 0) {
                          newPiece(currentGame.board, rowPosition, "x");
                        }
                        else {
                          newPiece(currentGame.board, rowPosition, "o");
                        }

                        ToClientObject.flush();
                        ToClientObject.reset();
                        ToClientObject.writeObject(currentGame.board);

                        if (checkWinner(currentGame.board) != null) {
                            String winMessage = "Congratulations " + userName + ", you win!";
                            ToClientData.writeUTF(winMessage);
                            System.out.println("Game Over!");
                            GameState.gameOver = true;
                            loop = false;
                        } else {
                            String waitMessage = "Wait for your next turn " + String.valueOf(currentGame.players[check]) + "...";
                            ToClientData.writeUTF(waitMessage);
                        }
                        
                        if(check == 0){
                            currentGame.playersTurn = 1;
                        } else {
                            currentGame.playersTurn = 0;
                        }

                    } catch (IOException e) {
                        return;
                    }
                }
                Thread.sleep(3000);
            }
        }
        catch(Exception e){
            System.out.println(e);
        } 
        
        try {
			Server.Client_info.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }  

  public static void printPattern(String[][] f) {
    for (int i =0;i<f.length;i++){
      for (int j=0;j<f[i].length;j++){
        System.out.print(f[i][j]);
      }
      System.out.println();
    }
  }

  public static void newPiece(String[][] newBoard, int column, String player){
    for (int i =5;i>=0;i--){
      if (newBoard[i][column] == " "){
        newBoard[i][column] = player;
        break;
      }
    }
  }

  public static String checkWinner(String[][] newBoard) {
    //Check horizontal win condition
    for (int i =0;i<6;i++) {
      for (int j=0;j<7;j+=2) {
        if ((newBoard[i][j+1] != " ")
        && (newBoard[i][j+3] != " ")
        && (newBoard[i][j+5] != " ")
        && (newBoard[i][j+7] != " ")
        && (newBoard[i][j+9] != " ")
        && ((newBoard[i][j+1] == newBoard[i][j+3])
        && (newBoard[i][j+3] == newBoard[i][j+5])
        && (newBoard[i][j+5] == newBoard[i][j+7])
        && (newBoard[i][j+7] == newBoard[i][j+9])))

          return newBoard[i][j+1]; 
      }
    }

    //Check vertical win condition
    for (int i=1;i<19;i+=2) {
      for (int j =0;j<4;j++) {
            if((newBoard[j][i] != " ")
            && (newBoard[j+1][i] != " ")
            && (newBoard[j+2][i] != " ")
            && (newBoard[j+3][i] != " ")
            && (newBoard[j+4][i] != " ")
            && ((newBoard[j][i] == newBoard[j+1][i])
            && (newBoard[j+1][i] == newBoard[j+2][i])
            && (newBoard[j+2][i] == newBoard[j+3][i])
            && (newBoard[j+3][i] == newBoard[j+4][i])))
              return newBoard[j][i]; 
      } 
    }

    for (int i=0;i<2;i++) {
      for (int j=1;j<15;j+=2) {
            if((newBoard[i][j] != " ")
            && (newBoard[i+1][j+2] != " ")
            && (newBoard[i+2][j+4] != " ")
            && (newBoard[i+3][j+6] != " ")
            && (newBoard[i+4][j+8] != " ")
            && ((newBoard[i][j] == newBoard[i+1][j+2])
            && (newBoard[i+1][j+2] == newBoard[i+2][j+4])
            && (newBoard[i+2][j+4] == newBoard[i+3][j+6])
            && (newBoard[i+3][j+6] == newBoard[i+4][j+8])))
              return newBoard[i][j]; 
      } 
    }

    for (int i=0;i<2;i++) {
      for (int j=5;j<19;j+=2) {
            if((newBoard[i][j] != " ")
            && (newBoard[i+1][j-2] != " ")
            && (newBoard[i+2][j-4] != " ")
            && (newBoard[i+3][j-6] != " ")
            && (newBoard[i+4][j-8] != " ")
            && ((newBoard[i][j] == newBoard[i+1][j-2])
            && (newBoard[i+1][j-2] == newBoard[i+2][j-4])
            && (newBoard[i+2][j-4] == newBoard[i+3][j-6])
            && (newBoard[i+3][j-6] == newBoard[i+4][j-8])))
              return newBoard[i][j]; 
      } 
    }
    return null;
  }
}  