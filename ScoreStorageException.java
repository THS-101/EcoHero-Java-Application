

// Thrown when scores cannot be saved or loaded from the text file.
// Extends EcoHeroException so callers can handle all app errors uniformly.
// Used by ScoreStorage.save() and ScoreStorage.loadAll().
public class ScoreStorageException extends EcoHeroException {

    // Creates the exception with a descriptive error message.
    public ScoreStorageException(String message) {
        super(message);
    }

    // Creates the exception with a message and the original IOException as cause.
    // The cause gives more technical detail when debugging file problems.
    public ScoreStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
