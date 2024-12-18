package game.database;

public record HighScore(String name, int score) {

    @Override
    public String toString() {
        return "HighScore{" + "name=" + name + ", score=" + score + '}';
    }
}