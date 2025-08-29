# Brick Breaker - Java Brick Breaking Game

A complete brick-breaking game developed in Java with Swing, offering a smooth and addictive gaming experience.

## Features

### Gameplay
- **Physical Ball** : Bounces off walls, paddle, and destroys bricks with realistic physics
- **Responsive Controls** : Paddle controlled by arrow keys or A/D keys
- **Brick System** : Different types of bricks with various colors and resistances
  - Normal bricks (orange) : 1 hit, 10 points
  - Resistant bricks (red) : 2 hits, 25 points
  - Super resistant bricks (magenta) : 3 hits, 50 points
  - Unbreakable bricks (gray) : Indestructible
- **Lives System** : 3 lives, game over if ball falls
- **Multiple Levels** : 10 levels with increasing difficulty
- **Score System** : Points earned for each brick destroyed

### Interface and Menus
- **Main Menu** : Play, Scores, Quit options
- **Pause System** : Pause/resume with spacebar
- **End Screens** : Game Over, Level Complete, Game Complete
- **Real-time Display** : Score, lives, level, high score

### Technical Features
- **60 FPS** : Smooth animations with optimized game loop
- **Precise Collisions** : Advanced collision detection
- **OOP Architecture** : Modular and maintainable code
- **Intuitive Interface** : Colorful and attractive graphics

## Project Structure

```
brick-breaker-game/
├── src/main/java/com/brickbreaker/
│   ├── Ball.java              # Ball class with physics
│   ├── Paddle.java            # Paddle class
│   ├── Brick.java             # Brick class with types
│   ├── GameState.java         # Game state enumeration
│   ├── GamePanel.java         # Main game panel
│   └── BrickBreakerGame.java  # Main class and window
└── README.md                  # Instructions and documentation
```

## Compilation and Execution

### Prerequisites
- Java JDK 8 or higher
- Operating System: Windows, macOS, or Linux

### Compilation

```bash
# Navigate to the project directory
cd brick-breaker-game

# Create output directory
mkdir -p build

# Compile all Java files
javac -d build src/main/java/com/brickbreaker/*.java
```

### Execution

```bash
# Run the game from the build directory
java -cp build com.brickbreaker.BrickBreakerGame
```

### Launch Scripts (optional)

**For Linux/macOS** - Create `run.sh`:
```bash
#!/bin/bash
cd "$(dirname "$0")"
mkdir -p build
javac -d build src/main/java/com/brickbreaker/*.java
java -cp build com.brickbreaker.BrickBreakerGame
```

**For Windows** - Create `run.bat`:
```batch
@echo off
cd /d "%~dp0"
mkdir build 2>nul
javac -d build src/main/java/com/brickbreaker/*.java
java -cp build com.brickbreaker.BrickBreakerGame
pause
```

## Controls

- **Paddle Movement** : Left/Right arrow keys or A/D
- **Menu** : Up/Down arrows to navigate, Enter or Space to select
- **Pause** : Spacebar during gameplay
- **Main Menu** : Escape from game or pause
- **Restart** : R from Game Over screen
- **Next Level** : Space from Level Complete screen

## Gameplay and Strategies

### Playing Tips
1. **Angle Control** : Hit the ball on the paddle edges to change its trajectory
2. **Progressive Speed** : Ball slightly accelerates with each bounce
3. **Strategic Targeting** : Focus on resistant bricks first
4. **Unbreakable Bricks** : Use them for tactical bounces (from level 3 onwards)

### Point System
- Normal brick : 10 points
- Resistant brick : 25 points  
- Super resistant brick : 50 points
- Level bonus : Score multiplied by level

### Level Progression
- Level 1-2 : Normal and resistant bricks
- Level 3+ : Introduction of super resistant and unbreakable bricks
- Level 10 : Maximum difficulty with all brick types

## Code Architecture

### Main Classes
- **Ball** : Ball physics and collision management
- **Paddle** : Paddle control and keyboard interaction
- **Brick** : Brick types with resistance system
- **GamePanel** : Game loop, rendering and state management
- **BrickBreakerGame** : Main window and entry point

### Design Patterns Used
- **State Pattern** : Management of different game states
- **Observer Pattern** : Keyboard event handling
- **Component Pattern** : Separation of entity responsibilities

## Development and Extensions

### Possible Improvements
- Sound and sound effects
- Power-ups (enlarged paddle, multiple balls, etc.)
- Particles and visual effects
- Custom levels
- Score saving
- Multiplayer mode

### Modular Structure
The code is organized to facilitate extensions:
- Easy addition of new brick types
- Extensible state system
- Modular rendering architecture

## License

This project is developed for educational and demonstration purposes. Free to use and modify.

## Author

Developed with ❤️ in Java by @cheikhn414