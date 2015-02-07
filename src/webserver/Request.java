
package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;

/**
 *
 * @author Edilson Gonzalez
 */
public class Request extends Thread {
    
    private DataOutputStream out;
    private BufferedReader in;
 
    public Request(DataOutputStream out, BufferedReader in) {
        this.out = out;
        this.in = in; 
    }
    
       private String constructHTTPHeader(int returnCode, int fileType) {
        String s = "HTTP/1.1 ";

        switch (returnCode) {
            case 200:
                s += "200 OK";
                break;
            case 400:
                s += "400 Bad Request";
                break;

            case 404:
                s += "404 Not Found";
                break;
            case 405:
                s += "405 Method Not Allowed";
                break;
            case 408:
                s += "408 Request Timeout";
                break;
            case 415:
                s += "415 Unsupported Media Type";
                break;
            case 500:
                s += "500 Internal Server Error";
                break;
            case 501:
                s += "501 Not Implemented";
                break;
            case 505:
                s += "505 HTTP Version Not Supported";
                break;
        }

        s += "\r\n";
        s += "Connection: close\r\n";
        s += "Server: WildWebServer v0\r\n";

        switch (fileType) {
            case 0:
                break;
            case 1:
                s += "Content-Type: image/jpeg\r\n";
                break;
            case 2:
                s += "Content-Type: image/gif\r\n";
                break;
            case 3:
                s += "Content-Type: application/javascript\r\n";
                break;
            case 4:
                s += "Content-Type: text/css\r\n";
                break;
            case 5:
                s += "Content-Type: image/png\r\n";
                break;      
            case 6:
                s += "Content-Type: text/html\r\n";
                break;
        }

        s += "\r\n";
        return s;
    }

       private void httpHandler(BufferedReader input, DataOutputStream output) {
        int method = 0; //1-GET 2-POST 0-NOT SUPPORTED
        String http = new String();
        String path = new String();
        String file = new String();
        String user_agent = new String();
        try {
            String tmp = input.readLine();
            System.out.println("tmp = " + tmp);
            if (tmp.startsWith("GET")) {
                method = 1;
            } else if (tmp.startsWith("POST")) {
                method = 2;
            } else if (method == 0) {
                try {
                    output.writeBytes(constructHTTPHeader(501, 0));
                    input.close();
                    output.close();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            path = tmp.split(" ")[1];
            if (path.equals("/")) {
                path += "index.html";
            }
            if (!path.endsWith(".html") && !path.endsWith(".css") && !path.endsWith(".js") && !path.endsWith(".jpg") && !path.endsWith(".jpeg") && 
                    !path.endsWith(".png") && !path.endsWith(".gif")) {
                path += ".html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileInputStream requestedfile = null;
        try {
            requestedfile = new FileInputStream("./Mi_web/" + path);
        } catch (Exception e) {
            try {
                output.writeBytes(constructHTTPHeader(404, 0));
                requestedfile = new FileInputStream("./Mi_web/404.html");
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
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        try {
            int type_is = 0;
            if (path.endsWith(".js")) {
                type_is = 3;
            }
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                type_is = 1;
            }
            if (path.endsWith(".gif")) {
                type_is = 2;
            }
            if (path.endsWith(".css")) {
                type_is = 4;
            }
            if (path.endsWith(".png")) {
                type_is = 5;
            }
            if (path.endsWith(".html")) {
                type_is = 6;
            }
            output.writeBytes(constructHTTPHeader(200, type_is));

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
        } catch (Exception e) {
        }

    }

    @Override
    public void run() {
        this.httpHandler(in, out);
    }
    
    public BufferedReader getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    
}
