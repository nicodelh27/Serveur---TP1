// Nicolas Delhaye - 2B

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServeurMoyenne extends Thread {

    static final int PORT = 5555;
    Socket socket;

    ServeurMoyenne(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try{
            // Initialiser les flux de communication
            System.out.println("Nouveau client connecté.");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Envoyer le message d'accueil au client
            out.println("Bienvenue sur le serveur de calcul de moyenne. Le calcul de la moyenne est en cours par le serveur...");

            // Initialiser les listes de notes et de coefficients
            ArrayList<Double> notes = new ArrayList<Double>();
            ArrayList<Double> coefficients = new ArrayList<Double>();

            // Lire les arguments envoyés par le client
            String ligne;
            while ((ligne = in.readLine()) != null) {
                if (ligne.isEmpty()) {
                    double moyenne = calculerMoyenne(notes, coefficients);
                    System.out.println("Envoi de la moyenne au client...");
                    out.println("La moyenne est : " + moyenne);
                } else {
                    String[] arguments = ligne.split(" ");
                    double note = Double.parseDouble(arguments[0]);
                    double coefficient = Double.parseDouble(arguments[1]);
                    notes.add(note);
                    coefficients.add(coefficient);
                    System.out.println("Argument : " + ligne);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double calculerMoyenne(ArrayList<Double> notes, ArrayList<Double> coefficients) {
        double somme = 0;
        double sommeCoefficients = 0;
        for (int i = 0; i < notes.size(); i++) {
            somme += notes.get(i) * coefficients.get(i);
            sommeCoefficients += coefficients.get(i);
        }
        return somme / sommeCoefficients;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){

            System.out.println("Démarrage du serveur...");
            System.out.println("Serveur en attente de connexion sur le port " + PORT);

            // Accepter les connexions des clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion.");
                ServeurMoyenne serveurMoyenne = new ServeurMoyenne(clientSocket);
                serveurMoyenne.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}