

import javax.swing.*;
import java.awt.*;
import java.util.Random;

// The main application class — creates the window and controls screen navigation.
// Extends JFrame to be the actual OS window.
// Implements Navigable so modules can request screen changes without knowing app internals.
public class EcoHeroApp extends JFrame implements Navigable {

    // Manages swapping between named panels (screens) without destroying them.
    private CardLayout cardLayout;

    // The root container that holds all panels managed by cardLayout.
    private JPanel mainContainer;

    // The player's name entered on the welcome screen. Defaults to "Eco Learner".
    private String playerName;

    // The four feature modules — created once in the constructor and reused on each visit.
    private LearningModule learningModule;
    private QuizModule quizModule;
    private LeaderboardModule leaderboardModule;
    private RewardSystem rewardSystem;

    // Shared ScoreStorage instance passed to both QuizModule and LeaderboardModule
    // so they read/write the same scores.txt file.
    private ScoreStorage scoreStorage;

    // Reference to the greeting label on the menu so it can be updated after name entry.
    private JLabel menuGreetingLabel;

    // Used to pick a random daily eco tip without repeating the same one twice in a row.
    private final Random tipRandom = new Random();

    // Remembers the last tip index to avoid showing the same tip consecutively.
    private int lastTipIndex = -1;

    // Sets up all modules, configures the window, and shows the welcome screen.
    public EcoHeroApp() {
        playerName = "Eco Learner";
        rewardSystem = new RewardSystem();
        scoreStorage = new ScoreStorage();
        learningModule = new LearningModule();
        quizModule = new QuizModule(rewardSystem, scoreStorage);
        leaderboardModule = new LeaderboardModule(scoreStorage);

        setTitle("EcoHero - Recycling & Waste Management");
        setSize(430, 760);               // Fixed size mimics a phone screen.
        setLocationRelativeTo(null);     // Centre the window on the screen.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);             // Prevents layout from breaking if window is resized.

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Only welcome and menu are pre-built; other screens are built on demand.
        mainContainer.add(createWelcomePanel(), "welcome");
        mainContainer.add(createMenuPanel(), "menu");
        add(mainContainer);
        cardLayout.show(mainContainer, "welcome");
    }

    // Returns the player's name — used by QuizModule when saving the score.
    public String getPlayerName() { return playerName; }

    // Switches the app to the named screen.
    // Module panels are rebuilt on each visit (createPanel is called fresh each time)
    // except for "menu" and "welcome" which are persistent CardLayout panels.
    @Override
    public void navigateTo(String screenName) {
        if (screenName.equals("learning"))     showPanel(learningModule.createPanel(this));
        else if (screenName.equals("quiz"))    showPanel(quizModule.createPanel(this));
        else if (screenName.equals("leaderboard")) showPanel(leaderboardModule.createPanel(this));
        else if (screenName.equals("tips"))    showPanel(createTipsPanel());
        else if (screenName.equals("menu")) {
            updateMenuGreeting(); // Refresh the greeting in case the name was just set.
            cardLayout.show(mainContainer, "menu");
        }
        else cardLayout.show(mainContainer, screenName);
    }

    // Adds a dynamically built panel to the container and shows it immediately.
    // All module panels are shown this way — they replace the "dynamic" slot each time.
    /*
mainContainer
│
├── welcome
├── menu
└── dynamic(for learningModule, quizModule etc.)
    */
    public void showPanel(JPanel panel) {
        mainContainer.add(panel, "dynamic");
        cardLayout.show(mainContainer, "dynamic");
    }

    // Builds the first screen: logo, name entry field, intro text, and a start button.
    // The player must enter a name before proceeding — an empty name shows a warning dialog.
    private JPanel createWelcomePanel() {
        JPanel root = phonePanel();
        root.setLayout(new BorderLayout(12, 12));

        // Large logo with app name and tagline at the top.
        JLabel logo = new JLabel("<html><center><div style='font-size:42px'>♻️</div><h1>EcoHero</h1><p>Recycle smart. Live green.</p></center></html>");
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setForeground(AppColors.GREEN_DARK);
        root.add(logo, BorderLayout.NORTH);

        // Card holding the name input and intro text.
        JPanel card = new JPanel(new GridLayout(0, 1, 8, 8));
        card.setBackground(AppColors.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.GREEN_LIGHT, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel label = new JLabel("Enter your name:");
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 17));

        // Short intro text describing the app's purpose.
        JTextArea intro = textArea(13);
        intro.setText("• Welcome to EcoHero, an application about recycling and waste management." +
            "\n• Learn how to recycle properly, test your knowledge with quizzes, and track your eco points and badges. " +
            "\n• Your journey to becoming an EcoHero starts here!");

        JButton start = primaryButton("Start Eco Journey");
        start.addActionListener(e -> {
            String input = nameField.getText().trim();
            if (input.isEmpty()) {
                // Force the player to enter a name before proceeding.
                JOptionPane.showMessageDialog(root,
                    "Name is required. Please enter your name before starting.",
                    "Name Required",
                    JOptionPane.WARNING_MESSAGE);
                nameField.requestFocus();
                return;
            }
            playerName = input; // Save the entered name for use throughout the session.
            navigateTo("menu");
        });

        card.add(label); card.add(nameField); card.add(intro); card.add(start);
        root.add(card, BorderLayout.CENTER);

