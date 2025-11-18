package com.gomoku.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board for Gomoku.
 * The board is a rectangular grid of NxM cells where players place their pieces.
 * This class handles board state, move validation, and win detection.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class Board {
    private static final Logger logger = LogManager.getLogger(Board.class);
    private static final int DEFAULT_ROWS = 15;
    private static final int DEFAULT_COLS = 15;
    private static final int WIN_COUNT = 5; // 5 in a row to win
    
    private final int rows;
    private final int columns;
    private final CellState[][] grid;
    
    /**
     * Creates a new board with default size (15x15).
     */
    public Board() {
        this(DEFAULT_ROWS, DEFAULT_COLS);
    }
    
    /**
     * Creates a new board with specified dimensions.
     * 
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new CellState[rows][columns];
        initializeBoard();
        logger.info("Created new board with size {}x{}", rows, columns);
    }
    
    /**
     * Initializes all cells to EMPTY state.
     */
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = CellState.EMPTY;
            }
        }
    }
    
    /**
     * Gets the number of rows on the board.
     * 
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Gets the number of columns on the board.
     * 
     * @return the number of columns
     */
    public int getColumns() {
        return columns;
    }
    
    /**
     * Gets the state of a cell at the specified position.
     * 
     * @param row the row index
     * @param column the column index
     * @return the cell state
     */
    public CellState getCell(int row, int column) {
        if (!isValidPosition(row, column)) {
            return CellState.EMPTY;
        }
        return grid[row][column];
    }
    
    /**
     * Sets the state of a cell at the specified position.
     * 
     * @param row the row index
     * @param column the column index
     * @param state the new cell state
     */
    public void setCell(int row, int column, CellState state) {
        if (isValidPosition(row, column)) {
            grid[row][column] = state;
            logger.debug("Cell set at ({}, {}) to {}", row, column, state);
        }
    }
    
    /**
     * Checks if a position is within board boundaries.
     * 
     * @param row the row index
     * @param column the column index
     * @return true if the position is valid
     */
    public boolean isValidPosition(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
    
    /**
     * Checks if a move is valid (position is valid and cell is empty).
     * 
     * @param move the move to validate
     * @return true if the move is valid
     */
    public boolean isValidMove(Move move) {
        return isValidPosition(move.getRow(), move.getColumn()) 
               && grid[move.getRow()][move.getColumn()] == CellState.EMPTY;
    }
    
    /**
     * Places a piece on the board.
     * 
     * @param move the move to make
     * @param player the player making the move
     * @return true if the move was successful
     */
    public boolean makeMove(Move move, CellState player) {
        if (!isValidMove(move)) {
            logger.warn("Invalid move attempted: {}", move);
            return false;
        }
        grid[move.getRow()][move.getColumn()] = player;
        logger.info("Move made at {} by {}", move, player);
        return true;
    }
    
    /**
     * Gets a list of all valid (empty) positions on the board.
     * 
     * @return list of valid moves
     */
    public List<Move> getValidMoves() {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] == CellState.EMPTY) {
                    validMoves.add(new Move(i, j));
                }
            }
        }
        return validMoves;
    }
    
    /**
     * Checks if there is a winner on the board.
     * 
     * @return the winning player, or EMPTY if no winner
     */
    public CellState checkWinner() {
        // Check all positions for potential winning sequences
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                CellState cell = grid[row][col];
                if (cell != CellState.EMPTY) {
                    if (checkWinFrom(row, col, cell)) {
                        logger.info("Winner found: {}", cell);
                        return cell;
                    }
                }
            }
        }
        return CellState.EMPTY;
    }
    
    /**
     * Checks if there's a winning sequence starting from a position.
     * 
     * @param row starting row
     * @param col starting column
     * @param player the player to check for
     * @return true if a winning sequence exists
     */
    private boolean checkWinFrom(int row, int col, CellState player) {
        // Check horizontal
        if (checkDirection(row, col, 0, 1, player)) return true;
        // Check vertical
        if (checkDirection(row, col, 1, 0, player)) return true;
        // Check diagonal (top-left to bottom-right)
        if (checkDirection(row, col, 1, 1, player)) return true;
        // Check anti-diagonal (top-right to bottom-left)
        if (checkDirection(row, col, 1, -1, player)) return true;
        
        return false;
    }
    
    /**
     * Checks for WIN_COUNT consecutive pieces in a direction.
     * 
     * @param row starting row
     * @param col starting column
     * @param deltaRow row direction
     * @param deltaCol column direction
     * @param player the player to check for
     * @return true if WIN_COUNT consecutive pieces found
     */
    private boolean checkDirection(int row, int col, int deltaRow, int deltaCol, CellState player) {
        int count = 0;
        for (int i = 0; i < WIN_COUNT; i++) {
            int r = row + i * deltaRow;
            int c = col + i * deltaCol;
            if (isValidPosition(r, c) && grid[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
        return count >= WIN_COUNT;
    }
    
    /**
     * Checks if the board is full (no valid moves remaining).
     * 
     * @return true if the board is full
     */
    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] == CellState.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Clears the board, setting all cells to EMPTY.
     */
    public void clear() {
        initializeBoard();
        logger.info("Board cleared");
    }
    
    /**
     * Creates a string representation of the board.
     * 
     * @return the board as a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Column numbers
        sb.append("   ");
        for (int j = 0; j < columns; j++) {
            sb.append(String.format("%2d ", j));
        }
        sb.append("\n");
        
        // Board content
        for (int i = 0; i < rows; i++) {
            sb.append(String.format("%2d ", i));
            for (int j = 0; j < columns; j++) {
                sb.append(" ").append(grid[i][j].toChar()).append(" ");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Gets the board state as a 2D array.
     * 
     * @return copy of the board grid
     */
    public CellState[][] getGrid() {
        CellState[][] copy = new CellState[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, columns);
        }
        return copy;
    }
}
