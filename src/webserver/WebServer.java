
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
    
    private String construct_http_header(int return_code, int file_type) {
        String s = "HTTP/1.1 ";
        
        switch (return_code) {
          case 200:
            s += "200 OK";
            break;
          case 400:
            s += "400 Bad Request";
            break;
          case 403:
            s += "403 Forbidden";
            break;
          case 404:
            s += "404 Not Found";
            break;
          case 500:
            s += "500 Internal Server Error";
            break;
          case 501:
            s += "501 Not Implemented";
            break;
        }

        s += "\r\n";
        s += "Connection: close\r\n";
        s += "Server: SimpleHTTPtutorial v0\r\n";
        
        switch (file_type) {
          case 0:
            break;
          case 1:
            s += "Content-Type: image/jpeg\r\n";
            break;
          case 2:
            s += "Content-Type: image/gif\r\n";
          case 3:
            s += "Content-Type: application/x-zip-compressed\r\n";
          default:
            s += "Content-Type: text/html\r\n";
            break;
        }

        s += "\r\n";
        return s;
    }
    
    private void http_handler(BufferedReader input, DataOutputStream output) {
    int method = 0; //1-GET 2-POST 0-NOT SUPPORTED
    String http = new String(); 
    String path = new String(); 
    String file = new String(); 
    String user_agent = new String();
    try {
      String tmp = input.readLine(); 
      String tmp2 = new String(tmp);
      tmp.toUpperCase(); 
      
      if (tmp.startsWith("GET")) { 
        method = 1;
      } else if (tmp.startsWith("POST")) { 
        method = 2;
      } else if (method == 0) { 
        try {
          output.writeBytes(construct_http_header(501, 0));
          output.close();
          return;
        } catch (Exception e) {
          e.printStackTrace();
        } 
      }
      
      int start = 0;
      int end = 0;
      for (int a = 0; a < tmp2.length(); a++) {
        if (tmp2.charAt(a) == ' ' && start != 0) {
          end = a;
          break;
        }
        if (tmp2.charAt(a) == ' ' && start == 0) {
          start = a;
        }
      }
      path = tmp2.substring(start + 2, end);
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
    
    FileInputStream requestedfile = null;
    try {
      requestedfile = new FileInputStream(path);
    }catch (Exception e) {
      try {
        output.writeBytes(construct_http_header(404, 0));
        output.close();
      }catch (Exception e2) {
          e2.printStackTrace();
      }
    }

    try {
      int type_is = 0;
      if (path.endsWith(".zip")) {
        type_is = 3;
      }
      if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
        type_is = 1;
      }
      if (path.endsWith(".gif")) {
        type_is = 2;
      }
      output.writeBytes(construct_http_header(200, 5));

      if (method == 1) {
        while (true) {
          int b = requestedfile.read();
          if (b == -1) {
            break;
          }
          output.write(b);
        }
        
      }
      output.close();
      requestedfile.close();
    }

    catch (Exception e) {}

  }
    
}
