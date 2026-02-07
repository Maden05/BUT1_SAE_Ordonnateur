import tkinter as tk
from src.constantes import *
from src.controleur import Controleur
from src.gui.grille import Grille
from src.gui.action_bar import ActionBar
from src.gui.resultat_bar import ResultatBar


def main():
    root = tk.Tk()
    root.title("Algorithmes de graphes")
    root.state("zoomed")

    # CONFIGURATION DU RESPONSIVE
    root.grid_rowconfigure(0, weight=1)

    # Colonne 0 (ActionBar) : Taille fixe
    root.grid_columnconfigure(0, weight=0)
    # Colonne 1 (Grille) : S'étire pour prendre TOUT l'espace
    root.grid_columnconfigure(1, weight=1)
    # Colonne 2 (ResultatBar) : Taille fixe (ne bougera pas)
    root.grid_columnconfigure(2, weight=0)

    controleur = Controleur(root)

    canvas = tk.Canvas(
        root,
        bg=COULEUR_FOND,
        highlightthickness=0
    )
    canvas.grid(row=0, column=1, sticky="nsew")

    grille = Grille(canvas, controleur)
    controleur.set_grille(grille)

    action_bar = ActionBar(root, controleur, grille)
    action_bar.afficher_action_bar()
    controleur.set_action_bar(action_bar)

    resultat_bar = ResultatBar(root, controleur)
    resultat_bar.afficher_resultat_bar()
    controleur.set_resultat_bar(resultat_bar)

    root.mainloop()

if __name__ == "__main__":
    main()
