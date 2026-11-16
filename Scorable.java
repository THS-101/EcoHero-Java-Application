

// Contract for anything that can compute and explain a score.
// Implemented by RewardSystem.
public interface Scorable {
    // Converts earned points and total points into a percentage (0–100).
    // Uses weighted calculation to account for questions with different point values.
    int calculateScore(double earnedPoints, double totalPoints);

    // Returns a short motivational message based on the percentage score.
    String getMotivationalMessage(int percentage);
}
