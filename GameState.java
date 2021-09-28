package connectMoreThan4;

import java.io.*;  
import java.net.*;  
import java.util.Scanner;

public class GameState {

    public String[][] board = createPattern();

    public String[] players = new String[2];

    public Integer playersTurn = 0;

    public Boolean gameReady = true;

    public static Boolean gameOver = false;

    public static String[][] createPattern() {

     String[][] newBoard = new String[8][19];

      for (int i =0;i<newBoard.length;i++){

        for (int j =0;j<newBoard[i].length;j++){

          if (j% 2 == 0) newBoard[i][j] ="|";
          else newBoard[i][j] = " ";

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
}