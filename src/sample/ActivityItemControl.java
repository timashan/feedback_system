package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class is every single questionnaire that shows up on the Activity panel and many of this class are loaded depending on the number of questionnaires
 * This class shows all the charts related to the questionnaire and they can be surfed back and forth using the buttons
 * Also, sums up all the responses for this particular questionnaire and represent it on each chart
 * @author Kavishka Timashan
 */
public class ActivityItemControl{

    @FXML
    ImageView quickChart;                      //Where the chart is displayed

    @FXML
    Label question;                            //Label that shows the current question

    @FXML
    TitledPane titledPane;

    int questionnaireID;
    Boolean isForward=true;                     //If the List iterators was going forward set to true

    ArrayList<Feedback> feedbackAnswers;        //Stores responses for this questionnaire provided by users fetched (for different projects) by the database

    ListIterator<String> questionIterator;      //listIterator for questions
    ListIterator<Answers> answerIterator;       //listIterator for answers
    Answers answers;

    ArrayList<Integer[]> total;                 //total of each answer(sum of responses to each answer) of each question
    ListIterator<Integer[]> totalIterator;      //listIterator for summed responses
    Integer[] chartValues;                      //Temporarily contains the sum of responses for each answer

    /**
     * Triggered when the activityControl loads each questionnaire in its loadItems method
     * Initializes the QuestionAnswerInput class and the ListIterators
     */
    public void start() {
        Datasource datasource = new Datasource();
        feedbackAnswers=datasource.getFeedbackAnswers(questionnaireID);                                     //get all answers provided by the user from the database for this questionnaire
        QuestionAnswerInput.setQuestionList(datasource.getQuestionnaire(questionnaireID,false));  //stores all question from database in QuestionAnswerInput class
        QuestionAnswerInput.setAnswerList(datasource.getQuestionnaire(questionnaireID, true));    //stores all answers from database in QuestionAnswerInput class
        datasource.close();

        questionIterator= QuestionAnswerInput.getQuestionList().listIterator();                             //Initialize listIterator for questions
        answerIterator= QuestionAnswerInput.getAnswerList().listIterator();                                 //Initialize listIterator for answers

        total();                                                                                            //Initializes total
        totalIterator=total.listIterator();                                                                 //Initialize listIterator for summed responses
        next();                                                                                             //starts from first question
    }

    /**
     * Fired by the previous button
     * sets previous set of questions and answers in the questionnaire
     */
    public void previous(){
        if(isForward){                                      //If listIterator was going forward already execute
            if(questionIterator.hasPrevious()){
                questionIterator.previous();
                answerIterator.previous();
                totalIterator.previous();
            }
            isForward=false;
        }
        if(questionIterator.hasPrevious()){

            question.setText(questionIterator.previous());
            answers=answerIterator.previous();
            chartValues=totalIterator.previous();

            setAnswers();
        }
    }

    /**
     * Fired by the next button
     * sets next set of questions and answers in the questionnaire
     */
    public void next(){
        if(!isForward){                                  //If listIterator was going forward already skip
            if(questionIterator.hasNext()){
                questionIterator.next();
                answerIterator.next();
                totalIterator.next();
            }
            isForward=true;
        }
        if(questionIterator.hasNext()){

            question.setText(questionIterator.next());
            answers=answerIterator.next();
            chartValues=totalIterator.next();

            setAnswers();
        }
    }

    /**
     * Triggered by previous or next method
     * Converts data to a more compatible version for quickChart.io external api to understand
     */
    public void setAnswers(){
        StringBuilder dataValues=new StringBuilder();                                     //Creating the summed answers as a compatible string for the quickChart url
        for(Integer integer: chartValues){
            if(integer!=null){
                dataValues.append(integer);
            }
            dataValues.append(",");
        }

        StringBuilder dataLabels=new StringBuilder();                                     //Creating the answer labels as a compatible string for the quickChart url
        for(Answers.Slot slot: answers.getSlotList()){
            if(slot.getAnswer()!=null){
                dataLabels.append("'");
                String slotString = slot.getAnswer().replace(" ", "_");
                dataLabels.append(slotString);
                dataLabels.append("'");
                dataLabels.append(",");
            }
        }

        String answerType = answers.getSlot1().getAnswer().toLowerCase();                //Rating answers should have answer labels from 1-5
        if(answerType.equals("rate")){
            dataLabels=new StringBuilder("1,2,3,4,5");
        }

        new QuickChart(dataLabels,dataValues,feedbackAnswers.size());                    //Creates a mew quickChart with summed answers, answer labels and response count passed as parameters
        quickChart.setImage(new Image(QuickChart.quickChartUrl));                        //Set the quickChart imageView with new chart
    }

    /**
     * Triggered by start method when first loaded
     * Sums all the responses gathered(from different projects) for the same questionnaire
     */
    public void total(){
        total = new ArrayList<>();
        Integer[] slots;                                                              //Temporarily Contains all 5 summed answers

        int a,b,c,d,e;
        for(int i=0;i<feedbackAnswers.get(0).getFeedbackAnswers().length;i++){        //loops through all the questions in the questionnaire

            slots = new Integer[5];
            a=0; b=0; c=0; d=0; e=0;                                                  //Resets when looped to the next question

            for(int j=0;j<feedbackAnswers.size();j++){                                //loops through each response

                switch(feedbackAnswers.get(j).getFeedbackAnswers()[i]){               //sums up each answer
                    case "1":
                        a++;
                        slots[0]=a;break;
                    case "2":
                        b++;
                        slots[1]=b;break;
                    case "3":
                        c++;
                        slots[2]=c;break;
                    case "4":
                        d++;
                        slots[3]=d;break;
                    case "5":
                        e++;
                        slots[4]=e;break;
                }
            }
            total.add(slots);                                                         //Adds the slots Integer to the total arrayList
        }
    }
}
