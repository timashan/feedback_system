package sample;

/**
 * This class represents a single projects data with (title, status, projectID, questionnaireID, userName)
 * @author Kavishka Timashan
 */
public class Item {
    private String title;
    private String status;
    private int projectID;
    private int questionnaireID;
    private String username;

    /**
     * The 2 statues of a project are : COMPLETED & PENDING
     */
    public enum Status{
        COMPLETED, PENDING;
    }

    /**
     * Constructor
     * @param title
     * @param status
     * @param projectID
     * @param questionnaireID
     * @param username
     */
    public Item(String title, String status, int projectID, int questionnaireID, String username) {
        this.title = title;
        this.status = status;
        this.projectID = projectID;
        this.questionnaireID = questionnaireID;
        this.username = username;
    }

    /**
     * Returns the title of the project (String)
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the status of the project (String)
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the projectID of the project ID (int)
     * @return projectID
     */
    public int getProjectID() {
        return projectID;
    }

    /**
     * Returns the projectID of the questionnaire ID (int)
     * @return questionnaireID
     */
    public int getQuestionnaireID() {
        return questionnaireID;
    }

    /**
     * Returns the projectID of the username (String)
     * @return username
     */
    public String getUsername() {
        return username;
    }
}
