package vue;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modele.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import modele.AlgoHeuristique;

import static vue.VBoxRoot.apprenti;


/**
 * VBoxCanvas est une classe qui représente une boîte verticale contenant un canevas (Canvas) pour afficher une carte,
 * ainsi que des boutons pour interagir avec la carte et afficher des informations. Cette classe gère également les
 * déplacements de l'apprenti sur la carte en utilisant des timers pour animer les déplacements.
 * Elle est utilisée pour afficher et manipuler la carte dans l'interface utilisateur graphique.
 */
public class VBoxCanvas extends VBox implements ConstantesCanvas {

    private Label labelNombreDePas;

    private  GraphicsContext graphicsContext2D;

    private static ArrayList<Temple> listeTemples;

    private Position positionClique;

    private boolean cristalPris;

    private TextArea textAreaLog;


    /**
     * Constructeur de la classe VBoxCanvas qui crée une boîte verticale contenant un canevas pour afficher une carte,
     * ainsi que des boutons pour interagir avec la carte et afficher des informations.
     *
     * @param apprenti L'apprenti ordonnateur associé à la boîte. Utilisé pour interagir avec l'apprenti et les temples.
     */
    public VBoxCanvas(ApprentiOrdonnateur apprenti) {

        // Initialisation des élements de l'interface
        labelNombreDePas = new Label("Nombre de pas : 0");
        // Canvas pour dessiner la Carte
        Canvas canvasCarte = new Canvas(LARGEUR_CANVAS, HAUTEUR_CANVAS);
        graphicsContext2D = canvasCarte.getGraphicsContext2D();
        this.listeTemples = new ArrayList<>(); // Chaque nouvelle instance, une nouvelle liste est initialisée.
        System.out.println("Nombre de temples apres initialisation : " + listeTemples.size()); // Taille de la listeTemple
        this.cristalPris = false;

        // Création d'un ScrollPane pour envelopper le Canvas
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvasCarte);

        // Bouton permettant de faire les échanges de cristaux
        Button echangerCristalButton = new Button("Echanger les cristaux");
        echangerCristalButton.setOnAction(event -> {

            // Obtention du temple sur lequel se trouve l'apprenti
            Temple templeActuel = apprenti.getPositionApprenti().getTempleSurPosition(listeTemples);
            System.out.println(templeActuel);

            // Vérification si le cristal est déjà pris par l'apprenti
            if (!cristalPris) {
                // Si le cristal n'est pas pris, tentative de le prendre
                if (templeActuel != null && templeActuel.isPossedeCristal()) {
                    apprenti.echangerCristal(templeActuel);
                    cristalPris = true;
                    redessiner();
                } else {
                    apprenti.echangerCristal(templeActuel); // Essayez de déposer le cristal sur le temple actuel
                    cristalPris = true;
                    redessiner();
                }
            } else {
                // Si le cristal est déjà pris, tentative de l'échanger avec un autre temple
                if (positionClique != null) {
                    Temple templeCible = positionClique.getTempleSurPosition(listeTemples);
                    if (templeCible != null) {
                        apprenti.echangerCristal(templeCible);
                        cristalPris = false;
                        redessiner();
                    }
                }
            }
        });

        Button AlgoHeuristique = new Button("Lancer AlgoHeuristique");
        AlgoHeuristique.setOnAction(event -> {
            AlgoHeuristique algo = new AlgoHeuristique();
            algo.run();
        });

        Button TriSelection = new Button ("Lancer le Tri Selection");
        TriSelection.setOnAction(event -> {
            AlgoTriSelection algoTri = new AlgoTriSelection();
            algoTri.run();
        });


        // Ajout du TextArea pour afficher les informations
        textAreaLog = new TextArea();
        textAreaLog.setEditable(false); // Ne pas pouvoir éditer le text
        textAreaLog.setPrefHeight(500);// Ajuster la hauteur
        textAreaLog.setPrefWidth(670); // Ajuster la largeur

        // Création de la boîte à droite contenant les boutons et le TextArea
        VBox rightBox = new VBox(labelNombreDePas, echangerCristalButton, textAreaLog);
        rightBox.setSpacing(10);
        VBox.setMargin(labelNombreDePas, new Insets(10));
        VBox.setMargin(echangerCristalButton, new Insets(10));
        VBox.setMargin(textAreaLog, new Insets(10));

        // Ajout du bouton pour lancer l'algorithme de tri par sélection
        rightBox.getChildren().add(AlgoHeuristique);
        rightBox.getChildren().add(TriSelection);


        // Boîte principale contenant le ScrollPane, le Canvas et la boîte à droite
        HBox mainBox = new HBox(scrollPane, canvasCarte, rightBox);
        mainBox.setSpacing(10);
        HBox.setMargin(canvasCarte, new Insets(10));
        HBox.setMargin(rightBox, new Insets(10));

        // Ajout de la boîte principale à la VBoxCanvas
        this.getChildren().add(mainBox);

