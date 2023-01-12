package sample;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents the settings of the application
 * Settings can be modified using the JFX comboBox and JFX TextFields
 * Saves and reads from the settings.txt as objects
 * @author Kavishka Timashan
 */
public class SettingsControl implements Initializable {
    @FXML
    JFXComboBox chartType, theme;

    @FXML
    JFXTextField borderColor1, borderColor2, borderColor3, borderColor4, borderColor5;

    @FXML
    JFXTextField backgroundColor1, backgroundColor2, backgroundColor3, backgroundColor4, backgroundColor5;

    @FXML
    ImageView quickChart;

    static File file=new File("settings.txt");        //File path to store the settings in a txt
    static String themeString="styleDark.css";                 //Stores the filepath of the current theme (styleSheet/css)

    /**
     * Loads first
     * Loads all values (JFX TextFields, JFX ComboBox)
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(file.exists()){                                             //If the saved settings.txt exists with default/previous settings load it
            readSettings();
        }
        setChartType(QuickChart.getChartType());
        loadColors();                                                  //Load values into JFX TextFields

        if(themeString.equals("styleDark.css")){
            theme.getSelectionModel().select(0);
        }else {
            theme.getSelectionModel().select(1);
        }
    }

    /**
     * Fired by chartType JFX comboBox
     * To trigger the setChartType method
     */
    public void selectChartType(){
        setChartType(chartType.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * Fired by theme JFX comboBox
     * To trigger the setTheme method
     */
    public void selectTheme(){
      setTheme(theme.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * Triggered by selectTheme method
     * Changes themeString to store the current themes file name
     * @param themeSelect
     */
    public void setTheme(String themeSelect){
        switch(themeSelect){
            case "Dark" :
                themeString="styleDark.css";
                break;
            case "Light" :
                themeString="styleLight.css";
                break;
        }
    }

    /**
     * Triggered by initialize in first load and selectChartType methods
     * Sets the chartType JFX comboBox to current chartType
     * @param selectChart
     */
    public void setChartType(String selectChart){
        switch(selectChart){
            case "doughnut" :
                chartType.getSelectionModel().select(0);
                break;
            case "pie" :
                chartType.getSelectionModel().select(1);
                break;
        }
    }

    /**
     * Triggered bt initialize at first load
     * Sets all the values of JFX TextFields which are the background colors and border colors of the Chart
     */
    public void loadColors(){
        borderColor1.setText(QuickChart.borderColors[0]);
        borderColor2.setText(QuickChart.borderColors[1]);
        borderColor3.setText(QuickChart.borderColors[2]);
        borderColor4.setText(QuickChart.borderColors[3]);
        borderColor5.setText(QuickChart.borderColors[4]);

        backgroundColor1.setText(QuickChart.backgroundColors[0]);
        backgroundColor2.setText(QuickChart.backgroundColors[1]);
        backgroundColor3.setText(QuickChart.backgroundColors[2]);
        backgroundColor4.setText(QuickChart.backgroundColors[3]);
        backgroundColor5.setText(QuickChart.backgroundColors[4]);
        loadChart();                                                       //Reloads the chart
    }

    /**
     * Fired by the save button
     * Modifies the quickChart class and saves it to the settings.txt
     * Sets the theme and chart according to the given input
     */
    public void saveSettings(){
        QuickChart.borderColors[0]=borderColor1.getText().replaceAll(" ","");
        QuickChart.borderColors[1]=borderColor2.getText().replaceAll(" ","");
        QuickChart.borderColors[2]=borderColor3.getText().replaceAll(" ","");
        QuickChart.borderColors[3]=borderColor4.getText().replaceAll(" ","");
        QuickChart.borderColors[4]=borderColor5.getText().replaceAll(" ","");

        QuickChart.backgroundColors[0]=backgroundColor1.getText().replaceAll(" ","");
        QuickChart.backgroundColors[1]=backgroundColor2.getText().replaceAll(" ","");
        QuickChart.backgroundColors[2]=backgroundColor3.getText().replaceAll(" ","");
        QuickChart.backgroundColors[3]=backgroundColor4.getText().replaceAll(" ","");
        QuickChart.backgroundColors[4]=backgroundColor5.getText().replaceAll(" ","");
        QuickChart.chartType=chartType.getSelectionModel().getSelectedItem().toString();

        Stage window=(Stage) theme.getScene().getWindow();
        Scene scene= window.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(themeString).toExternalForm());               //Sets the theme

        loadChart();                                                                                    //Sets the chart
        writeSettings();                                                                                //Saves the settings
    }

    /**
     * Fired by the default button
     * Resets the chart to default background and border colors, saves it and loads the values in the JFX TextFields
     */
    public void defaultColors(){
        QuickChart.defaultColors();     //Executes the defaultColors method in quickChart class
        writeSettings();
        loadColors();
    }

    /**
     * Triggered by loadColors and saveSettings methods
     * loads/reloads the chart
     * The chart is set to constant data values and data labels so that the user can experiment with its colors
     */
    public void loadChart(){
        new QuickChart(new StringBuilder("1,2,3,4,5"),new StringBuilder("1,1,1,1,1"),0);
        quickChart.setImage(new Image(QuickChart.quickChartUrl));
    }

    /**
     * Triggered by the saveSettings and defaultColors methods
     * Saves the settings into a txt file (settings.txt) by writing it as an object
     */
    public void writeSettings(){
        try {
            FileOutputStream fileOutputStream;
            fileOutputStream= new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);

            outputStream.writeObject(themeString);
            outputStream.writeObject(QuickChart.chartType);
            outputStream.writeObject(QuickChart.borderColors);
            outputStream.writeObject(QuickChart.backgroundColors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Triggered bt the initialize during first load, by the start method(in the Main class) and by the login method (in the loginControl class)
     * Reads the previously saved settings
     * This method has tobe run when the application is started inorder to load the user preferred theme
     */
    public static void readSettings(){
        try {
            FileInputStream fileInputStream;
            fileInputStream= new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);

            themeString=(String) inputStream.readObject();

            QuickChart.chartType=(String) inputStream.readObject();
            QuickChart.borderColors=(String[]) inputStream.readObject();
            QuickChart.backgroundColors=(String[]) inputStream.readObject();

//        } catch (FileNotFoundException ignored){
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}


