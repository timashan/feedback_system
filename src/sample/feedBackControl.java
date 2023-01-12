package sample;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;

/**
 * This class is the feedBack page which displays all the the questions and answers of the questionnaire
 * The answers adjusts according to the question type
 * Their are 2 types of answers MCQ (Activates the radioButtons)  and rate questions (Activates the JFX slider)
 * MCQ consists of 5 radioButtons because only a maximum of 5 answer slots are available when creating the questions
 * Rate questions have a single JFX slider represented by emojis. Emojis change color when selected as the answer
 * @author Kavishka Timashan
 */
public class feedBackControl{
    @FXML
    GridPane answerPane;
    @FXML
    Button previous, next, back, submit;
    @FXML
    Label question, status;
    @FXML
    RadioButton slot1, slot2, slot3, slot4, slot5;
    @FXML
    ToggleGroup toggleAnswer;
    @FXML
    JFXSlider rate;
    @FXML
    HBox emojiPanel;
    @FXML
    ImageView emoji1, emoji2, emoji3, emoji4, emoji5;

    Boolean isForward=true;

    int projectID= QuestionAnswerInput.getProjectID();
    ListIterator<String> questionIterator= QuestionAnswerInput.getQuestionList().listIterator();
    ListIterator<Answers> answerIterator= QuestionAnswerInput.getAnswerList().listIterator();

    Answers answers;

    /**
     * Loads first
     * Loads the first question by default
     */
    @FXML
    public void initialize(){
        if(questionIterator.hasNext()){
            nextPage();
        }
    }

    /**
     * Fired by the previous button
     * If a previous question exists set the questions and answers to the labels
     */
    @FXML
    public void previousPage(){
        if(isForward){
            if(questionIterator.hasPrevious()){
                questionIterator.previous();
                answerIterator.previous();
            }
            isForward=false;
        }
        if(questionIterator.hasPrevious()){
            question.setText(questionIterator.previous());
            answers=answerIterator.previous();

            reset();
            verify();
            setSlots();
        }
    }

    /**
     * Fired by the next button
     * If a next question exists set the questions and answers to the labels
     */
    @FXML
    public void nextPage(){
        if(!isForward){
            if(questionIterator.hasNext()){
                questionIterator.next();
                answerIterator.next();
            }
            isForward=true;
        }
        if(questionIterator.hasNext()){
            question.setText(questionIterator.next());
            answers=answerIterator.next();

            reset();
            verify();
            setSlots();

        }else {
            String result=QuestionAnswerInput.generateResult();        //If the responder answers all given questions allow to submit
            if(result.contains("0")){
                status.setText("INCOMPLETE QUESTIONNAIRE");
                submit.setVisible(false);
            }else{
                status.setText("");
                submit.setVisible(true);
            }
        }
    }

    /**
     * Triggered by previousPage and NextPage methods
     * Sets the answers appropriately
     * Answer types can be a mcq (list of radioButtons) or a rating (JFX Slider)
     */
    public void setSlots(){
        String answerType = answers.getSlot1().getAnswer().toLowerCase();
        if(answerType.equals("rate")){                                     //If the 1st answer slot itself says rate enable JFX slider for the responder to answer
            answerPane.getChildren().add(0,rate);
            answerPane.getChildren().add(1,emojiPanel);
            rate.setVisible(true);
            return;
        }
        if(answers.getSlot1().getAnswer()!=null){                          //Setting preparing the answer slots for the MCQ (Contains 5 radioButtons/answers)
            answerPane.getChildren().add(0,slot1);
            slot1.setText(answers.getSlot1().getAnswer()); }
        if(answers.getSlot2().getAnswer()!=null){
            answerPane.getChildren().add(1,slot2);
            slot2.setText(answers.getSlot2().getAnswer()); }
        if(answers.getSlot3().getAnswer()!=null){
            answerPane.getChildren().add(2,slot3);
            slot3.setText(answers.getSlot3().getAnswer()); }
        if(answers.getSlot4().getAnswer()!=null){
            answerPane.getChildren().add(3,slot4);
            slot4.setText(answers.getSlot4().getAnswer()); }
        if(answers.getSlot5().getAnswer()!=null){
            answerPane.getChildren().add(4,slot5);
            slot5.setText(answers.getSlot5().getAnswer());
        }
    }

