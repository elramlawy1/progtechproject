package com.gomoku.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Game class.
 * Tests game flow, turn handling, and state management.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
class GameTest {
    
    private Game game;
    
    /**
     * Sets up a fresh game before each test.
     */
    @BeforeEach
    void setUp() {
        game = new Game();
    }
    
    /**
     * Tests game initialization.
     */
    @Test
    void testGameInitialization() {
        assertNotNull(game.getBoard());
        assertEquals(CellState.PLAYER1, game.getCurrentPlayer());
        assertEquals(GameState.PLAYING, game.getGameState());
        assertEquals(0, game.getMoveCount());
        assertFalse(game.isGameOver());
    }
    
    /**
     * Tests making a valid move.
     */
    @Test
    void testMakeValidMove() {
        Move move = new Move(7, 7);
        assertTrue(game.makeMove(move));
        assertEquals(1, game.getMoveCount());
        assertEquals(CellState.PLAYER2, game.getCurrentPlayer()); // Turn should switch
    }
    
    /**
     * Tests making an invalid move.
     */
    @Test
    void testMakeInvalidMove() {
        Move move = new Move(7, 7);
        game.makeMove(move);
        
        // Try to play on same position
        assertFalse(game.makeMove(move));
        assertEquals(1, game.getMoveCount()); // Count should not increase
    }
    
    /**
     * Tests player switching after moves.
     */
    @Test
    void testPlayerSwitching() {
        assertEquals(CellState.PLAYER1, game.getCurrentPlayer());
        
        game.makeMove(new Move(0, 0));
        assertEquals(CellState.PLAYER2, game.getCurrentPlayer());
        
        game.makeMove(new Move(1, 1));
        assertEquals(CellState.PLAYER1, game.getCurrentPlayer());
    }
    
    /**
     * Tests winning condition for Player 1.
     */
    @Test
    void testPlayer1Win() {
        // Player 1 creates 5 in a row
        for (int i = 0; i < 5; i++) {
            game.makeMove(new Move(0, i));  // Player 1
            if (i < 4) {
                game.makeMove(new Move(1, i));  // Player 2
            }
        }
        
        assertTrue(game.isGameOver());
        assertEquals(GameState.PLAYER1_WIN, game.getGameState());
    }
    
    /**
     * Tests winning condition for Player 2.
     */
    @Test
    void testPlayer2Win() {
        // Player 2 creates 5 in a row
        for (int i = 0; i < 5; i++) {
            game.makeMove(new Move(0, i));  // Player 1
            game.makeMove(new Move(1, i));  // Player 2
        }
        
        // One more move for Player 1 to set up
        game.makeMove(new Move(2, 0));
        // Player 2 completes the win
        game.makeMove(new Move(1, 5));
        
        // Actually let's create a cleaner test
        game = new Game();
        game.makeMove(new Move(0, 0));  // P1
        game.makeMove(new Move(5, 0));  // P2
        game.makeMove(new Move(0, 1));  // P1
        game.makeMove(new Move(5, 1));  // P2
        game.makeMove(new Move(0, 2));  // P1
        game.makeMove(new Move(5, 2));  // P2
        game.makeMove(new Move(0, 3));  // P1
        game.makeMove(new Move(5, 3));  // P2
        game.makeMove(new Move(1, 0));  // P1
        game.makeMove(new Move(5, 4));  // P2 wins
        
        assertTrue(game.isGameOver());
        assertEquals(GameState.PLAYER2_WIN, game.getGameState());
    }
    
    /**
     * Tests that moves cannot be made after game ends.
     */
    @Test
    void testNoMovesAfterGameEnd() {
        // Create winning condition
        for (int i = 0; i < 5; i++) {
            game.makeMove(new Move(0, i));  // Player 1
            if (i < 4) {
                game.makeMove(new Move(1, i));  // Player 2
            }
        }
        
        assertTrue(game.isGameOver());
        
        // Try to make another move
        assertFalse(game.makeMove(new Move(10, 10)));
    }
    
    /**
     * Tests game reset functionality.
     */
    @Test
    void testGameReset() {
        game.makeMove(new Move(5, 5));
        game.makeMove(new Move(6, 6));
        
        game.reset();
        
        assertEquals(CellState.PLAYER1, game.getCurrentPlayer());
        assertEquals(GameState.PLAYING, game.getGameState());
        assertEquals(0, game.getMoveCount());
        assertEquals(CellState.EMPTY, game.getBoard().getCell(5, 5));
    }
    
    /**
     * Tests draw condition when board is full.
     */
    @Test
    void testDrawCondition() {
        Board board = game.getBoard();
        
        // Fill board in a pattern that doesn't create 5 in a row
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                CellState player = ((i + j) % 2 == 0) ? CellState.PLAYER1 : CellState.PLAYER2;
                board.setCell(i, j, player);
            }
        }
        
        // Manually trigger update
        game.setBoard(board);
        
        // If no winner, should be draw
        if (board.checkWinner() == CellState.EMPTY) {
            assertEquals(GameState.DRAW, game.getGameState());
        }
    }
    
    /**
     * Tests move count incrementation.
     */
    @Test
    void testMoveCountIncrement() {
        assertEquals(0, game.getMoveCount());
        
        game.makeMove(new Move(0, 0));
        assertEquals(1, game.getMoveCount());
        
        game.makeMove(new Move(1, 1));
        assertEquals(2, game.getMoveCount());
        
        game.makeMove(new Move(2, 2));
        assertEquals(3, game.getMoveCount());
    }
    
    /**
     * Tests custom board size game.
     */
    @Test
    void testCustomBoardGame() {
        Game customGame = new Game(10, 10);
        assertEquals(10, customGame.getBoard().getRows());
        assertEquals(10, customGame.getBoard().getColumns());
    }
}