        // Dessiner la grille et l'apprent
        dessinerGrille();
        dessinerApprenti();

        // Gestion du clic sur la souris
        canvasCarte.setOnMouseClicked(event -> {
            int abscisse = (int) event.getX() / CARRE;
            int ordonnee = (int) event.getY() / CARRE;

            // Vérification si le clic est sur une case valide
            if (abscisse == 0 || ordonnee == 0) {
                System.out.println("Clic non valide sur les cases de coordonnees.");
                System.out.println(" ----------------------------------------------");
                return;
            }

            // Création d'une nouvelle position à partir des coordonnées du clic
            positionClique = new Position(abscisse, ordonnee);
            // Dessin du clic sur la case sélectionnée
            dessinerClique();
            // Déplacement de l'apprenti vers la position cliquée avec un délai
            deplacementAvecTimer(apprenti.getPositionApprenti(), positionClique);
        });

        // Rediriger la sortie standard vers le TextArea
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // Utilisation de Platform.runLater pour mettre à jour l'interface graphique depuis un thread non JavaFX
                Platform.runLater(() -> textAreaLog.appendText(String.valueOf((char) b)));
            }
        });

        // Redirection de System.out et System.err vers le PrintStream
        System.setOut(printStream);
        System.setErr(printStream);
    }



    /**
     * Redessine l'ensemble de la carte avec les temples, l'apprenti, et la position cliquée.
     * Cette méthode efface d'abord le contenu actuel du canvas, puis dessine la grille,
     * les temples, la position cliquée (si elle existe), et enfin l'apprenti.
     * Si l'apprenti est sur un temple, ce temple est également coloré pour le distinguer.
     */
    public void redessiner() {
        // Efface le contenu du canvas
        graphicsContext2D.clearRect(0, 0, LARGEUR_CANVAS, HAUTEUR_CANVAS);
        dessinerGrille();// Dessine la grille avec la méthode dessinerGrille
        setTemples(listeTemples);// Dessine les temple avec la méthode setTemples
        dessinerClique(); // Affiche en rouge le clic de l'utilisateur
        dessinerApprenti();// Dessine l'apprenti avec la méthode dessineApprenti

        // Vérifie si l'apprenti est sur un temple
        if (VBoxRoot.getApprenti().getPositionApprenti() != null) {
            Temple templeActuel = VBoxRoot.getApprenti().getPositionApprenti().getTempleSurPosition(listeTemples);
            if (templeActuel != null) {
                // Colorie le temple sur lequel se trouve l'apprenti
                graphicsContext2D.setFill(COLORSTEMPLES[templeActuel.getCouleurTemple()]);
                graphicsContext2D.fillRect(templeActuel.getPositionTemple().getAbscisse() * CARRE, templeActuel.getPositionTemple().getOrdonnee() * CARRE, CARRE, CARRE);
            }
        }
    }


    /**
     * Dessine un rectangle rouge sur la position cliquée sur la grille, si elle existe.
     * Cette méthode est appelée lorsque l'utilisateur clique sur une case de la grille.
     */
    private void dessinerClique() {
        // Vérifie si une position cliquée a été définie
        if (positionClique != null) {
            // Dessine un rectangle rouge à la position cliquée
            graphicsContext2D.setFill(Color.RED);
            graphicsContext2D.fillRect(positionClique.getAbscisse() * CARRE, positionClique.getOrdonnee() * CARRE, CARRE, CARRE);
        }
    }


    /**
     * Met à jour la liste des temples et dessine les temples ainsi que les cristaux sur le canvas.
     * Cette méthode est utilisée pour actualiser l'affichage lorsque la liste des temples est mise à jour.
     *
     * @param temples La liste des temples à afficher sur le canvas.
     */
    public void setTemples(ArrayList<Temple> temples) {
        // Met à jour la liste des temples
        this.listeTemples = temples;

        // Parcourt la liste des temples pour les dessiner sur le canvas
        for (Temple temple : listeTemples) {
            // Obtient la position et la couleur du temple
            Position position = temple.getPositionTemple();
            int couleur = temple.getCouleurTemple();

            // Dessine le temple avec sa couleur
            graphicsContext2D.setFill(COLORSTEMPLES[couleur]);
            graphicsContext2D.fillRect(position.getAbscisse() * CARRE, position.getOrdonnee() * CARRE, CARRE, CARRE);

            // Vérifie si le temple possède un cristal
            if (temple.isPossedeCristal()) {
                // Dessine un contour blanc autour du cristal
                double ovalWidth = CARRE / 1.5;
                double ovalHeight = CARRE / 2.5;
                graphicsContext2D.setFill(Color.WHITE);
                graphicsContext2D.fillOval(position.getAbscisse() * CARRE + (CARRE - ovalWidth) / 2 - 1, position.getOrdonnee() * CARRE + (CARRE - ovalHeight) / 2 - 1, ovalWidth + 2, ovalHeight + 2);

                // Dessine le cristal avec la couleur du temple
                int couleurCristal = temple.getCouleurCristal();
                graphicsContext2D.setFill(COLORSTEMPLES[couleurCristal]);
                graphicsContext2D.fillOval(position.getAbscisse() * CARRE + (CARRE - ovalWidth) / 2, position.getOrdonnee() * CARRE + (CARRE - ovalHeight) / 2, ovalWidth, ovalHeight);
            }
        }
    }



    /**
     * Dessine la grille sur le canvas avec des lignes verticales et horizontales représentant les cases,
     * ainsi que les étiquettes des abscisses (x) et des ordonnées (y).
     */
    private void dessinerGrille() {
        graphicsContext2D.setStroke(COULEUR_GRILLE); // Couleur de la grille/ Stroke pour les contours

        // Dessiner les lignes verticales et horizontales de la grille
        for (int i = 0; i <= LARGEUR_CANVAS; i += CARRE) {
            graphicsContext2D.strokeLine(i, 0, i, HAUTEUR_CANVAS); // Lignes verticales
            graphicsContext2D.strokeLine(0, i, LARGEUR_CANVAS, i); // Lignes horizontales
        }

        // Dessiner les étiquettes des abscisses (x) de -15 à 15
        for (int i = -16; i <= 15; i++) {
            int x = (i + 16) * CARRE; // Convertir les coordonnées de grille en pixels
            graphicsContext2D.setFill(Color.BLACK);
            // Utiliser une taille de police plus petite pour les étiquettes
           // graphicsContext2D.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
           graphicsContext2D.fillText(String.valueOf(i), x + CARRE / 2, CARRE / 2);
        }

        // Dessiner les étiquettes des ordonnées (y) de -15 à 15
        for (int i = -16; i <= 15; i++) {
            int y = (i + 16) * CARRE; // Convertir les coordonnées de grille en pixels
            graphicsContext2D.setFill(Color.BLACK);
            // Utiliser une taille de police plus petite pour les coordonnées
           // graphicsContext2D.setFont(  Font.font("Arial", FontWeight.NORMAL, 10));
            graphicsContext2D.fillText(String.valueOf(i), CARRE / 2, y + CARRE / 2);
        }
    }


    /**
     * Dessine l'icône représentant l'apprenti sur la position actuelle sur le canvas.
     */
    private void dessinerApprenti() {
        Position positionApprenti = VBoxRoot.getApprenti().getPositionApprenti(); // Obtient la position actuelle de l'apprenti
        // Remplit l'icône de l'apprenti avec la couleur définie
        graphicsContext2D.setFill(COULEUR_APPRENTI);

        // Calcule les coordonnées de l'icône de l'apprenti sur le canvas
        double x = positionApprenti.getAbscisse() * CARRE + (CARRE - LARGEUR_OVALE) / 2;
        double y = positionApprenti.getOrdonnee() * CARRE + (CARRE - HAUTEUR_OVALE) / 2;

        // Dessine l'icône de l'apprenti sous forme d'ovale sur le canvas
        graphicsContext2D.fillOval(x, y, LARGEUR_OVALE, HAUTEUR_OVALE);
    }


    /**
     * Effectue le déplacement de l'apprenti d'une position à une autre avec un timer.
     * @param parPositionApprenti La position actuelle de l'apprenti.
     * @param parPositionCible La position cible vers laquelle l'apprenti doit se déplacer.
     */
    public void deplacementAvecTimer(Position parPositionApprenti, Position parPositionCible) {
        // Vérifie si la position cible est valide
        if (parPositionCible.getAbscisse() == 0 || parPositionCible.getOrdonnee() == 0) {
            System.out.println("Deplacement interdit sur les cases de coordonnees.");
            return;
        }
        Timer timer = new Timer();
        // Définit la tâche à effectuer à chaque intervalle de temps
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // Exécute la tâche dans la file d'attente de la plateforme JavaFX
                Platform.runLater(() -> {
                    // Déplace l'apprenti vers la position cible
                    parPositionApprenti.deplacementUneCase(parPositionCible);
                    // Redessine la carte après le déplacement
                    redessiner();
                    // Met à jour l'affichage du nombre de pas
                    labelNombreDePas.setText("Nombre de pas : " + Position.getNombreDePas());
                    // Arrête le timer lorsque l'apprenti atteint la position cible
                    if (parPositionApprenti.equals(parPositionCible)) {
                        timer.cancel();
                    }
                });
            }
        };
        // Planifie la tâche à effectuer à intervalle régulier avec un délai initial de 1000 ms et un intervalle de 200 ms
        timer.scheduleAtFixedRate(timerTask, 1000, 200);
    }


    /**
     * Renvoie l'étiquette pour afficher le nombre de pas.
     *
     * @return L'étiquette pour afficher le nombre de pas.
     */
    public Label getLabelNombreDePas() {
        return labelNombreDePas;
    }

}
