

// A question where the only valid answers are "True" or "False".
// Extends Question — no extra fields needed since the options are always the same.
public class TrueFalseQuestion extends Question {

    // Sets up the question text and which of "True" or "False" is correct.
    // pointValue defaults to 1 (via Question's convenience constructor).
    public TrueFalseQuestion(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    // Always returns exactly ["True", "False"] so QuizModule renders two radio buttons.
    // No need to store options as a field since they never change.
    @Override
    public String[] getOptions() { return new String[]{"True", "False"}; }

    // Identifies this as a true/false question for the quiz header label.
    @Override
    public String getQuestionType() { return "True / False"; }
}
