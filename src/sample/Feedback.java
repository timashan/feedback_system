package sample;

/**
 * This class is used to stores the responders Answers along with the (project ID, questionnaire ID)
 * Used by the feedbackAnswers method (in the Datasource class)
 * @author Kavishka Timashan
 */
public class Feedback {
    private int projectID;
    private int questionnaireID;
    private String[] feedbackAnswers;

    /**
     * Constructor
     * @param projectID
     * @param questionnaireID
     * @param feedbackAnswers
     */
    public Feedback(int projectID, int questionnaireID, String[] feedbackAnswers) {
        this.projectID = projectID;
        this.questionnaireID = questionnaireID;
        this.feedbackAnswers = feedbackAnswers;
    }

    /**
     * Returns the answers as String values of a particular questionnaire
     * @return feedbackAnswers (String Array)
     */
    public String[] getFeedbackAnswers() {
        return feedbackAnswers;
    }
}
