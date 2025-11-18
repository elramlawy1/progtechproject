package com.gomoku.db;

import com.gomoku.model.CellState;
import com.gomoku.model.Game;
import com.gomoku.model.Move;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GameRepository class.
 * Tests database operations for saving and loading games.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
class GameRepositoryTest {
    
    private GameRepository repository;
    private DatabaseManager dbManager;
    
    /**
     * Sets up test database before each test.
     */
    @BeforeEach
    void setUp() {
        dbManager = DatabaseManager.getInstance();
        dbManager.resetDatabase(); // Clean slate for each test
        repository = new GameRepository();
    }
    
    /**
     * Cleans up after tests.
     */
    @AfterEach
    void tearDown() {
        dbManager.resetDatabase();
    }
    
    /**
     * Tests saving a new game.
     */
    @Test
    void testSaveGame() {
        Game game = new Game();
        game.makeMove(new Move(5, 5));
        game.makeMove(new Move(6, 6));
        
        assertTrue(repository.saveGame("test_game", game));
        assertTrue(repository.gameExists("test_game"));
    }
    
    /**
     * Tests loading a saved game.
     */
    @Test
    void testLoadGame() {
        Game originalGame = new Game();
        originalGame.makeMove(new Move(5, 5));
        originalGame.makeMove(new Move(6, 6));
        
        repository.saveGame("test_game", originalGame);
        
        Game loadedGame = repository.loadGame("test_game");
        assertNotNull(loadedGame);
        assertEquals(CellState.PLAYER1, loadedGame.getBoard().getCell(5, 5));
        assertEquals(CellState.PLAYER2, loadedGame.getBoard().getCell(6, 6));
    }
    
    /**
     * Tests loading non-existent game.
     */
    @Test
    void testLoadNonExistentGame() {
        Game game = repository.loadGame("nonexistent");
        assertNull(game);
    }
    
    /**
     * Tests deleting a game.
     */
    @Test
    void testDeleteGame() {
        Game game = new Game();
        repository.saveGame("test_game", game);
        
        assertTrue(repository.gameExists("test_game"));
        assertTrue(repository.deleteGame("test_game"));
        assertFalse(repository.gameExists("test_game"));
    }
    
    /**
     * Tests listing saved games.
     */
    @Test
    void testListSavedGames() {
        repository.saveGame("game1", new Game());
        repository.saveGame("game2", new Game());
        repository.saveGame("game3", new Game());
        
        List<String> games = repository.listSavedGames();
        assertEquals(3, games.size());
        assertTrue(games.contains("game1"));
        assertTrue(games.contains("game2"));
        assertTrue(games.contains("game3"));
    }
    
    /**
     * Tests overwriting existing game.
     */
    @Test
    void testOverwriteGame() {
        Game game1 = new Game();
        game1.makeMove(new Move(0, 0));
        repository.saveGame("test_game", game1);
        
        Game game2 = new Game();
        game2.makeMove(new Move(1, 1));
        repository.saveGame("test_game", game2);
        
        Game loaded = repository.loadGame("test_game");
        assertEquals(CellState.EMPTY, loaded.getBoard().getCell(0, 0));
        assertEquals(CellState.PLAYER1, loaded.getBoard().getCell(1, 1));
    }
    
    /**
     * Tests saving game with custom board size.
     */
    @Test
    void testSaveCustomSizeGame() {
        Game game = new Game(10, 12);
        game.makeMove(new Move(5, 5));
        
        repository.saveGame("custom_game", game);
        
        Game loaded = repository.loadGame("custom_game");
        assertNotNull(loaded);
        assertEquals(10, loaded.getBoard().getRows());
        assertEquals(12, loaded.getBoard().getColumns());
        assertEquals(CellState.PLAYER1, loaded.getBoard().getCell(5, 5));
    }
    
    /**
     * Tests game existence check.
     */
    @Test
    void testGameExists() {
        assertFalse(repository.gameExists("nonexistent"));
        
        repository.saveGame("existing_game", new Game());
        assertTrue(repository.gameExists("existing_game"));
    }
}
