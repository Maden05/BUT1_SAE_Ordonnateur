package modele;

import java.util.ArrayList;


/**
 * Représente une position avec des coordonnées en abscisse et ordonnée.
 */
public class Position {
    /**
     * Compteur statique pour suivre le nombre de déplacements
     * */
    private static int nombreDePas = 0;
    /**
     * Coordonnée en abscisse
     * */
    private int abscisse;
    /**
     * Coordonnée en ordonnée
     * */
    private int ordonnee;


    /**
     * Constructeur avec paramètres pour initialiser les coordonnées.
     *
     * @param abscisse La coordonnée en abscisse.
     * @param ordonnee La coordonnée en ordonnée.
     */
    public Position(int abscisse, int ordonnee) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
    }


    /**
     * Constructeur sans paramètres, initialise les coordonnées à (0, 0).
     */
    public Position() {
        this.abscisse = 0;
        this.ordonnee = 0;
    }


    /**
     * Déplace la position actuelle d'une case vers une position cible.
     * Le déplacement se fait en incrémentant ou en décrémentant l'abscisse ou l'ordonnée.
     *
     * @param parPosition La position cible vers laquelle se déplacer.
     */
    public void deplacementUneCase(Position parPosition) {
        // Incrémente le compteur de pas
        nombreDePas++;
        // Déplace d'une case vers la gauche si la position actuelle est à droite de la position cible
        if (this.abscisse > parPosition.abscisse) {
            this.abscisse -= 1;
            return;
        }
        // Déplace d'une case vers la droite si la position actuelle est à gauche de la position cible
        if (this.abscisse < parPosition.abscisse) {
            this.abscisse += 1;
            return;
        }
        // Déplace d'une case vers le bas si la position actuelle est au-dessus de la position cible
        if (this.ordonnee > parPosition.ordonnee) {
            this.ordonnee -= 1;
            return;
        }
        // Déplace d'une case vers le haut si la position actuelle est en dessous de la position cible
        if (this.ordonnee < parPosition.ordonnee) {
            this.ordonnee += 1;
            return;
        }
    }

    /**
     * Vérifie si deux positions sont égales en comparant leurs coordonnées.
     *
     * @param parPosition La position à comparer avec la position actuelle.
     * @return true si les coordonnées des deux positions sont égales, false sinon.
     */
    public boolean equals(Position parPosition) {
        return this.ordonnee == parPosition.ordonnee && this.abscisse == parPosition.abscisse;
    }

    /**
     * Obtient la coordonnée en abscisse.
     *
     * @return La coordonnée en abscisse.
     */
    public int getAbscisse() {
        return abscisse;
    }



    /**
     * Obtient la coordonnée en ordonnée.
     *
     * @return La coordonnée en ordonnée.
     */
    public int getOrdonnee() {
        return ordonnee;
    }

    /**
     * Réinitialise le nombre de pas à zéro.
     */
    public static void resetNombreDePas() {
        nombreDePas = 0;
    }



    /**
     * Obtient le nombre total de déplacements effectués.
     *
     * @return Le nombre de déplacements effectués par toutes les instances de la classe Position.
     */
    public static int getNombreDePas() {
        return nombreDePas;
    }


    /**
     * Recherche et retourne le temple situé à la position actuelle.
     *
     * @param listeTemples La liste des temples.
     * @return Le temple situé à la position actuelle ou null si aucun temple n'est trouvé.
     */
    public Temple getTempleSurPosition(ArrayList<Temple> listeTemples) {
        // Parcours la liste des temples
        for (Temple temple : listeTemples) {
            // Vérifie si la position du temple correspond à la position actuelle
            if (temple.getPositionTemple().equals(this)) {
                // Retourne le temple trouvé à la position actuelle
                return temple;
            }
        }
        // Retourne null si aucun temple n'est trouvé à la position actuelle
        return null; // Aucun temple trouvé à cette position
    }


    /**
     * Représente l'objet Position sous forme de chaîne de caractères.
     * @return Une chaîne de caractères contenant les coordonnées de la position.
     */
    @Override
    public String toString() {
        return "Position{" +
                "abscisse=" + abscisse +
                ", ordonnee=" + ordonnee +
                '}';
    }


}