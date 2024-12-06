// Nicolas Delhaye - 2B

import java.io.*;
import java.net.*;

public class ClientMoyenne {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5555)){

            // Initialiser les flux de communication
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connexion établie avec le serveur.");

            // Afficher le message d'accueil du serveur
            System.out.println(in.readLine());

            // Envoyer les arguments au serveur
            for(int i = 0; i < args.length; i=i+2) {
                String ligne = args[i] + " " + args[i+1];
                out.println(ligne);
            }

            // Envoyer une ligne vide pour signaler la fin des arguments
            out.println("");

            // Recevoir la moyenne du serveur et l'afficher
            String moyenne = in.readLine();
            System.out.println(moyenne);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}