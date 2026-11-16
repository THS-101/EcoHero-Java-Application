

import javax.swing.Icon;

// A data holder for one page of learning content.
// Implements Displayable so it fits the same contract as modules.
// LearningModule creates 10 of these and shows them one at a time.
public class LearningPage implements Displayable {

    // The page heading shown in bold at the top of the card (e.g. "What is Recycling?").
    private String title;

    // The main body text explaining the eco topic.
    private String content;

    // A short practical tip shown in the yellow highlight at the bottom of the card.
    private String ecoTip;

    // The image displayed on the card. Loaded from a local file by LearningModule.
    private Icon icon;

    // Sets up all four parts of a learning page at once.
    public LearningPage(String title, String content, String ecoTip, Icon icon) {
        this.title = title;
        this.content = content;
        this.ecoTip = ecoTip;
        this.icon = icon;
    }

    // Returns the page heading — satisfies Displayable.
    public String getTitle() { return title; }

    // Returns the body text — satisfies Displayable (maps "description" to "content").
    public String getDescription() { return content; }

    // Returns the eco tip shown in the warning-coloured box at the bottom.
    public String getEcoTip() { return ecoTip; }

    // Returns the image icon. Can be null if the image file was not found.
    public Icon getIcon() { return icon; }
}
