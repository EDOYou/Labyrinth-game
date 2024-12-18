package game.core;

import game.database.HighScore;
import game.database.HighScores;
import game.entities.Player;
import game.entities.Dragon;
import game.entities.Wall;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabyrinthManager {
    private final GameBoard gameBoard;
    private final Player player;
    private final Dragon dragon;
    private final ArrayList<GameObject> walls;
    private final Random random = new Random();
    private int labyrinthsSolved;
    private HighScores highScores;

    public LabyrinthManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.player = new Player(0, 19);
        this.dragon = new Dragon(random.nextInt(18) + 1, random.nextInt(18) + 1);
        this.walls = new ArrayList<>();
        initializeWalls();
        this.labyrinthsSolved = 0;

        try {
            highScores = new HighScores(10);
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(LabyrinthManager.class.getName()).log(Level.SEVERE, "Something is wrong with database connection", e);
        }
    }

    private void initializeWalls() {
        boolean[][] grid = new boolean[20][20];
        int x = 0, y = 19;
        grid[x][y] = true;

        Random random = new Random();
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

        for (int i = 0; i < 20; i++) {
            walls.add(new Wall(i, 0));
            walls.add(new Wall(i, 19));
            walls.add(new Wall(0, i));
            walls.add(new Wall(19, i));
        }

        walls.removeIf(wall ->
                (wall.getX() == 0 && wall.getY() == 19) ||
                        (wall.getX() == 0 && wall.getY() == 18) ||
                        (wall.getX() == 1 && wall.getY() == 19)
        );

        walls.removeIf(wall ->
                (wall.getX() == 19 && wall.getY() == 0) ||
                        (wall.getX() == 18 && wall.getY() == 0) ||
                        (wall.getX() == 19 && wall.getY() == 1)
        );

        System.out.println("Clearing exit area: (19, 0), (18, 0), (19, 1)");
        System.out.println("Labyrinth initialized with a solvable path.");
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
        return dx <= 1 && dy <= 1;
    }

    public void saveHighScore(String playerName) {
        try {
            highScores.putHighScore(playerName, labyrinthsSolved);
            System.out.println("High score saved for player: " + playerName);
        } catch (SQLException e) {
            Logger.getLogger(LabyrinthManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showHighScores() {
        try {
            System.out.println("High Scores:");
            for (HighScore score : highScores.getHighScores()) {
                System.out.println(score);
            }
        } catch (SQLException e) {
            Logger.getLogger(LabyrinthManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void handleGameOver(boolean won) {
        if (won) {
            labyrinthsSolved++;
            System.out.println("Congratulations! You solved the labyrinth!");

            String playerName = JOptionPane.showInputDialog("You won! Enter your name:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                saveHighScore(playerName.trim());
            }
        } else {
            System.out.println("You were caught by the dragon!");

            String playerName = JOptionPane.showInputDialog("Game Over! Enter your name:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                saveHighScore(playerName.trim());
            }
        }
        showHighScores();
        System.exit(0);
    }
}
