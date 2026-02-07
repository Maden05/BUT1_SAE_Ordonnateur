package vue;

import modele.FileNotFoundException;
import modele.Position;
import modele.Temple;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LectureScenario implements ConstantesCanvas{

    public static ArrayList<Temple> lecture(File fichierScenario) {
        ArrayList<Temple> templesDuScenario = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(fichierScenario);
            while (scanner.hasNext()) {
                // Lecture des coordonnées, de la couleur et du cristal pour chaque temple
                int posX = scanner.nextInt() + LARGEUR_CANVAS/(2*CARRE);
                int posY = scanner.nextInt() + HAUTEUR_CANVAS/(2*CARRE);
                int couleur = scanner.nextInt();
                int cristal = scanner.nextInt();

                // Création d'un nouveau temple avec les valeurs lues
                Temple temple = new Temple(new Position(posX, posY), couleur, cristal);
                // Ajout du temple à la liste des temples du scénario
                templesDuScenario.add(temple);

            }
            // Fermeture du scanner pour libérer les ressources
            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            // Gestion de l'exception en cas de fichier non trouvé
            throw new RuntimeException(e);
        }
        // Retourne la liste des temples créée à partir du fichier
        return templesDuScenario;
    }
}
