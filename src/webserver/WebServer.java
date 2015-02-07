package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Edilson Gonzalez
 */

public class WebServer extends Thread {

    private final int PORT = 3000; //Puerto en el que se recibiran requests de clientes
    private ServerSocket server = null;
    private ThreadPool tp; 

    public WebServer(ThreadPool tp) throws Exception {
        server = new ServerSocket(PORT);
        this.tp=tp;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientRequest = server.accept();
                DataOutputStream out = new DataOutputStream(clientRequest.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientRequest.getInputStream()));
                Request r = new Request(out,in);
                tp.execute(r);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
