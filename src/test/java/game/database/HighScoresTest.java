package game.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class HighScoresTest {

    @Test
    public void testSaveAndRetrieveHighScore() throws Exception {
        HighScores highScores = new HighScores(10);
        highScores.putHighScore("TestPlayer", 5);

        ArrayList<HighScore> scores = highScores.getHighScores();
        assertFalse(scores.isEmpty(), "High scores should not be empty.");
        assertEquals("TestPlayer", scores.get(0).name());
        assertEquals(5, scores.get(0).score());
    }
}
