


// Contract for anything that can show a title and description.
// Implemented by AppModule and LearningPage.
public interface Displayable {
    // Returns the display title of this item.
    String getTitle();

    // Returns a short description or body text for this item.
    String getDescription();
}
