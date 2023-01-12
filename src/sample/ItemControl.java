package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class represents a single project in the dashboard panel
 * @author Kavishka Timashan
 */
public class ItemControl{
    @FXML
    Button feedback;
    @FXML
    Label head, status, username;

    int questionnaireID, projectID;

    /**
     * Fired by feedBack button
     * QuestionAnswerInput class have to prepared before directing user
     * Directs user to the feedBack page
     * @throws IOException
     */
    public void feedbackPage() throws IOException{
        QuestionAnswerInput.setProjectID(projectID);                                                        //setting up questions and answers for QuestionAnswerInput class
        Datasource datasource = new Datasource();
        QuestionAnswerInput.setQuestionList(datasource.getQuestionnaire(questionnaireID,false));
        QuestionAnswerInput.setAnswerList(datasource.getQuestionnaire(questionnaireID, true));
        datasource.close();

        if(!status.getText().equals(Item.Status.COMPLETED.toString())){                                      //incomplete projects shouldn't allow feedBack submissions
            return;
        }

        Stage window=(Stage) feedback.getScene().getWindow();                                                //Sets the node in the Controller class

        Scene scene = window.getScene();
        VBox centerPanel=(VBox) scene.lookup("#centerPanel0");

        centerPanel.getChildren().removeAll(centerPanel.getChildren());
        Node node = FXMLLoader.load(getClass().getResource("layout/feedBack.fxml"));
        centerPanel.getChildren().add(node);
    }
}
