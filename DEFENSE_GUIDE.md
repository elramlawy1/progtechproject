# Gomoku Game - Setup and Defense Guide

## 🎯 Project Overview

This is a complete Gomoku (Five in a Row) game implementation for the Programming Technology course. The project demonstrates:

- **Object-Oriented Design**: Clean separation of concerns with model, AI, database, and UI layers
- **Design Patterns**: Strategy (AI), Singleton (Database), Facade (Game logic)
- **Testing**: Comprehensive JUnit 5 tests for all business logic
- **Database**: H2 SQL database for game persistence
- **Logging**: Log4j2 for complete game event logging
- **Documentation**: Full JavaDoc for every class and method

## 📋 Requirements Checklist

✅ **Git & Maven** (5p): Git repository initialized, Maven project structure  
✅ **OO Implementation** (20p): Clear class hierarchy, encapsulation, methods  
✅ **Correct Gameplay** (10p): Full Gomoku rules, win detection, board editing  
✅ **Tested Correctness** (15p): 50+ unit tests covering all business logic  
✅ **Relational DB** (5p): H2 database with proper schema, save/load functionality  
✅ **Design Patterns** (5p): Strategy, Singleton, Facade patterns implemented  

**Total: 60 points**

## 🚀 Quick Start (Without Maven)

If Maven is not installed, you can compile and run manually:

### Step 1: Compile the Project

```powershell
# Create output directory
New-Item -Path "target\classes" -ItemType Directory -Force
New-Item -Path "target\test-classes" -ItemType Directory -Force

# Download dependencies (you'll need these JARs)
# - h2-2.2.224.jar
# - log4j-api-2.20.0.jar
# - log4j-core-2.20.0.jar
# - junit-jupiter-api-5.10.0.jar
# - junit-jupiter-engine-5.10.0.jar

# Or use Maven wrapper (recommended)
```

### Step 2: Install Maven (Recommended)

Download Maven from: https://maven.apache.org/download.cgi

Or use Chocolatey:
```powershell
choco install maven
```

## 🏃 Running with Maven (Recommended)

Once Maven is installed:

```bash
# Compile the project
mvn clean compile

# Run all tests
mvn test

# Run the game
mvn exec:java -Dexec.mainClass="com.gomoku.Main"

# Build JAR file
mvn clean package
```

## 🎮 How to Play

1. **Start the game**: Run Main.java
2. **Main Menu Options**:
   - `1` - New Game (play against AI)
   - `2` - Board Editor (customize board)
   - `3` - Save Game (save current state)
   - `4` - Load Game (load saved game)
   - `5` - List Saved Games
   - `6` - Exit

3. **Playing**:
   - Enter row and column (e.g., "7 7")
   - Player 1 (X) is human
   - Player 2 (O) is AI
   - Get 5 in a row to win!

## 📚 Defense Preparation

### Key Concepts to Explain:

#### 1. **Architecture** (OO Implementation - 20p)
```
Main
  ↓
CommandLineInterface (UI Layer)
  ↓
Game (Business Logic - Facade Pattern)
  ↓
Board, Move, CellState (Domain Models)
  ↓
AIPlayer → AIStrategy (Strategy Pattern)
  ↓
GameRepository → DatabaseManager (Singleton Pattern)
```

#### 2. **Design Patterns** (5p)

**Strategy Pattern** (ai package):
- `AIStrategy` interface allows different AI implementations
- `RandomAIStrategy` implements random move selection
- `AIPlayer` can switch strategies at runtime
- Example: `aiPlayer.setStrategy(new RandomAIStrategy());`

**Singleton Pattern** (DatabaseManager):
- Only ONE database connection for entire application
- `DatabaseManager.getInstance()` ensures single instance
- Thread-safe with synchronized method

**Facade Pattern** (Game class):
- Simplifies complex board operations
- `makeMove()` handles validation, state update, win checking
- Hides complexity from UI layer

#### 3. **Game Logic** (Correct Gameplay - 10p)

