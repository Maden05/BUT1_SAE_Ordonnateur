import tkinter as tk

from src.constantes import *
from src.gui.scroll_bar import ScrollBar


class ResultatBar:
    """Cette barre affiche les informations envoyées par le contrôleur après
    chaque exécution d'un algorithme. Elle utilise un système de défilement
    pour permettre la consultation de plusieurs résultats successifs.

    Attributs:
        controleur (Controleur): Référence vers le contrôleur pour la communication logique.
        cadre_principal (tk.Frame): Le conteneur parent à largeur fixe.
        scroll (ScrollBar): Système de défilement intégré à la barre.
        contenu_interne (tk.Frame): Zone située dans la scrollbar recevant les widgets.
        zone_resultats (tk.Frame): Sous-cadre spécifique contenant uniquement les labels de résultats.
    """
    def __init__(self, parent, controleur):
        self.controleur = controleur

        # Cadre principal à largeur fixe
        self.cadre_principal = tk.Frame(parent, bg=BARRES_COULEUR_FOND, width=LARGEUR_RESULTAT_BAR)
        self.cadre_principal.grid_propagate(False)

        # Scroll bar
        self.scroll = ScrollBar(
            self.cadre_principal,
            width=LARGEUR_RESULTAT_BAR,
            bg=BARRES_COULEUR_FOND
        )

        self.scroll.pack(fill="both", expand=True)

        self.contenu_interne = self.scroll.content

        # --- CONTENU ---
        tk.Label(
            self.contenu_interne,
            text="Résultats",
            font=("Arial", 14, "bold"),
            bg=BARRES_COULEUR_FOND
        ).pack(pady=10)

        tk.Button(
            self.contenu_interne,
            text="🗑️ Effacer",
            command=self.effacer_resultat,
            bg=ACTION_BAR_COULEUR_BOUTONS
        ).pack(pady=(0, 10))

        # Zone interne qui contiendra les labels
        self.zone_resultats = tk.Frame(
            self.contenu_interne,
            bg=BARRES_COULEUR_FOND
        )
        self.zone_resultats.pack(fill="both", expand=True)

    def afficher_resultat_bar(self):
        # sticky="ns" pour prendre toute la hauteur sans s'élargir
        self.cadre_principal.grid(row=0, column=2, sticky="ns")

    def afficher_resultat(self, algo, chemin_solution, cout):
        self.ajouter_label(f"{algo} :")
        if cout is not None:
            self.ajouter_label(f"La distance est de : {len(chemin_solution) - 1}")
            self.ajouter_label(f"Le cout est de : {cout}")
        else:
            self.ajouter_label("Aucun résultat n'est possible")
        self.ajouter_label("-------------------------------------")

    def ajouter_label(self, texte, gras=False):
        fnt = ("Arial", 11, "bold") if gras else ("Arial", 11)

        # wraplength force le texte à rester dans la largeur impartie
        lbl = tk.Label(
            self.zone_resultats,
            text=texte,
            font=fnt,
            bg=BARRES_COULEUR_FOND,
            wraplength=LARGEUR_RESULTAT_BAR - 20,
            justify="center"
        )
        lbl.pack(pady=2, fill="x")

    def effacer_resultat(self):
        for composant in self.zone_resultats.winfo_children():
            composant.destroy()