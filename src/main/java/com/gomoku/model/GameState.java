package com.gomoku.model;

/**
 * Represents the current state of the game.
 * Tracks game status and determines if the game is over.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public enum GameState {
    /**
     * Game is in progress.
     */
    PLAYING,
    
    /**
     * Player 1 has won the game.
     */
    PLAYER1_WIN,
    
    /**
     * Player 2 has won the game.
     */
    PLAYER2_WIN,
    
    /**
     * Game ended in a draw (board full, no winner).
     */
    DRAW;
    
    /**
     * Checks if the game is over.
     * 
     * @return true if the game state is not PLAYING
     */
    public boolean isGameOver() {
        return this != PLAYING;
    }
    
    /**
     * Gets a human-readable message for the game state.
     * 
     * @return a message describing the game state
     */
    public String getMessage() {
        switch (this) {
            case PLAYER1_WIN:
                return "Player 1 (X) wins!";
            case PLAYER2_WIN:
                return "Player 2 (O) wins!";
            case DRAW:
                return "Game is a draw!";
            default:
                return "Game in progress...";
        }
    }
}
