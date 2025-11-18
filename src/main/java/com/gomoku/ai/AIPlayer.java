package com.gomoku.ai;

import com.gomoku.model.Board;
import com.gomoku.model.Move;

/**
 * AI Player that uses a strategy to choose moves.
 * This demonstrates the Strategy design pattern - the AI can use different strategies.
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
     * Makes the AI choose its next move.
     * 
     * @param board the current game board
     * @return the chosen move
     */
    public Move makeMove(Board board) {
        return strategy.chooseMove(board);
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
