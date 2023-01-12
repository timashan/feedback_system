package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class shows all the questionnaires in vertical order
 * Also allows the user to temporarily change the chartType
 * Only accessible to the admin
 * @author Kavishka Timashan
 */
public class ActivityControl implements Initializable{

    @FXML
    public ComboBox comboBox;                   //Used to change chartType

    @FXML
    VBox activityPanel;                          //All the questionnaires are displayed here

    Node[] node;                                 //Stores all questionnaires
    ArrayList<Integer> questionnaireIDs;         //Stores all questionnaire IDs (Required for size)
    ActivityItemControl[] activityItemControl;   //Stores all activityItemControl controllers (questionnaire controllers)

    /**
     * Loads first
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        Datasource datasource = new Datasource();
        questionnaireIDs=datasource.getQuestionnaireIDs();                        //add all questionnaireIDs from database
        datasource.close();
        activityItemControl=new ActivityItemControl[questionnaireIDs.size()];

        loadItems();

        setSelect(QuickChart.getChartType());                                     //Displays previously saved chartType when the Activity panel is loaded
    }

    /**
     * Triggered by the initialize method
     * Loads each questionnaire (activityItem.fxml and ActivityItemControl
     */
    public void loadItems(){
        node = new Node[questionnaireIDs.size()];

        for (int i=0 ;i<questionnaireIDs.size(); i++){

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/activityItem.fxml"));      //activityItem.fxml and ActivityItemControl are loaded (questionnaires)
            try {
                node[i] = fxmlLoader.load();
                activityItemControl[i] = fxmlLoader.getController();                                        //Stores the controller of each questionnaire
                activityItemControl[i].questionnaireID=questionnaireIDs.get(i);                             //Sets its relevant questionnaireID
                activityItemControl[i].titledPane.setText("Questionnaire : " + questionnaireIDs.get(i));    //Sets the titlePane name in order
                activityItemControl[i].start();                                                             //The start method should be run only after feeding the above data

                activityPanel.getChildren().add(node[i]);                                                   //The prepared questionnaire is added to the activityPanel

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fired by the combobox JFX-Combobox
     * Changes the chart Type instantly when selected
     */
    public void select(){
        setSelect(comboBox.getSelectionModel().getSelectedItem().toString());

        for(ActivityItemControl activityItemControl: activityItemControl){     //Reloading the charts because the chartTpe is changed
            activityItemControl.setAnswers();
        }
    }

    /**
     * Triggered by initialized at first launch, and also triggered by select method if chartType is changed using comboBox
     * Modifies quickChart.io url(external api) in the quickChart class, by only modifying the chartType
     * @param select
     */
    public void setSelect(String select){
        switch(select){
            case "doughnut" :
                comboBox.getSelectionModel().select(0);   //Displays the current chartType if not
                QuickChart.chartType="doughnut";                //Modifies the quickChart urls chartType
                break;
            case "pie" :
                comboBox.getSelectionModel().select(1);
                QuickChart.chartType="pie";
                break;
        }
    }
}
