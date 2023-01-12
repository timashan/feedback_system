package sample;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class allows the application to communicate with it's database
 * All the methods contain SQL queries useful for retrieving and updating the database
 * @author Kavishka Timashan
 */
public class Datasource {
    public static final String DB_NAME = "user.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
    public static final String TABLE_PROFILE = "profile";
    public static final String COLUMN_USERID = "userID";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";

    public static final String TABLE_PROJECT = "projects";
    public static final String COLUMN_PROJECT_ID = "projectID";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_QUESTIONNAIRE_ID = "questionnaireID";
    public static final String COLUMN_FEEDBACK = "feedback";

    public static final String TABLE_QUESTIONNAIRE = "questionnaire";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER1 = "answer1";
    public static final String COLUMN_ANSWER2 = "answer2";
    public static final String COLUMN_ANSWER3 = "answer3";
    public static final String COLUMN_ANSWER4 = "answer4";
    public static final String COLUMN_ANSWER5 = "answer5";

    Connection connection;

    private static String username=null;
    private static String userID=null;

    /**
     * Constructor of the class
     * Creates a connection with the database
     */
    public Datasource() {
        try {
            connection=DriverManager.getConnection(CONNECTION_STRING);
//            System.out.println("CONNECTED TO DATABASE");
        } catch (SQLException e) {
            System.out.println("Unable to connect to database");
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection with the database if previously established
     */
    public void close(){
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifies the user when logging in and returns the users role
     * Used by the samplePage method (in loginControl class)
     * @param username
     * @param pass
     * @return role
     */
    public String login(String username, String pass){
        Datasource.username =username;

        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT "+COLUMN_USERID+","+COLUMN_PASSWORD+","+COLUMN_ROLE+" from "+TABLE_PROFILE+" where "+COLUMN_USERNAME+" = '"+username+"'");) {

            Datasource.userID=result.getString(COLUMN_USERID);

            String role = result.getString(COLUMN_ROLE);
            String password = result.getString(COLUMN_PASSWORD);

            if(pass.equals(password)){
                return role;
            }else{
                return null;
            }

        } catch (SQLException e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * Fetches user details (role, first Name, Last Name, user Name, email, user ID)
     * Used by the initialize & dashboard (in Controller class), initialize (in profileControl class), senConnected (in Client class), static method(in DashboardItem class)
     * @return userDetails (a string array)
     */
    public String[] Details(){
        if(username==null){
            return null;
        }

        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * from "+TABLE_PROFILE+" where "+COLUMN_USERNAME+" = '"+ Datasource.username+"'")){

            String[] userDetails = new String[6];
            userDetails[0]=result.getString(COLUMN_ROLE);
            userDetails[1]=result.getString(COLUMN_FIRSTNAME);
            userDetails[2]=result.getString(COLUMN_LASTNAME);
            userDetails[3]=result.getString(COLUMN_USERNAME);
            userDetails[4]=result.getString(COLUMN_EMAIL);
            userDetails[5]=result.getString(COLUMN_USERID);

            return userDetails;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the users profile info (first Name, last Name, user Name, email)
     * Used by save method(in profileControl class)
     * @param firstname
     * @param lastname
     * @param username
     * @param email
     */
    public void saveDetails(String firstname, String lastname, String username, String email){
        String tempUsername, tempEmail;
        if(!checkExistence(username, COLUMN_USERNAME)){
            tempUsername = username;
        }else{
            tempUsername = Datasource.username;
        }

        if(!checkExistence(email, COLUMN_EMAIL)){
            tempEmail = email;
        }else{
            tempEmail = Details()[4];
        }

        try(Statement statement = connection.createStatement()){
            statement.execute("UPDATE "+TABLE_PROFILE+" SET "+COLUMN_FIRSTNAME+" ='"+firstname+"', "+COLUMN_LASTNAME+" = '"+lastname+"', "+COLUMN_USERNAME+"='"+tempUsername+"', "+COLUMN_EMAIL+"='"+tempEmail+"' where "+COLUMN_USERNAME+" ='"+Datasource.username+"';");
            Datasource.username=tempUsername;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the existence of a record in the profile Table
     * Used by verifyUsername and verifyEmail mehods in profileControl class
     * @param value
     * @param ColumnName
     * @return true if exists and false if not (Boolean)
     */
    public Boolean checkExistence(String value, String ColumnName){
        if(value.isEmpty()){
            return true;
        }

        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT COUNT() from "+TABLE_PROFILE+" where "+ColumnName+"='"+value+"'")){

            if(result.getInt("COUNT()")==1){
                return true;
            }else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies if old password is correct and if so update the password
     * Used by the save method (in the profileControl class)
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Boolean savePassword(String oldPassword, String newPassword){
        if(oldPassword.isEmpty() || newPassword.isEmpty()){
            return false;
        }

        try(Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT "+COLUMN_PASSWORD+" from "+TABLE_PROFILE+" where "+COLUMN_USERNAME+"='"+Datasource.username+"'")){

            String sqlPassword = result.getString(COLUMN_PASSWORD);

            if(oldPassword.equals(sqlPassword)){
                statement.execute("UPDATE "+TABLE_PROFILE+" SET "+COLUMN_PASSWORD+"='"+newPassword+"' where "+COLUMN_USERNAME+"='"+Datasource.username+"'");
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches the data of either all the projects or the projects related to a user (Tile, status, project ID, questionnaire ID and username of the project)
     * Used by the static method of DashboardItem
     * @param all
     * @return dashboardItem (ArrayList)
     */
    public ArrayList<Item> getProjects(boolean all){
        ResultSet result;

        try(Statement statement=connection.createStatement()) {

            if(all){
                result = statement.executeQuery("SELECT * from "+TABLE_PROJECT+" join "+TABLE_PROFILE+" on "+TABLE_PROJECT+"."+COLUMN_USERID+" = "+TABLE_PROFILE+"."+COLUMN_USERID+"");
            }else {
                result = statement.executeQuery("SELECT * from "+TABLE_PROJECT+" join "+TABLE_PROFILE+" on "+TABLE_PROJECT+"."+COLUMN_USERID+" = "+TABLE_PROFILE+"."+COLUMN_USERID+" where "+COLUMN_USERNAME+"='"+Datasource.username+"'");
            }

            ArrayList<Item> dashboardItem = new ArrayList<>();
            while(result.next()){
                dashboardItem.add(new Item(result.getString(COLUMN_TITLE),result.getString(COLUMN_STATUS),result.getInt(COLUMN_PROJECT_ID) ,result.getInt(COLUMN_QUESTIONNAIRE_ID), result.getString(COLUMN_USERNAME)));
            }
            return dashboardItem;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches either the questions or the answers of the questionnaire
     * Used by the start method (in ActivityItemControl class) and feedbackPage method (ItemControl class)
     * @param projectID
     * @param getAnswers
     * @return answerList/questionList (Both arrayLists)
     */
    public ArrayList getQuestionnaire(int projectID, Boolean getAnswers){
        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * from "+TABLE_QUESTIONNAIRE+" where "+COLUMN_QUESTIONNAIRE_ID+"='"+projectID+"'")){

            ArrayList<String> questionList=new ArrayList<>();
            ArrayList<Answers> answerList=new ArrayList<>();

            while (result.next()){
                questionList.add(result.getString(COLUMN_QUESTION));
                answerList.add(new Answers(result.getString(COLUMN_ANSWER1),result.getString(COLUMN_ANSWER2),result.getString(COLUMN_ANSWER3), result.getString(COLUMN_ANSWER4), result.getString(COLUMN_ANSWER5)));
            }

            if(getAnswers){
                return answerList;
            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves the answers given by the user for the questionnaire as feedback
     * Used by the submit method(in feedBack class)
     * @param feedback
     * @param projectID
     */
    public void saveFeedback(String feedback, int projectID){
        try(Statement statement = connection.createStatement()){
            statement.execute("UPDATE projects SET feedback= '"+feedback+"' where "+COLUMN_PROJECT_ID+"='"+projectID+"'");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches profile data of all the user data from the database (User ID, username, email, role)
     * Used by the initialize method (in Admin class)
     * @return Users (arrayList)
     */
    public ArrayList<User> getUsers(){
        try(Statement statement=connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * from profile")) {

            ArrayList<User> Users = new ArrayList();
            while(result.next()){
                Users.add(new User(result.getInt(COLUMN_USERID), result.getString(COLUMN_USERNAME), result.getString(COLUMN_EMAIL), result.getString(COLUMN_ROLE)));
            }
            return Users;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetchers the profile info of a single user from the database (User ID, username, email, role)
     * Used by the run method (in Echoer class)
     * @param userID
     * @return User (Object)
     */
    public User getUser(int userID){
        try(Statement statement=connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * from profile where userID='"+userID+"'")){

            User user = new User(result.getInt(COLUMN_USERID),result.getString(COLUMN_USERNAME), result.getString(COLUMN_EMAIL), result.getString(COLUMN_ROLE));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches all feedBack/questionnaire answers (Feedback) given by the responders from the database along with (Project ID and Questionnaire ID)
     * Used by the start method (in ActivityItemControl class)
     * @param questionnaireID
     * @return feedbackAnswers (ArrayList)
     */
    public ArrayList<Feedback> getFeedbackAnswers(int questionnaireID){

        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * from projects where questionnaireID='"+questionnaireID+"' and feedback not null;")) {

            ArrayList<Feedback> feedbackAnswers = new ArrayList<>();
            while(result.next()){
                feedbackAnswers.add(new Feedback(result.getInt(COLUMN_PROJECT_ID),result.getInt(COLUMN_QUESTIONNAIRE_ID),result.getString(COLUMN_FEEDBACK).split(",")));
            }

            return feedbackAnswers;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the questionnaire IDS of all the questionnaires.
     * Used by the start method (in ActivityControl class)
     * @return questionnaireIDs (Integer ArrayList)
     */
    public ArrayList<Integer> getQuestionnaireIDs(){
        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT distinct questionnaireID from projects where feedback not null;")) {

            ArrayList<Integer> questionnaireIDs = new ArrayList<>();
            while(result.next()){
                questionnaireIDs.add(result.getInt(COLUMN_QUESTIONNAIRE_ID));
            }
            return questionnaireIDs;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}