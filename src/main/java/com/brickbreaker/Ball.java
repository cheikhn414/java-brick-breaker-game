package com.brickbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ball {
    private double x, y;
    private double velocityX, velocityY;
    private final int radius;
    private final Color color;
    private static final double SPEED_INCREASE = 0.02;
    
    public Ball(double x, double y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = 3;
        this.velocityY = -3;
        this.color = Color.WHITE;
    }
    
    public void update() {
        x += velocityX;
        y += velocityY;
    }
    
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    }
    
    public void bounceX() {
        velocityX = -velocityX;
        increaseSpeed();
    }
    
    public void bounceY() {
        velocityY = -velocityY;
        increaseSpeed();
    }
    
    private void increaseSpeed() {
        if (Math.abs(velocityX) < 8) {
            velocityX += velocityX > 0 ? SPEED_INCREASE : -SPEED_INCREASE;
        }
        if (Math.abs(velocityY) < 8) {
            velocityY += velocityY > 0 ? SPEED_INCREASE : -SPEED_INCREASE;
        }
    }
    
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocityX = 3;
        this.velocityY = -3;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public int getRadius() { return radius; }
    
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
}