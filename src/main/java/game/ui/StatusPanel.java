package game.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {
    private final JLabel labyrinthsLabel;
    private final JLabel dragonLabel;
    private final JLabel timerLabel;

    public StatusPanel() {
        labyrinthsLabel = new JLabel("Labyrinths Solved: 0");
        dragonLabel = new JLabel("Dragon Nearby: No");
        timerLabel = new JLabel("Time: 0");

        this.add(labyrinthsLabel);
        this.add(dragonLabel);
        this.add(timerLabel);
    }

    public void setLabyrinthsSolved(int labyrinthsSolved) {
        labyrinthsLabel.setText("Labyrinths Solved: " + labyrinthsSolved);
    }

    public void setDragonNearby(boolean isNearby) {
        dragonLabel.setText("Dragon Nearby: " + (isNearby ? "Yes" : "No"));
    }

    public void setTimer(int timeInSeconds) {
        timerLabel.setText("Time: " + timeInSeconds);
    }
}
