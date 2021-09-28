package connectMoreThan4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerTest {
	
	protected static Socket Client_info;
    protected static GameState currentGame;
    protected static Integer idNumber;
    
	Server testServer = new Server(Client_info, currentGame, idNumber);
	GameState testGameState = new GameState();
	
	public String[][] testBoard;
	
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	@BeforeEach
	public void setUp() {
	    System.setOut(new PrintStream(outputStreamCaptor));
	    testBoard = testGameState.createPattern();
	}
	
	@AfterEach
	public void tearDown() {
	    System.setOut(standardOut);
	}
	
	@Test
	void testServerPrintPattern() {
		
		testServer.printPattern(testBoard);
		
		assertNotEquals(null, outputStreamCaptor.toString().trim());
	}
	
	@Test
	void testCheckWinner() {
		
		testBoard[5][1] = "x";
		testBoard[5][3] = "x";
		testBoard[5][5] = "x";
		testBoard[5][7] = "x";
		assertNotEquals("x", testServer.checkWinner(testBoard));
		testBoard[5][9] = "x";
		assertEquals("x", testServer.checkWinner(testBoard));
		
		testBoard = testGameState.createPattern();
		testBoard[5][1] = "x";
		testBoard[4][1] = "x";
		testBoard[3][1] = "x";
		testBoard[2][1] = "x";
		assertNotEquals("x", testServer.checkWinner(testBoard));
		testBoard[1][1] = "x";
		assertEquals("x", testServer.checkWinner(testBoard));
		
		testBoard = testGameState.createPattern();
		testBoard[5][1] = "x";
		testBoard[4][3] = "x";
		testBoard[3][5] = "x";
		testBoard[2][7] = "x";
		assertNotEquals("x", testServer.checkWinner(testBoard));
		testBoard[1][9] = "x";
		assertEquals("x", testServer.checkWinner(testBoard));
		
		testBoard = testGameState.createPattern();
		testBoard[5][9] = "x";
		testBoard[4][7] = "x";
		testBoard[3][5] = "x";
		testBoard[2][3] = "x";
		assertNotEquals("x", testServer.checkWinner(testBoard));
		testBoard[1][1] = "x";
		assertEquals("x", testServer.checkWinner(testBoard));
	}

}