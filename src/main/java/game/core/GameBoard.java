package game.core;

import game.controller.LabyrinthController;
import game.entities.Player;
import game.entities.Dragon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameBoard extends JPanel {
    private final LabyrinthManager labyrinthManager;
    private final Player player;
    private final Dragon dragon;
    private final ArrayList<GameObject> walls;
    private final LabyrinthController controller;

    public GameBoard() {
        labyrinthManager = new LabyrinthManager(this);
        player = labyrinthManager.getPlayer();
        dragon = labyrinthManager.getDragon();
        walls = labyrinthManager.getWalls();

        controller = new LabyrinthController(labyrinthManager);
        setFocusable(true);
        addKeyListener(controller);
        requestFocusInWindow();

        Timer timer = new Timer(500, e -> updateGame());
        timer.start();

        SwingUtilities.invokeLater(this::requestFocusInWindow);
        setPreferredSize(new Dimension(20 * 35, 20 * 35));
    }

    private void updateGame() {
        labyrinthManager.moveDragon();
        if (labyrinthManager.isGameWon()) {
            JOptionPane.showMessageDialog(this, "You escaped the labyrinth! Congratulations!");
            System.exit(0);
        } else if (labyrinthManager.isGameLost()) {
            JOptionPane.showMessageDialog(this, "You were caught by the dragon! Game Over.");
            System.exit(0);
        }
        repaint();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawVisibleLabyrinth(g2d);
        player.draw(g2d);
        dragon.draw(g2d);
    }

    private void drawVisibleLabyrinth(Graphics2D g2d) {
        int playerX = player.getX();
        int playerY = player.getY();

        for (GameObject wall : walls) {
            if (Math.abs(wall.getX() - playerX) <= 3 && Math.abs(wall.getY() - playerY) <= 3) {
                wall.draw(g2d);
            }
        }
    }
}
