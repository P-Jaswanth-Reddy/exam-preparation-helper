import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExamPrepHelperApp 
{
    public static void main(String[] args)
     {
        SwingUtilities.invokeLater(() -> new ExamPrepHelperApp().createAndShowGUI());
    }

    private void createAndShowGUI() 
    {
        JFrame frame = new JFrame("Exam Preparation Helper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();
        TopicPanel topicPanel = new TopicPanel();
        tabbedPane.add("Topics", topicPanel);
        tabbedPane.add("Quiz", new QuizPanel(topicPanel));
        tabbedPane.add("Study Planner", new StudyPlannerPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}


class TopicPanel extends JPanel
 {
    private JTextField topicField;
    private JButton addButton;
    private DefaultListModel<String> topicListModel;
    private JList<String> topicList;
    private ArrayList<Question> questions;

    public TopicPanel()
     {
        setLayout(new BorderLayout());

        topicField = new JTextField(20);
        addButton = new JButton("Add Topic");
        topicListModel = new DefaultListModel<>();
        topicList = new JList<>(topicListModel);
        questions = new ArrayList<>();

        
        addPredefinedTopicsAndQuestions();

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Topic Name:"));
        inputPanel.add(topicField);
        inputPanel.add(addButton);

        addButton.addActionListener(e -> addTopic());

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(topicList), BorderLayout.CENTER);
        add(createAddQuestionPanel(), BorderLayout.SOUTH);
    }

    
    private void addPredefinedTopicsAndQuestions()
     {
        
        String[] predefinedTopics = {"Inheritance", "Packages"};

        
        for (String topic : predefinedTopics) {
            topicListModel.addElement(topic);
        }

        
        questions.add(new Question("Inheritance", "Which keyword is used for inheritance in Java?", "extends"));
        questions.add(new Question("Inheritance", "Can a class inherit constructors from its superclass in Java?", "No"));
        questions.add(new Question("Inheritance", " Can interfaces be used to achieve multiple inheritance in Java?", "Yes"));
        questions.add(new Question("Packages", "Which keyword is used to import a package in Java?", "import"));
        questions.add(new Question("Packages", "Which of the following is a predefined package in Java?", "util"));
        questions.add(new Question("Packages", "Which prefixes are reserved for core Java packages and Java extensions, respectively?", "java"));
    }

    private void addTopic()
     {
        String topicName = topicField.getText().trim();
        if (!topicName.isEmpty()) {
            topicListModel.addElement(topicName);
            topicField.setText("");
        }
    }

    private JPanel createAddQuestionPanel() {
        JPanel questionPanel = new JPanel(new BorderLayout());

        JButton addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(e -> {
            String topic = topicList.getSelectedValue();
            if (topic != null) {
                String questionText = JOptionPane.showInputDialog("Enter the question:");
                String answer = JOptionPane.showInputDialog("Enter the answer:");
                if (questionText != null && answer != null) {
                    questions.add(new Question(topic, questionText, answer));
                }
            } else 
            {
                JOptionPane.showMessageDialog(this, "Please select a topic first.");
            }
        });

        questionPanel.add(addQuestionButton, BorderLayout.CENTER);
        return questionPanel;
    }

    public ArrayList<Question> getQuestions()
     {
        return questions;
    }
}


class QuizPanel extends JPanel 
{
    private TopicPanel topicPanel;
    private JTextArea questionArea;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel resultLabel;
    private Question currentQuestion;
    private int score;

    public QuizPanel(TopicPanel topicPanel) 
    {
        this.topicPanel = topicPanel;

        setLayout(new BorderLayout());

        questionArea = new JTextArea(5, 30);
        questionArea.setEditable(false);
        answerField = new JTextField(20);
        submitButton = new JButton("Submit Answer");
        resultLabel = new JLabel("Score: 0");

        submitButton.addActionListener(e -> checkAnswer());

        JPanel quizPanel = new JPanel();
        quizPanel.add(new JLabel("Answer:"));
        quizPanel.add(answerField);
        quizPanel.add(submitButton);

        add(new JScrollPane(questionArea), BorderLayout.NORTH);
        add(quizPanel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        JButton startQuizButton = new JButton("Start Quiz");
        startQuizButton.addActionListener(e -> startQuiz());
        add(startQuizButton, BorderLayout.EAST);
    }

    private void startQuiz()
     {
        ArrayList<Question> questions = topicPanel.getQuestions();
        if (!questions.isEmpty()) {
            Random rand = new Random();
            currentQuestion = questions.get(rand.nextInt(questions.size()));
            questionArea.setText(currentQuestion.getQuestionText());
        } else {
            JOptionPane.showMessageDialog(this, "No questions available.");
        }
    }

    private void checkAnswer() {
        String answer = answerField.getText().trim();
        if (answer.equalsIgnoreCase(currentQuestion.getAnswer())) {
            score++;
            resultLabel.setText("Score: " + score);
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect! The correct answer was: " + currentQuestion.getAnswer());
        }
        answerField.setText("");
        startQuiz();
    }
}


class StudyPlannerPanel extends JPanel
 {
    private JTable studyTable;
    private DefaultTableModel studyTableModel;

    public StudyPlannerPanel() {
        setLayout(new BorderLayout());

        studyTableModel = new DefaultTableModel(new String[]{"Topic", "Completed"}, 0);
        studyTable = new JTable(studyTableModel);

        add(new JScrollPane(studyTable), BorderLayout.CENTER);

        JButton addStudySessionButton = new JButton("Add Study Session");
        addStudySessionButton.addActionListener(e -> addStudySession());
        JButton markAsCompletedButton = new JButton("Mark as Completed");
        markAsCompletedButton.addActionListener(e -> markAsCompleted());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addStudySessionButton);
        buttonPanel.add(markAsCompletedButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addStudySession()
     {
        String topic = JOptionPane.showInputDialog("Enter the topic to study:");
        if (topic != null && !topic.trim().isEmpty()) {
            studyTableModel.addRow(new Object[]{topic, "No"});
        }
    }

    private void markAsCompleted() 
    {
        int selectedRow = studyTable.getSelectedRow();
        if (selectedRow >= 0) {
            studyTableModel.setValueAt("Yes", selectedRow, 1);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a study session.");
        }
    }
}


class Question
 {
    private String topic;
    private String questionText;
    private String answer;

    public Question(String topic, String questionText, String answer) {
        this.topic = topic;
        this.questionText = questionText;
        this.answer = answer;
    }

    public String getTopic()
     {
        return topic;
    }

    public String getQuestionText()
     {
        return questionText;
    }

    public String getAnswer()
    {
        return answer;
    }
}
