package com.gomoku.ai;

import com.gomoku.model.Board;
import com.gomoku.model.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * SIMPLE: Random AI that picks any empty cell randomly.
 * 
 * HOW IT WORKS:
 * 1. Get list of all empty cells on the board
 * 2. Pick one randomly
 * 3. Return that move
 * 
 * IMPLEMENTS STRATEGY PATTERN: This class implements the AIStrategy interface.
 * We could easily create a SmartAIStrategy later and swap it in!
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class RandomAIStrategy implements AIStrategy {
    private static final Logger logger = LogManager.getLogger(RandomAIStrategy.class);
    private final Random random;
    
    /**
     * Creates a new random AI strategy with default random seed.
     */
    public RandomAIStrategy() {
        this.random = new Random();
    }
    
    /**
     * Creates a new random AI strategy with specified seed (for testing).
     * 
     * @param seed the random seed
     */
    public RandomAIStrategy(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * SIMPLE: Pick a random empty cell from the board.
     * 
     * HOW IT WORKS:
     * 1. Ask board for list of all empty positions
     * 2. Generate random number from 0 to (number of empty cells - 1)
     * 3. Pick the cell at that random index
     * 
     * EXAMPLE: If there are 10 empty cells, pick number between 0-9,
     *          return the cell at that position in the list.
     * 
     * @param board the current game board
     * @return a randomly selected empty position, or null if board is full
     */
    @Override
    public Move chooseMove(Board board) {
        List<Move> validMoves = board.getValidMoves();  // Get all empty cells
        
        // If no empty cells, return null
        if (validMoves.isEmpty()) {
            logger.warn("No valid moves available");
            return null;
        }
        
        // Pick a random cell from the list
        Move chosenMove = validMoves.get(random.nextInt(validMoves.size()));
        logger.info("AI chose move: {}", chosenMove);
        return chosenMove;
    }
    
    /**
     * Gets the name of this strategy.
     * 
     * @return "Random AI"
     */
    @Override
    public String getStrategyName() {
        return "Random AI";
    }
}
