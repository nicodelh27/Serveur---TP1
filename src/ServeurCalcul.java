import java.io.*;
import java.net.*;
import java.util.Random;

public class ServeurCalcul extends Thread {

    static final int PORT = 2222;
    Socket socket;

    ServeurCalcul(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try{
            System.out.println("Nouveau client connecté.");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Random random = new Random();
            out.println("Donnez le résultat de chaque opération :");

            while (true) {
                // Générer une opération aléatoire
                int num1 = random.nextInt(100);
                int num2 = random.nextInt(100);
                char[] operations = {'+', '-', '*', '/'};
                char operation = operations[random.nextInt(4)];

                int resultatCorrect;
                String question;

                switch (operation) {
                    case '+':
                        resultatCorrect = num1 + num2;
                        question = num1 + " + " + num2 + " = ";
                        break;
                    case '-':
                        resultatCorrect = num1 - num2;
                        question = num1 + " - " + num2 + " = ";
                        break;
                    case '*':
                        resultatCorrect = num1 * num2;
                        question = num1 + " * " + num2 + " = ";
                        break;
                    case '/':
                        resultatCorrect = num2 == 0 ? 0 : num1 / num2;  // éviter la division par zéro
                        question = num1 + " / " + num2 + " = ";
                        break;
                    default:
                        continue;
                }

                // Envoyer la question au client
                out.println(question);

                // Lire la réponse du client
                String reponse = in.readLine();
                try {
                    int reponseUtilisateur = Integer.parseInt(reponse);
                    if (reponseUtilisateur == resultatCorrect) {
                        out.println("Réponse correcte.");
                    } else {
                        out.println("Faux ! La bonne réponse est " + resultatCorrect);
                    }
                } catch (NumberFormatException e) {
                    out.println("Votre nombre n’est pas un entier ! La bonne réponse est " + resultatCorrect);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Démarrage du serveur...");
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur en attente de connexion sur le port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion.");
                ServeurCalcul serveurCalcul = new ServeurCalcul(clientSocket);
                serveurCalcul.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}