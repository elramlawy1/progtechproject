package com.gomoku.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Board class.
 * Tests board initialization, move validation, and win detection.
 * 
 * @author Gomoku Team
 * @version 1.0
 */
class BoardTest {
    
    private Board board;
    
    /**
     * Sets up a fresh board before each test.
     */
    @BeforeEach
    void setUp() {
        board = new Board(15, 15);
    }
    
    /**
     * Tests board initialization with correct dimensions.
     */
    @Test
    void testBoardInitialization() {
        assertEquals(15, board.getRows());
        assertEquals(15, board.getColumns());
        
        // All cells should be empty
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                assertEquals(CellState.EMPTY, board.getCell(i, j));
            }
        }
    }
    
    /**
     * Tests custom board size.
     */
    @Test
    void testCustomBoardSize() {
        Board customBoard = new Board(10, 12);
        assertEquals(10, customBoard.getRows());
        assertEquals(12, customBoard.getColumns());
    }
    
    /**
     * Tests valid position checking.
     */
    @Test
    void testIsValidPosition() {
        assertTrue(board.isValidPosition(0, 0));
        assertTrue(board.isValidPosition(14, 14));
        assertTrue(board.isValidPosition(7, 7));
        
        assertFalse(board.isValidPosition(-1, 0));
        assertFalse(board.isValidPosition(0, -1));
        assertFalse(board.isValidPosition(15, 0));
        assertFalse(board.isValidPosition(0, 15));
    }
    
    /**
     * Tests making valid moves.
     */
    @Test
    void testMakeValidMove() {
        Move move = new Move(5, 5);
        assertTrue(board.makeMove(move, CellState.PLAYER1));
        assertEquals(CellState.PLAYER1, board.getCell(5, 5));
    }
    
    /**
     * Tests making invalid moves.
     */
    @Test
    void testMakeInvalidMove() {
        Move move = new Move(5, 5);
        board.makeMove(move, CellState.PLAYER1);
        
        // Cannot place on occupied cell
        assertFalse(board.makeMove(move, CellState.PLAYER2));
        
        // Invalid position
        Move invalidMove = new Move(20, 20);
        assertFalse(board.makeMove(invalidMove, CellState.PLAYER1));
    }
    
    /**
     * Tests getting valid moves.
     */
    @Test
    void testGetValidMoves() {
        assertEquals(225, board.getValidMoves().size()); // 15x15 = 225
        
        board.makeMove(new Move(0, 0), CellState.PLAYER1);
        assertEquals(224, board.getValidMoves().size());
        
        board.makeMove(new Move(1, 1), CellState.PLAYER2);
        assertEquals(223, board.getValidMoves().size());
    }
    
    /**
     * Tests horizontal win detection.
     */
    @Test
    void testHorizontalWin() {
        // Place 5 in a row horizontally
        for (int i = 0; i < 5; i++) {
            board.makeMove(new Move(7, i), CellState.PLAYER1);
        }
        
        assertEquals(CellState.PLAYER1, board.checkWinner());
    }
    
    /**
     * Tests vertical win detection.
     */
    @Test
    void testVerticalWin() {
        // Place 5 in a row vertically
        for (int i = 0; i < 5; i++) {
            board.makeMove(new Move(i, 7), CellState.PLAYER2);
        }
        
        assertEquals(CellState.PLAYER2, board.checkWinner());
    }
    
    /**
     * Tests diagonal win detection (top-left to bottom-right).
     */
    @Test
    void testDiagonalWin() {
        // Place 5 in a row diagonally
        for (int i = 0; i < 5; i++) {
            board.makeMove(new Move(i, i), CellState.PLAYER1);
        }
        
        assertEquals(CellState.PLAYER1, board.checkWinner());
    }
    
    /**
     * Tests anti-diagonal win detection (top-right to bottom-left).
     */
    @Test
    void testAntiDiagonalWin() {
        // Place 5 in a row anti-diagonally
        for (int i = 0; i < 5; i++) {
            board.makeMove(new Move(i, 4 - i), CellState.PLAYER2);
        }
        
        assertEquals(CellState.PLAYER2, board.checkWinner());
    }
    
    /**
     * Tests no winner scenario.
     */
    @Test
    void testNoWinner() {
        board.makeMove(new Move(0, 0), CellState.PLAYER1);
        board.makeMove(new Move(1, 1), CellState.PLAYER2);
        board.makeMove(new Move(2, 2), CellState.PLAYER1);
        
        assertEquals(CellState.EMPTY, board.checkWinner());
    }
    
    /**
     * Tests board full detection.
     */
    @Test
    void testBoardFull() {
        assertFalse(board.isFull());
        
        // Fill the board
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board.setCell(i, j, CellState.PLAYER1);
            }
        }
        
        assertTrue(board.isFull());
    }
    
    /**
     * Tests clearing the board.
     */
    @Test
    void testClearBoard() {
        board.makeMove(new Move(5, 5), CellState.PLAYER1);
        board.makeMove(new Move(6, 6), CellState.PLAYER2);
        
        board.clear();
        
        assertEquals(CellState.EMPTY, board.getCell(5, 5));
        assertEquals(CellState.EMPTY, board.getCell(6, 6));
        assertEquals(225, board.getValidMoves().size());
    }
    
    /**
     * Tests edge case - win at board edge.
     */
    @Test
    void testWinAtEdge() {
        // Horizontal win at top edge
        for (int i = 0; i < 5; i++) {
            board.makeMove(new Move(0, i), CellState.PLAYER1);
        }
        assertEquals(CellState.PLAYER1, board.checkWinner());
        
        board.clear();
        
        // Vertical win at left edge
        for (int i = 0; i < 5; i++) {
            board.makeMove(new Move(i, 0), CellState.PLAYER2);
        }
        assertEquals(CellState.PLAYER2, board.checkWinner());
    }
    
    /**
     * Tests that 4 in a row is not a win.
     */
    @Test
    void testFourInRowNotWin() {
        for (int i = 0; i < 4; i++) {
            board.makeMove(new Move(5, i), CellState.PLAYER1);
        }
        
        assertEquals(CellState.EMPTY, board.checkWinner());
    }
}
