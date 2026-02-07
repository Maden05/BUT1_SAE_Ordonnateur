package modele;


/**
 * Représente un temple avec une position, une couleur, et un cristal.
 */
public class Temple {
    /**
     * Position du temple
     * */
    private Position positionTemple;
    /**
     * Couleur du temple
     * */
    private int couleurTemple;
    /**
     * Couleur du cristal présent dans le temple
     * */
    private int couleurCristal;
    /**
     * Indique si le temple possède un cristal
     * */
    private boolean possedeCristal;


    /**
     * Constructeur pour initialiser un temple avec sa position, sa couleur et la couleur de son cristal.
     *
     * @param positionTemple La position du temple.
     * @param couleurTemple La couleur du temple.
     * @param couleurCristal La couleur du cristal présent dans le temple.
     */
    public Temple(Position positionTemple, int couleurTemple, int couleurCristal) {
        this.positionTemple = positionTemple;
        this.couleurTemple = couleurTemple;
        this.couleurCristal = couleurCristal;
        this.possedeCristal = couleurCristal != -1; // Initialisation en fonction de la présence d'un cristal
    }


    /**
     * Vérifie si le temple possède un cristal.
     *
     * @return true si le temple possède un cristal, false sinon.
     */
    public boolean isPossedeCristal() {
        return possedeCristal;// Retourne un booléen indiquant si le temple possède un cristal.
    }

    /**
     * Définit si le temple possède un cristal.
     *
     * @param possedeCristal true si le temple possède un cristal, false sinon.
     */
    public void setPossedeCristal(boolean possedeCristal) {
        this.possedeCristal = possedeCristal; //  Définit si le temple possède un cristal.
    }


    /**
     * Obtient la position du temple.
     *
     * @return La position du temple.
     */
    public Position getPositionTemple() {
        return positionTemple; // Retourne la position du temple.
    }

    /**
     * Obtient la couleur du temple.
     * @return La couleur du temple.
     */
    public int getCouleurTemple() {
        return couleurTemple; // Retourne la couleur du temple.
    }


    /**
     * Obtient la couleur du cristal présent dans le temple.
     * @return La couleur du cristal présent dans le temple.
     */
    public int getCouleurCristal() {
        return this.couleurCristal; // Retourne la couleur du cristal présent dans le temple.
    }


    /**
     * Définit la couleur du cristal présent dans le temple.
     * Met à jour également l'état de possession du cristal.
     *
     * @param couleurCristal La nouvelle couleur du cristal.
     */
    public void setCouleurCristal(int couleurCristal) {
        this.couleurCristal = couleurCristal; // Définit la nouvelle couleur du cristal.
        this.possedeCristal = couleurCristal != -1; // Mettre à jour la possession du cristal.
    }



    /**
     * L'objectif est de remetre l'affichage des coordonnées ds position x et y des temple correctement
     * */
    private String getAdjustedPosition() {
        int adjustedAbscisse = positionTemple.getAbscisse() - 16;
        int adjustedOrdonnee = positionTemple.getOrdonnee() - 16;
        return "Position{abscisse=" + adjustedAbscisse + ", ordonnee=" + adjustedOrdonnee + "}";
    }

    /**
     * Représente l'objet Temple sous forme de chaîne de caractères.
     *
     * @return Une chaîne de caractères contenant les informations du temple.
     */
    @Override
    public String toString() {
        // L'objet temple avec sa position, couleur, couleurCristal et si elle possède ou non un cristal
        return "Temple{" +
                "positionTemple=" + getAdjustedPosition() +
                ", couleurTemple=" + couleurTemple +
                ", couleurCristal=" + couleurCristal +
                ", possedeCristal=" + possedeCristal +
                '}';
    }

}
