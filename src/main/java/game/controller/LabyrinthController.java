package game.controller;

import game.entities.GameObject;
import game.core.LabyrinthManager;
import game.entities.Wall;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class LabyrinthController extends KeyAdapter {
    private final LabyrinthManager manager;
    private final ArrayList<GameObject> walls;
    private final Random random = new Random();

    public LabyrinthController(LabyrinthManager manager, ArrayList<GameObject> walls) {
        this.manager = manager;
        this.walls = walls;
    }

    public void initializeWalls() {
        walls.clear();
        boolean[][] grid = new boolean[20][20];
        int x = 0, y = 19;
        grid[x][y] = true;

        while (x != 19 || y != 0) {
            if (x < 19 && (random.nextBoolean() || y == 0)) {
                x++;
            } else if (y > 0) {
                y--;
            }
            grid[x][y] = true;
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (!grid[i][j] && random.nextDouble() < 0.3) {
                    walls.add(new Wall(i, j));
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> manager.movePlayer(0, -1);
            case KeyEvent.VK_DOWN -> manager.movePlayer(0, 1);
            case KeyEvent.VK_LEFT -> manager.movePlayer(-1, 0);
            case KeyEvent.VK_RIGHT -> manager.movePlayer(1, 0);
        }
        System.out.println("Key Pressed: " + e.getKeyCode());
    }
}
