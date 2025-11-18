package com.gomoku.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database manager using Singleton design pattern.
 * Manages the H2 database connection and schema initialization.
 * Only one instance of this class exists throughout the application.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    
    private static final String DB_URL = "jdbc:h2:./gomoku_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private Connection connection;
    
    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the database connection and creates tables.
     */
    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initializeDatabase();
            logger.info("Database connection established");
        } catch (SQLException e) {
            logger.error("Failed to connect to database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Gets the single instance of DatabaseManager (Singleton pattern).
     * 
     * @return the DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Gets the database connection.
     * 
     * @return the SQL connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Initializes the database schema by creating necessary tables.
     * Creates tables for saved games and board states.
     */
    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Create table for saved games
            String createGameTable = 
                "CREATE TABLE IF NOT EXISTS saved_games (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "game_name VARCHAR(255) NOT NULL UNIQUE, " +
                "rows INT NOT NULL, " +
                "columns INT NOT NULL, " +
                "current_player VARCHAR(10) NOT NULL, " +
                "move_count INT NOT NULL, " +
                "save_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            stmt.execute(createGameTable);
            
            // Create table for board cells
            String createBoardTable = 
                "CREATE TABLE IF NOT EXISTS board_cells (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "game_id INT NOT NULL, " +
                "row_index INT NOT NULL, " +
                "col_index INT NOT NULL, " +
                "cell_state VARCHAR(10) NOT NULL, " +
                "FOREIGN KEY (game_id) REFERENCES saved_games(id) ON DELETE CASCADE" +
                ")";
            stmt.execute(createBoardTable);
            
            logger.info("Database schema initialized");
        } catch (SQLException e) {
            logger.error("Failed to initialize database schema", e);
            throw new RuntimeException("Database schema initialization failed", e);
        }
    }
    
    /**
     * Closes the database connection.
     * Should be called when the application shuts down.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection", e);
        }
    }
    
    /**
     * Resets the database by dropping all tables.
     * Use with caution - this deletes all saved games.
     */
    public void resetDatabase() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS board_cells");
            stmt.execute("DROP TABLE IF EXISTS saved_games");
            initializeDatabase();
            logger.info("Database reset completed");
        } catch (SQLException e) {
            logger.error("Failed to reset database", e);
        }
    }
}
