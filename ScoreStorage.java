
import java.io.*;

// Handles reading and writing quiz scores to a plain text file (scores.txt).
// Implements Saveable so it can be used anywhere that interface is expected.
public class ScoreStorage implements Saveable {

    // The file where scores are stored. Defaults to "scores.txt" in the working directory.
    private File scoreFile;

    // Default constructor — uses "scores.txt" as the file name.
    public ScoreStorage() {
        this("scores.txt");
    }

    // Overloaded constructor — accepts a custom file name.
    // Useful for testing with a separate file without touching real scores.
    public ScoreStorage(String fileName) {
        this.scoreFile = new File(fileName);
    }

    // Appends one score entry to the file in "name,score,badge" format.
    // Uses append mode (true) so old entries are never overwritten.
    // Throws ScoreStorageException if the file cannot be written.
    @Override
    public void save(String playerName, int score, String badge) throws ScoreStorageException {
        try (FileWriter writer = new FileWriter(scoreFile, true)) {
            writer.write(playerName + "," + score + "," + badge + System.lineSeparator());
        } catch (IOException e) {
            throw new ScoreStorageException("Cannot save score. Please check file permission or folder location.", e);
        }
    }

    // Reads all entries from the file, sorts them by score (highest first),
    // and returns them as a numbered, formatted string for the leaderboard.
    // Returns a friendly message if the file does not exist yet.
    // Throws ScoreStorageException if the file exists but cannot be read.
    @Override
    public String loadAll() throws ScoreStorageException {
        if (!scoreFile.exists()) return "No scores yet. Complete the quiz first!";
        try (BufferedReader reader = new BufferedReader(new FileReader(scoreFile))) {
            String line;
            java.util.List<ScoreEntry> entries = new java.util.ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    try {
                        int score = Integer.parseInt(parts[1].trim());
                        entries.add(new ScoreEntry(parts[0].trim(), score, parts[2].trim()));
                    } catch (NumberFormatException ignored) {
                        // Skip lines that don't have a valid integer score.
                    }
                }
            }
            if (entries.isEmpty()) return "No valid scores found.";

            // Sort descending so the highest scorer appears first.
            entries.sort((a, b) -> Integer.compare(b.score, a.score));

            StringBuilder builder = new StringBuilder();
            int rank = 1;
            for (ScoreEntry entry : entries) {
                builder.append(rank++)
                       .append(". ")
                       .append(entry.playerName)
                       .append(" - ")
                       .append(entry.score)
                       .append("% - ")
                       .append(entry.badge)
                       .append("\n");
            }
            return builder.toString();
        } catch (IOException e) {
            throw new ScoreStorageException("Cannot load scores from text file.", e);
        }
    }

    // Private helper class used only during sorting inside loadAll().
    // Holds one parsed score entry before it is turned into a display string.
    private static class ScoreEntry {
        String playerName;
        int score;
        String badge;

        ScoreEntry(String playerName, int score, String badge) {
            this.playerName = playerName;
            this.score = score;
            this.badge = badge;
        }
    }
}
