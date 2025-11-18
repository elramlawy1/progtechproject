package com.gomoku.ai;

import com.gomoku.model.Board;
import com.gomoku.model.Move;

/**
 * SIMPLE: Interface for AI strategies (STRATEGY PATTERN).
 * 
 * WHY USE THIS: We might want different AI types:
 * - RandomAI (picks randomly) ← we have this one!
 * - SmartAI (blocks opponent, tries to win)
 * - DefensiveAI (focuses on blocking)
 * 
 * STRATEGY PATTERN: Define an interface, different AIs implement it.
 * Then we can easily swap one AI for another without changing other code!
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
