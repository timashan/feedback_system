package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class displays all the users in the database
 * The details about each  user are online status, Ip address, user ID, username, Email and role
 * The reload button helps the user reload the list
 * Only accessible to the admin
 * @author Kavishka Timashan
 */
public class Admin implements Initializable {
    @FXML
    VBox userPanel;                                               //userPanel Vbox is placed inside a scrollPane therefore it's scrollable

    public static ArrayList<User> users;                          //Stores all users. User class contains userID, userName, email and role
    userItemControl userItemControl;
    Node[] node;

    public static ArrayList<User> newLogins = new ArrayList<>();  //Stores the users who recently came online

    /**
     * Loads first
     * Gets all users from the database
     * Triggers both reload and loadUsers methods
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(users==null){
            Datasource datasource=new Datasource();
            users = datasource.getUsers();
            datasource.close();
        }
        reload();
        loadUsers();
    }

    /**
     * Sets the values of all user gui components
     * Loads all userItem.fxml and userItemControl.java
     */
    public void loadUsers(){
        node = new Node[users.size()];
        for(int i=0; i<node.length; i++){

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/userItem.fxml"));
            try {
                node[i]=fxmlLoader.load();
                userItemControl = fxmlLoader.getController();

                if(users.get(i).isOnline()){                                                        //Sets user status to online
                    userItemControl.status.setText("Online");
                    userItemControl.status.setStyle("-fx-text-fill: #20aefa; -fx-font-size: 20;");

                }else{
                    userItemControl.status.setText("Offline");                                       //Sets user status to offline
                }
                                                                                                     //Sets all userItemControl values such as the userID, username, email, and role
                userItemControl.userID.setText(String.valueOf(users.get(i).getUserID()));
                userItemControl.username.setText(users.get(i).getUserName());
                userItemControl.email.setText(users.get(i).getEmail());
                userItemControl.role.setText(users.get(i).getRole());

                userItemControl.ipAddress.setText(users.get(i).getIpAddress());                      //Gets and sets Ip Address of the user (Using ipify external api)

                userPanel.getChildren().add(node[i]);                                                //Adds the prepared node to the userPanel Vbox
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fired by the reload button
     * Reloads the page with new login data (Useful when users information change)
     */
    public void reloadBtn(){
        reload();
        empty();
        loadUsers();
    }

    /**
     * Whenever a user comes online and the page is refreshed, the old data of the user is replaced with the new data (Online status and IP address)
     */
    public void reload(){
        if(newLogins.isEmpty()){                                             //No need to perform if no data has tobe updated
            return;
        }

        for(int i=0;i<newLogins.size();i++) {                                 //Every user is compared with the newly logged in users
            for(int j=0;j<users.size();j++){
                if(users.get(j).getUserID()==newLogins.get(i).getUserID()){   //true if this particular user came online
                    users.remove(j);                                          //old data of user is removed
                    users.add(newLogins.get(i));                              //new data of user is added
                }
            }
            newLogins.remove(i);                                              //User data is updated so list should have less work
        }
    }

    /**
     * Empties the userPanel Vbox
     */
    public void empty(){
        userPanel.getChildren().removeAll(userPanel.getChildren());
    }
}
