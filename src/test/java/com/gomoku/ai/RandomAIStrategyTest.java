package com.gomoku.ai;

import com.gomoku.model.Board;
import com.gomoku.model.CellState;
import com.gomoku.model.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RandomAIStrategy class.
 * Tests AI move selection and strategy behavior.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
class RandomAIStrategyTest {
    
    private RandomAIStrategy strategy;
    private Board board;
    
    /**
     * Sets up test fixtures before each test.
     */
    @BeforeEach
    void setUp() {
        strategy = new RandomAIStrategy(12345); // Use seed for reproducibility
        board = new Board(15, 15);
    }
    
    /**
     * Tests that AI chooses a valid move.
     */
    @Test
    void testChooseValidMove() {
        Move move = strategy.chooseMove(board);
        
        assertNotNull(move);
        assertTrue(board.isValidMove(move));
    }
    
    /**
     * Tests AI with nearly full board.
     */
    @Test
    void testChooseMoveOnNearlyFullBoard() {
        // Fill board except one cell
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                if (!(i == 7 && j == 7)) {
                    board.setCell(i, j, CellState.PLAYER1);
                }
            }
        }
        
        Move move = strategy.chooseMove(board);
        assertNotNull(move);
        assertEquals(7, move.getRow());
        assertEquals(7, move.getColumn());
    }
    
    /**
     * Tests AI returns null when no moves available.
     */
    @Test
    void testChooseMoveOnFullBoard() {
        // Fill entire board
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                board.setCell(i, j, CellState.PLAYER1);
            }
        }
        
        Move move = strategy.chooseMove(board);
        assertNull(move);
    }
    
    /**
     * Tests strategy name.
     */
    @Test
    void testStrategyName() {
        assertEquals("Random AI", strategy.getStrategyName());
    }
    
    /**
     * Tests that AI chooses different moves (with different seeds).
     */
    @Test
    void testRandomness() {
        RandomAIStrategy strategy1 = new RandomAIStrategy(111);
        RandomAIStrategy strategy2 = new RandomAIStrategy(222);
        
        Move move1 = strategy1.chooseMove(board);
        Move move2 = strategy2.chooseMove(board);
        
        // Moves might be same by chance, but strategies are different
        assertNotNull(move1);
        assertNotNull(move2);
    }
    
    /**
     * Tests AIPlayer with strategy.
     */
    @Test
    void testAIPlayer() {
        AIPlayer aiPlayer = new AIPlayer(strategy);
        
        assertEquals("Random AI", aiPlayer.getStrategyName());
        
        Move move = aiPlayer.makeMove(board);
        assertNotNull(move);
        assertTrue(board.isValidMove(move));
    }
    
    /**
     * Tests changing AI strategy.
     */
    @Test
    void testChangeStrategy() {
        AIPlayer aiPlayer = new AIPlayer(strategy);
        RandomAIStrategy newStrategy = new RandomAIStrategy();
        
        aiPlayer.setStrategy(newStrategy);
        assertEquals(newStrategy, aiPlayer.getStrategy());
    }
}
