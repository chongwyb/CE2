import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.Test;

public class TextBuddyTest {
	@Test
	public void testExecutedCommands() {

		// Compare String
		assertEquals("b", TextBuddy.initialSort(new Stack<String>(), "a", "b"));

		// Get File Name
		assertEquals("mytextfile",
				TextBuddy.getFileName(new String[] { "mytextfile" }));

	}
}
