package com.gomoku.model;

/**
 * Represents a cell state on the game board.
 * Each cell can be empty, contain Player 1's piece, or contain Player 2's piece.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public enum CellState {
    /**
     * Represents an empty cell with no piece.
     */
    EMPTY,
    
    /**
     * Represents a cell occupied by Player 1 (Human player).
     */
    PLAYER1,
    
    /**
     * Represents a cell occupied by Player 2 (AI opponent).
     */
    PLAYER2;
    
    /**
     * Converts the cell state to a character for display.
     * 
     * @return '.' for EMPTY, 'X' for PLAYER1, 'O' for PLAYER2
     */
    public char toChar() {
        switch (this) {
            case PLAYER1:
                return 'X';
            case PLAYER2:
                return 'O';
            default:
                return '.';
        }
    }
    
    /**
     * Creates a CellState from a character.
     * 
     * @param c the character to convert ('X', 'O', or '.')
     * @return the corresponding CellState
     */
    public static CellState fromChar(char c) {
        switch (c) {
            case 'X':
                return PLAYER1;
            case 'O':
                return PLAYER2;
            default:
                return EMPTY;
        }
    }
}
