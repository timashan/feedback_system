package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class verifies the user, identifies their role and logs in
 * There are 2 roles as Admin and Client
 * @author Kavishka Timashan
 */
public class loginControl{
    @FXML
    JFXTextField username;
    @FXML
    JFXPasswordField password;
    @FXML
    JFXButton login;
    @FXML
    Label info;

    static Controller controller;  //sample.fxml controller gets saved when logged in

    /**
     * fired by login button
     * validates inputs against database
     */
    public void samplePage(){
        String role = null;

        if(!username.getText().isEmpty() ){                                                //validates input not empty
            Datasource datasource = new Datasource();
            role =datasource.login(username.getText().toLowerCase(),password.getText());  //database login methods returns the role of the user
            datasource.close();
        }

        if(role==null){
            info.setText("Username or Password is incorrect.");                           //if incorrect login input inform user

        } else if(role.equals("Admin")){  //Login as admin
            Server server =new Server();
            server.start();               //Start a new server
            client();
            login();


        }else if(role.equals("Client")){  //Login as a client
            client();
            login();
        }
    }

    /**
     * Logs in as admin/client
     * Directs to sample.fxml and Controller.java
     * Triggered only by samplePage method
     */
    public void login(){
        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("layout/sample.fxml"));              //direct user to sample.fxml
            Scene scene = new Scene(fxmlLoader.load(), 1280,720);
            SettingsControl.readSettings();                                                                    //read previously saved settings
            scene.getStylesheets().add(getClass().getResource(SettingsControl.themeString).toExternalForm());  //set to saved theme

            Stage window=(Stage) login.getScene().getWindow();
            window.setScene(scene);

            controller=fxmlLoader.getController();                                                             //get the sample.fxml controller
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lets server know that this user is online
     */
    public void client(){
        Client client = new Client(Client.ClientFunction.CONNECT);
        client.start();
    }

    /**
     * To login using the keyboard "Enter" key
     * @param keyEvent
     */
    public void loginEnterKey(KeyEvent keyEvent){
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            if(!username.getText().isEmpty()){
                password.requestFocus();                //directs user to password
                if(!password.getText().isEmpty()){
                    samplePage();                      //login
                }
            }
        }
    }

    /**
     * Exits application
     */
    public void quit(){
        Platform.exit();
    }
}
