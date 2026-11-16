


// Base custom exception for the entire app.
// All app-specific exceptions extend this so callers can
// catch either a specific type or all app errors at once with one catch block.
public class EcoHeroException extends Exception {

    // Creates an exception with just a message.
    public EcoHeroException(String message) { super(message); }

    // Creates an exception with a message AND the original cause.
    // Used when wrapping lower-level exceptions (e.g. IOException from file operations).
    public EcoHeroException(String message, Throwable cause) { super(message, cause); }
}
