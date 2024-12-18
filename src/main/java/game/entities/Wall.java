package game.entities;

import game.core.GameObject;

import java.awt.Graphics2D;
import java.awt.Color;

public class Wall extends GameObject {
    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x * 35, y * 35, 35, 35);
    }
}
