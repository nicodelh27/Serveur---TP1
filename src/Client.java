import java.io.*;
import java.net.*;
class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("127.0.0.1",1111);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());

            String ligne;
            out.println("Hello");
            out.flush();
            ligne = in.readLine();
            System.out.println("Le serveur a répondu : "+ligne);
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
