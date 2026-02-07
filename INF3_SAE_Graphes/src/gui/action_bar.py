import tkinter as tk

from src.constantes import *
from src.gui.scroll_bar import ScrollBar


class ActionBar:
    """
    Cette classe crée un panneau latéral fixe contenant des widgets
     permettant à l'utilisateur de modifier la grille,
    de choisir un mode d'édition ou de lancer des algorithmes.

    Attributs:
        controleur (Controleur): Référence vers le contrôleur logique de l'application.
        grille (Grille): Référence vers l'objet gérant l'affichage des hexagones.
        cadre_principal (tk.Frame): Conteneur parent de la barre d'action.
        scroll (ScrollBar): Système de défilement pour accéder à tous les outils.
        contenu_interne (tk.Frame): Cadre situé à l'intérieur de la scrollbar où sont placés les widgets.
        mode (tk.StringVar): Variable Tkinter stockant le mode d'édition actuel (ex: "mur", "debut").
    """

    def __init__(self, parent, controleur, grille):
        self.controleur = controleur
        self.grille = grille

        # Conteneur principal fixe (ne bouge pas lors de l'ajout de widgets)
        self.cadre_principal = tk.Frame(parent, bg=BARRES_COULEUR_FOND, width=LARGEUR_ACTION_BAR)
        self.cadre_principal.grid_propagate(False)

        # Scroll bar
        self.scroll = ScrollBar(
            self.cadre_principal,
            width=LARGEUR_ACTION_BAR,
            bg=BARRES_COULEUR_FOND
        )

        self.scroll.pack(fill="both", expand=True)

        self.contenu_interne = self.scroll.content

        # --- CONTENU ---
        self.mode = tk.StringVar(value="debut")
        controleur.mode = self.mode

        # --- OUTILS ---
        tk.Label(self.contenu_interne, text="Outils", font=("Arial", 14, "bold"), bg=BARRES_COULEUR_FOND).pack(pady=10)

        self.ajouter_bouton_commande("Carte aleatoire", self.grille.dessiner_aleatoirement)

        self.ajouter_scale("de mur aléatoire", controleur.generation_aleatoire_nombre_mur)
        self.ajouter_scale("de foret aléatoire", controleur.generation_aleatoire_nombre_foret)
        self.ajouter_scale("de sable aléatoire", controleur.generation_aleatoire_nombre_sable)
        self.ajouter_scale("d'eau aléatoire", controleur.generation_aleatoire_nombre_eau)

        self.ajouter_bouton_commande("Tout effacer", self.grille.dessiner_grille)
        self.ajouter_bouton_commande("Effacer flèches", lambda: self.grille.canvas.delete("ui_fleche"))
        self.ajouter_bouton_mode("Gomme", "gomme")

        # --- TERRAINS ---
        tk.Label(self.contenu_interne, text="Terrains", font=("Arial", 14, "bold"), bg=BARRES_COULEUR_FOND).pack(pady=10)
        self.ajouter_bouton_mode("Point de départ", "debut")
        self.ajouter_bouton_mode("Point d'arrivée", "fin")
        self.ajouter_bouton_mode("Barrière", "mur")
        self.ajouter_bouton_mode("Foret (3)", "foret")
        self.ajouter_bouton_mode("Sable (5)", "sable")
        self.ajouter_bouton_mode("Eau (10)", "eau")
        self.ajouter_bouton_mode("Glace (-1)", "glace")

        # --- ALGORITHMES ---
        tk.Label(self.contenu_interne, text="Algorithmes", font=("Arial", 14, "bold"), bg=BARRES_COULEUR_FOND).pack(pady=10)
        self.ajouter_bouton_commande("Parcours en largeur", lambda: self.controleur.lancer_algo("Parcours en largeur"))
        self.ajouter_bouton_commande("Dijkstra", lambda: self.controleur.lancer_algo("Dijkstra"))
        self.ajouter_bouton_commande("Dijkstra Complet", lambda: self.controleur.lancer_algo("Dijkstra complet"))
        self.ajouter_bouton_commande("Parcours en profondeur",lambda: self.controleur.lancer_algo("Parcours en profondeur"))
        self.ajouter_bouton_commande("A Star", lambda: self.controleur.lancer_algo("A Star"))
        self.ajouter_bouton_commande("Bellman-Ford", lambda: self.controleur.lancer_algo("Bellman-Ford"))


        # --- OPTIONS ---
        tk.Label(self.contenu_interne, text="Options", font=("Arial", 14, "bold"), bg=BARRES_COULEUR_FOND).pack(pady=10)
        tk.Scale(self.contenu_interne, variable=controleur.vitesse_tous_chemins, from_=1, to=int(DELAI_VAGUE * 2), length=180,
                 orient="horizontal", label="Vitesse exploration", bg=ACTION_BAR_COULEUR_BOUTONS).pack()
        tk.Scale(self.contenu_interne, variable=controleur.vitesse_chemin_final, from_=1, to=(DELAI_VAGUE * 2), length=180,
                 orient="horizontal", label="Vitesse final", bg=ACTION_BAR_COULEUR_BOUTONS).pack()

    def afficher_action_bar(self):
        self.cadre_principal.grid(row=0, column=0, sticky="ns")

    def ajouter_scale(self, texte, nombre_element):
        tk.Scale(
            self.contenu_interne, variable=nombre_element, from_=0, to=NOMBRE_TERRAIN_MAXIMUM, length=200,
            showvalue=False, orient="horizontal", label=f"Proportion {texte}", bg=ACTION_BAR_COULEUR_BOUTONS).pack()

    def ajouter_bouton_mode(self, texte, mode):
        tk.Radiobutton(self.contenu_interne, text=texte, variable=self.mode, value=mode,
                       indicatoron=False, width=22, pady=5, bg=ACTION_BAR_COULEUR_BOUTONS).pack(pady=2)

    def ajouter_bouton_commande(self, texte, commande):
        tk.Button(self.contenu_interne, text=texte, command=commande, width=22, pady=5, bg=ACTION_BAR_COULEUR_BOUTONS).pack(pady=2)