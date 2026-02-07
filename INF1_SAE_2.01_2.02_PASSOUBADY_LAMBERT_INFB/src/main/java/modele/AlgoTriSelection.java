package modele;

import javafx.application.Platform;
import vue.VBoxRoot;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static vue.VBoxRoot.canvas;
import static vue.VBoxRoot.getApprenti;

public class AlgoTriSelection implements Runnable {

    private Timer timerTriSelec;

    private int currentTempleIndex = 0;

    private Set<Temple> templesTraites = new HashSet<>();


    @Override
    public void run() {
        // est utilisé pour exécuter la méthode executerAlgoTri() dans le thread de l'interface graphique. Pour les MAJ de l'inteface U.
        Platform.runLater(this::executerAlgoTri);
    }

    private void executerAlgoTri() {
        if (currentTempleIndex < getApprenti().getListeTemples().size()) {
            Temple templeAvecCristalLePlusPetit = trouverTempleAvecCristalLePlusPetit(currentTempleIndex);
            if (templeAvecCristalLePlusPetit != null && !templesTraites.contains(templeAvecCristalLePlusPetit)) {
                System.out.println("Temple avec le plus petit cristal trouvé : " + templeAvecCristalLePlusPetit);
                deplacerApprentiVersTemple(templeAvecCristalLePlusPetit);
            } else {
                currentTempleIndex++;
                System.out.println("Pas de temple à traiter ou déjà traité. Increment currentTempleIndex à: " + currentTempleIndex);
                executerAlgoTri();
            }
        } else {
            System.out.println("Tous les cristaux sont correctement placés.");
        }
    }


