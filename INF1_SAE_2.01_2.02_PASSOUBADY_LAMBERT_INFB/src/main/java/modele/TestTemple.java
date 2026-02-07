/**
 * Classe de test unitaire pour la classe Temple.
 * Elle teste les différentes méthodes de la classe Temple.
 */
package modele;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Cette class a pour objectif de tester le bon fonctionnement des méthode de la class Temple
 */
public class TestTemple {

    /**
     * Test de la méthode getPositionTemple().
     * Vérifie si la position du temple est correctement récupérée.
     */
    @Test
    public void testGetPositionTemple() {
        // Création d'un temple avec une position spécifique
        Temple temple = new Temple(new Position(0, 0), 1, 2);
        // Récupération de la position du temple
        Position position = temple.getPositionTemple();
        // Vérification des coordonnées de la position
        assertEquals(0, position.getAbscisse());
        assertEquals(0, position.getOrdonnee());
    }

    /**
     * Test de la méthode getCouleurTemple().
     * Vérifie si la couleur du temple est correctement récupérée.
     */
    @Test
    public void testGetCouleurTemple() {
        // Création d'un temple avec une couleur spécifique
        Temple temple = new Temple(new Position(0, 0), 1, 2);
        // Vérification de la couleur du temple
        assertEquals(1, temple.getCouleurTemple());
    }

    /**
     * Test de la méthode getCouleurCristal().
     * Vérifie si la couleur du cristal du temple est correctement récupérée.
     */
    @Test
    public void testGetCouleurCristal() {
        // Création d'un temple avec une couleur de cristal spécifique
        Temple temple = new Temple(new Position(0, 0), 1, 2);
        // Vérification de la couleur du cristal du temple
        assertEquals(2, temple.getCouleurCristal());
    }

    /**
     * Test de la méthode isPossedeCristal().
     * Vérifie si le temple possède un cristal.
     */
    @Test
    public void testIsPossedeCristal() {
        // Création d'un temple possédant un cristal
        Temple temple1 = new Temple(new Position(0, 0), 1, 2);
        // Vérification s'il possède un cristal
        assertEquals(true, temple1.isPossedeCristal());

        // Création d'un temple ne possédant pas de cristal
        Temple temple2 = new Temple(new Position(0, 0), 1, -1);
        // Vérification s'il ne possède pas de cristal
        assertEquals(false, temple2.isPossedeCristal());
    }

    /**
     * Test de la méthode setPossedeCristal(boolean).
     * Vérifie si la présence d'un cristal peut être modifiée.
     */
    @Test
    public void testSetPossedeCristal() {
        // Création d'un temple avec la possibilité de modifier la présence du cristal
        Temple temple = new Temple(new Position(0, 0), 1, 2);
        // Modification de la présence du cristal
        temple.setPossedeCristal(false);
        // Vérification si le temple ne possède plus de cristal
        assertEquals(false, temple.isPossedeCristal());
    }

    /**
     * Test de la méthode setCouleurCristal(int).
     * Vérifie si la couleur du cristal du temple peut être modifiée.
     */
    @Test
    public void testSetCouleurCristal() {
        // Création d'un temple avec la possibilité de modifier la couleur du cristal
        Temple temple = new Temple(new Position(0, 0), 1, 2);
        // Modification de la couleur du cristal
        temple.setCouleurCristal(3);
        // Vérification si la couleur du cristal a été modifiée
        assertEquals(3, temple.getCouleurCristal());
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
