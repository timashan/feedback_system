package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This is the main class
 * @author Kavishka Timashan
 */
public class Main extends Application {

    /**
     * This is the main method
     * Loads login.fxml and loginControl.java
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layout/login.fxml"));
        SettingsControl.readSettings();                                                                  //Reads setting.txt file to obtain preciously saved settings
        root.getStylesheets().add(getClass().getResource(SettingsControl.themeString).toExternalForm()); //Set to previously saved settings

//        primaryStage.setTitle("FEEDBACK SYSTEM");                                                      //set windows title
        primaryStage.initStyle(StageStyle.UNDECORATED);                                                  //Removes windows title bar
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
