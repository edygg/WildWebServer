
package webserver;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 *
 * @author Edilson Gonzalez
 */
public class Request extends Thread {
    
    private PrintWriter out;
    private BufferedReader in;

    public Request(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        
    }
    
    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    
}
