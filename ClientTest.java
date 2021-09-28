package connectMoreThan4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientTest {
	
	Client testClient = new Client();
	GameState testGameState = new GameState();
	
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	@BeforeEach
	public void setUp() {
	    System.setOut(new PrintStream(outputStreamCaptor));
	}
	
	@AfterEach
	public void tearDown() {
	    System.setOut(standardOut);
	}

	@Test
	void testDropPiece() {
		
		//Random case
		String input = "3";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    
	    assertEquals(7, testClient.dropPiece());
	    
	    //Edge case 1
  		input = "0";
  	    in = new ByteArrayInputStream(input.getBytes());
  	    System.setIn(in);
  	    
  	    assertEquals(1, testClient.dropPiece());
	  	    
	  	//Edge case 2
		input = "9";
	    in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    
	    assertEquals(19, testClient.dropPiece());  
	}
	
	@Test
	void testClientPrintPattern() {
		
		String[][] testBoard = testGameState.createPattern();
		
		testClient.printPattern(testBoard);
		
		assertNotEquals(null, outputStreamCaptor.toString().trim());
	}
	
}