    private void deplacerApprentiVersTemple(Temple templeCible) {
        // Vérifie si le temple cible possède un cristal
        if (templeCible.isPossedeCristal()) {
            // Obtient les positions actuelles de l'apprenti et du temple cible
            Position positionCible = templeCible.getPositionTemple();
            Position positionApprenti = getApprenti().getPositionApprenti();


            System.out.println("Déplacement de l'apprenti vers le temple cible: " + templeCible);
            // Crée un nouveau Timer et TimerTask pour planifier le déplacement de l'apprenti
            timerTriSelec = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        // Déplace l'apprenti d'une case vers le temple cible
                        positionApprenti.deplacementUneCase(positionCible);
                        // Met à jour l'affichage du nombre de pas
                        VBoxRoot.getCanvas().getLabelNombreDePas().setText("Nombre de pas : " + Position.getNombreDePas());
                        // Redessine le canvas pour refléter le nouveau déplacement de l'apprenti
                        canvas.redessiner();
                        // Vérifie si l'apprenti est arrivé au temple cible
                        if (positionApprenti.equals(positionCible)) {
                            System.out.println("Apprenti arrivé au temple cible: " + templeCible);
                            // Annule le Timer et passe à l'échange avec le temple cible
                            timerTriSelec.cancel();
                            echangeEtDeplaceVersTempleCible(templeCible);
                        }
                    });
                }
            };
            // Planifie le déplacement de l'apprenti à intervalles réguliers
            timerTriSelec.scheduleAtFixedRate(timerTask, 1000, 200);
        } else {
            // Si le temple cible ne possède pas de cristal, passer directement à l'échange avec le temple suivant
            System.out.println("Le temple cible ne possède pas de cristal. Passage direct à l'échange avec le temple suivant.");
            echangeEtDeplaceVersTempleCible(templeCible);
        }
    }


    private void echangeEtDeplaceVersTempleCible(Temple templeCible) {
        // Obtient la couleur du cristal du temple cible
        int couleurCristal = templeCible.getCouleurCristal();
        // Trouve un temple de destination approprié pour l'échange
        Temple templeDestination = trouverTempleParCouleur(couleurCristal);

        System.out.println("Échange et déplacement vers temple cible: " + templeCible);
        // Vérifie si un temple de destination approprié a été trouvé et si le temple cible possède un cristal
        if (templeDestination != null && templeCible.isPossedeCristal()) {
            System.out.println("Temple destination trouvé pour échange: " + templeDestination);
            // Effectue l'échange du cristal de l'apprenti avec celui du temple cible
            getApprenti().echangerCristal(templeCible);
            // Déplace l'apprenti vers le temple de destination pour l'échange
            deplacerApprentiVersTemplePourEchange(templeDestination, templeCible);
        } else {
            // Marquer ce temple comme traité car il est à la bonne place
            templesTraites.add(templeCible);
            // Incrémente l'index du temple actuel pour passer au suivant dans l'algorithme de tri
            currentTempleIndex++;
            System.out.println("Pas de temple destination trouvé ou pas de cristal à échanger, incrémentation de currentTempleIndex à: " + currentTempleIndex);
            // Exécute à nouveau l'algorithme de tri
            executerAlgoTri();
        }
    }


    private void deplacerApprentiVersTemplePourEchange(Temple templeDestination, Temple templeInitial) {
        // Obtient les positions de l'apprenti et du temple de destination
        Position positionCible = templeDestination.getPositionTemple();
        Position positionApprenti = getApprenti().getPositionApprenti();

        System.out.println("Déplacement de l'apprenti vers le temple pour échange: " + templeDestination);
        // Initialise un nouveau timer pour contrôler le déplacement de l'apprenti
        timerTriSelec = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Déplace l'apprenti d'une case vers le temple de destination
                    positionApprenti.deplacementUneCase(positionCible);
                    // Met à jour le nombre de pas affiché sur le canvas
                    VBoxRoot.getCanvas().getLabelNombreDePas().setText("Nombre de pas : " + Position.getNombreDePas());
                    // Redessine le canvas pour refléter le déplacement de l'apprenti
                    canvas.redessiner();
                    // Vérifie si l'apprenti est arrivé au temple de destination
                    if (positionApprenti.equals(positionCible)) {
                        System.out.println("Apprenti arrivé au temple destination pour échange: " + templeDestination);
                        // Arrête le timer après l'arrivée de l'apprenti au temple de destination
                        timerTriSelec.cancel();
                        // Récupère la couleur du cristal du temple de destination pour l'échange
                        int couleurCristalEchange = templeDestination.getCouleurCristal();
                        // Effectue l'échange de cristal entre l'apprenti et le temple de destination
                        getApprenti().echangerCristal(templeDestination);
                        // Marque le temple destination comme traité
                        templesTraites.add(templeDestination);
                        // Déplace l'apprenti vers le temple initial avec le cristal nouvellement échangé
                        deplacerApprentiRetour(templeInitial, couleurCristalEchange);
                    }
                });
            }
        };
        // Planifie la tâche du timer pour contrôler le déplacement de l'apprenti à intervalles réguliers
        timerTriSelec.scheduleAtFixedRate(timerTask, 1000, 200);
    }

    private void deplacerApprentiRetour(Temple templeInitial, int couleurCristalEchange) {
        // Obtient les positions de l'apprenti et du temple initial
        Position positionCible = templeInitial.getPositionTemple();
        Position positionApprenti = getApprenti().getPositionApprenti();

        System.out.println("Retour de l'apprenti au temple initial: " + templeInitial);
        // Initialise un nouveau timer pour contrôler le déplacement de l'apprenti
        timerTriSelec = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Déplace l'apprenti d'une case vers le temple initial
                    positionApprenti.deplacementUneCase(positionCible);
                    // Met à jour le nombre de pas affiché sur le canvas
                    VBoxRoot.getCanvas().getLabelNombreDePas().setText("Nombre de pas : " + Position.getNombreDePas());
                    // Redessine le canvas pour refléter le déplacement de l'apprenti
                    canvas.redessiner();
                    // Vérifie si l'apprenti est arrivé au temple initial
                    if (positionApprenti.equals(positionCible)) {
                        System.out.println("Apprenti revenu au temple initial: " + templeInitial);
                        // Arrête le timer après l'arrivée de l'apprenti au temple initial
                        timerTriSelec.cancel();
                        // Vérifie s'il faut effectuer un échange de cristal
                        if (couleurCristalEchange != ApprentiOrdonnateur.COULEUR_TRANSPARENTE) {
                            getApprenti().echangerCristal(templeInitial);
                        }
                        // Marque le temple initial comme traité
                        templesTraites.add(templeInitial);
                        // Incrémente l'index du temple actuel
                        currentTempleIndex++;
                        System.out.println("Incrémentation de currentTempleIndex à: " + currentTempleIndex);
                        // Exécute à nouveau l'algorithme de tri
                        executerAlgoTri();
                    }
                });
            }
        };
        // Planifie la tâche du timer pour contrôler le déplacement de l'apprenti à intervalles réguliers
        timerTriSelec.scheduleAtFixedRate(timerTask, 1000, 200);
    }


    private Temple trouverTempleAvecCristalLePlusPetit(int startIndex) {
        // Initialise le temple avec le cristal le plus petit et la valeur du cristal le plus petit
        Temple templeAvecCristalLePlusPetit = null;
        int plusPetitCristal = Integer.MAX_VALUE;

        // Parcourt la liste des temples à partir de l'indice spécifié
        for (int i = startIndex; i < getApprenti().getListeTemples().size(); i++) {
            Temple temple = getApprenti().getListeTemples().get(i);
            int couleurCristal = temple.getCouleurCristal();
            // Vérifie si le temple a un cristal valide et s'il n'a pas encore été traité
            if (couleurCristal < plusPetitCristal && couleurCristal != -1 && !templesTraites.contains(temple)) {
                // Met à jour le temple avec le cristal le plus petit et la valeur du cristal le plus petit
                plusPetitCristal = couleurCristal;
                templeAvecCristalLePlusPetit = temple;
            }
        }
        // Retourne le temple avec le cristal le plus petit ou null s'il n'y en a aucun qui correspond aux critères
        return templeAvecCristalLePlusPetit;
    }


    private Temple trouverTempleParCouleur(int couleur) {
        // Parcourt la liste des temples de l'apprenti
        for (Temple temple : getApprenti().getListeTemples()) {
            // Vérifie si la couleur du temple correspond à celle spécifiée
            if (temple.getCouleurTemple() == couleur) {
                // Retourne le temple ayant la couleur spécifiée
                return temple;
            }
        }
        // Retourne null si aucun temple avec la couleur spécifiée n'a été trouvé
        return null;
    }

}