        // SDG label at the bottom for academic/submission context.
        root.add(new JLabel("SDG 12 • Responsible Consumption and Production", SwingConstants.CENTER), BorderLayout.SOUTH);
        return root;
    }

    // Builds the main menu screen with buttons for each module.
    // Created once and kept in the CardLayout — not rebuilt on each visit.
    private JPanel createMenuPanel() {
        JPanel root = phonePanel();
        root.setLayout(new BorderLayout(12, 12));

        // Greeting label stored as a field so updateMenuGreeting() can update it later.
        menuGreetingLabel = headerLabel("Hello, " + playerName, "Choose your EcoHero mission");
        root.add(menuGreetingLabel, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(0, 1, 12, 12));
        menu.setBackground(AppColors.CREAM);

        // Each menu button uses a custom style with a title and subtitle line.
        JButton learn       = menuButton("Learning Module",  "10 pages of recycling knowledge");
        JButton quiz        = menuButton("Quiz Challenge",   "20 questions + score + badge");
        JButton leaderboard = menuButton("Leaderboard",      "View ranking, scores and badges");
        JButton tips        = menuButton("Daily Eco Tips",   "Get simple daily green habits");
        JButton exit        = secondaryButton("Exit Application");

        learn.addActionListener(e -> navigateTo("learning"));
        quiz.addActionListener(e -> navigateTo("quiz"));
        leaderboard.addActionListener(e -> navigateTo("leaderboard"));
        tips.addActionListener(e -> navigateTo("tips"));
        exit.addActionListener(e -> System.exit(0)); // Closes the application cleanly.

        menu.add(learn); menu.add(quiz); menu.add(leaderboard); menu.add(tips); menu.add(exit);
        root.add(menu, BorderLayout.CENTER);
        return root;
    }

    // Updates the greeting label text after the player enters their name on the welcome screen.
    // Called by navigateTo("menu") so the name is always current.
    private void updateMenuGreeting() {
        if (menuGreetingLabel != null) {
            menuGreetingLabel.setText("<html><center><h1>Hello, " + playerName + "</h1><p>Start your EcoHero mission!</p></center></html>");
        }
    }

    // Builds the Daily Eco Tips screen with a random tip and a "Show Another" button.
    // Tips are shown in random order but never the same tip twice in a row.
    private JPanel createTipsPanel() {
        JPanel root = phonePanel();
        root.setLayout(new BorderLayout(10, 10));
        root.add(headerLabel("Daily Eco Tips", "Small actions can make a big difference"), BorderLayout.NORTH);

        // Pool of 10 eco tips rotated randomly each time the button is pressed.
        String[] tips = {
            "Bring your own reusable shopping bag to reduce plastic waste.",
            "Use a refillable water bottle instead of buying plastic bottles.",
            "Separate paper, plastic, glass and metal before recycling.",
            "Switch off lights and fans when leaving a room.",
            "Reduce food waste by taking only what you can finish.",
            "Reuse old containers for storage instead of throwing them away.",
            "Donate usable clothes, books or electronics instead of discarding them.",
            "Avoid single-use plastic straws, cups and cutlery.",
            "Recycle e-waste properly because batteries and electronics can harm the environment.",
            "Choose products with less packaging when shopping."
        };

        JTextArea tipArea = textArea(18);
        tipArea.setBackground(AppColors.WHITE);
        tipArea.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        tipArea.setText("Eco Tip:\n\n" + tips[getNextTipIndex(tips.length)]);

        // Each click picks a new random tip that is different from the last one shown.
        JButton nextTip = primaryButton("Show Another Tip");
        nextTip.addActionListener(e -> tipArea.setText("Eco Tip:\n\n" + tips[getNextTipIndex(tips.length)]));

        JButton back = secondaryButton("Back to Menu");
        back.addActionListener(e -> navigateTo("menu"));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        buttonPanel.setBackground(AppColors.CREAM);
        buttonPanel.add(nextTip);
        buttonPanel.add(back);

        root.add(tipArea, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);
        return root;
    }

    // Returns a random tip index that is different from the last one shown.
    // Loops until a new index is found — safe because there are 10 tips.
    private int getNextTipIndex(int tipCount) {
        if (tipCount <= 1) return 0;
        int nextIndex;
        do {
            nextIndex = tipRandom.nextInt(tipCount);
        } while (nextIndex == lastTipIndex);
        lastTipIndex = nextIndex;
        return nextIndex;
    }

    // Creates a base panel with the app's cream background and standard padding.
    // Used by every screen as its root container for visual consistency.
    public JPanel phonePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AppColors.CREAM);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        return panel;
    }

    // Creates a centred green header label using HTML for multi-line formatting.
    // Used at the top of every module screen.
    public JLabel headerLabel(String title, String subtitle) {
        JLabel label = new JLabel(rewardSystem.htmlTitle(title, subtitle));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(AppColors.GREEN_DARK);
        return label;
    }

    // Creates a non-editable, word-wrapping text area with a given font size.
    // Used for body text, quiz questions, and result summaries across all screens.
    public JTextArea textArea(int size) {
        JTextArea area = new JTextArea();
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, size));
        area.setForeground(AppColors.TEXT);
        area.setOpaque(true);
        return area;
    }

    // Creates a solid green button — used for the main action on each screen.
    public JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(AppColors.GREEN);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        return button;
    }

    // Creates a light green button — used for secondary actions like "Back" or "Previous".
    public JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(AppColors.GREEN_LIGHT);
        button.setForeground(AppColors.GREEN_DARK);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    // Creates a large menu button with a bold title and a smaller subtitle underneath.
    // Used only on the main menu screen for the four module buttons.
    private JButton menuButton(String title, String subtitle) {
        JButton button = new JButton("<html><b>" + title + "</b><br><span style='font-size:10px'>" + subtitle + "</span></html>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(AppColors.WHITE);
        button.setForeground(AppColors.GREEN_DARK);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.PLAIN, 15));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.GREEN_LIGHT, 2),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        return button;
    }

    // Entry point — launches the app on the Swing Event Dispatch Thread.
    // invokeLater ensures all UI creation happens on the correct thread, which is required by Swing.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EcoHeroApp().setVisible(true));
    }
}
