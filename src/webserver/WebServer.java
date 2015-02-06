
package webserver;

import java.io.BufferedReader;
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
   
    public WebServer() throws Exception {
        server = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        while(true){
            try {
                Socket clientRequest = server.accept();
                PrintWriter out = new PrintWriter(clientRequest.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientRequest.getInputStream()));
                String data = in.readLine();
                
                while (data != null) {
                    System.out.println(data);
                    data = in.readLine();
                }
                
                in.close();
                out.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
   
   
}
