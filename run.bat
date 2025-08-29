@echo off
rem Script de lancement pour Brick Breaker (Windows)
rem Usage: run.bat

title Brick Breaker - Compilation et lancement

echo 🎮 Brick Breaker - Compilation et lancement...

rem Se placer dans le répertoire du script
cd /d "%~dp0"

rem Créer le répertoire build s'il n'existe pas
if not exist build mkdir build

rem Compiler les sources Java
echo 📦 Compilation des sources...
javac -d build src/main/java/com/brickbreaker/*.java

rem Vérifier si la compilation a réussi
if %errorlevel% == 0 (
    echo ✅ Compilation réussie!
    echo 🚀 Lancement du jeu...
    
    rem Lancer le jeu
    java -cp build com.brickbreaker.BrickBreakerGame
) else (
    echo ❌ Erreur de compilation!
    pause
    exit /b 1
)

pause