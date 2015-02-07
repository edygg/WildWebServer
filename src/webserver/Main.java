
package webserver;

import java.io.File;

/**
 *
 * @author Edilson Gonzalez
 */
public class Main {
    
    public static void main(String[] args) {
        File file = new File("./Mi_web/");
        file.mkdir();
        
        
       try {
            WebServer server = new WebServer();
            server.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
