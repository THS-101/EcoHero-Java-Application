
import javax.swing.JPanel;

// Base class for every screen/feature module in the app.
// Implements Displayable so each module knows its own title and description.
// All four modules (Learning, Quiz, Leaderboard, RewardSystem) extend this.
public abstract class AppModule implements Displayable {

    // The module's name shown in the UI (e.g. "Quiz Module").
    protected String title;

    // A short description of what the module does.
    protected String description;

    // Sets up the module's basic identity (title and description).
    public AppModule(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Returns the module's title — required by Displayable.
    public String getTitle() { return title; }

    // Returns the module's description — required by Displayable.
    public String getDescription() { return description; }

    // Generates an HTML string used by headerLabel() in EcoHeroApp.
    // Wraps heading and subtitle in centred HTML so JLabel renders it nicely.
    public String htmlTitle(String heading, String subtitle) {
        return "<html><center><h1>" + heading + "</h1><p>" + subtitle + "</p></center></html>";
    }

    // Forces every subclass to build and return its own Swing panel.
    // Called by EcoHeroApp.navigateTo() when the user selects this module.
    // The app reference is passed in so the module can use shared UI helpers
    // and call navigateTo() to move to another screen.
    public abstract JPanel createPanel(EcoHeroApp app);
}
