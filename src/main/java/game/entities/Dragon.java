package game.entities;

import game.core.GameObject;

import java.awt.Graphics2D;
import java.awt.Color;

public class Dragon extends GameObject {
    public Dragon(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillOval(x * 35, y * 35, 35, 35);
    }
}
