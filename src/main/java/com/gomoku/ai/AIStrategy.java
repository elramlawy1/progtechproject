package com.gomoku.ai;

import com.gomoku.model.Board;
import com.gomoku.model.Move;

/**
 * Strategy interface for AI opponents.
 * This demonstrates the Strategy design pattern - different AI strategies can be implemented.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public interface AIStrategy {
    /**
     * Chooses the next move based on the current board state.
     * 
     * @param board the current game board
     * @return the chosen move
     */
    Move chooseMove(Board board);
    
    /**
     * Gets the name of this strategy.
     * 
     * @return the strategy name
     */
    String getStrategyName();
}
