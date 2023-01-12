package sample;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class displays the users profile info (first Name, last Name, user Name, Email, Password)
 * Allows the user to modify their details if valid
 * validates input before processing and also informs the user about it
 * @author Kavishka Timashan
 */
public class profileControl implements Initializable{
    @FXML
    Label role, updateLabel, usernameStatus, passwordStatus, emailStatus;
    @FXML
    JFXTextField firstname, lastname, username, email;
    @FXML
    Button save;
    @FXML
    JFXPasswordField oldPassword, newPassword, retypePassword;

    String usernameString, firstnameString, lastnameString, emailString;    //Temporarily stored values used inorder to detect change (Compare old value with new value)

    /**
     * Loads first
     * Fills up all the JFX TextFields with user info taken from the database
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Datasource datasource = new Datasource();
        String[] user = datasource.Details();   //Fetch the users profile info

        if(user!=null){
            role.setText("< "+user[0]+" >");
            firstname.setText(user[1]);
            lastname.setText(user[2]);
            username.setText(user[3]);
            email.setText(user[4]);
        }
        datasource.close();
        setStrings();                           //Temporarily stores the user values
    }

    /**
     * Triggered by the save button
     * Modifies database values
     */
    public void save(){
        Datasource datasource = new Datasource();
        Boolean validPassword=datasource.savePassword(oldPassword.getText(), newPassword.getText());                        //Verifies the password
        if(!validPassword){
            passwordStatus.setText("* Invalid Password");
            return;
        }
        datasource.saveDetails(firstname.getText(), lastname.getText(), username.getText().toLowerCase(), email.getText());   //Modifies all user data
        datasource.close();
        usernameString=username.getText();

        updateLabel.setVisible(true);                                                                                         //updateLabel makes the user aware that the database got updated

        new Timer().schedule(new TimerTask() {                                                                                //A timer to fade away the "updated label"
            @Override
            public void run() {
                updateLabel.setVisible(false);
            }
        },2000);

        setStrings();                                                                                                         //Temporarily stores the user values
    }

    /**
     * Triggered by verifyInput
     * Checks the availability of username against the database
     */
    public void verifyUsername(){
        if(usernameString.equals(username.getText())){                                                  //If input for username hasn't changed fade off "valid/invalid" label and no further action needed
            usernameStatus.setVisible(false);
            return;
        }
        Datasource datasource = new Datasource();
        Boolean invalid = datasource.checkExistence(username.getText(), datasource.COLUMN_USERNAME);
        datasource.close();
        usernameStatus.setVisible(true);

        if(invalid){                                                                                   //If the username is already present don't allow the user to save
            usernameStatus.setText("* USERNAME EXISTS");
            usernameStatus.setStyle("-fx-text-fill: #d42020; -fx-font-size: 15");
            save.setDisable(true);

        }else{
            usernameStatus.setText("VALID USERNAME");
            usernameStatus.setStyle("-fx-text-fill: #20aefa; -fx-font-size: 15");                      //If the username doesn't exist allow the user to save
        }
    }

    /**
     * Triggered by verifyInput
     * Checks the availability of email against the database
     */
    public void verifyEmail(){
        if(emailString.equals(email.getText())){                                                      //If input for email hasn't changed fade off "valid/invalid" label and no further action needed
            emailStatus.setVisible(false);
            return;
        }
        Datasource datasource = new Datasource();
        Boolean invalid = datasource.checkExistence(email.getText(), datasource.COLUMN_EMAIL);
        datasource.close();
        emailStatus.setVisible(true);

        if(invalid){                                                                                 //If the email is already present don't allow the user to save
            emailStatus.setText("* EMAIL EXISTS");
            emailStatus.setStyle("-fx-text-fill: #d42020; -fx-font-size: 15");
            save.setDisable(true);
        }else{
            emailStatus.setText("VALID EMAIL");                                                      //If the email doesn't exist allow the user to save
            emailStatus.setStyle("-fx-text-fill: #20aefa; -fx-font-size: 15");
        }
    }

    /**
     * Triggered whenever the user inputs data to the JFX TextFields
     * Checks if the given input has changed and is valid. If only valid allow user to save.
     */
    public void verifyInput(){
        if(!oldPassword.getText().isEmpty()) {    //Checks if password is empty
            save.setDisable(false);
        }else if(usernameString.equals(username.getText()) && firstnameString.equals(firstname.getText()) && lastnameString.equals(lastname.getText()) && emailString.equals(email.getText())) {  //Checks if old data equals new data and if equal don't allow the user to save
            save.setDisable(true);
        }else if(username.getText().isEmpty() || firstname.getText().isEmpty() || lastname.getText().isEmpty() || email.getText().isEmpty()){     //Checks if the JFX TextFields are empty and if so don't allow the user to save
            save.setDisable(true);
        }else {
            save.setDisable(false);
        }
        verifyEmail();                       //Validates availability of email
        verifyUsername();                    //Validates availability of username
        verifyPassword();                    //Validates availability of password
    }

    /**
     * Triggered by the verifyInput
     * Validates password is in correct format
     */
    public void verifyPassword(){
        if(!oldPassword.getText().isEmpty() && !retypePassword.getText().isEmpty()) {    //Password fields are not empty

            if (!newPassword.getText().equals(retypePassword.getText())) {               //new  Password = reType password
                save.setDisable(true);
                passwordStatus.setText("* Password Mismatch");
            } else if(newPassword.getText().equals(oldPassword.getText())){               //new Password = old Password
                save.setDisable(true);
                passwordStatus.setText("* Enter a new password");
            } else {
                passwordStatus.setText("");
            }
        }
    }

    /**
     * Triggered by initialize at first load and by save
     * Stores the values so they can be compared with new values inorder to detect change
     */
    public void setStrings(){
        usernameString=username.getText();
        firstnameString=firstname.getText();
        lastnameString=lastname.getText();
        emailString=email.getText();
        verifyInput();
    }
}