    /**
     * Triggered by previousPage and NextPage methods
     * Resets all the answer slots (both MCQ/Rate)
     */
    public void reset(){
        answerPane.getChildren().removeAll(answerPane.getChildren());
//        slot1.setSelected(true);                                       //SET slot1 as Default
        toggleAnswer.selectToggle(null);
        rate.setValue(0);
        emojiReset();
    }

    /**
     * Fired whenever a radioButton is selected or the JFX slider is set to a value
     * Stores the answer given by the responder for a particular question
     */
    public void select(){
        JFXRadioButton radioButton=(JFXRadioButton) toggleAnswer.getSelectedToggle();
        String value=null;

        String answerType = answers.getSlot1().getAnswer().toLowerCase();
        if(answerType.equals("rate")){                                                 //Obtain answer from slider
            value= String.valueOf(Math.round(rate.getValue()));
        }else {
            value=radioButton.getId();                                                 //Obtain radioButton Id which is the answer
        }

        if(value.equals("0")){                                                         //Value 0 is when unanswered, perform verify method so that the JFX slider will be set to previous answer. This prevents responder from setting the MCQ to unanswered
            verify();
            return;
        }

        answers.getSlot1().setSelect(false);                                            //Resets all slots to false
        answers.getSlot2().setSelect(false);
        answers.getSlot3().setSelect(false);
        answers.getSlot4().setSelect(false);
        answers.getSlot5().setSelect(false);

        switch (value){                                                                 //Store the value in the correct slot
            case "1":answers.getSlot1().setSelect(true);break;
            case "2":answers.getSlot2().setSelect(true);break;
            case "3":answers.getSlot3().setSelect(true);break;
            case "4":answers.getSlot4().setSelect(true);break;
            case "5":answers.getSlot5().setSelect(true);break;
        }
        emoji(value);                                                                   //Change emoji color to visualize as selected
    }

    /**
     * Fired by the back button
     * Directs user to the dashBoard panel
     * @throws IOException
     */
    public void samplePage() throws IOException {
        Stage window=(Stage) submit.getScene().getWindow();
        Scene scene = window.getScene();
        loginControl.controller.dashboard();
    }

    /**
     * Triggered by previousPage, NextPage and select methods
     * Previously answered questions will remain answered
     * The slider uses this method to reset the answer to the previous answer instead of 0 (in the select method)
     */
    public void verify(){
        if(answers.getSlot1().isSelect()){
            slot1.setSelected(true);
            rate.setValue(1);
            emoji("1");
        }else if(answers.getSlot2().isSelect()){
            slot2.setSelected(true);
            rate.setValue(2);
            emoji("2");
        }else if(answers.getSlot3().isSelect()){
            slot3.setSelected(true);
            rate.setValue(3);
            emoji("3");
        }else if(answers.getSlot4().isSelect()){
            slot4.setSelected(true);
            rate.setValue(4);
            emoji("4");
        }else if(answers.getSlot5().isSelect()){
            slot5.setSelected(true);
            rate.setValue(5);
            emoji("5");
        }
    }

    /**
     * Triggered by the reset and emoji methods
     * Resets the emojis back to unselected
     */
    public void emojiReset(){
        emoji1.setImage(new Image("emoji/1.png"));
        emoji2.setImage(new Image("emoji/2.png"));
        emoji3.setImage(new Image("emoji/3.png"));
        emoji4.setImage(new Image("emoji/4.png"));
        emoji5.setImage(new Image("emoji/5.png"));
    }

    /**
     * Triggered by select and verify methods
     * Changes emoji into selected state
     * @param value
     */
    public void emoji(String value){
        emojiReset();
        switch(value){
            case "1" :emoji1.setImage(new Image("emoji/selected/1.png"));break;
            case "2" :emoji2.setImage(new Image("emoji/selected/2.png"));break;
            case "3" :emoji3.setImage(new Image("emoji/selected/3.png"));break;
            case "4" :emoji4.setImage(new Image("emoji/selected/4.png"));break;
            case "5" :emoji5.setImage(new Image("emoji/selected/5.png"));break;
        }
    }

    /**
     * Fired by JFX submit button
     * Save the feedBack answers to te database
     */
    public void submit(){
        submit.setText("SUBMITTED");
        Datasource datasource = new Datasource();
        datasource.saveFeedback(QuestionAnswerInput.generateResult(), projectID);
        datasource.close();
    }
}