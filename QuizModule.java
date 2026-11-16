

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Runs the 20-question eco quiz and shows a result screen at the end.
// Extends AppModule so it plugs into the app's navigation system.
public class QuizModule extends AppModule {

    // Holds all 20 questions (mix of MCQ, True/False, Fill-in-the-Blank).
    private ArrayList<Question> questions;

    // Index of the question currently being shown (0-based).
    private int index;

    // Count of correctly answered questions (shown on the result screen).
    private int correct;

    // Total points the player actually earned (used for weighted score calculation).
    private double earnedPoints;

    // Maximum possible points for all questions answered so far (the denominator for score%).
    private double totalPoints;

    // Used to compute the final percentage score and select a badge.
    private RewardSystem rewardSystem;

    // Used to save the result after the quiz ends.
    private ScoreStorage storage;

    // Receives RewardSystem and ScoreStorage via constructor (dependency injection).
    // Sharing these instances with EcoHeroApp ensures scores are saved to the same file.
    public QuizModule(RewardSystem rewardSystem, ScoreStorage storage) {
        super("Quiz Module", "20-question eco quiz with MCQ, True/False and Fill-in-the-Blank.");
        this.rewardSystem = rewardSystem;
        this.storage = storage;
        questions = new ArrayList<Question>();
        loadQuestions();
    }

    // Creates all 20 quiz questions covering the same topics as the learning pages.
    // Mix of question types tests different levels of knowledge recall.
    private void loadQuestions() {
        questions.add(new MultipleChoiceQuestion("Which SDG is most related to responsible consumption and recycling?", new String[]{"SDG 3", "SDG 4", "SDG 12", "SDG 16"}, "SDG 12"));
        questions.add(new TrueFalseQuestion("Recycling can reduce waste sent to landfills.", "True"));
        questions.add(new FillBlankQuestion("The 3R concept means Reduce, Reuse and ____.", "Recycle"));
        questions.add(new MultipleChoiceQuestion("Which item should NOT be placed in a normal paper recycling bin?", new String[]{"Clean newspaper", "Cardboard", "Food-stained paper", "Office paper"}, "Food-stained paper"));
        questions.add(new TrueFalseQuestion("Batteries should be thrown into normal rubbish bins.", "False"));
        questions.add(new MultipleChoiceQuestion("What is composting mainly used for?", new String[]{"Turning food scraps into soil nutrients", "Making plastic bags", "Burning metal", "Deleting files"}, "Turning food scraps into soil nutrients"));
        questions.add(new FillBlankQuestion("Old phones and computers are examples of ____ waste.", "E"));
        questions.add(new MultipleChoiceQuestion("Which action is the best example of Reduce?", new String[]{"Buying only what you need", "Throwing everything away", "Using more plastic", "Ignoring labels"}, "Buying only what you need"));
        questions.add(new TrueFalseQuestion("Using a reusable water bottle helps reduce plastic waste.", "True"));
        questions.add(new MultipleChoiceQuestion("Why should recyclables be rinsed?", new String[]{"To avoid contamination", "To make them heavier", "To change their color", "To waste water"}, "To avoid contamination"));
        questions.add(new FillBlankQuestion("Waste that can harm oceans and animals is often ____ pollution.", "Plastic"));
        questions.add(new MultipleChoiceQuestion("Which colour of recycle bin is for paper?", new String[]{"Blue", "Yellow", "Brown", "Orange"}, "Blue"));
        questions.add(new TrueFalseQuestion("Responsible consumption means buying more things than needed.", "False"));
        questions.add(new MultipleChoiceQuestion("Which material is commonly recyclable?", new String[]{"Clean aluminium can", "Used tissue", "Dirty food wrapper", "Broken ceramic"}, "Clean aluminium can"));
        questions.add(new FillBlankQuestion("A clean community can improve health, safety and quality of ____.", "life"));
        questions.add(new MultipleChoiceQuestion("What should you do with e-waste?", new String[]{"Send it to proper collection points", "Throw it into rivers", "Burn it", "Hide it in soil"}, "Send it to proper collection points"));
        questions.add(new TrueFalseQuestion("Small daily eco habits can create long-term impact.", "True"));
        questions.add(new MultipleChoiceQuestion("Which is the most eco-friendly first step?", new String[]{"Reduce", "Pollute", "Waste", "Overbuy"}, "Reduce"));
        questions.add(new FillBlankQuestion("Reusing means using an item ____.", "again"));
        questions.add(new TrueFalseQuestion("Sorting waste correctly makes recycling easier.", "True"));
    }

    // Resets all counters and starts the quiz from question 1.
    // Called every time the player navigates to the quiz, including on retry.
    @Override
    public JPanel createPanel(EcoHeroApp app) {
        index = 0; correct = 0; earnedPoints = 0; totalPoints = 0;
        return createQuestionPanel(app);
    }

