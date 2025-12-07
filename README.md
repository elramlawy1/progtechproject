# Gomoku Game - Programming Technology Course Project

A Java implementation of Gomoku (Five in a Row) game with AI opponent and database persistence.

## Features

- ✅ 2-player Gomoku game (Human vs AI)
- ✅ Command-line interface
- ✅ Board editor for custom setups
- ✅ Random AI opponent
- ✅ Save/Load games using H2 SQL database
- ✅ Comprehensive logging with Log4j2
- ✅ Extensive unit tests
- ✅ Complete JavaDoc documentation
- ✅ Design patterns: Strategy, Singleton, Facade

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running the Game

```bash
mvn exec:java -Dexec.mainClass="com.gomoku.Main"
```

Or compile and run:

```bash
mvn clean package
java -jar target/gomoku-game-1.0.0.jar
```

## Project Structure

```
src/
├── main/java/com/gomoku/
│   ├── Main.java                 # Entry point
│   ├── model/                    # Game models
│   │   ├── Board.java
│   │   ├── CellState.java
│   │   ├── Game.java
│   │   ├── GameState.java
│   │   └── Move.java
│   ├── ai/                       # AI implementation
│   │   ├── AIPlayer.java
│   │   ├── AIStrategy.java
│   │   └── RandomAIStrategy.java
│   ├── db/                       # Database layer
│   │   ├── DatabaseManager.java
│   │   └── GameRepository.java
│   └── ui/                       # User interface
│       └── CommandLineInterface.java
└── test/java/com/gomoku/         # Unit tests
```

## Design Patterns

1. **Strategy Pattern**: AI strategies (AIStrategy interface, RandomAIStrategy implementation)
2. **Singleton Pattern**: DatabaseManager (single database connection)
3. **Facade Pattern**: Game class simplifies complex game logic

## How to Play

1. Start the game
2. Choose "New Game" from the menu
3. Player 1 (X) plays first
4. Enter row and column (e.g., "7 7")
5. AI (Player 2, O) responds automatically
6. First to get 5 in a row wins!

## Authors

- Youssef

## Course

Programming Technology 2025
