package game.core;

import game.controller.LabyrinthController;
import game.entities.GameObject;
import game.entities.Player;
import game.entities.Dragon;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameBoard extends JPanel {
    private final LabyrinthManager labyrinthManager;
    private final LabyrinthController labyrinthController;
    private final Player player;
    private final Dragon dragon;
    private final ArrayList<GameObject> walls;

    public GameBoard() {
        walls = new ArrayList<>();
        labyrinthManager = new LabyrinthManager(this, walls); // Pass walls to the manager
        labyrinthController = new LabyrinthController(labyrinthManager, walls); // Initialize the controller
        player = labyrinthManager.getPlayer();
        dragon = labyrinthManager.getDragon();

        labyrinthController.initializeWalls(); // Initialize walls via controller

        setFocusable(true);
        addKeyListener(labyrinthController);
        requestFocusInWindow();

        Timer timer = new Timer(500, e -> updateGame());
        timer.start();

        SwingUtilities.invokeLater(this::requestFocusInWindow);
        setPreferredSize(new Dimension(20 * 35, 20 * 35));
    }

    private void updateGame() {
        labyrinthManager.moveDragon();
        if (labyrinthManager.isGameLost()) {
            labyrinthManager.handleGameOver();
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

    public LabyrinthManager getLabyrinthManager() {
        return labyrinthManager;
    }
}