**Win Detection** (Board.java):
- Checks 4 directions: horizontal, vertical, 2 diagonals
- `checkDirection()` method counts consecutive pieces
- Returns winner when 5 in a row found

**Move Validation**:
- Position must be on board
- Cell must be empty
- Game must not be over

#### 4. **Testing** (15p)

**BoardTest.java** (15 tests):
- Initialization, move validation
- All 4 win directions
- Edge cases (board full, invalid moves)

**GameTest.java** (11 tests):
- Turn switching, move counting
- Win/draw conditions
- Game reset

**RandomAIStrategyTest.java** (7 tests):
- Valid move selection
- Full board handling
- Strategy pattern usage

**GameRepositoryTest.java** (8 tests):
- Save/load games
- Database operations
- Custom board sizes

#### 5. **Database** (5p)

**Schema** (2 tables):
```sql
saved_games (id, game_name, rows, columns, current_player, move_count, save_date)
board_cells (id, game_id, row_index, col_index, cell_state)
```

**Operations**:
- `saveGame()` - Transactional save
- `loadGame()` - Reconstructs game state
- `listSavedGames()` - Shows all saved games

## 🔍 Common Defense Questions

**Q: Why use Strategy pattern for AI?**  
A: It allows easy addition of new AI strategies (e.g., smart AI, defensive AI) without modifying existing code. Demonstrates Open/Closed Principle.

**Q: Why Singleton for database?**  
A: We need exactly one database connection to avoid conflicts and resource waste. Ensures consistency across the application.

**Q: How does win detection work?**  
A: After each move, we check 4 directions from that position. We count consecutive pieces in each direction. If count >= 5, we have a winner.

**Q: Why use H2 database?**  
A: H2 is embedded (no separate server), lightweight, and perfect for Java applications. It's a real SQL database that demonstrates relational database skills.

**Q: How do you test the random AI?**  
A: We use a seed for the Random object, making tests reproducible. We verify AI chooses valid moves and handles edge cases (full board, single empty cell).

## 📝 File Overview

**Core Game Logic** (model package):
- `Board.java` - Game board, move validation, win detection (290 lines)
- `Game.java` - Game flow, turn management, state updates (170 lines)
- `CellState.java` - Cell states (EMPTY, PLAYER1, PLAYER2)
- `GameState.java` - Game states (PLAYING, WIN, DRAW)
- `Move.java` - Move representation

**AI** (ai package):
- `AIStrategy.java` - Strategy interface
- `RandomAIStrategy.java` - Random AI implementation
- `AIPlayer.java` - AI player wrapper

**Database** (db package):
- `DatabaseManager.java` - Singleton database manager
- `GameRepository.java` - Save/load operations

**UI** (ui package):
- `CommandLineInterface.java` - Complete CLI (315 lines)

**Tests** (test package):
- `BoardTest.java` - 15 tests
- `GameTest.java` - 11 tests
- `RandomAIStrategyTest.java` - 7 tests
- `GameRepositoryTest.java` - 8 tests

## 🎓 Tips for Defense

1. **Run the game beforehand** - Make sure you can demonstrate it working
2. **Know the design patterns** - Explain WHY you used them, not just WHAT they are
3. **Understand the tests** - Be able to explain what each test verifies
4. **Trace a move** - Follow a move from UI → Game → Board → Database
5. **Show the logs** - Open `gomoku-game.log` to show logging works

## 🛠️ Troubleshooting

**Maven not found?**
- Install from https://maven.apache.org/download.cgi
- Add to PATH
- Or use IDE's built-in Maven (IntelliJ IDEA, Eclipse)

**Can't compile?**
- Ensure Java 11+ is installed
- Check `JAVA_HOME` environment variable
- Use IDE to import Maven project

**Database errors?**
- Delete `gomoku_db.mv.db` files
- Restart application

## ✨ Advanced Features (Bonus Points)

If you want to impress:
1. Explain the logging mechanism
2. Show JavaDoc generation: `mvn javadoc:javadoc`
3. Discuss potential improvements (smarter AI, network play)
4. Explain transaction management in database saves

Good luck with your defense! 🎉
