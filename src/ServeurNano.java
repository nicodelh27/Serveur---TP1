import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurNano extends Thread {
    static final int PORT = 4444;
    Socket socket;

    ServeurNano(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);){
            System.out.println("Nouveau client connecté.");
            Random random = new Random();

            String line = in.readLine();
            System.out.println(line);
            String[] parts = line.split(" ");
            if (parts.length > 1) {
                String fileName = parts[1];
                fileName = fileName.substring(1);
                File file = new File(fileName);
                if (file.exists() && !fileName.contains("/")) {
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html");
                    out.println("Connection: close");
                    out.println();
                    try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                        String fileLine;
                        while ((fileLine = fileReader.readLine()) != null) {
                            out.println(fileLine);
                        }
                    }
                } else {
                    out.println("HTTP/1.1 404 Not Found");
                    out.println("Connection: close");
                    out.println("Content-Type: text/plain");
                    out.println();
                    out.println("File not found.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Démarrage du serveur...");
            System.out.println("Serveur en attente de connexion sur le port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion.");
                ServeurNano serveurNano = new ServeurNano(clientSocket);
                serveurNano.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
