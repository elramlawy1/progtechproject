@echo off
REM Run the Gomoku game

echo ===================================
echo Starting Gomoku Game...
echo ===================================
echo.

where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    call mvn exec:java -Dexec.mainClass="com.gomoku.Main"
) else (
    echo Maven not found!
    echo Please run setup.bat first to install Maven.
    echo.
    echo Or open the project in an IDE (IntelliJ IDEA, Eclipse, VS Code)
    echo and run the Main.java file directly.
)

pause
