package com.brickbreaker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private static final int DELAY = 16; // ~60 FPS

    private final Timer timer;
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private GameState gameState;

    private int score;
    private int lives;
    private int level;
    private int highScore;

    private final Font gameFont;
    private final Font titleFont;
    private final Font buttonFont;

    private int menuSelection = 0;
    private final String[] menuOptions = {"Jouer", "Scores", "Quitter"};

    public GamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyListener());

        gameFont = new Font("Arial", Font.BOLD, 16);
        titleFont = new Font("Arial", Font.BOLD, 48);
        buttonFont = new Font("Arial", Font.BOLD, 24);

        gameState = GameState.MENU;
        score = 0;
        lives = 3;
        level = 1;
        highScore = 0;

        initializeGame();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initializeGame() {
        ball = new Ball(PANEL_WIDTH / 2.0, PANEL_HEIGHT - 150, 10);
        paddle = new Paddle(PANEL_WIDTH / 2.0 - 50, PANEL_HEIGHT - 50, 100, 15, PANEL_WIDTH);
        createLevel(level);
    }

    private void createLevel(int levelNum) {
        bricks = new ArrayList<>();
        int brickWidth = 70;
        int brickHeight = 20;
        int rows = Math.min(5 + levelNum, 10);
        int cols = 10;
        int startX = (PANEL_WIDTH - (cols * brickWidth + (cols - 1) * 5)) / 2;
        int startY = 50;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + 5);
                int y = startY + row * (brickHeight + 5);

                Brick.BrickType type = determineBrickType(row, levelNum);
                bricks.add(new Brick(x, y, brickWidth, brickHeight, type));
            }
        }
    }

    private Brick.BrickType determineBrickType(int row, int level) {
        if (level >= 3 && row == 0 && Math.random() < 0.1) {
            return Brick.BrickType.UNBREAKABLE;
        }
        if (level >= 2 && row <= 1 && Math.random() < 0.3) {
            return Brick.BrickType.SUPER;
        }
        if (row <= 2 && Math.random() < 0.4) {
            return Brick.BrickType.STRONG;
        }
        return Brick.BrickType.NORMAL;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (gameState) {
            case PLAYING ->
                updateGame();
            case MENU, PAUSED, GAME_OVER, LEVEL_COMPLETE, GAME_COMPLETE -> {
            }
        }
        repaint();
    }

    private void updateGame() {
        ball.update();
        paddle.update();

        checkWallCollisions();
        checkPaddleCollision();
        checkBrickCollisions();

        if (ball.getY() > PANEL_HEIGHT) {
            lives--;
            if (lives <= 0) {
                gameState = GameState.GAME_OVER;
                if (score > highScore) {
                    highScore = score;
                }
            } else {
                resetBall();
            }
        }

        if (allBricksDestroyed()) {
            level++;
            if (level > 10) {
                gameState = GameState.GAME_COMPLETE;
                if (score > highScore) {
                    highScore = score;
                }
            } else {
                gameState = GameState.LEVEL_COMPLETE;
            }
        }
    }

    private void checkWallCollisions() {
        if (ball.getX() - ball.getRadius() <= 0 || ball.getX() + ball.getRadius() >= PANEL_WIDTH) {
            ball.bounceX();
        }
        if (ball.getY() - ball.getRadius() <= 0) {
            ball.bounceY();
        }
    }

    private void checkPaddleCollision() {
        if (ball.getBounds().intersects(paddle.getBounds()) && ball.getVelocityY() > 0) {
            double angle = paddle.calculateBallBounceAngle(ball.getX());
            double speed = Math.sqrt(ball.getVelocityX() * ball.getVelocityX()
                    + ball.getVelocityY() * ball.getVelocityY());
            ball.setVelocityX(speed * Math.sin(angle));
            ball.setVelocityY(-Math.abs(speed * Math.cos(angle)));
        }
    }

    private void checkBrickCollisions() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                boolean destroyed = brick.hit();

                if (destroyed) {
                    score += brick.getPoints();
                }

                double ballCenterX = ball.getX();
                double ballCenterY = ball.getY();
                double brickCenterX = brick.getX() + brick.getWidth() / 2.0;
                double brickCenterY = brick.getY() + brick.getHeight() / 2.0;

                double dx = ballCenterX - brickCenterX;
                double dy = ballCenterY - brickCenterY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    ball.bounceX();
                } else {
                    ball.bounceY();
                }
                break;
            }
        }
    }

    private boolean allBricksDestroyed() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && brick.getType() != Brick.BrickType.UNBREAKABLE) {
                return false;
            }
        }
        return true;
    }

    private void resetBall() {
        ball.reset(PANEL_WIDTH / 2.0, PANEL_HEIGHT - 150);
        paddle.reset(PANEL_WIDTH / 2.0 - 50, PANEL_HEIGHT - 50);
    }

    private void resetGame() {
        score = 0;
        lives = 3;
        level = 1;
        initializeGame();
        gameState = GameState.PLAYING;
    }

    private void nextLevel() {
        createLevel(level);
        resetBall();
        gameState = GameState.PLAYING;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (gameState) {
            case MENU ->
                drawMenu(g2d);
            case PLAYING ->
                drawGame(g2d);
            case PAUSED -> {
                drawGame(g2d);
                drawPauseOverlay(g2d);
            }
            case GAME_OVER ->
                drawGameOver(g2d);
            case LEVEL_COMPLETE ->
                drawLevelComplete(g2d);
            case GAME_COMPLETE ->
                drawGameComplete(g2d);
        }
    }

    private void drawMenu(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        String title = "BRICK BREAKER";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (PANEL_WIDTH - fm.stringWidth(title)) / 2;
        g2d.drawString(title, x, 150);

        g2d.setFont(buttonFont);
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == menuSelection) {
                g2d.setColor(Color.YELLOW);
                g2d.drawString("> " + menuOptions[i] + " <", 300, 300 + i * 50);
            } else {
                g2d.setColor(Color.WHITE);
                g2d.drawString(menuOptions[i], 320, 300 + i * 50);
            }
        }

        g2d.setColor(Color.GRAY);
        g2d.setFont(gameFont);
        g2d.drawString("High Score: " + highScore, 10, PANEL_HEIGHT - 10);
        g2d.drawString("Contrôles: Flèches ou A/D pour bouger, ESPACE pour pause, ÉCHAP pour menu", 48, 500);
    }

    private void drawGame(Graphics2D g2d) {
        ball.render(g2d);
        paddle.render(g2d);

        for (Brick brick : bricks) {
            brick.render(g2d);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        g2d.drawString("Score: " + score, 10, 25);
        g2d.drawString("Vies: " + lives, 120, 25);
        g2d.drawString("Niveau: " + level, 200, 25);
        g2d.drawString("High Score: " + highScore, PANEL_WIDTH - 150, 25);
    }

    private void drawPauseOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        String text = "PAUSE";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2);

        g2d.setFont(gameFont);
        text = "Appuyez sur ESPACE pour continuer";
        fm = g2d.getFontMetrics();
        x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 + 50);
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.setFont(titleFont);
        String text = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 - 100);

        g2d.setColor(Color.WHITE);
        g2d.setFont(buttonFont);
        text = "Score Final: " + score;
        fm = g2d.getFontMetrics();
        x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 - 20);

        if (score == highScore && score > 0) {
            g2d.setColor(Color.YELLOW);
            text = "NOUVEAU RECORD!";
            fm = g2d.getFontMetrics();
            x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
            g2d.drawString(text, x, PANEL_HEIGHT / 2 + 20);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        text = "Appuyez sur R pour rejouer ou ÉCHAP pour le menu";
        fm = g2d.getFontMetrics();
        x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 + 80);
    }

    private void drawLevelComplete(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.setFont(titleFont);
        String text = "NIVEAU TERMINÉ!";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 - 50);

        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        text = "Appuyez sur ESPACE pour le niveau suivant";
        fm = g2d.getFontMetrics();
        x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 + 20);
    }

    private void drawGameComplete(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.setFont(titleFont);
        String text = "FÉLICITATIONS!";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 - 100);

        g2d.setColor(Color.WHITE);
        g2d.setFont(buttonFont);
        text = "Tous les niveaux terminés!";
        fm = g2d.getFontMetrics();
        x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 - 20);

        text = "Score Final: " + score;
        fm = g2d.getFontMetrics();
        x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 + 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        text = "Appuyez sur R pour rejouer ou ÉCHAP pour le menu";
        fm = g2d.getFontMetrics();
        x = (PANEL_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, PANEL_HEIGHT / 2 + 80);
    }

    private class GameKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (gameState) {
                case MENU ->
                    handleMenuKeys(e);
                case PLAYING -> {
                    handleGameKeys(e);
                    paddle.keyPressed(e);
                }
                case PAUSED ->
                    handlePauseKeys(e);
                case GAME_OVER, GAME_COMPLETE ->
                    handleGameOverKeys(e);
                case LEVEL_COMPLETE ->
                    handleLevelCompleteKeys(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (gameState == GameState.PLAYING) {
                paddle.keyReleased(e);
            }
        }

        private void handleMenuKeys(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP, KeyEvent.VK_W ->
                    menuSelection = (menuSelection - 1 + menuOptions.length) % menuOptions.length;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
                    menuSelection = (menuSelection + 1) % menuOptions.length;
                case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                    switch (menuSelection) {
                        case 0 -> // Jouer
                            resetGame();
                        case 1 -> // Scores
                            JOptionPane.showMessageDialog(GamePanel.this,
                                    "Meilleur Score: " + highScore,
                                    "Scores", JOptionPane.INFORMATION_MESSAGE);
                        case 2 -> // Quitter
                            System.exit(0);
                    }
                }
            }
        }

        private void handleGameKeys(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE ->
                    gameState = GameState.PAUSED;
                case KeyEvent.VK_ESCAPE ->
                    gameState = GameState.MENU;
            }
        }

        private void handlePauseKeys(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE ->
                    gameState = GameState.PLAYING;
                case KeyEvent.VK_ESCAPE ->
                    gameState = GameState.MENU;
            }
        }

        private void handleGameOverKeys(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_R ->
                    resetGame();
                case KeyEvent.VK_ESCAPE ->
                    gameState = GameState.MENU;
            }
        }

        private void handleLevelCompleteKeys(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE ->
                    nextLevel();
                case KeyEvent.VK_ESCAPE ->
                    gameState = GameState.MENU;
            }
        }
    }
}
