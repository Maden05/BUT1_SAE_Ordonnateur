package modele;

import java.util.ArrayList;


/**
 * La classe ApprentiOrdonnateur représente le gestionnaire principal de l'apprenti dans l'application.
 * Elle gère la position de l'apprenti sur la carte, les interactions avec les temples et les cristaux.
 */
public class ApprentiOrdonnateur {
    private static Position positionApprenti; /** La position actuelle de l'apprenti sur la carte.*/
    private static ArrayList<Temple> listeTemples; /** La liste des temples présents sur la carte.*/
    private static int cristal; /** La couleur du cristal actuellement détenu par l'apprenti. -1 si aucun cristal.*/
    public static final int COULEUR_TRANSPARENTE = -1; /**Cristal transparent de l'apprenti */


    /**
     * Constructeur de la classe ApprentiOrdonnateur.
     * Initialise la position de l'apprenti et la couleur du cristal.
     */
    public ApprentiOrdonnateur() {
        positionApprenti = new Position(1 + 15, 1 + 15); // Position initiale de l'apprenti.
        cristal = -1; // Aucun cristal au départ.

    }


    /**
     * Récupère la position actuelle de l'apprenti.
     * @return La position actuelle de l'apprenti.
     */
    public Position getPositionApprenti() {
        return positionApprenti;
    }



    /**
     * Récupère la couleur du cristal que porte l'apprenti.
     * @return La couleur du cristal que porte l'apprenti.
     */
    public static int getCristal() {
        return cristal;
    }


    /**
     * Gère l'échange de cristal entre l'apprenti et un temple.
     *
     * @param temple Le temple avec lequel l'échange doit avoir lieu.
     */
    public void echangerCristal(Temple temple) {
        // Vérifie si le temple est null
        if (temple == null) {
            System.out.println("Le temple est null, impossible d'echanger le cristal.");
            return;
        }

        // Vérifier si le temple possède un cristal
        if (temple.isPossedeCristal()) {
            // Si l'apprenti n'a pas de cristal, prendre le cristal du temple
            if (cristal == COULEUR_TRANSPARENTE) {
                // Prend le cristal du temple et met à jour l'état du temple
                this.cristal = temple.getCouleurCristal();
                temple.setPossedeCristal(false);
                System.out.println("Cristal pris du temple.");
            } else {
                // Sinon, échanger les couleurs des cristaux entre l'apprenti et le temple
                int tempCouleur = this.cristal; // Stocke la valeur de la couleur du cristal
                this.cristal = temple.getCouleurCristal();
                temple.setCouleurCristal(tempCouleur);
                System.out.println("Cristal echange avec le temple.");
            }
        } else {
            // Si le temple ne possède pas de cristal, déposer le cristal dans le temple
            temple.setCouleurCristal(this.cristal);
            temple.setPossedeCristal(true);
            this.cristal = COULEUR_TRANSPARENTE; // Réinitialise le cristal de l'apprenti à transparent
            System.out.println("Cristal depose dans le temple.");
        }
    }



    /**
     * Définit la liste des temples pour l'apprenti.
     * @param listeTemples La liste des temples à définir.
     */
    public static void setListeTemples(ArrayList<Temple> listeTemples) {
        ApprentiOrdonnateur.listeTemples = listeTemples; // défini ou met à jour la list des temples
    }


    /**
     * Récupère la liste des temples associée à l'apprenti.
     * @return La liste des temples associée à l'apprenti.
     */
    public static ArrayList<Temple> getListeTemples() {
        return listeTemples;
    }


    /**
     * Retourne une chaîne de caractères représentant l'ApprentiOrdonnateur avec sa position et son cristal.
     * @return Une chaîne de caractères représentant l'ApprentiOrdonnateur.
     */
    @Override
    public String toString() {
        // Construit et retourne une chaîne de caractères décrivant l'ApprentiOrdonnateur
        return "ApprentiOrdonnateur{" +
                "positionApprenti=" + positionApprenti +
                ", cristal=" + cristal +
                '}';
    }


}
