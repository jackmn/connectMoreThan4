package connectMoreThan4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ServerThreadTest {

	ServerThread testServerThread = new ServerThread();

	@Test
	void testThreadInitializedVariables() {
		
		assertEquals(6666, testServerThread.PORT);
		
	}

}
