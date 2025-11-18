package com.gomoku;

import com.gomoku.db.DatabaseManager;
import com.gomoku.ui.CommandLineInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main entry point for the Gomoku game application.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    
    /**
     * Main method that starts the application.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        logger.info("Gomoku application started");
        
        try {
            // Start the command-line interface
            CommandLineInterface cli = new CommandLineInterface();
            cli.start();
            
        } catch (Exception e) {
            logger.error("Fatal error occurred", e);
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            // Clean up resources
            DatabaseManager.getInstance().close();
            logger.info("Gomoku application terminated");
        }
    }
}
