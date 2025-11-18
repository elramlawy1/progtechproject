package com.gomoku.ai;

import com.gomoku.model.Board;
import com.gomoku.model.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * Random AI strategy that chooses moves randomly from available positions.
 * This is a simple AI implementation suitable for beginners.
 * Implements the Strategy design pattern.
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
     * Chooses a random move from all valid positions.
     * 
     * @param board the current game board
     * @return a randomly selected valid move, or null if no moves available
     */
    @Override
    public Move chooseMove(Board board) {
        List<Move> validMoves = board.getValidMoves();
        
        if (validMoves.isEmpty()) {
            logger.warn("No valid moves available");
            return null;
        }
        
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
