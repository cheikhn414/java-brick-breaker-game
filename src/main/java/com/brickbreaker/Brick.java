package com.brickbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Brick {
    private final int x, y;
    private final int width, height;
    private Color color;
    private boolean destroyed;
    private final int points;
    private int hitCount;
    private final int maxHits;
    private final BrickType type;
    
    public enum BrickType {
        NORMAL(1, 10, Color.ORANGE),
        STRONG(2, 25, Color.RED),
        SUPER(3, 50, Color.MAGENTA),
        UNBREAKABLE(Integer.MAX_VALUE, 0, Color.GRAY);
        
        private final int hits;
        private final int points;
        private final Color color;
        
        BrickType(int hits, int points, Color color) {
            this.hits = hits;
            this.points = points;
            this.color = color;
        }
        
        public int getHits() { return hits; }
        public int getPoints() { return points; }
        public Color getColor() { return color; }
    }
    
    public Brick(int x, int y, int width, int height, BrickType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.maxHits = type.getHits();
        this.hitCount = 0;
        this.points = type.getPoints();
        this.color = type.getColor();
        this.destroyed = false;
    }
    
    public boolean hit() {
        if (destroyed || type == BrickType.UNBREAKABLE) {
            return false;
        }
        
        hitCount++;
        updateColor();
        
        if (hitCount >= maxHits) {
            destroyed = true;
            return true;
        }
        return false;
    }
    
    private void updateColor() {
        if (type == BrickType.STRONG) {
            color = hitCount >= 1 ? new Color(139, 0, 0) : Color.RED;
        } else if (type == BrickType.SUPER) {
            color = switch (hitCount) {
                case 1 -> new Color(139, 0, 139);
                case 2 -> new Color(75, 0, 130);
                default -> Color.MAGENTA;
            };
        }
    }
    
    public void render(Graphics2D g2d) {
        if (destroyed) return;
        
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        
        if (type == BrickType.STRONG || type == BrickType.SUPER) {
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(maxHits - hitCount), 
                         x + width/2 - 5, y + height/2 + 5);
        }
        
        if (type == BrickType.UNBREAKABLE) {
            g2d.setColor(Color.WHITE);
            g2d.drawLine(x, y, x + width, y + height);
            g2d.drawLine(x + width, y, x, y + height);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isDestroyed() { return destroyed; }
    public int getPoints() { return points; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public BrickType getType() { return type; }
    
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}