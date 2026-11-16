


// Contract for anything that can present answer choices and check a user's answer.
// Implemented by the abstract Question class (and inherited by all question types).
public interface QuestionHandler {
    // Checks whether the given answer matches the correct answer.
    // Throws InvalidAnswerException if the answer is null or empty.
    boolean checkAnswer(String userAnswer) throws InvalidAnswerException;

    // Returns the list of selectable options for this question.
    // Returns an empty array for fill-in-the-blank questions (no options to show).
    String[] getOptions();
}
