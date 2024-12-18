package game.controller;

import game.core.LabyrinthManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LabyrinthController extends KeyAdapter {
    private final LabyrinthManager manager;

    public LabyrinthController(LabyrinthManager manager) {
        this.manager = manager;
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
