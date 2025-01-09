package game.core;

import game.controller.LabyrinthController;
import game.entities.GameObject;
import game.entities.Player;
import game.entities.Dragon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LabyrinthManagerTest {

    @Test
    public void testGameWinCondition() {
        ArrayList<GameObject> walls = new ArrayList<>();
        LabyrinthManager manager = new LabyrinthManager(null, walls);
        Player player = manager.getPlayer();
        player.setX(19);
        player.setY(0);

        assertTrue(manager.isGameWon());
    }

    @Test
    public void testGameLossCondition() {
        ArrayList<GameObject> walls = new ArrayList<>();
        LabyrinthManager manager = new LabyrinthManager(null, walls);
        Dragon dragon = manager.getDragon();
        dragon.setX(0);
        dragon.setY(19);

        assertTrue(manager.isGameLost());
    }

    @Test
    public void testUpdateStatus() {
        ArrayList<GameObject> walls = new ArrayList<>();
        LabyrinthManager manager = new LabyrinthManager(null, walls);
        manager.updateStatus();
    }

    @Test
    public void testRestartLabyrinth() {
        ArrayList<GameObject> walls = new ArrayList<>();
        LabyrinthController controller = new LabyrinthController(null, walls);
        LabyrinthManager manager = new LabyrinthManager(null, walls);

        manager.setGameWindow(null);
        controller.initializeWalls();
        Player player = manager.getPlayer();

        player.setX(19);
        player.setY(0);
        manager.updateStatus();

        controller.initializeWalls();
        player.setX(0);
        player.setY(19);

        assertEquals(0, player.getX());
        assertEquals(19, player.getY());
    }
}
