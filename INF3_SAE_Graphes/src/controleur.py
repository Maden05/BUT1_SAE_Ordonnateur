from tkinter import DoubleVar

from src.constantes import DELAI_VAGUE, DELAI_CHEMIN, NOMBRE_TERRAIN_MAXIMUM

class Controleur:
    def __init__(self, root):
        self.root = root
        self.mode = None
        self.grille = None
        self.action_bar = None
        self.resultat_bar = None

        self.vitesse_tous_chemins = DoubleVar()
        self.vitesse_tous_chemins.set(DELAI_VAGUE)

        self.vitesse_chemin_final = DoubleVar()
        self.vitesse_chemin_final.set(DELAI_CHEMIN)

        self.generation_aleatoire_nombre_mur = DoubleVar()
        self.generation_aleatoire_nombre_foret = DoubleVar()
        self.generation_aleatoire_nombre_sable = DoubleVar()
        self.generation_aleatoire_nombre_eau = DoubleVar()

        self.generation_aleatoire_nombre_mur.set(NOMBRE_TERRAIN_MAXIMUM // 2)
        self.generation_aleatoire_nombre_foret.set(NOMBRE_TERRAIN_MAXIMUM // 2)
        self.generation_aleatoire_nombre_sable.set(NOMBRE_TERRAIN_MAXIMUM // 2)
        self.generation_aleatoire_nombre_eau.set(NOMBRE_TERRAIN_MAXIMUM // 2)

    def set_grille(self, grille):
        self.grille = grille

    def set_action_bar(self, action_bar):
        self.action_bar = action_bar

    def set_resultat_bar(self, resultat_bar):
        self.resultat_bar = resultat_bar

    def afficher_resultat(self, algo, chemin_solution, cout):
        if self.resultat_bar:
            self.resultat_bar.afficher_resultat(algo, chemin_solution, cout)

    def ajouter_label_resultat_bar(self, texte):
        if self.resultat_bar:
            self.resultat_bar.ajouter_label(texte)
            self.resultat_bar.ajouter_label("-------------------------------------")

    def lancer_algo(self, algo):
        if not self.grille:
            print("Erreur : grille non initialisée")
            return

        debut = self.grille.point_de_depart
        arrivee = self.grille.point_arrivee

        if not debut or not arrivee:
            print("Erreur : départ ou arrivée non défini")
            return

        print("Lancement de l'algo:", algo)
        print("Départ:", debut, "Arrivée:", arrivee)

        self.grille.lancer_algo_visu(algo)

    def reconstruction_chemin(self, chemin_trouve, debut_coords, fin_coords, cellule_precedente):
        chemin_solution = []
        if chemin_trouve:
            actuel = fin_coords
            while actuel is not None:
                chemin_solution.append(actuel)
                actuel = cellule_precedente.get(actuel)
            chemin_solution.reverse()
            print("Chemin trouvé")
        else:
            print("Aucun chemin trouvé entre", debut_coords, "et", fin_coords)
        return chemin_solution
