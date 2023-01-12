package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * This class does all the functioning of a client
 * @author Kavishka Timashan
 */
public class Client extends Thread{
    private static String ipAddress;
    private ClientFunction function;

    /**
     * The 2 types of statues : CONNECT and DISCONNECT
     */
    public enum ClientFunction{
        CONNECT, DISCONNECT
    }

    /**
     * Constructor
     * @param clientFunction
     */
    public Client(ClientFunction clientFunction) {
        this.function = clientFunction;
    }

    /**
     * Runs the sendConnected method in a different method
     */
    @Override
    public void run() {
        sendConnected();
    }

    /**
     * Lets the server know if a client is connected/disconnected
     */
    public void sendConnected(){
        Datasource datasource=new Datasource();
        getApi();

        try(Socket socket = new Socket("localhost",5000)){

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            if(function==ClientFunction.CONNECT){                              //A connect request
                printWriter.println(ClientFunction.CONNECT.toString());

            } else if(function==ClientFunction.DISCONNECT){                    //A disconnect request
                printWriter.println(ClientFunction.DISCONNECT.toString());
            }

            printWriter.println(ipAddress);
            printWriter.println(datasource.Details()[5]);

        } catch (ConnectException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the IP address of the user using ipify external api
     */
    public static void getApi(){
        try (Scanner s = new Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            ipAddress=s.next();
            System.out.println("My current IP address is " + ipAddress);
        } catch (UnknownHostException e){
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