    // Builds the UI for the current question (index).
    // Called again for each question as the player progresses.
    // Creates radio buttons for MCQ/T-F, or a text field for fill-in-the-blank.
    private JPanel createQuestionPanel(EcoHeroApp app) {
        JPanel root = app.phonePanel();
        root.setLayout(new BorderLayout(10, 10));
        Question q = questions.get(index);

        // Accumulate the total possible points as each question is shown.
        totalPoints += q.getPointValue();

        // Header shows current question number and type (e.g. "Question 3 of 20 • True / False").
        JLabel header = app.headerLabel("Eco Quiz", "Question " + (index + 1) + " of " + questions.size() + " • " + q.getQuestionType());

        // Progress bar gives a visual sense of how far through the quiz the player is.
        JProgressBar progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(index);
        progressBar.setStringPainted(true);
        progressBar.setString("Progress: " + index + " / " + questions.size());

        JPanel top = new JPanel(new BorderLayout(6, 6));
        top.setBackground(AppColors.CREAM);
        top.add(header, BorderLayout.NORTH);
        top.add(progressBar, BorderLayout.SOUTH);
        root.add(top, BorderLayout.NORTH);

        // Centre card showing the question text and answer input area.
        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBackground(AppColors.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Non-editable text area for the question text (wraps long questions cleanly).
        JTextArea question = new JTextArea(q.getQuestionText());
        question.setFont(new Font("Arial", Font.BOLD, 17));
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setEditable(false);
        question.setOpaque(false);
        question.setForeground(AppColors.TEXT);
        question.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        center.add(question, BorderLayout.NORTH);

        // ButtonGroup ensures only one radio button can be selected at a time.
        ButtonGroup group = new ButtonGroup();
        JPanel answerPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        answerPanel.setBackground(AppColors.WHITE);

        // Text field used only for fill-in-the-blank questions.
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));

        // If the question has options, render radio buttons; otherwise show a text field.
        if (q.getOptions().length > 0) {
            for (String option : q.getOptions()) {
                JRadioButton rb = new JRadioButton(option);
                rb.setActionCommand(option);
                rb.setBackground(AppColors.WHITE);
                rb.setFont(new Font("Arial", Font.PLAIN, 15));
                group.add(rb);
                answerPanel.add(rb);
            }
        } else {
            answerPanel.add(new JLabel("Type your answer:"));
            answerPanel.add(textField);
        }
        center.add(answerPanel, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        // Button label changes to "Submit Quiz" on the last question.
        JButton next = app.primaryButton(index == questions.size() - 1 ? "Submit Quiz" : "Next Question");
        JButton home = app.secondaryButton("Exit Quiz");

        // Exit button abandons the quiz and returns to the menu without saving.
        home.addActionListener(e -> app.navigateTo("menu"));

        next.addActionListener(e -> {
            try {
                // Validate the question itself before checking the player's answer.
                q.validate();

                // Read the selected radio button or text field depending on question type.
                String answer = q.getOptions().length > 0
                    ? (group.getSelection() == null ? "" : group.getSelection().getActionCommand())
                    : textField.getText();

                boolean isCorrect = q.checkAnswer(answer);
                if (isCorrect) {
                    correct++;
                    earnedPoints += q.getPointValue(); // Only adds points for correct answers.
                    JOptionPane.showMessageDialog(root,
                        "Correct! Great job, EcoHero!",
                        "Answer Feedback",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Show the correct answer so the player can learn from mistakes.
                    JOptionPane.showMessageDialog(root,
                        "Wrong answer.\nCorrect answer: " + q.getCorrectAnswer(),
                        "Answer Feedback",
                        JOptionPane.INFORMATION_MESSAGE);
                }

                if (index < questions.size() - 1) {
                    // Move to the next question by rebuilding the panel for the next index.
                    index++;
                    progressBar.setValue(index);
                    progressBar.setString("Progress: " + index + " / " + questions.size());
                    app.showPanel(createQuestionPanel(app));
                } else {
                    // All questions answered — show the result screen.
                    progressBar.setValue(questions.size());
                    progressBar.setString("Progress: " + questions.size() + " / " + questions.size());
                    app.showPanel(createResultPanel(app));
                }
            } catch (InvalidAnswerException ex) {
                // Player tried to proceed without selecting/typing an answer.
                JOptionPane.showMessageDialog(root, ex.getMessage(), "Answer Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new GridLayout(1, 2, 8, 8));
        bottom.setBackground(AppColors.CREAM);
        bottom.add(home); bottom.add(next);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    // Builds the result screen shown after the last question is submitted.
    // Calculates the score, assigns a badge, saves the result, and shows a summary.
    private JPanel createResultPanel(EcoHeroApp app) {
        JPanel root = app.phonePanel();
        root.setLayout(new BorderLayout(10, 10));

        // Calculate final percentage and badge using RewardSystem.
        int percentage = rewardSystem.calculateScore(earnedPoints, totalPoints);
        String badge = rewardSystem.getBadge(percentage);

        // Save the result immediately — shows an error dialog if the file write fails.
        try { storage.save(app.getPlayerName(), percentage, badge); }
        catch (ScoreStorageException ex) {
            JOptionPane.showMessageDialog(root, ex.getMessage(), "Storage Error", JOptionPane.ERROR_MESSAGE);
        }

        root.add(app.headerLabel("Quiz Result", "Your EcoHero performance"), BorderLayout.NORTH);

        // Text area summarising the player's performance.
        JTextArea result = app.textArea(16);
        result.setText("Player: " + app.getPlayerName() + "\n\n" +
            "Correct Answers: " + correct + " / " + questions.size() + "\n" +
            "Eco Points: " + (int) earnedPoints + " / " + (int) totalPoints + "\n" +
            "Weighted Score: " + percentage + "%\n" +
            "Badge: " + badge + "\n\n" +
            rewardSystem.getMotivationalMessage(percentage));
        result.setBackground(AppColors.WHITE);
        result.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.add(result, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 2, 8, 8));
        bottom.setBackground(AppColors.CREAM);

        // Try Again resets all counters and starts the quiz from question 1.
        JButton retry = app.secondaryButton("Try Again");

        // Main Menu returns to the menu without clearing the saved score.
        JButton menu = app.primaryButton("Main Menu");
        retry.addActionListener(e -> app.showPanel(createPanel(app)));
        menu.addActionListener(e -> app.navigateTo("menu"));
        bottom.add(retry); bottom.add(menu);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }
}
