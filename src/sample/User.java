package sample;

/**
 * This class represents a single user
 * The profile info is built to be compatible for the Admin panel
 * @author Kavishka Timashan
 */
public class User {
    private int userID;
    private String userName;
    private String email;
    private String role;

    private String ipAddress;
    private boolean online=false;

    /**
     * Constructor
     * @param userID
     * @param userName
     * @param email
     * @param role
     * @param ipAddress
     */
    public User(int userID, String userName, String email, String role, String ipAddress) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.ipAddress = ipAddress;
    }

    /**
     * Constructor without IP address
     * @param userID
     * @param userName
     * @param email
     * @param role
     */
    public User(int userID, String userName, String email, String role) {
       this(userID,userName,email,role,"xxx");
    }

    /**
     * Returns user ID (int)
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Returns userName (String)
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns email (String)
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns role (String)
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns IP address (String)
     * IP address is fetched by ipify external api
     * @return ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Returns the users status (online/offline)
     * @return online
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets the IP address String value
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Sets the online String value
     * @param online
     */
    public void setOnline(boolean online) {
        this.online = online;
    }
}
