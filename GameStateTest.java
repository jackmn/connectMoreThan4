package connectMoreThan4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameStateTest {
	
	GameState testGameState = new GameState();

	@Test
	void testCreatePattern() {
		String[][] testBoard = testGameState.createPattern();
		
		assertEquals(8, testBoard.length);
		
		assertEquals(19, testBoard[0].length);
		
		assertEquals("|", testBoard[0][0]);
		
		assertEquals(" ", testBoard[0][1]);
		
		assertEquals("-", testBoard[6][2]);
		
		assertEquals("3", testBoard[7][7]);
	}
	
	@Test
	void testGameInitializedVariables() {
		
		assertEquals(null, testGameState.players[0]);
		
		assertEquals(0, testGameState.playersTurn);
		
		assertEquals(true, testGameState.gameReady);
		
		assertEquals(false, testGameState.gameOver);
	}

}
