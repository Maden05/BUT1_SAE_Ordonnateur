package modele;


import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;


/**
 * Classe de test unitaire pour la classe ApprentiOrdonnateur.
 * Elle teste les différentes méthodes de la classe ApprentiOrdonnateur.
 */
public class TestApprentiOrdonnateur {

    /**
     * Test de la méthode getPositionApprenti().
     * Vérifie si la position de l'apprenti est correctement récupérée.
     */
    @Test
    public void testGetPositionApprenti() {
        // Création d'un ApprentiOrdonnateur avec une position spécifique
        ApprentiOrdonnateur apprenti = new ApprentiOrdonnateur();
        Position position = apprenti.getPositionApprenti();
        // Vérification des coordonnées de la position
        assertEquals(16, position.getAbscisse());
        assertEquals(16, position.getOrdonnee());
    }

    /**
     * Test de la méthode getCristal().
     * Vérifie si la couleur du cristal de l'apprenti est correctement récupérée.
     */
    @Test
    public void testGetCristal() {
        // Création d'un ApprentiOrdonnateur avec un cristal spécifique
        ApprentiOrdonnateur apprenti = new ApprentiOrdonnateur();
        int cristal = ApprentiOrdonnateur.getCristal();
        assertEquals(-1, cristal);
    }

    /**
     * Test de la méthode echangerCristal().
     * Vérifie si l'échange de cristal avec un temple fonctionne correctement.
     */
    @Test
    public void testEchangerCristal() {
        // Création d'un ApprentiOrdonnateur avec un cristal
        ApprentiOrdonnateur apprenti = new ApprentiOrdonnateur();
        Temple temple = new Temple(new Position(0, 0), 1, 2);
        apprenti.echangerCristal(temple);
        assertEquals(2, ApprentiOrdonnateur.getCristal());
    }

    /**
     * Test de la méthode setListeTemples().
     * Vérifie si la liste des temples associée à l'apprenti est correctement définie.
     */
    @Test
    public void testSetListeTemples() {
        // Création d'une liste de temples
        ArrayList<Temple> temples = new ArrayList<>();
        Temple temple1 = new Temple(new Position(1, 1), 1, 2);
        Temple temple2 = new Temple(new Position(2, 2), 2, 3);
        temples.add(temple1);
        temples.add(temple2);

        // Définition de la liste des temples pour l'apprenti
        ApprentiOrdonnateur.setListeTemples(temples);

        // Vérification si la liste des temples a été correctement définie
        assertEquals(temples, ApprentiOrdonnateur.getListeTemples());
    }

    /**
     * Test de la méthode echangerCristal() avec un temple nul.
     * Vérifie si un message d'erreur est affiché lorsque le temple est null.
     */
    @Test
    public void testEchangerCristalTempleNull() {
        // Création d'un ApprentiOrdonnateur avec un cristal
        ApprentiOrdonnateur apprenti = new ApprentiOrdonnateur();
        // Appel de echangerCristal() avec un temple null
        apprenti.echangerCristal(null);
        // Vérification si un message d'erreur est affiché
        // (Ceci dépend de l'implémentation de la méthode echangerCristal(), qui doit gérer le cas d'un temple null)
        // Par exemple, si le message d'erreur est affiché sur la sortie standard, vous pouvez vérifier cela en capturant la sortie standard
    }

    /**
     * Test de la méthode echangerCristal() lorsque l'apprenti possède déjà un cristal.
     * Vérifie si l'échange de cristal avec un temple fonctionne correctement lorsque l'apprenti possède déjà un cristal.
     */
    @Test
    public void testEchangerCristalAvecCristalPrecedent() {
        // Création d'un ApprentiOrdonnateur avec un cristal
        ApprentiOrdonnateur apprenti = new ApprentiOrdonnateur();
        Temple temple1 = new Temple(new Position(0, 0), 1, 2);
        // L'apprenti récupère un cristal
        apprenti.echangerCristal(temple1);
        Temple temple2 = new Temple(new Position(1, 1), 2, 3);
        // L'apprenti échange son cristal avec un autre temple
        apprenti.echangerCristal(temple2);
        assertEquals(3, ApprentiOrdonnateur.getCristal());
    }

    /**
     * Test de la méthode echangerCristal() lorsque le temple ne possède pas de cristal.
     * Vérifie si l'apprenti dépose son cristal dans le temple lorsque le temple ne possède pas de cristal.
     */
    @Test
    public void testEchangerCristalTempleSansCristal() {
        // Création d'un ApprentiOrdonnateur avec un cristal
        ApprentiOrdonnateur apprenti = new ApprentiOrdonnateur();
        Temple temple = new Temple(new Position(0, 0), 1, ApprentiOrdonnateur.COULEUR_TRANSPARENTE);
        // L'apprenti échange son cristal avec un temple ne possédant pas de cristal
        apprenti.echangerCristal(temple);
        assertEquals(ApprentiOrdonnateur.COULEUR_TRANSPARENTE, ApprentiOrdonnateur.getCristal());
    }


    /**
     * Méthode principale pour exécuter les tests.
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        // Exécution des tests de la classe TestApprentiOrdonnateur
        org.junit.runner.JUnitCore.main("modele.TestApprentiOrdonnateur");
    }
}
