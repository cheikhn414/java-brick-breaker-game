package com.brickbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class BrickBreakerGame extends JFrame {

    private final GamePanel gamePanel;

    public BrickBreakerGame() {
        setTitle("Brick Breaker - Jeu de Casse-Briques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);

        setIconImage(createGameIcon());
    }

    private Image createGameIcon() {
        Image icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) icon.getGraphics();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 32, 32);

        g2d.setColor(Color.ORANGE);
        g2d.fillRect(4, 4, 24, 8);
        g2d.setColor(Color.RED);
        g2d.fillRect(4, 12, 24, 8);

        g2d.setColor(Color.CYAN);
        g2d.fillRect(12, 24, 8, 3);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(14, 20, 4, 4);

        g2d.dispose();
        return icon;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Impossible de définir le look and feel système: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new BrickBreakerGame();
            frame.setVisible(true);
        });
    }
}
