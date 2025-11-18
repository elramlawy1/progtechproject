@echo off
REM Simple setup script for Gomoku project

echo ===================================
echo Gomoku Game - Setup Script
echo ===================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven found! Installing dependencies and building...
    call mvn clean compile
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo Build successful!
        echo.
        echo To run tests: mvn test
        echo To run game: mvn exec:java -Dexec.mainClass="com.gomoku.Main"
        echo.
    ) else (
        echo Build failed. Please check errors above.
    )
) else (
    echo.
    echo Maven is not installed!
    echo.
    echo Please install Maven from:
    echo https://maven.apache.org/download.cgi
    echo.
    echo Or use Chocolatey: choco install maven
    echo.
    echo Alternatively, open this project in IntelliJ IDEA or Eclipse
    echo and use their built-in Maven support.
    echo.
)

pause
