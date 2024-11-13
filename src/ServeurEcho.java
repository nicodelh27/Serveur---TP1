import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurEcho extends Thread {

    static final int PORT = 1111;
    Socket s;

    ServeurEcho(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());
            String line;
            while((line=in.readLine())!=null) {
                out.println("echo : "+line);
                out.flush();
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket passiveSocket = new ServerSocket(PORT);
            while (true) {
                Socket activeSocket = passiveSocket.accept();
                ServeurEcho s = new ServeurEcho(activeSocket);
                s.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
