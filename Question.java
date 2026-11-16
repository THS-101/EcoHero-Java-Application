


// Abstract base for all quiz question types (MCQ, True/False, Fill-in-the-Blank).
// Implements QuestionHandler (check answer, get options) and Validatable (self-check).
// Subclasses only need to define getOptions() and getQuestionType().
public abstract class Question implements QuestionHandler, Validatable {

    // The question text displayed to the player.
    protected String questionText;

    // The expected correct answer used for comparison in checkAnswer().
    protected String correctAnswer;

    // Points awarded if this question is answered correctly.
    // Default is 1; fill-in-the-blank uses 2 (set via the overloaded constructor).
    protected int pointValue;

    // Convenience constructor — defaults pointValue to 1.
    public Question(String questionText, String correctAnswer) {
        this(questionText, correctAnswer, 1);
    }

    // Full constructor — used when a question is worth more than 1 point.
    // Constructor overloading allows flexible point assignment without separate setters.
    public Question(String questionText, String correctAnswer, int pointValue) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.pointValue = pointValue;
    }

    // Returns the question text shown on screen.
    public String getQuestionText() { return questionText; }

    // Returns the correct answer — shown in the "Wrong answer" feedback dialog.
    public String getCorrectAnswer() { return correctAnswer; }

    // Returns how many points this question is worth.
    // Used by QuizModule to compute earnedPoints and totalPoints.
    public int getPointValue() { return pointValue; }

    // Checks that questionText and correctAnswer are not blank before the quiz uses them.
    // Called by QuizModule before checking the player's answer.
    // Throws InvalidAnswerException with a clear message if something is missing.
    @Override
    public void validate() throws InvalidAnswerException {
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new InvalidAnswerException("Question text is missing.");
        }
        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            throw new InvalidAnswerException("Correct answer is missing for: " + questionText);
        }
    }

    // Compares the player's answer to the correct answer (case-insensitive, trimmed).
    // Throws InvalidAnswerException if the player submitted nothing.
    // FillBlankQuestion overrides this to add its own empty-check message.
    @Override
    public boolean checkAnswer(String userAnswer) throws InvalidAnswerException {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            throw new InvalidAnswerException("Please choose or type an answer before going next.");
        }
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    // Forces each subclass to identify its own type (e.g. "Multiple Choice").
    // Used in the quiz header so the player knows what kind of question they are on.
    public abstract String getQuestionType();
}
