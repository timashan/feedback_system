package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This class handles the each client for the server
 * @author Kavishka Timashan
 */
public class Echoer extends Thread{
    private Socket socket;

    /**
     * Constructor
     * @param socket
     */
    public Echoer(Socket socket) {
        this.socket = socket;
    }

    static String ipAddress;
    static int userID;
    String function;

    /**
     * Triggered by the server whenever a client sends a request
     * Executes the method in a different thread
     * Fetches data from the client (online status and IP address)
     */
    @Override
    public void run() {
        System.out.println("Client Connected");
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            function = input.readLine();
            ipAddress = input.readLine();
            userID = Integer.parseInt(input.readLine());

            Datasource datasource = new Datasource();
            User user = datasource.getUser(userID);
            datasource.close();

            if(function.equals("CONNECT")){                  //The client connected

                user.setIpAddress(ipAddress);
                user.setOnline(true);

                Admin.newLogins.add(user);

            }else if(function.equals("DISCONNECT")){         //The client disconnected

                System.out.println("client exited");
                user.setOnline(false);
                Admin.newLogins.add(user);
            }

        }catch (IOException e){
            e.getMessage();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
