
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
        ThreadPool tp = new ThreadPool(); 
        
       try {
            WebServer server = new WebServer(tp);
            tp.start();
            server.start();
            
            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
