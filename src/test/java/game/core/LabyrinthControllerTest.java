package game.core;

import game.controller.LabyrinthController;
import game.entities.GameObject;
import game.entities.Player;
import game.entities.Wall;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LabyrinthControllerTest {

    @Test
    public void testInitializeWalls() {
        ArrayList<GameObject> walls = new ArrayList<>();
        LabyrinthManager mockManager = new LabyrinthManager(null, walls);
        LabyrinthController controller = new LabyrinthController(mockManager, walls);

        controller.initializeWalls();

        assertFalse(walls.isEmpty());
        boolean startClear = walls.stream().noneMatch(w -> w.getX() == 0 && w.getY() == 19);
        boolean exitClear = walls.stream().noneMatch(w -> w.getX() == 19 && w.getY() == 0);

        assertTrue(startClear);
        assertTrue(exitClear);
    }

    @Test
    public void testWallBlockingMovement() {
        ArrayList<GameObject> walls = new ArrayList<>();
        LabyrinthManager mockManager = new LabyrinthManager(null, walls);
        LabyrinthController controller = new LabyrinthController(mockManager, walls);

        walls.add(new Wall(0, 18));
        Player player = mockManager.getPlayer();

        JPanel panel = new JPanel();

        KeyEvent upKey = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, ' ');

        controller.keyPressed(upKey);
        assertEquals(0, player.getX());
        assertEquals(19, player.getY());
    }
}
