package game.entities;

import game.core.GameObject;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;

public class Wall extends GameObject {
    private Image wall;

    public Wall(int x, int y) {
        super(x, y);
        loadImage();
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("/home/etagkan/Desktop/Labyrinth/src/main/resources/images/wall.jpeg");
        Image originalImage = ii.getImage();
        wall = originalImage.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(wall, x * 35, y * 35, null);
    }
}
