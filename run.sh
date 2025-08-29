#!/bin/bash

# Script de lancement pour Brick Breaker
# Usage: ./run.sh

echo "🎮 Brick Breaker - Compilation et lancement..."

# Se placer dans le répertoire du script
cd "$(dirname "$0")"

# Créer le répertoire build s'il n'existe pas
mkdir -p build

# Compiler les sources Java
echo "📦 Compilation des sources..."
javac -d build src/main/java/com/brickbreaker/*.java

# Vérifier si la compilation a réussi
if [ $? -eq 0 ]; then
    echo "✅ Compilation réussie!"
    echo "🚀 Lancement du jeu..."
    
    # Lancer le jeu
    java -cp build com.brickbreaker.BrickBreakerGame
else
    echo "❌ Erreur de compilation!"
    exit 1
fi