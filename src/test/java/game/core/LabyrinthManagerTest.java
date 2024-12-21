package game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class LabyrinthManagerTest {

    @Test
    public void testLabyrinthInitialization() {
        GameBoard mockGameBoard = new GameBoard();
        LabyrinthManager manager = new LabyrinthManager(mockGameBoard);

        ArrayList<GameObject> walls = manager.getWalls();
        assertFalse(walls.isEmpty(), "Walls should not be empty after initialization.");
        assertEquals(0, manager.getPlayer().getX());
        assertEquals(19, manager.getPlayer().getY());
    }

    @Test
    public void testPlayerMovementValid() {
        GameBoard mockGameBoard = new GameBoard();
        LabyrinthManager manager = new LabyrinthManager(mockGameBoard);

        manager.movePlayer(1, 0); // Move right
        assertEquals(1, manager.getPlayer().getX());
        assertEquals(19, manager.getPlayer().getY());
    }

    @Test
    public void testPlayerMovementInvalid() {
        GameBoard mockGameBoard = new GameBoard();
        LabyrinthManager manager = new LabyrinthManager(mockGameBoard);

        manager.movePlayer(0, -1); // Move up into a wall
        assertEquals(0, manager.getPlayer().getX());
        assertEquals(19, manager.getPlayer().getY());
    }

    @Test
    public void testDragonMovement() {
        GameBoard mockGameBoard = new GameBoard();
        LabyrinthManager manager = new LabyrinthManager(mockGameBoard);

        int initialX = manager.getDragon().getX();
        int initialY = manager.getDragon().getY();

        manager.moveDragon();

        int newX = manager.getDragon().getX();
        int newY = manager.getDragon().getY();

        assertTrue(Math.abs(newX - initialX) <= 1);
        assertTrue(Math.abs(newY - initialY) <= 1);
        assertNotEquals(initialX, newX, "Dragon should have moved.");
        assertNotEquals(initialY, newY, "Dragon should have moved.");
    }

    @Test
    public void testGameWinCondition() {
        GameBoard mockGameBoard = new GameBoard();
        LabyrinthManager manager = new LabyrinthManager(mockGameBoard);

        manager.movePlayer(19, -19); // Move directly to (19, 0)
        assertTrue(manager.isGameWon(), "Player should have won the game.");
    }

    @Test
    public void testGameLossCondition() {
        GameBoard mockGameBoard = new GameBoard();
        LabyrinthManager manager = new LabyrinthManager(mockGameBoard);

        manager.getDragon().setX(1);
        manager.getDragon().setY(19);

        assertTrue(manager.isGameLost(), "Player should have lost the game.");
    }

    @Test
    public void testWallInitialization() {
        GameBoard mockGameBoard = new GameBoard();
        LabyrinthManager manager = new LabyrinthManager(mockGameBoard);

        ArrayList<GameObject> walls = manager.getWalls();
        boolean startClear = walls.stream().noneMatch(w -> w.getX() == 0 && w.getY() == 19);
        boolean exitClear = walls.stream().noneMatch(w -> w.getX() == 19 && w.getY() == 0);

        assertTrue(startClear, "Start point should be clear.");
        assertTrue(exitClear, "Exit point should be clear.");
    }
}
