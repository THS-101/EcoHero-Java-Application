

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.File;
import java.net.URL;

// Shows 10 eco-learning pages one at a time using Previous / Next buttons.
// Extends AppModule so it plugs into the app's navigation system.
public class LearningModule extends AppModule {

    // Holds all 10 LearningPage objects created in loadPages().
    private ArrayList<LearningPage> pages;

    // Tracks which page is currently on screen (0-based index).
    private int currentIndex;

    // Builds the module, sets up the pages list, and loads all 10 pages.
    public LearningModule() {
        super("Learning Module", "10 eco-learning screens about recycling and waste management.");
        pages = new ArrayList<LearningPage>();
        currentIndex = 0;
        loadPages();
    }

    // Creates all 10 LearningPage objects with their title, content, eco tip, and image.
    // Each page covers a different recycling/eco topic used in the quiz.
    private void loadPages() {
        pages.add(new LearningPage("What is Recycling?", "Recycling turns used materials into new products. It reduces waste sent to landfills and saves natural resources.", "Rinse bottles and cans before recycling.", loadIcon("recycle.jpeg")));
        pages.add(new LearningPage("The 3R Concept", "Reduce means use less. Reuse means use again. Recycle means process waste into something useful.", "Always reduce first because it prevents waste from being created.", loadIcon("3r.png")));
        pages.add(new LearningPage("Plastic Pollution", "Plastic can take hundreds of years to break down. It harms oceans, animals and human health.", "Bring your own water bottle and shopping bag.", loadIcon("plastic.jpeg")));
        pages.add(new LearningPage("Paper Waste", "Paper comes from trees. Recycling paper saves trees, water and energy.", "Use both sides of paper before recycling it.", loadIcon("paper.jpeg")));
        pages.add(new LearningPage("Food Waste", "Food waste produces methane in landfills. Composting food scraps can turn waste into soil nutrients.", "Plan meals and avoid buying too much food.", loadIcon("food.jpeg")));
        pages.add(new LearningPage("E-Waste", "Old phones, batteries and computers contain valuable metals and harmful chemicals. They need special recycling.", "Never throw batteries into normal rubbish bins.", loadIcon("ewaste.jpeg")));
        pages.add(new LearningPage("Waste Sorting", "Different materials need different bins. Proper sorting makes recycling faster and cleaner.", "Check the recycling symbol and local bin rules.", loadIcon("sorting.jpeg")));
        pages.add(new LearningPage("Clean Community", "A clean community improves health, safety and quality of life. Everyone has a role.", "Join a clean-up activity or pick up litter safely.", loadIcon("community.jpeg")));
        pages.add(new LearningPage("Responsible Consumption", "Responsible consumption means buying only what we need and choosing products with less packaging.", "Choose refill packs and durable items.", loadIcon("responsible.jpeg")));
        pages.add(new LearningPage("Daily Eco Habits", "Small actions such as saving electricity, recycling and reducing plastic can create big impact over time.", "Start with one simple green habit today.", loadIcon("habits.jpeg")));
    }

    // Tries to load an image from the file system first, then falls back to the classpath.
    // Returns null if the image cannot be found by either method —
    // the UI then shows "Image unavailable" text instead of crashing.
    private ImageIcon loadIcon(String fileName) {
        // Try loading directly from the working directory (works when running from project root).
        File file = new File(fileName);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(fileName);
            if (icon.getIconWidth() > 0) return icon;
        }

        // Fall back to the classpath (works inside a JAR or when using an IDE run config).
        URL resource = getClass().getResource(fileName);
        if (resource == null) resource = getClass().getResource("/" + fileName);
        if (resource != null) {
            ImageIcon icon = new ImageIcon(resource);
            if (icon.getIconWidth() > 0) return icon;
        }

        // Image not found anywhere — caller handles null gracefully.
        return null;
    }

    // Builds the full learning screen with a card showing the current page.
    // Resets to page 1 every time the module is opened.
    @Override
    public JPanel createPanel(EcoHeroApp app) {
        currentIndex = 0;
        JPanel root = app.phonePanel();
        root.setLayout(new BorderLayout(10, 10));

        // Header at the top of the screen.
        JLabel header = app.headerLabel("EcoHero Learning", "Swipe-style learning pages");
        root.add(header, BorderLayout.NORTH);

        // White card panel that holds the page content.
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(AppColors.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.GREEN_LIGHT, 2),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        // Shows "Page X / 10" so the player knows their position.
        JLabel progress = new JLabel();
        progress.setHorizontalAlignment(SwingConstants.CENTER);

        // Shows the page's image icon.
        JLabel icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        // Shows the page title in large bold green text.
        JLabel title = new JLabel();
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(AppColors.GREEN_DARK);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Shows the main body text of the page.
        JTextArea content = app.textArea(13);

        // Shows the eco tip in the yellow warning-coloured box.
        JTextArea tip = app.textArea(12);
        tip.setBackground(AppColors.WARNING);
        tip.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Runnable used to refresh all UI components whenever the page changes.
        // Defined as a Runnable so it can be called from both the constructor
        // and the button listeners without duplicating code.
        Runnable refresh = new Runnable() {
            public void run() {
                LearningPage page = pages.get(currentIndex);
                progress.setText("Page " + (currentIndex + 1) + " / " + pages.size());
                if (page.getIcon() != null) {
                    icon.setIcon(page.getIcon());
                    icon.setText("");
                } else {
                    // Graceful fallback when the image file is missing.
                    icon.setIcon(null);
                    icon.setText("Image unavailable");
                }
                title.setText(page.getTitle());
                content.setText(page.getDescription());
                tip.setText("Eco Tip: " + page.getEcoTip());
            }
        };

        // Assembles the centre section: progress + icon on top, text below.
        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBackground(AppColors.WHITE);
        center.add(progress, BorderLayout.NORTH);
        center.add(icon, BorderLayout.CENTER);

        JPanel textBox = new JPanel(new BorderLayout(8, 8));
        textBox.setBackground(AppColors.WHITE);
        textBox.add(title, BorderLayout.NORTH);
        textBox.add(content, BorderLayout.CENTER);
        textBox.add(tip, BorderLayout.SOUTH);

        card.add(center, BorderLayout.NORTH);
        card.add(textBox, BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER);

        // Navigation buttons at the bottom: Previous | Home | Next.
        JButton prev = app.secondaryButton("← Previous");
        JButton next = app.primaryButton("Next →");
        JButton home = app.secondaryButton("Home");

        // Move one page back, but stop at the first page.
        prev.addActionListener(e -> { if (currentIndex > 0) currentIndex--; refresh.run(); });

        // Move one page forward, but stop at the last page.
        next.addActionListener(e -> { if (currentIndex < pages.size() - 1) currentIndex++; refresh.run(); });

        // Returns to the main menu without resetting currentIndex.
        home.addActionListener(e -> app.navigateTo("menu"));

        JPanel buttons = new JPanel(new GridLayout(1, 3, 8, 8));
        buttons.setBackground(AppColors.CREAM);
        buttons.add(prev); buttons.add(home); buttons.add(next);
        root.add(buttons, BorderLayout.SOUTH);

        // Load the first page's content before showing the screen.
        refresh.run();
        return root;
    }
}
