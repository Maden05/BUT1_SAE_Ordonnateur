package vue;

import javafx.scene.paint.Color;


/**
 * Interface contenant les constantes utilisées pour le canvas.
 */
/**
 * Interface contenant les constantes utilisées pour le canvas.
 */
public interface ConstantesCanvas {
    /** Largeur du canvas. */
    final int LARGEUR_CANVAS = 801;

    /** Hauteur du canvas. */
    final int HAUTEUR_CANVAS = 801;

    /** Taille d'un carré de la grille. */
    final int CARRE = 25;

    /** Couleur de la grille. */
    final Color COULEUR_GRILLE = Color.BLACK;

    /** Couleur de l'apprenti. */
    final Color COULEUR_APPRENTI = Color.BLACK;

    /** Largeur de l'ovale de l'apprenti. */
    final int LARGEUR_OVALE = 15;

    /** Hauteur de l'ovale de l'apprenti. */
    final int HAUTEUR_OVALE = 15;

    /** Intitulé du menu des scénarios. */
    final String INTITULE_MENU_SCENARIOS = "scenario";

    /** Couleurs des temples. */
    Color[] COLORSTEMPLES = {
            Color.CYAN, Color.BLACK, Color.PINK, Color.OLIVE, Color.BROWN,
            Color.CHOCOLATE, Color.YELLOW, Color.ORANGE, Color.GRAY, Color.DARKBLUE,
            Color.PURPLE, Color.GREEN, Color.HOTPINK, Color.RED, Color.TURQUOISE
    };
}




