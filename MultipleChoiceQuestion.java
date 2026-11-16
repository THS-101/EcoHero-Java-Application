


// A question with a fixed set of answer options shown as radio buttons.
// Extends Question and adds an options array for the choices.
public class MultipleChoiceQuestion extends Question {

    // The selectable answer choices displayed as radio buttons in the quiz UI.
    private String[] options;

    // Sets up the question text, answer options, and the correct answer.
    // pointValue defaults to 1 (via Question's convenience constructor).
    public MultipleChoiceQuestion(String questionText, String[] options, String correctAnswer) {
        super(questionText, correctAnswer);
        this.options = options;
    }

    // Returns the answer options so QuizModule can render them as radio buttons.
    @Override
    public String[] getOptions() { return options; }

    // Identifies this as a multiple choice question for the quiz header label.
    @Override
    public String getQuestionType() { return "Multiple Choice"; }

    // Extends the base validate() to also check that at least 2 options exist.
    // A multiple choice question with fewer than 2 options makes no sense.
    @Override
    public void validate() throws InvalidAnswerException {
        super.validate(); // Still checks questionText and correctAnswer
        if (options == null || options.length < 2) {
            throw new InvalidAnswerException("Multiple choice question needs at least 2 options.");
        }
    }
}
