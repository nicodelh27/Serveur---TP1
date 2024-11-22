import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientNano {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 4444);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){

            System.out.println("Connexion établie avec le serveur.");

            out.println("GET index.html HTTP/1.1");
            out.println("Connection: close");
            out.println();

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
