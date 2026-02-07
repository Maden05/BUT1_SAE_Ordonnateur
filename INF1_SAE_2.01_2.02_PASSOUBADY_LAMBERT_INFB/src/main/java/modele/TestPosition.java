/**
 * Classe de test unitaire pour la classe Position.
 * Elle teste les différentes méthodes de la classe Position.
 */
package modele;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Cette class à pour objectif de tester le bon fonctionnement des méthode
 * de la class Position
 * */
public class TestPosition {

    /**
     * Test de la méthode deplacementUneCase().
     * Vérifie si la position se déplace correctement vers une position cible.
     */
    @Test
    public void testDeplacementUneCase() {
        // Création d'une position initiale (0, 0)
        Position positionInitiale = new Position(0, 0);
        // Création d'une position cible (1, 1)
        Position positionCible = new Position(1, 1);
        // Déplacement de la position initiale vers la position cible
        positionInitiale.deplacementUneCase(positionCible);
        // Vérification si la position initiale est maintenant la position cible
        assertTrue(positionInitiale.equals(positionCible));
    }

    /**
     * Test de la méthode equals().
     * Vérifie si deux positions sont considérées égales si elles ont les mêmes coordonnées.
     */
    @Test
    public void testEquals() {
        // Création de deux positions avec les mêmes coordonnées (0, 0)
        Position position1 = new Position(0, 0);
        Position position2 = new Position(0, 0);
        // Vérification si les deux positions sont considérées égales
        assertTrue(position1.equals(position2));
    }

    /**
     * Test de la méthode getTempleSurPosition().
     * Vérifie si la méthode retourne correctement le temple situé à une position donnée.
     */
    @Test
    public void testGetTempleSurPosition() {
        // Création de deux temples avec des positions différentes
        Temple temple1 = new Temple(new Position(0, 0), 1, 2);
        Temple temple2 = new Temple(new Position(1, 1), 2, 3);
        // Création d'une liste de temples
        ArrayList<Temple> listeTemples = new ArrayList<>();
        listeTemples.add(temple1);
        listeTemples.add(temple2);
        // Création d'une position correspondant à la position du temple1
        Position position = new Position(0, 0);
        // Récupération du temple sur cette position
        Temple templeSurPosition = position.getTempleSurPosition(listeTemples);
        // Vérification si le temple récupéré est bien le temple1
        assertEquals(temple1, templeSurPosition);
    }

    /**
     * Test de la méthode resetNombreDePas().
     * Vérifie si le nombre de pas est réinitialisé à zéro.
     */
    @Test
    public void testResetNombreDePas() {
        // Incrémentation du nombre de pas
        Position.resetNombreDePas();
        // Vérification si le nombre de pas est égal à zéro après réinitialisation
        assertEquals(0, Position.getNombreDePas());
    }

    /**
     * Test de la méthode getAbscisse().
     * Vérifie si la coordonnée en abscisse est correctement récupérée.
     */
    @Test
    public void testGetAbscisse() {
        // Création d'une position avec une coordonnée en abscisse spécifique
        Position position = new Position(3, 5);
        // Vérification de la coordonnée en abscisse
        assertEquals(3, position.getAbscisse());
    }

    /**
     * Test de la méthode getOrdonnee().
     * Vérifie si la coordonnée en ordonnée est correctement récupérée.
     */
    @Test
    public void testGetOrdonnee() {
        // Création d'une position avec une coordonnée en ordonnée spécifique
        Position position = new Position(3, 5);
        // Vérification de la coordonnée en ordonnée
        assertEquals(5, position.getOrdonnee());
    }



    /**
     * Méthode principale pour exécuter les tests.
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        // Exécution des tests de la classe TestTemple
        org.junit.runner.JUnitCore.main("modele.TestTemple");
    }


}
