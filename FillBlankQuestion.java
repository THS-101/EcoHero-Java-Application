


// A question where the player types a free-text answer into a text field.
// Extends Question — harder than MCQ/T-F, so it is worth 2 points by default.
public class FillBlankQuestion extends Question {

    // Sets up the question text and the expected answer.
    // Passes pointValue = 2 to the parent constructor because
    // fill-in-the-blank requires recall rather than recognition.
    public FillBlankQuestion(String questionText, String correctAnswer) {
        super(questionText, correctAnswer, 2);
    }

    // Returns an empty array — no radio buttons needed for this question type.
    // QuizModule checks options.length == 0 to decide whether to show a text field instead.
    @Override
    public String[] getOptions() { return new String[0]; }

    // Identifies this question type for the quiz header label.
    @Override
    public String getQuestionType() { return "Fill in the Blank"; }

    // Overrides the base checkAnswer to provide a more specific error message
    // when the player submits a blank text field (different wording from MCQ/T-F).
    @Override
    public boolean checkAnswer(String userAnswer) throws InvalidAnswerException {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            throw new InvalidAnswerException("Please type your answer before going next.");
        }
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }
}
