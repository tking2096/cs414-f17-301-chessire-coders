package edu.colostate.cs.cs414.chesshireCoders.jungleClient.app.game;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.colostate.cs.cs414.chesshireCoders.jungleClient.app.game.GameBoard;

public class TestGameBoard {
	GameBoard testBoard;
	
	@Before
	public void init() {
		testBoard = new GameBoard();
	}
	
	//Currently in development. Check back later.
	@Test
	public void testCheckMove() {
		//check that every piece can be moved by 1;
		
	}
}
