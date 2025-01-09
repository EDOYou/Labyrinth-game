package game.core;

import game.controller.LabyrinthController;
import game.database.HighScore;
import game.database.HighScores;
import game.entities.GameObject;
import game.entities.Player;
import game.entities.Dragon;
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

    public LabyrinthManager(GameBoard gameBoard, ArrayList<GameObject> walls) {
        this.gameBoard = gameBoard;
        this.walls = walls;
        this.player = new Player(0, 19);
        this.dragon = new Dragon(random.nextInt(18) + 1, random.nextInt(18) + 1);
        this.labyrinthsSolved = 0;

        try {
            highScores = new HighScores(10);
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(LabyrinthManager.class.getName()).log(Level.SEVERE, "Database connection error", e);
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

    public void movePlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        if (isValidMove(newX, newY)) {
            player.setX(newX);
            player.setY(newY);
            updateStatus();

            if (isGameWon()) {
                handleLabyrinthSolved();
            }
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

            if (isGameLost()) {
                handleGameOver();
            }
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

    private void handleLabyrinthSolved() {
        labyrinthsSolved++;
        JOptionPane.showMessageDialog(null, "Congratulations! You solved the labyrinth!");
        restartLabyrinth();
    }

    public void restartLabyrinth() {
        walls.clear();
        LabyrinthController controller = new LabyrinthController(this, walls);
        controller.initializeWalls();

        player.setX(0);
        player.setY(19);
        dragon.setX(random.nextInt(18) + 1);
        dragon.setY(random.nextInt(18) + 1);

        initializeGame();
        gameBoard.repaint();
    }

    public void handleGameOver() {
        if (timer != null) {
            timer.stop();
        }

        String playerName = JOptionPane.showInputDialog("Game Over! Enter your name:");
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
}
