package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import modele.Position;
import modele.Temple;
import vue.LectureScenario;
import vue.VBoxCanvas;
import vue.VBoxRoot;

import java.io.File;
import java.util.ArrayList;

/**
 * Le contrôleur est responsable de gérer les événements associés à la sélection des scénarios dans la barre de menus.
 * Lorsque l'utilisateur sélectionne un scénario, le contrôleur charge les temples correspondants à partir du fichier de scénario
 * et les affiche sur le canvas. Il réinitialise également le nombre de pas à zéro après chaque sélection de scénario.
 */
public class Controleur implements EventHandler<ActionEvent> {
    /**
     * Gère l'événement déclenché lors de la sélection d'un élément de menu.
     * Lorsque l'utilisateur sélectionne un scénario, les temples correspondants sont chargés à partir du fichier de scénario
     * et affichés sur le canvas.
     *
     * @param actionEvent L'événement d'action déclenché par la sélection d'un élément de menu.
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // Récupérer les données utilisateur associées à l'élément de menu sélectionné
        Object userData = ((MenuItem) actionEvent.getSource()).getUserData();
        // Vérifier si l'utilisateur a sélectionné un fichier de scénario
        if (userData instanceof File) { // l'utilisateur a choisi un scénario
            File fichierScenario = (File) userData; // Obtenir le fichier de scénario
            // Charger les temples à partir du fichier de scénario
            ArrayList<Temple> temples = LectureScenario.lecture(fichierScenario);
            // Mettre à jour la liste des temples de l'apprenti
            VBoxRoot.getApprenti().setListeTemples(temples);
            // Obtenir l'instance de VBoxCanvas
            VBoxCanvas canvas = VBoxRoot.getCanvas();// Récupérer l'instance de VBoxCanvas
            canvas.setTemples(temples); // Dessiner les temples sur la grille
        }
         // Réinitialiser le nombre de pas à zéro après chaque sélection de scénario
       Position.resetNombreDePas();
    }


}
