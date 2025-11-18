package com.gomoku.ai;

import com.gomoku.model.Board;
import com.gomoku.model.Move;

/**
 * SIMPLE: AI Player that uses a strategy to choose moves.
 * 
 * STRATEGY PATTERN EXAMPLE:
 * - AIPlayer doesn't know HOW to pick moves
 * - It just asks its strategy "what move should I make?"
 * - We can swap strategies easily: aiPlayer.setStrategy(new SmartAI())
 * 
 * This is like having a chess player who can switch between different
 * playing styles without changing the player itself!
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class AIPlayer {
    private AIStrategy strategy;
    
    /**
     * Creates an AI player with the specified strategy.
     * 
     * @param strategy the AI strategy to use
     */
    public AIPlayer(AIStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Gets the current strategy.
     * 
     * @return the AI strategy
     */
    public AIStrategy getStrategy() {
        return strategy;
    }
    
    /**
     * Sets a new strategy for the AI.
     * 
     * @param strategy the new AI strategy
     */
    public void setStrategy(AIStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * SIMPLE: Ask the strategy to choose a move.
     * The AIPlayer doesn't decide - it delegates to its strategy!
     * 
     * @param board the current game board
     * @return the move chosen by the strategy
     */
    public Move makeMove(Board board) {
        return strategy.chooseMove(board);  // Ask strategy: "what move should I make?"
    }
    
    /**
     * Gets the name of the current strategy.
     * 
     * @return the strategy name
     */
    public String getStrategyName() {
        return strategy.getStrategyName();
    }
}
