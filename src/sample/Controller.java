package sample;


import com.jfoenix.controls.JFXScrollPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the main controller, the first page that loads when the user logs in
 * The sidebar is controlled by this class which represents 5 panels namely Dashboard, Admin, Activity, Profile and settings
 * This class also controls the dashboard Panel which is the default panel and consists of all the projects related to the clients
 * @author Kavishka Timashan
 */
public class Controller{
    @FXML
    Button logout;
    @FXML
    VBox centerPanel, centerPanel0, sideBar;                    //centerPanel vbox is scrollable because it's placed inside a JFX scrollPane
    @FXML
    JFXScrollPane scroll;
    @FXML
    ToggleButton admin, activity, dashboard, profile, settings; //sidebar ToggleButtons
    @FXML
    Label quote;

    /**
     *Loads first
     * sets sidebar for admin/client
     */
    public void initialize() {
        new DashboardItem();
        sideBar.getChildren().removeAll(admin,activity);     //Clear sidebar admin controls
        String role=new Datasource().Details()[0];           //get users role
        String firstName=new Datasource().Details()[1];
        nextQuote();                                         //create a new quote

        if(role.equals("Admin")){                            //Set admin controls if admin
            sideBar.getChildren().add(1,admin);
            sideBar.getChildren().add(2,activity);
        }
        dashboard();                                         //Dashboard is default panel
    }

    /**
     * The 1st Panel
     * Contains all projects relevant to the client(s).
     */
    public void dashboard(){
        dashboard.setSelected(true);                                                                                 //ToggleButton shouldn't be unchecked if clicked again
        empty();                                                                                                     //clear sidebar
        centerPanel0.getChildren().add(scroll);
        Node[] node =new Node[DashboardItem.getDashboardItem().size()];
        for(int i=0; i< node.length; i++){                                                                           //load every project
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/Item.fxml"));
                node[i]=fxmlLoader.load();

                ItemControl itemControl= fxmlLoader.getController();                                                 //The controller of each project is ItemControl
                itemControl.head.setText(DashboardItem.getDashboardItem().get(i).getTitle());                        //Each projects has a head,status,questionnaireID and a projectID and they have to be given inorder to differentiate each project
                itemControl.status.setText(DashboardItem.getDashboardItem().get(i).getStatus());
                itemControl.questionnaireID=DashboardItem.getDashboardItem().get(i).getQuestionnaireID();
                itemControl.projectID=DashboardItem.getDashboardItem().get(i).getProjectID();

                if(new Datasource().Details()[0].equals("Admin")){                                                   //Admin should see each clients username on each project
                    itemControl.username.setText(DashboardItem.getDashboardItem().get(i).getUsername());
                }

                if(!DashboardItem.getDashboardItem().get(i).getStatus().equals(Item.Status.COMPLETED.toString())){   //if project is not in COMPLETED state feedback button should be disabled
                    itemControl.feedback.setDisable(true);
                }

                centerPanel.getChildren().add(node[i]);                                                               //Add project to centerPanel
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Direct user to profile.fxml and profileControl.java
     * The user can modify his/her profile information
     * @throws IOException
     */
    public void profile() throws IOException{
        profile.setSelected(true);
        empty();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/profile.fxml"));
        Node node=fxmlLoader.load();
        centerPanel0.getChildren().add(node);
    }

    /**
     * Direct user to Admin.fxml and Admin.java
     * The user can see the list of users with their status
     * Admin only
     * @throws IOException
     */
    public void admin() throws IOException{
        admin.setSelected(true);
        empty();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/Admin.fxml"));
        Node node=fxmlLoader.load();
        centerPanel0.getChildren().add(node);
    }

    /**
     * Direct user to activity.fxml and ActivityControl.java
     * The user can see illustrated graph data from the user responses
     * Admin only
     * @throws IOException
     */
    public void activity() throws IOException{
        activity.setSelected(true);
        empty();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/activity.fxml"));
        Node node=fxmlLoader.load();
        centerPanel0.getChildren().add(node);
    }

    /**
     * Direct user to settings.fxml and settingsControl.java
     * The user can modify his/her settings
     * @throws IOException
     */
    public void settings() throws IOException{
        settings.setSelected(true);
        empty();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/settings.fxml"));
        Node node=fxmlLoader.load();
        centerPanel0.getChildren().add(node);
    }

    /**
     * Directs user to login.fxml and loginControl.java
     * @throws IOException
     */
    public void logout() throws IOException {
        Client client = new Client(Client.ClientFunction.DISCONNECT);
        client.start();
        Parent root = FXMLLoader.load(getClass().getResource("layout/login.fxml"));

        SettingsControl.readSettings();
        root.getStylesheets().add(getClass().getResource(SettingsControl.themeString).toExternalForm());

        Stage window=(Stage) logout.getScene().getWindow();
        window.setScene(new Scene(root, 1280,720));
    }

    /**
     * Both the VBOXs children should be emptied before adding components
     */
    public void empty(){
        centerPanel0.getChildren().removeAll(centerPanel0.getChildren());
        centerPanel.getChildren().removeAll(centerPanel.getChildren());
    }

    /**
     * Randomly changes color of the topBar of the JFX scrollPane
     */
    public void nextQuote(){
        new Quote();                           //Create new quote
        quote.setText(Quote.quote);            //sets the quote label
        int val=(int) (1+(Math.random()*4));   //Picks random number from 1 to 4
        switch(val){
            case 1: scroll.getMainHeader().setStyle("-fx-background-color: #3949ab");break;
            case 2: scroll.getMainHeader().setStyle("-fx-background-color: #6761ca"); break;
            case 3: scroll.getMainHeader().setStyle("-fx-background-color: #138fc8"); break;
            case 4: scroll.getMainHeader().setStyle("-fx-background-color: #0a223c"); break;
        }
    }

    /**
     * Quits the application
     * Lets the server know that the user went offline since he/she logged in
     */
    public void quit(){
        Client client = new Client(Client.ClientFunction.DISCONNECT);
        client.start();

        Platform.exit();
//        System.exit(0);  //ALTERNATE EXIT
    }
}
