package game.ui;

import game.core.GameBoard;
import game.core.LabyrinthManager;
import game.database.HighScore;
import game.database.HighScores;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameWindow extends JFrame {
    private GameBoard gameBoard;
    private final StatusPanel statusPanel;

    public GameWindow() {
        super("Labyrinth Game");

        gameBoard = new GameBoard();
        LabyrinthManager labyrinthManager = gameBoard.getLabyrinthManager();
        labyrinthManager.setGameWindow(this);

        statusPanel = new StatusPanel();
        this.add(statusPanel, BorderLayout.NORTH);
        this.add(gameBoard, BorderLayout.CENTER);

        JMenuBar menuBar = createMenuBar();
        this.setJMenuBar(menuBar);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem showHighScores = new JMenuItem("Show Highscores");
        JMenuItem restartGame = new JMenuItem("Restart Game");

        showHighScores.addActionListener(e -> showHighScores());
        restartGame.addActionListener(e -> restartGame());

        menu.add(showHighScores);
        menu.add(restartGame);
        menuBar.add(menu);
        return menuBar;
    }

    public void updateStatus(int labyrinthsSolved, boolean dragonNearby) {
        statusPanel.setLabyrinthsSolved(labyrinthsSolved);
        statusPanel.setDragonNearby(dragonNearby);
    }

    public void updateTimer(int elapsedTime) {
        statusPanel.setTimer(elapsedTime);
    }

    private void showHighScores() {
        try {
            HighScores highScores = new HighScores(10);
            ArrayList<HighScore> scores = highScores.getHighScores();

            StringBuilder sb = new StringBuilder();
            for (HighScore score : scores) {
                sb.append(score.name()).append(": ").append(score.score()).append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(250, 150));

            JOptionPane.showMessageDialog(this, scrollPane, "Highscores", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving highscores.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restartGame() {
        this.getContentPane().remove(gameBoard);
        gameBoard = new GameBoard();
        LabyrinthManager labyrinthManager = gameBoard.getLabyrinthManager();
        labyrinthManager.setGameWindow(this);

        this.add(gameBoard, BorderLayout.CENTER);
        gameBoard.requestFocusInWindow();
        updateStatus(0, false);

        this.revalidate();
        this.repaint();
    }
}
