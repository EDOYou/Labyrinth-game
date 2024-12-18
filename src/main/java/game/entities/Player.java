package game.entities;

import game.core.GameObject;

import java.awt.Graphics2D;
import java.awt.Color;

public class Player extends GameObject {
    public Player(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(x * 35, y * 35, 35, 35);
        System.out.println("Drawing player at: (" + x + ", " + y + ")");
    }
}
