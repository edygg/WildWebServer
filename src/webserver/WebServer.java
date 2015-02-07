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
        while (true) {
            try {
                Socket clientRequest = server.accept();
                DataOutputStream out = new DataOutputStream(clientRequest.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientRequest.getInputStream()));
                httpHandler(in, out);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
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
            case 3:
                s += "Content-Type: application/x-zip-compressed\r\n";
            default:
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
            if (!path.endsWith(".html")) {
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
            if (path.endsWith(".zip")) {
                type_is = 3;
            }
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                type_is = 1;
            }
            if (path.endsWith(".gif")) {
                type_is = 2;
            }
            output.writeBytes(constructHTTPHeader(200, 5));

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

}
