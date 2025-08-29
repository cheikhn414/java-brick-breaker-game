package com.brickbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Paddle {
    private double x, y;
    private final int width, height;
    private final double speed;
    private final Color color;
    private boolean leftPressed, rightPressed;
    private final int gameWidth;
    
    public Paddle(double x, double y, int width, int height, int gameWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = 6.0;
        this.color = Color.CYAN;
        this.gameWidth = gameWidth;
        this.leftPressed = false;
        this.rightPressed = false;
    }
    
    public void update() {
        if (leftPressed && x > 0) {
            x -= speed;
        }
        if (rightPressed && x + width < gameWidth) {
            x += speed;
        }
        
        if (x < 0) x = 0;
        if (x + width > gameWidth) x = gameWidth - width;
    }
    
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect((int)x, (int)y, width, height);
        
        g2d.setColor(Color.WHITE);
        g2d.drawRect((int)x, (int)y, width, height);
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect((int)x + 2, (int)y + 2, width - 4, height - 4);
    }
    
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> rightPressed = true;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> rightPressed = false;
        }
    }
    
    public double calculateBallBounceAngle(double ballX) {
        double paddleCenter = x + width / 2.0;
        double offset = ballX - paddleCenter;
        double normalizedOffset = offset / (width / 2.0);
        
        double maxAngle = Math.PI / 3;
        return normalizedOffset * maxAngle;
    }
    
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.leftPressed = false;
        this.rightPressed = false;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getCenterX() { return x + width / 2.0; }
}