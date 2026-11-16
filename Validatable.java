

// Contract for anything that can check its own data is valid before being used.
// Implemented by Question — called before the quiz accepts an answer.
public interface Validatable {
    // Checks that required fields (question text, correct answer, options) are properly set.
    // Throws InvalidAnswerException with a descriptive message if something is missing or wrong.
    void validate() throws InvalidAnswerException;
}
