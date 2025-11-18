package com.gomoku.model;

/**
 * Represents a move in the Gomoku game.
 * A move consists of a row and column position on the board.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class Move {
    private final int row;
    private final int column;
    
    /**
     * Creates a new move at the specified position.
     * 
     * @param row the row position (0-based index)
     * @param column the column position (0-based index)
     */
    public Move(int row, int column) {
        this.row = row;
        this.column = column;
    }
    
    /**
     * Gets the row position of this move.
     * 
     * @return the row index
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Gets the column position of this move.
     * 
     * @return the column index
     */
    public int getColumn() {
        return column;
    }
    
    /**
     * Returns a string representation of this move.
     * 
     * @return a string in the format "Move(row, column)"
     */
    @Override
    public String toString() {
        return "Move(" + row + ", " + column + ")";
    }
    
    /**
     * Checks if this move is equal to another object.
     * 
     * @param obj the object to compare with
     * @return true if the object is a Move with the same position
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return row == move.row && column == move.column;
    }
    
    /**
     * Generates a hash code for this move.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return 31 * row + column;
    }
}
