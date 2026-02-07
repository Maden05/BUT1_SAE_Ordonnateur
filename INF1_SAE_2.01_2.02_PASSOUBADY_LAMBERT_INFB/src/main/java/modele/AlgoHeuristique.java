package modele;
import javafx.application.Platform;
import vue.VBoxCanvas;
import vue.VBoxRoot;
import java.util.Timer;
import java.util.TimerTask;
import static java.lang.Math.abs;
import static vue.VBoxRoot.canvas;
import static vue.VBoxRoot.getApprenti;



/**
 * Cette classe implémente un algorithme heuristique pour guider le déplacement de l'apprenti
 * afin de placer les cristaux dans les temples de manière optimale. L'algorithme recherche
 * le temple le plus proche qui possède un cristal dont la couleur ne correspond pas à celle
 * de son propre cristal, ou un temple dont la couleur correspond à celle du cristal de l'apprenti.
 * L'apprenti est déplacé vers ce temple cible, et une fois arrivé, il échange le cristal si nécessaire
 * et continue l'exécution de l'algorithme. Cette classe implémente l'interface Runnable pour être
 * exécutée dans un thread séparé, permettant ainsi une interaction fluide avec l'interface graphique.
 */
public class AlgoHeuristique implements Runnable { // Runnable, interface fonctionnelle qui définit une tâche à effectuer. Elle contient seulement la méthode run.

    /**
     * Timer utilisé pour planifier les déplacements de l'apprenti selon un intervalle de temps spécifié.
     */
    Timer timer;

    /**
     * Permet de synchroniser les actions de l'apprenti avec l'interface utilisateur.
     * Utilise la méthode Platform.runLater() pour exécuter la tâche d'exécution de l'algorithme sur le thread
     * de l'interface utilisateur, permettant ainsi de visualiser les déplacements de l'apprenti sur l'interface graphique.
     */
        @Override
        public void run() {
            Platform.runLater(() -> { // met a jour l'interface graphiquen visualisation de chaque action dans le graphique..
                executerAlgorithme();
            });
        }


    /**
     * Exécute l'algorithme heuristique pour déplacer l'apprenti vers le temple le plus proche
     * avec un cristal non correspondant à sa couleur, ou vers un temple ayant la même couleur
     * que le cristal de l'apprenti.
     */
    void executerAlgorithme() {
        Temple templeCible = templePlusProcheAvecCristalNonCorrespondant();

        // Vérifier si un temple cible valide a été trouvé
        if (templeCible != null && templeCible.getPositionTemple() != null) {
            annulerDeplacement(); // Annuler la tâche de timer existante, le cas échéant
            // Déplacer l'apprenti vers le temple cible
            deplacerApprentiVersTemple(templeCible);
        } else {
            // Aucun temple cible valide trouvé, tous les cristaux sont placés
            System.out.println("Tous les cristaux sont placés.");
        }
    }


    /**
     * Annule le déplacement en cours.
     */
    public void annulerDeplacement() {
        if (timer != null) {
            timer.cancel();
            timer = null; // Réinitialiser le timer à null
        }
    }


    /**
     * Méthode pour faire déplacer l'apprenti vers une position donnée.
     *
     * @param temple Le temple cible vers lequel déplacer l'apprenti.
     */
    void deplacerApprentiVersTemple(Temple temple) {
        Position positionCible = temple.getPositionTemple();
        Position positionApprenti = getApprenti().getPositionApprenti();

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    positionApprenti.deplacementUneCase(positionCible);
                    VBoxRoot.getCanvas().getLabelNombreDePas().setText("Nombre de pas : " + Position.getNombreDePas());
                    canvas.redessiner(); // Redessiner la carte après le déplacement
                    if (positionApprenti.equals(positionCible)) {
                        timer.cancel();
                        // Vérifie si l'apprenti est arrivé à la position cible avant d'échanger le cristal
                        getApprenti().echangerCristal(temple); // Utilisation de la méthode echangerCristal avec temple

                        // Après avoir échangé le cristal, continuer l'algorithme
                        Platform.runLater(() -> executerAlgorithme());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 200);
    }

    /**
     * Trouve le temple le plus proche qui a un cristal non correspondant à sa couleur.
     *
     * @return Le temple le plus proche avec un cristal non correspondant.
     */
    public Temple templePlusProcheAvecCristalNonCorrespondant() {
        Temple templePlusProche = null;
        int distanceMin = Integer.MAX_VALUE;
        boolean apprentiPossedeCristal = getApprenti().getCristal() != ApprentiOrdonnateur.COULEUR_TRANSPARENTE;

        for (Temple temple : getApprenti().getListeTemples()) {
            // Si l'apprenti ne possède pas de cristal, chercher le temple le plus proche avec un cristal non correspondant
            if (!apprentiPossedeCristal && temple.isPossedeCristal() && temple.getCouleurTemple() != temple.getCouleurCristal()) {
                int distance = distanceApprenti(temple);
                if (distance < distanceMin) {
                    distanceMin = distance;
                    templePlusProche = temple;
                }
            }
            // Si l'apprenti possède un cristal, chercher le temple qui a la même couleur que son cristal
            else if (apprentiPossedeCristal && temple.getCouleurTemple() == getApprenti().getCristal()) {
                int distance = distanceApprenti(temple);
                if (distance < distanceMin) {
                    distanceMin = distance;
                    templePlusProche = temple;
                }
            }
        }

        if (templePlusProche != null) {
            System.out.println("Temple le plus proche avec cristal non correspondant : " + templePlusProche);
        } else {
            System.out.println("Aucun temple correspondant trouvé.");
        }

        return templePlusProche;
    }

    /**
     * Calcule la distance de Manhattan entre l'apprenti et un temple donné.
     *
     * @param temple Le temple pour lequel la distance doit être calculée.
     * @return La distance de Manhattan entre l'apprenti et le temple.
     */
    public int distanceApprenti(Temple temple) {
        int posXApprenti = getApprenti().getPositionApprenti().getAbscisse();
        int posYApprenti = getApprenti().getPositionApprenti().getOrdonnee();
        int posXTemple = temple.getPositionTemple().getAbscisse();
        int posYTemple = temple.getPositionTemple().getOrdonnee();
        return abs(posXApprenti - posXTemple) + abs(posYApprenti - posYTemple); // Calcul de Manhattan
    }
}
