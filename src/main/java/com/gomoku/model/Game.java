package com.gomoku.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SIMPLE: Main game controller - manages whose turn it is and if someone won.
 * 
 * FACADE PATTERN: This class hides the complexity of the board.
 * Instead of UI calling board.makeMove(), board.checkWinner(), board.switchPlayer(),
 * UI just calls ONE method: game.makeMove() and this class handles everything!
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
     * SIMPLE: The main method! Does everything needed for one move.
     * 
     * WHAT IT DOES:
     * 1. Check if game is already over (can't move if game ended)
     * 2. Try to place the piece on the board
     * 3. Count this move
     * 4. Check if someone won or if it's a draw
     * 5. Switch to other player (if game not over)
     * 
     * THIS IS THE FACADE PATTERN - one simple method does many things!
     * 
     * @param move the position where to place the piece
     * @return true if move worked, false if invalid move
     */
    public boolean makeMove(Move move) {
        // Step 1: Make sure game is still playing
        if (gameState.isGameOver()) {
            logger.warn("Attempted move when game is over");
            return false;  // Can't move, game already ended
        }
        
        // Step 2: Try to place piece on board
        if (!board.makeMove(move, currentPlayer)) {
            return false;  // Invalid move (position taken or out of bounds)
        }
        
        // Step 3: Count this move
        moveCount++;
        
        // Step 4: Check if game ended (someone won or draw)
        updateGameState();
        
        // Step 5: Switch to other player (if game still going)
        if (!gameState.isGameOver()) {
            switchPlayer();
        }
        
        return true;  // Move successful!
    }
    
    /**
     * SIMPLE: Change from PLAYER1 to PLAYER2, or PLAYER2 to PLAYER1.
     * Uses ternary operator: (condition) ? valueIfTrue : valueIfFalse
     */
    private void switchPlayer() {
        // If current player is PLAYER1, switch to PLAYER2, otherwise switch to PLAYER1
        currentPlayer = (currentPlayer == CellState.PLAYER1) ? CellState.PLAYER2 : CellState.PLAYER1;
        logger.debug("Player switched to {}", currentPlayer);
    }
    
    /**
     * SIMPLE: Check if someone won or if it's a draw.
     * 
     * HOW IT WORKS:
     * 1. Ask the board "is there a winner?"
     * 2. If PLAYER1 has 5-in-a-row → PLAYER1 wins
     * 3. If PLAYER2 has 5-in-a-row → PLAYER2 wins  
     * 4. If board is full and no winner → DRAW
     * 5. Otherwise → game continues
     */
    private void updateGameState() {
        CellState winner = board.checkWinner();  // Ask board who won
        
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
        // If none of above, gameState stays PLAYING
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
