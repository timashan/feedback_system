package sample;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class initializes the server when logged in as Admin
 * @author Kavishka Timashan
 */
public class Server extends Thread{
    /**
     * Run method in a different thread
     */
    @Override
    public void run() {
        receiveConnected();
    }

    /**
     * Initializes the server (serverSocket api)
     * Creates an echoer to handle the client functions
     */
    public void receiveConnected(){
        System.out.println("server initialized");
        try(ServerSocket serverSocket = new ServerSocket(5000)) {
            while(true){
                new Echoer(serverSocket.accept()).start();
            }

        } catch(IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }
}

/*
KILL PROCESS - WIN
netstat -nao | find "5000"
taskkill /PID 14772 /F
 */
