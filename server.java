import java.io.*;  
import java.net.*;  
import java.util.Scanner;

public class server implements Runnable{ 
    protected static Socket Client_info;
    protected static gameState currentGame;
    protected static Integer idNumber;

    public server(Socket clientSocket, gameState globalGame, Integer playerNumber) {
        server.Client_info = clientSocket;
        server.currentGame = globalGame;
        server.idNumber = playerNumber;
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
                        if(gameState.gameOver == false){
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
                            if (checkWinner(currentGame.board) == "x") {
                                String winMessage = "Congratulations " + userName + ", you win!";
                                ToClientData.writeUTF(winMessage);
                                System.out.println("Game Over!");
                                gameState.gameOver = true;
                            }
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
        
    }  

    public static String[][] createPattern() {
      String[][] newBoard = new String[8][19];

      for (int i =0;i<newBoard.length;i++) {

        for (int j =0;j<newBoard[i].length;j++) {
          if (j% 2 == 0) {
            newBoard[i][j] ="|";
          } else {
            newBoard[i][j] = " ";
          }

          if (i==6) {
            newBoard[i][j]= "-";
          }

          if (i==7 & j% 2 != 0) {
            newBoard[i][j]= String.valueOf((j-1)/2);
          }
        }

      }
      return newBoard;
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

  public static String checkWinner(String[][] f) {
    //Check horizontal win condition
    for (int i =0;i<6;i++) {
      for (int j=0;j<7;j+=2) {
        if ((f[i][j+1] != " ")
        && (f[i][j+3] != " ")
        && (f[i][j+5] != " ")
        && (f[i][j+7] != " ")
        && (f[i][j+9] != " ")
        && ((f[i][j+1] == f[i][j+3])
        && (f[i][j+3] == f[i][j+5])
        && (f[i][j+5] == f[i][j+7])
        && (f[i][j+7] == f[i][j+9])))

          return f[i][j+1]; 
      }
    }

    //Check vertical win condition
    for (int i=1;i<19;i+=2) {
      for (int j =0;j<4;j++) {
            if((f[j][i] != " ")
            && (f[j+1][i] != " ")
            && (f[j+2][i] != " ")
            && (f[j+3][i] != " ")
            && (f[j+4][i] != " ")
            && ((f[j][i] == f[j+1][i])
            && (f[j+1][i] == f[j+2][i])
            && (f[j+2][i] == f[j+3][i])
            && (f[j+3][i] == f[j+4][i])))
              return f[j][i]; 
      } 
    }

    for (int i=0;i<2;i++) {
      for (int j=1;j<15;j+=2) {
            if((f[i][j] != " ")
            && (f[i+1][j+2] != " ")
            && (f[i+2][j+4] != " ")
            && (f[i+3][j+6] != " ")
            && (f[i+4][j+8] != " ")
            && ((f[i][j] == f[i+1][j+2])
            && (f[i+1][j+2] == f[i+2][j+4])
            && (f[i+2][j+4] == f[i+3][j+6])
            && (f[i+3][j+6] == f[i+4][j+8])))
              return f[i][j]; 
      } 
    }

    for (int i=0;i<2;i++) {
      for (int j=5;j<19;j+=2) {
            if((f[i][j] != " ")
            && (f[i+1][j-2] != " ")
            && (f[i+2][j-4] != " ")
            && (f[i+3][j-6] != " ")
            && (f[i+4][j-8] != " ")
            && ((f[i][j] == f[i+1][j-2])
            && (f[i+1][j-2] == f[i+2][j-4])
            && (f[i+2][j-4] == f[i+3][j-6])
            && (f[i+3][j-6] == f[i+4][j-8])))
              return f[i][j]; 
      } 
    }
    return null;
  }
}  