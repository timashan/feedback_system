package sample;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class acts as a middleman when dealing with questionnaires
 * Used by feedBackControl, ItemControl and activityItemControl classes to retrieve and update data
 * @author Kavishka Timashan
 */
public final class QuestionAnswerInput {

    private static int projectID;
    private static ArrayList<String> questionList=new ArrayList<>();
    private static ArrayList<Answers> answerList=new ArrayList<>();

    /**
     * Stores the projectID while accessing a questionnaire
     * @param projectID
     */
    public static void setProjectID(int projectID) {
        QuestionAnswerInput.projectID = projectID;
    }

    /**
     * Stores the questions of the accessed questionnaire
     * @param questionList
     */
    public static void setQuestionList(ArrayList<String> questionList) {
        QuestionAnswerInput.questionList = questionList;
    }

    /**
     * Stores the answers of the accessed questionnaire
     * @param answerList
     */
    public static void setAnswerList(ArrayList<Answers> answerList) {
        QuestionAnswerInput.answerList = answerList;
    }

    /**
     * Returns the project ID (int) of the project containing the questionnaire
     * @return projectID
     */
    public static int getProjectID() {
        return projectID;
    }

    /**
     * Returns the questions stored in the ArrayList
     * @return questionList (ArrayList)
     */
    public static ArrayList<String> getQuestionList() {
        return questionList;
    }

    /**
     * Returns the answers stored in the ArrayList
     * @return answerList (ArrayList)
     */
    public static ArrayList<Answers> getAnswerList() {
        return answerList;
    }

    /**
     * Converts the set of answers to a single string compatible to the database
     * @return the answers String
     */
    public static String generateResult(){
        int[] result = new int[answerList.size()];

        for (int i=0;i<answerList.size();i++){
            for (int k=0;k<answerList.get(i).getSlotList().size();k++){
                if(answerList.get(i).getSlotList().get(k).isSelect()){
                    result[i]=k+1;
                }
            }
        }
        return Arrays.toString(result).replace("[","").replace("]","").replace(" ","");
    }
}