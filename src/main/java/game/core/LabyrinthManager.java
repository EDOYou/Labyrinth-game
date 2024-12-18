package game.core;

import game.entities.Player;
import game.entities.Dragon;
import game.entities.Wall;

import java.util.ArrayList;
import java.util.Random;

public class LabyrinthManager {
    private final GameBoard gameBoard;
    private final Player player;
    private final Dragon dragon;
    private final ArrayList<GameObject> walls;
    private final Random random = new Random();
    private int labyrinthsSolved;

    public LabyrinthManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.player = new Player(0, 19);
        this.dragon = new Dragon(random.nextInt(18) + 1, random.nextInt(18) + 1);
        this.walls = new ArrayList<>();
        initializeWalls();
        this.labyrinthsSolved = 0;
    }

    private void initializeWalls() {
        for (int i = 0; i < 20; i++) {
            walls.add(new Wall(i, 0));
            walls.add(new Wall(i, 19));
            walls.add(new Wall(0, i));
            walls.add(new Wall(19, i));
        }

        for (int i = 1; i < 19; i++) {
            for (int j = 1; j < 19; j++) {
                if (random.nextDouble() < 0.3) {
                    walls.add(new Wall(i, j));
                    System.out.println("Wall added at: (" + i + ", " + j + ")");
                }
            }
        }

        walls.removeIf(wall ->
                (wall.getX() == 0 && wall.getY() == 19) ||
                        (wall.getX() == 0 && wall.getY() == 18) ||
                        (wall.getX() == 1 && wall.getY() == 19)
        );

        System.out.println("Walls initialized. Starting area cleared.");
    }

    public ArrayList<GameObject> getWalls() {
        return walls;
    }

    public Player getPlayer() {
        return player;
    }

    public Dragon getDragon() {
        return dragon;
    }

    public void movePlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        System.out.println("Trying to move player to: (" + newX + ", " + newY + ")"); // Debug

        if (isValidMove(newX, newY)) {
            player.setX(newX);
            player.setY(newY);
            System.out.println("Player moved to: (" + newX + ", " + newY + ")");
            gameBoard.repaint();
        } else {
            System.out.println("Invalid move to: (" + newX + ", " + newY + ")");
        }
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || y < 0 || x >= 20 || y >= 20) {
            System.out.println("Move out of bounds: (" + x + ", " + y + ")");
            return false;
        }

        for (GameObject wall : walls) {
            if (wall.getX() == x && wall.getY() == y) {
                System.out.println("Move blocked by wall at: (" + x + ", " + y + ")");
                return false;
            }
        }
        return true;
    }


    public void moveDragon() {
        int dx = 0, dy = 0;
        switch (random.nextInt(4)) {
            case 0 -> dy = -1;
            case 1 -> dy = 1;
            case 2 -> dx = -1;
            case 3 -> dx = 1;
        }
        int newX = dragon.getX() + dx;
        int newY = dragon.getY() + dy;

        if (isValidMove(newX, newY)) {
            dragon.setX(newX);
            dragon.setY(newY);
        }
    }

    public boolean isGameWon() {
        return player.getX() == 19 && player.getY() == 0;
    }

    public boolean isGameLost() {
        int dx = Math.abs(player.getX() - dragon.getX());
        int dy = Math.abs(player.getY() - dragon.getY());
        return dx <= 1 && dy <= 1; // Dragon is a neighbor
    }
}
