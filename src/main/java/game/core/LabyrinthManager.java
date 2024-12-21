package game.core;

import game.database.HighScore;
import game.database.HighScores;
import game.entities.Player;
import game.entities.Dragon;
import game.entities.Wall;
import game.ui.GameWindow;

import javax.swing.Timer;
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
    private GameWindow gameWindow;
    private int elapsedTime;
    private Timer timer;

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

        initializeGame();
    }

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    private void initializeGame() {
        elapsedTime = 0;
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(1000, e -> updateElapsedTime());
        timer.start();
        updateStatus();
    }

    private void updateElapsedTime() {
        elapsedTime++;
        if (gameWindow != null) {
            gameWindow.updateTimer(elapsedTime);
        }
    }

    public void updateStatus() {
        if (gameWindow != null) {
            boolean dragonNearby = isDragonNearby();
            gameWindow.updateStatus(labyrinthsSolved, dragonNearby);
        }
    }

    private boolean isDragonNearby() {
        int dx = Math.abs(player.getX() - dragon.getX());
        int dy = Math.abs(player.getY() - dragon.getY());
        return dx <= 3 && dy <= 3;
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

    public void movePlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        if (isValidMove(newX, newY)) {
            player.setX(newX);
            player.setY(newY);
            updateStatus();
            gameBoard.repaint();
        }
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || y < 0 || x >= 20 || y >= 20) {
            return false;
        }
        for (GameObject wall : walls) {
            if (wall.getX() == x && wall.getY() == y) {
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

    public void handleGameOver(boolean won) {
        if (timer != null) {
            timer.stop();
        }
        if (won) {
            labyrinthsSolved++;
        }

        String message = won ? "You solved the labyrinth!" : "You were caught by the dragon!";
        JOptionPane.showMessageDialog(null, message);

        String playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            saveHighScore(playerName.trim());
        }
        showHighScores();
        System.exit(0);
    }

    private void saveHighScore(String playerName) {
        try {
            highScores.putHighScore(playerName, labyrinthsSolved);
        } catch (SQLException e) {
            Logger.getLogger(LabyrinthManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void showHighScores() {
        try {
            System.out.println("High Scores:");
            for (HighScore score : highScores.getHighScores()) {
                System.out.println(score);
            }
        } catch (SQLException e) {
            Logger.getLogger(LabyrinthManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Dragon getDragon() {
        return dragon;
    }

    public ArrayList<GameObject> getWalls() {
        return walls;
    }
}
