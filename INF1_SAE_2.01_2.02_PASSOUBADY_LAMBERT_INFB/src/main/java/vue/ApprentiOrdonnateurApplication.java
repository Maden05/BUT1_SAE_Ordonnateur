package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.ApprentiOrdonnateur;


/**
 * Classe principale pour l'application JavaFX de l'Apprenti Ordonnateur.
 * Cette classe hérite de javafx.application.Application et sert de point d'entrée pour l'application JavaFX.
 */
public class ApprentiOrdonnateurApplication extends Application {

    /**
     * Point d'entrée principal pour l'application JavaFX.
     * Initialise la scène principale et affiche la fenêtre de l'application.
     *
     * @param stage Le conteneur principal pour toutes les scènes de l'application.
     */
    public void start(Stage stage) {
        VBox root = new VBoxRoot();  // Crée une instance de VBoxRoot comme racine de la scène
        Scene scene = new Scene(root, 900,700 ); // Crée une nouvelle scène avec VBoxRoot comme racine, et définit sa taille
        stage.setScene(scene);  // Associe la scène au stage principal
        stage.setTitle("Apprenti ordonnateur"); // Définit le titre de la fenêtre de l'application
        stage.show(); // Affiche la fenêtre de l'application
    }

    public static void main(String[] args) {
        launch(args);
    }
}
