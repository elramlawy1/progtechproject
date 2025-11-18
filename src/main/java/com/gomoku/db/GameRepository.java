package com.gomoku.db;

import com.gomoku.model.Board;
import com.gomoku.model.CellState;
import com.gomoku.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for game persistence operations.
 * Handles saving and loading games from the database.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class GameRepository {
    private static final Logger logger = LogManager.getLogger(GameRepository.class);
    private final DatabaseManager dbManager;
    
    /**
     * Creates a new game repository.
     */
    public GameRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Saves a game to the database.
     * 
     * @param gameName the name to save the game under
     * @param game the game to save
     * @return true if save was successful
     */
    public boolean saveGame(String gameName, Game game) {
        Connection conn = dbManager.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            // Delete existing game with same name
            deleteGame(gameName);
            
            // Insert game metadata
            String insertGame = "INSERT INTO saved_games (game_name, rows, columns, current_player, move_count) VALUES (?, ?, ?, ?, ?)";
            int gameId;
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertGame, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, gameName);
                pstmt.setInt(2, game.getBoard().getRows());
                pstmt.setInt(3, game.getBoard().getColumns());
                pstmt.setString(4, game.getCurrentPlayer().name());
                pstmt.setInt(5, game.getMoveCount());
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    gameId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to get game ID");
                }
            }
            
            // Insert board cells
            String insertCell = "INSERT INTO board_cells (game_id, row_index, col_index, cell_state) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertCell)) {
                Board board = game.getBoard();
                for (int i = 0; i < board.getRows(); i++) {
                    for (int j = 0; j < board.getColumns(); j++) {
                        CellState state = board.getCell(i, j);
                        if (state != CellState.EMPTY) {
                            pstmt.setInt(1, gameId);
                            pstmt.setInt(2, i);
                            pstmt.setInt(3, j);
                            pstmt.setString(4, state.name());
                            pstmt.addBatch();
                        }
                    }
                }
                pstmt.executeBatch();
            }
            
            conn.commit();
            logger.info("Game '{}' saved successfully", gameName);
            return true;
            
        } catch (SQLException e) {
            logger.error("Failed to save game '{}'", gameName, e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed", ex);
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Failed to reset auto-commit", e);
            }
        }
    }
    
    /**
     * Loads a game from the database.
     * 
     * @param gameName the name of the game to load
     * @return the loaded game, or null if not found
     */
    public Game loadGame(String gameName) {
        Connection conn = dbManager.getConnection();
        
        try {
            // Load game metadata
            String selectGame = "SELECT * FROM saved_games WHERE game_name = ?";
            int gameId;
            int rows, columns, moveCount;
            CellState currentPlayer;
            
            try (PreparedStatement pstmt = conn.prepareStatement(selectGame)) {
                pstmt.setString(1, gameName);
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    logger.warn("Game '{}' not found", gameName);
                    return null;
                }
                
                gameId = rs.getInt("id");
                rows = rs.getInt("rows");
                columns = rs.getInt("columns");
                currentPlayer = CellState.valueOf(rs.getString("current_player"));
                moveCount = rs.getInt("move_count");
            }
            
            // Create game and board
            Game game = new Game(rows, columns);
            game.setCurrentPlayer(currentPlayer);
            
            // Load board cells
            String selectCells = "SELECT * FROM board_cells WHERE game_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectCells)) {
                pstmt.setInt(1, gameId);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    int row = rs.getInt("row_index");
                    int col = rs.getInt("col_index");
                    CellState state = CellState.valueOf(rs.getString("cell_state"));
                    game.getBoard().setCell(row, col, state);
                }
            }
            
            logger.info("Game '{}' loaded successfully", gameName);
            return game;
            
        } catch (SQLException e) {
            logger.error("Failed to load game '{}'", gameName, e);
            return null;
        }
    }
    
    /**
     * Deletes a saved game from the database.
     * 
     * @param gameName the name of the game to delete
     * @return true if deletion was successful
     */
    public boolean deleteGame(String gameName) {
        Connection conn = dbManager.getConnection();
        String deleteSql = "DELETE FROM saved_games WHERE game_name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, gameName);
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                logger.info("Game '{}' deleted", gameName);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            logger.error("Failed to delete game '{}'", gameName, e);
            return false;
        }
    }
    
    /**
     * Lists all saved game names.
     * 
     * @return list of saved game names
     */
    public List<String> listSavedGames() {
        List<String> gameNames = new ArrayList<>();
        Connection conn = dbManager.getConnection();
        String selectSql = "SELECT game_name, save_date FROM saved_games ORDER BY save_date DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {
            
            while (rs.next()) {
                gameNames.add(rs.getString("game_name"));
            }
            
        } catch (SQLException e) {
            logger.error("Failed to list saved games", e);
        }
        
        return gameNames;
    }
    
    /**
     * Checks if a game with the given name exists.
     * 
     * @param gameName the game name to check
     * @return true if the game exists
     */
    public boolean gameExists(String gameName) {
        Connection conn = dbManager.getConnection();
        String selectSql = "SELECT COUNT(*) FROM saved_games WHERE game_name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setString(1, gameName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            logger.error("Failed to check game existence", e);
        }
        
        return false;
    }
}
