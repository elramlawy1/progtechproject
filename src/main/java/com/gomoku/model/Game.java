package com.gomoku.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main game logic class for Gomoku.
 * Manages the game flow, turn handling, and game state updates.
 * Demonstrates the Facade design pattern by providing a simple interface to complex game logic.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class Game {
    private static final Logger logger = LogManager.getLogger(Game.class);
    
    private Board board;
    private CellState currentPlayer;
    private GameState gameState;
    private int moveCount;
    
    /**
     * Creates a new game with default board size.
     */
    public Game() {
        this(15, 15);
    }
    
    /**
     * Creates a new game with specified board dimensions.
     * 
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public Game(int rows, int columns) {
        this.board = new Board(rows, columns);
        this.currentPlayer = CellState.PLAYER1;
        this.gameState = GameState.PLAYING;
        this.moveCount = 0;
        logger.info("New game started with board size {}x{}", rows, columns);
    }
    
    /**
     * Gets the game board.
     * 
     * @return the current board
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Gets the current player.
     * 
     * @return the player whose turn it is
     */
    public CellState getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Gets the current game state.
     * 
     * @return the game state
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * Gets the total number of moves made.
     * 
     * @return the move count
     */
    public int getMoveCount() {
        return moveCount;
    }
    
    /**
     * Makes a move for the current player.
     * Validates the move, updates the board, checks for win/draw, and switches players.
     * 
     * @param move the move to make
     * @return true if the move was successful
     */
    public boolean makeMove(Move move) {
        if (gameState.isGameOver()) {
            logger.warn("Attempted move when game is over");
            return false;
        }
        
        if (!board.makeMove(move, currentPlayer)) {
            return false;
        }
        
        moveCount++;
        updateGameState();
        
        if (!gameState.isGameOver()) {
            switchPlayer();
        }
        
        return true;
    }
    
    /**
     * Switches the current player.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == CellState.PLAYER1) ? CellState.PLAYER2 : CellState.PLAYER1;
        logger.debug("Player switched to {}", currentPlayer);
    }
    
    /**
     * Updates the game state by checking for a winner or draw.
     */
    private void updateGameState() {
        CellState winner = board.checkWinner();
        
        if (winner == CellState.PLAYER1) {
            gameState = GameState.PLAYER1_WIN;
            logger.info("Game ended: Player 1 wins");
        } else if (winner == CellState.PLAYER2) {
            gameState = GameState.PLAYER2_WIN;
            logger.info("Game ended: Player 2 wins");
        } else if (board.isFull()) {
            gameState = GameState.DRAW;
            logger.info("Game ended: Draw");
        }
    }
    
    /**
     * Resets the game to initial state.
     */
    public void reset() {
        board.clear();
        currentPlayer = CellState.PLAYER1;
        gameState = GameState.PLAYING;
        moveCount = 0;
        logger.info("Game reset");
    }
    
    /**
     * Checks if the game is over.
     * 
     * @return true if the game has ended
     */
    public boolean isGameOver() {
        return gameState.isGameOver();
    }
    
    /**
     * Gets a message describing the current game state.
     * 
     * @return the game state message
     */
    public String getGameStateMessage() {
        return gameState.getMessage();
    }
    
    /**
     * Sets the board (used for loading saved games).
     * 
     * @param board the board to set
     */
    public void setBoard(Board board) {
        this.board = board;
        updateGameState();
    }
    
    /**
     * Sets the current player (used for loading saved games).
     * 
     * @param player the player to set as current
     */
    public void setCurrentPlayer(CellState player) {
        this.currentPlayer = player;
    }
}
