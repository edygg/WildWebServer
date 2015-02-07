
package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.PrintWriter;

/**
 *
 * @author Edilson Gonzalez
 */
public class Request extends Thread {
    
    private DataOutputStream out;
    private BufferedReader in;
    private int state;
    public static int READY = 1;
    public static int RUNNING = 2;
    public static int STOPED = 3;

    public Request(DataOutputStream out, BufferedReader in) {
        this.out = out;
        this.in = in;
        this.state = READY; 
    }

    @Override
    public void run() {
        this.state = RUNNING;
    }
    
    public int getRequestState() {
        return state;
    }
    
    public BufferedReader getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    
}
