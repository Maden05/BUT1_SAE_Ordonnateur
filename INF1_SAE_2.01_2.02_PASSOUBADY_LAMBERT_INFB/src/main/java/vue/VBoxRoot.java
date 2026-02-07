package vue;

import controleur.Controleur;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import modele.ApprentiOrdonnateur;
import modele.Temple;
import java.io.File;
import java.util.ArrayList;


/**
 * La classe VBoxRoot représente la racine du conteneur VBox pour l'interface utilisateur.
 * Elle configure la barre de menus et gère le chargement des scénarios.
 */
public class VBoxRoot extends VBox implements ConstantesCanvas {

    private static Controleur controleur;

    protected static ApprentiOrdonnateur apprenti;

    public static VBoxCanvas canvas;


    public VBoxRoot() {
        // Initialisation de l'apprenti, du contrôleur et du canvas ( Nouvelles instances)
        apprenti = new ApprentiOrdonnateur();
        controleur = new Controleur();
        canvas = new VBoxCanvas(apprenti);

        // Création et ajout de la barre de menus
        MenuBar menuBar = new MenuBar();
        this.getChildren().add(menuBar); // Ajout de la barre de menus au conteneur VBoxRoot
        VBox.setMargin(menuBar, new Insets(9)); // Taille de la barre de menus

        // Création du menu des scénarios
        Menu menuScenarios = new Menu((INTITULE_MENU_SCENARIOS)); // Création d'un nouveau menu pour les scénarios
        menuBar.getMenus().add(menuScenarios); // Ajout du menu des scénarios à la barre de menus

        // Chargement des fichiers de scénarios depuis le répertoire "scenarios"
        File[] scenarios = new File("scenarios").listFiles(); // Liste des fichiers dans le répertoire "scenarios"
        for (int i = 0; i < scenarios.length; i++) {
            // Création d'un élément de menu pour chaque fichier de scénario
            MenuItem menuItem = new MenuItem(scenarios[i].getName()); // Création d'un nouvel élément de menu avec le nom du fichier de scénario
            menuItem.setUserData(scenarios[i]); // Associe le fichier de scénario à l'élément de menu
            menuItem.setOnAction(controleur); // Définit l'action à exécuter lorsque l'élément de menu est sélectionné
            menuScenarios.getItems().add(menuItem); // Ajoute l'élément de menu au menu des scénarios
        }

        // Ajout du canvas à VBoxRoot
        this.getChildren().addAll(canvas); // Ajout du canvas au conteneur VBoxRoot
    }


    public static ApprentiOrdonnateur getApprenti() {
        return apprenti;
    }

    public static VBoxCanvas getCanvas() {
        return canvas;
    }


}
