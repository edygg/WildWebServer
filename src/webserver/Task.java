
package webserver;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 *
 * @author Edilson Gonzalez
 */
public class Task extends Thread {
    
    private PrintWriter out;
    private BufferedReader in;

    public Task(PrintWriter out, BufferedReader in) {
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
