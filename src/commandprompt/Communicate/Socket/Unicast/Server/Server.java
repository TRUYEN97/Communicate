/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.Socket.Unicast.Server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class Server implements Runnable {
    private final int port;

    
    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try ( ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = null;
            while ((socket = serverSocket.accept()) != null) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
