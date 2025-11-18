package com.gomoku.ui;

import com.gomoku.ai.AIPlayer;
import com.gomoku.ai.RandomAIStrategy;
import com.gomoku.db.GameRepository;
import com.gomoku.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface for the Gomoku game.
 * Provides an interactive text-based interface for game play and board editing.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class CommandLineInterface {
    private static final Logger logger = LogManager.getLogger(CommandLineInterface.class);
    
    private Game game;
    private AIPlayer aiPlayer;
    private GameRepository repository;
    private Scanner scanner;
    private boolean running;
    
    /**
     * Creates a new command-line interface.
     */
    public CommandLineInterface() {
        this.game = new Game();
        this.aiPlayer = new AIPlayer(new RandomAIStrategy());
        this.repository = new GameRepository();
        this.scanner = new Scanner(System.in);
        this.running = true;
        logger.info("CLI initialized");
    }
    
    /**
     * Starts the command-line interface main loop.
     */
    public void start() {
        printWelcome();
        
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            handleMainMenuChoice(choice);
        }
        
        scanner.close();
        logger.info("CLI terminated");
    }
    
    /**
     * Prints the welcome message.
     */
    private void printWelcome() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     GOMOKU (Five in a Row)        ║");
        System.out.println("║    Programming Technology 2025    ║");
        System.out.println("╚════════════════════════════════════╝\n");
    }
    
    /**
     * Prints the main menu.
     */
    private void printMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. New Game");
        System.out.println("2. Board Editor");
        System.out.println("3. Save Game");
        System.out.println("4. Load Game");
        System.out.println("5. List Saved Games");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }
    
    /**
     * Handles main menu choice.
     * 
     * @param choice the user's menu selection
     */
    private void handleMainMenuChoice(String choice) {
        switch (choice) {
            case "1":
                startNewGame();
                break;
            case "2":
                openBoardEditor();
                break;
            case "3":
                saveGame();
                break;
            case "4":
                loadGame();
                break;
            case "5":
                listSavedGames();
                break;
            case "6":
                exit();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Starts a new game.
     */
    private void startNewGame() {
        System.out.println("\n=== NEW GAME ===");
        System.out.print("Enter board size (rows columns, default 15 15): ");
        String input = scanner.nextLine().trim();
        
        int rows = 15, cols = 15;
        if (!input.isEmpty()) {
            String[] parts = input.split("\\s+");
            try {
                rows = Integer.parseInt(parts[0]);
                cols = Integer.parseInt(parts[1]);
            } catch (Exception e) {
                System.out.println("Invalid input. Using default 15x15 board.");
            }
        }
        
        game = new Game(rows, cols);
        logger.info("Started new game with board size {}x{}", rows, cols);
        playGame();
    }
    
    /**
     * Main game play loop.
     */
    private void playGame() {
        while (!game.isGameOver()) {
            System.out.println("\n" + game.getBoard());
            
            if (game.getCurrentPlayer() == CellState.PLAYER1) {
                handleHumanMove();
            } else {
                handleAIMove();
            }
        }
        
        System.out.println("\n" + game.getBoard());
        System.out.println("\n*** " + game.getGameStateMessage() + " ***");
        logger.info("Game ended: {}", game.getGameStateMessage());
    }
    
    /**
     * Handles a human player's move.
     */
    private void handleHumanMove() {
        System.out.println("\nPlayer 1's turn (X)");
        System.out.print("Enter move (row column) or 'menu' to return: ");
        String input = scanner.nextLine().trim();
        
        if (input.equalsIgnoreCase("menu")) {
            return;
        }
        
        try {
            String[] parts = input.split("\\s+");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            
            Move move = new Move(row, col);
            if (game.makeMove(move)) {
                logger.info("Human player made move: {}", move);
            } else {
                System.out.println("Invalid move! Try again.");
                handleHumanMove();
            }
        } catch (Exception e) {
            System.out.println("Invalid input format. Use: row column");
            handleHumanMove();
        }
    }
    
    /**
     * Handles the AI player's move.
     */
    private void handleAIMove() {
        System.out.println("\nPlayer 2's turn (O) - AI is thinking...");
        Move move = aiPlayer.makeMove(game.getBoard());
        
        if (move != null) {
            game.makeMove(move);
            System.out.println("AI played at: " + move.getRow() + " " + move.getColumn());
            logger.info("AI made move: {}", move);
        }
    }
    
    /**
     * Opens the board editor.
     */
    private void openBoardEditor() {
        System.out.println("\n=== BOARD EDITOR ===");
        System.out.println("Commands:");
        System.out.println("  set <row> <col> <player>  - Set cell (player: 1, 2, or 0 for empty)");
        System.out.println("  show                      - Display board");
        System.out.println("  clear                     - Clear board");
        System.out.println("  done                      - Finish editing");
        
        boolean editing = true;
        while (editing) {
            System.out.print("\nEditor> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            
            if (parts.length == 0) continue;
            
            switch (parts[0].toLowerCase()) {
                case "set":
                    if (parts.length == 4) {
                        try {
                            int row = Integer.parseInt(parts[1]);
                            int col = Integer.parseInt(parts[2]);
                            int player = Integer.parseInt(parts[3]);
                            
                            CellState state = CellState.EMPTY;
                            if (player == 1) state = CellState.PLAYER1;
                            else if (player == 2) state = CellState.PLAYER2;
                            
                            game.getBoard().setCell(row, col, state);
                            System.out.println("Cell updated.");
                        } catch (Exception e) {
                            System.out.println("Invalid input.");
                        }
                    } else {
                        System.out.println("Usage: set <row> <col> <player>");
                    }
                    break;
                    
                case "show":
                    System.out.println("\n" + game.getBoard());
                    break;
                    
                case "clear":
                    game.getBoard().clear();
                    System.out.println("Board cleared.");
                    break;
                    
                case "done":
                    editing = false;
                    break;
                    
                default:
                    System.out.println("Unknown command.");
            }
        }
    }
    
    /**
     * Saves the current game.
     */
    private void saveGame() {
        System.out.print("\nEnter name for saved game: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }
        
        if (repository.saveGame(name, game)) {
            System.out.println("Game saved successfully!");
        } else {
            System.out.println("Failed to save game.");
        }
    }
    
    /**
     * Loads a saved game.
     */
    private void loadGame() {
        System.out.print("\nEnter name of game to load: ");
        String name = scanner.nextLine().trim();
        
        Game loadedGame = repository.loadGame(name);
        if (loadedGame != null) {
            this.game = loadedGame;
            System.out.println("Game loaded successfully!");
            System.out.println(game.getBoard());
        } else {
            System.out.println("Failed to load game.");
        }
    }
    
    /**
     * Lists all saved games.
     */
    private void listSavedGames() {
        List<String> games = repository.listSavedGames();
        
        System.out.println("\n=== SAVED GAMES ===");
        if (games.isEmpty()) {
            System.out.println("No saved games found.");
        } else {
            for (int i = 0; i < games.size(); i++) {
                System.out.println((i + 1) + ". " + games.get(i));
            }
        }
    }
    
    /**
     * Exits the application.
     */
    private void exit() {
        System.out.println("\nThank you for playing Gomoku!");
        running = false;
    }
}
