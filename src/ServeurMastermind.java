import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class ServeurMastermind extends Thread {
    static final int PORT = 3333;
    int nbChiffres;
    Socket socket;

    ServeurMastermind(Socket socket) {
        this.socket = socket;
        this.nbChiffres = 6;
    }

    public void run() {
        try{
            System.out.println("Nouveau client connecté.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Random random = new Random();

            out.println("Bienvenue sur le jeu du Mastermind ! Avec combien de chiffres voulez-vous jouer ? (1-9)");
            String nbChiffresString = in.readLine();
            try {
                int nbChiffres = Integer.parseInt(nbChiffresString);
                setNbChiffres(nbChiffres);
                out.println("Partie à " + nbChiffres + " chiffres.");
            } catch (Exception e) {
                out.println("Erreur : partie à 6 chiffres par défaut.");
            }


            int[] chiffres = new int[nbChiffres];
            for (int i = 0; i < nbChiffres; i++) {
                chiffres[i] = random.nextInt(10);
            }

            while (true){
                String question = "Votre proposition : ";
                out.println(question);

                String reponse = in.readLine();
                char[] reponseUtilisateur = reponse.toCharArray();
                int[] reponseInt = new int[nbChiffres];

                String[] resultat = new String[nbChiffres];

                try {

                    for (int i = 0; i < nbChiffres; i++) {
                        reponseInt[i] = Character.getNumericValue(reponseUtilisateur[i]);
                    }

                    int score = 0;
                    for(int i = 0; i < nbChiffres; i++) {
                        if (reponseInt[i] == chiffres[i]) {
                            resultat[i] = Integer.toString(chiffres[i]);
                            score++;
                        } else if (rechercheElement(chiffres, reponseInt[i])) {
                            resultat[i] = "X";
                        } else {
                            resultat[i] = "-";
                        }
                    }
                    if (score == nbChiffres) {
                        out.println("Bravo, vous avez trouvé le nombre !");
                        break;
                    } else {
                        out.println("Résultat : " + String.join("", resultat));
                    }
                } catch (Exception e) {
                    out.println("Veuillez entrer un nombre de " + nbChiffres + " chiffres.");
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
                ServeurMastermind serveurMastermind = new ServeurMastermind(clientSocket);
                serveurMastermind.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean rechercheElement(int[] tab, int element) {
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] == element) {
                return true;
            }
        }
        return false;
    }

    public void setNbChiffres(int nbChiffres) {
        this.nbChiffres = nbChiffres;
    }
}
