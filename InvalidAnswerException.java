


// Thrown when a quiz answer is missing, empty, or otherwise invalid.
// Extends EcoHeroException so it can be caught at the base level if needed.
// Used by Question.checkAnswer() and Question.validate().
public class InvalidAnswerException extends EcoHeroException {

    // Creates the exception with a user-friendly message shown in the quiz dialog.
    public InvalidAnswerException(String message) { super(message); }
}
