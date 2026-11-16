

// Contract for anything that can persist and retrieve score data.
// Implemented by ScoreStorage.
public interface Saveable {
    // Writes one quiz result (player name, score, badge) to storage.
    // Throws ScoreStorageException if the file cannot be written.
    void save(String playerName, int score, String badge) throws ScoreStorageException;

    // Reads all saved results and returns them as a formatted string.
    // Throws ScoreStorageException if the file cannot be read.
    String loadAll() throws ScoreStorageException;
}
