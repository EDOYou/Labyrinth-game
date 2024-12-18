package game.database;

import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    public static void main(String[] args) {
        String[] names = new String[] {"Peter", "Adrienne", "Ethan", "Jane", "Paul", "Geoffrey", "Joe", "Laura"};
        try {
            Random random = new Random();
            HighScores highScores = new HighScores(3);
            System.out.println(highScores.getHighScores());
            highScores.putHighScore(names[random.nextInt(names.length)], random.nextInt(100));
            System.out.println(highScores.getHighScores());
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}