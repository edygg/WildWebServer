
package webserver;

/**
 *
 * @author Edilson Gonzalez
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            WebServer server = new WebServer();
            server.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
