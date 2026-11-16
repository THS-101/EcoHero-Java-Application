


// Contract for anything that can switch between screens.
// Only EcoHeroApp implements this, but passing it as Navigable
// to modules keeps modules loosely coupled — they don't need
// to know about the full EcoHeroApp class.
public interface Navigable {
    // Switches the app to the named screen (e.g. "menu", "quiz", "learning").
    void navigateTo(String screenName);
}
