

import javax.swing.*;
import java.awt.*;

// Displays all saved quiz scores from the text file, sorted from highest to lowest.
// Extends AppModule so it plugs into the app's navigation system.
public class LeaderboardModule extends AppModule {

    // Reference to ScoreStorage so this module can load score data when the panel is built.
    private ScoreStorage storage;

    // Receives ScoreStorage via the constructor (dependency injection).
    // The same storage instance is shared with QuizModule so both use the same file.
    public LeaderboardModule(ScoreStorage storage) {
        super("Leaderboard", "Displays saved quiz scores from a text file.");
        this.storage = storage;
    }

    // Builds the leaderboard screen with a scrollable text area showing all saved scores.
    // Calls storage.loadAll() to read and format the data from scores.txt.
    // Shows the exception message in the text area if the file cannot be read.
    @Override
    public JPanel createPanel(EcoHeroApp app) {
        JPanel root = app.phonePanel();
        root.setLayout(new BorderLayout(10, 10));

        // Header label at the top identifying the screen and data source.
        root.add(app.headerLabel("Leaderboard", "Saved using scores.txt"), BorderLayout.NORTH);

        // Scrollable text area displaying the ranked scores.
        JTextArea area = app.textArea(14);
        area.setBackground(AppColors.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        try { area.setText(storage.loadAll()); }
        catch (ScoreStorageException e) { area.setText(e.getMessage()); }
        root.add(new JScrollPane(area), BorderLayout.CENTER);

        // Button to return to the main menu.
        JButton back = app.primaryButton("Back to Menu");
        back.addActionListener(e -> app.navigateTo("menu"));
        root.add(back, BorderLayout.SOUTH);

        return root;
    }
}
