

// Handles all score calculation, badge assignment, and motivational feedback.
// Extends AppModule to fit the module pattern used across the app.
// Implements Scorable to fulfil the scoring contract.
public class RewardSystem extends AppModule implements Scorable {

    // Sets the module's title and description passed to AppModule.
    public RewardSystem() {
        super("Eco Rewards", "Badges and motivational feedback.");
    }

    // Converts earned points into a 0–100 percentage.
    // Uses weighted division so fill-in-the-blank (worth 2 pts) counts more than MCQ (1 pt).
    // Returns 0 if totalPoints is 0 or less to prevent division by zero.
    @Override
    public int calculateScore(double earnedPoints, double totalPoints) {
        if (totalPoints <= 0) return 0;
        return (int) Math.round((earnedPoints * 100.0) / totalPoints);
    }

    // Returns a motivational message based on how well the player scored.
    // Five tiers: Outstanding (80+), Good (60+), Fair (40+), Low (20+), Needs work (<20).
    @Override
    public String getMotivationalMessage(int percentage) {
        if (percentage >= 80) return "Outstanding! You are a real EcoHero!";
        if (percentage >= 60) return "That's good! Your green habits are growing.";
        if (percentage >= 40) return "Good try! Review the learning pages and try again.";
        if (percentage >= 20) return "You can do better! Every eco step matters.";
        return "Don't give up! Start small and improve daily.";
    }

    // Returns a badge name based on the player's percentage score.
    // Badges are shown on the result screen and saved in the leaderboard.
    // Five tiers from highest to lowest: Planet Protector → Eco Starter.
    public String getBadge(int percentage) {
        if (percentage >= 90) return "Planet Protector";
        if (percentage >= 80) return "Eco Master";
        if (percentage >= 60) return "Green Learner";
        if (percentage >= 40) return "Recycling Rookie";
        return "Eco Starter";
    }

    // Required by AppModule but RewardSystem has no dedicated screen.
    // Returns an empty panel as a placeholder — navigation to "rewards" is not used.
    @Override
    public javax.swing.JPanel createPanel(EcoHeroApp app) {
        return app.phonePanel();
    }
}
