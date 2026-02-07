import random
import time
import tkinter as tk

from src.algos.a_star import run_a_star
from src.algos.bellman_ford import run_bellman_ford
from src.algos.dijkstra import run_dijkstra
from src.algos.dijkstra_complet import run_dijkstra_complet
from src.algos.largeur import run_parcours_largeur
from src.algos.profondeur import run_parcours_profondeur
from src.model.hexagone import *


class Grille:
    """
    Classe visuelle et logique de la grille d'hexagones.

    Cette classe est responsable du dessin du canevas, de la gestion du redimensionnement
    dynamique, du placement des terrains et de la visualisation étape par étape
    des algorithmes (propagation des flèches).

    Attributs:
        canvas (tk.Canvas): Surface de dessin Tkinter.
        controleur (Controleur): Gère la logique globale.
        hexagones (list): Liste d'objets Hexagone présents sur la grille.
        point_de_depart (tuple): Coordonnées (ligne, colonne) de l'origine.
        point_arrivee (tuple): Coordonnées (ligne, colonne) de l'arrivée.
        nombre_colonnes (int): Largeur de la grille en nombre d'hexagones.
        nombre_lignes (int): Hauteur de la grille en nombre d'hexagones.
    """

    def __init__(self, canvas, controleur):
        self.canvas = canvas
        self.controleur = controleur
        self.hexagones = []  # liste des hexagones
        self.point_de_depart = None
        self.point_arrivee = None

        self.nombre_colonnes = 0
        self.nombre_lignes = 0

        # On attend que le canvas soit affiché pour calculer la taille initiale
        self.canvas.bind("<Configure>", self.redimensionner)

    def redimensionner(self, event):
        """
        Recalcule les dimensions de la grille lors d'un changement de taille de fenêtre.

        Args:
            event (tk.Event): L'événement contenant les nouvelles largeur et hauteur.
        """
        # On ne redessine que si la taille change vraiment
        nouvelle_largeur = event.width
        nouvelle_hauteur = event.height

        # Calcul du nombre de colonnes et lignes possibles
        # Formule basée sur x = margin + col * (1.5 * HEX_COTE)
        self.controleur.grille.nombre_colonnes = int((nouvelle_largeur - 40) / (1.5 * HEX_COTE))
        self.controleur.nombre_lignes = int((nouvelle_hauteur - 40) / HEX_HAUTEUR)

        self.dessiner_grille()

    def dessiner_grille(self):
        """
        Initialise visuellement la grille sur le canvas.

        Efface les dessins précédents et crée les polygones pour chaque hexagone
        en appliquant le décalage spécifique aux colonnes impaires.
        """
        self.canvas.delete("all")
        self.hexagones.clear()
        self.point_de_depart = None
        self.point_arrivee = None

        for colonne in range(self.controleur.grille.nombre_colonnes):
            decalage = (HEX_HAUTEUR / 2) if colonne % 2 == 1 else 0
            for ligne in range(self.controleur.nombre_lignes):
                # On centre légèrement avec un offset de 20px
                x = 50 + colonne * (1.5 * HEX_COTE)
                y = 35 + ligne * HEX_HAUTEUR + decalage

                hexa = Hexagone(ligne, colonne, x, y)
                points = self.get_hex_points(x, y)

                hexa_id = self.canvas.create_polygon(
                    points, fill=GRILLE_COULEUR_FOND, outline="gray", width=1
                )
                hexa.id = hexa_id
                self.hexagones.append(hexa)

                # Bind click & drag
                self.canvas.tag_bind(hexa_id, "<Button-1>", lambda e, c=hexa_id: self.on_click(c))
                self.canvas.tag_bind(hexa_id, "<B1-Motion>", self.on_drag)

    def dessiner_aleatoirement(self):
        """Génère un terrain aléatoire basé sur les proportions définies dans l'ActionBar."""
        self.dessiner_grille()
        nombre_mur = int(self.controleur.generation_aleatoire_nombre_mur.get())
        nombre_foret = int(self.controleur.generation_aleatoire_nombre_foret.get())
        nombre_sable = int(self.controleur.generation_aleatoire_nombre_sable.get())
        nombre_eau = int(self.controleur.generation_aleatoire_nombre_eau.get())

        nom_vers_nombre_terrain = {
            "mur": nombre_mur,
            "foret": nombre_foret,
            "sable": nombre_sable,
            "eau": nombre_eau
        }

        while sum(nom_vers_nombre_terrain.values()) != 0:
            type_choisi_aleatoirement = random.choice(["mur", "foret", "sable", "eau"])
            if nom_vers_nombre_terrain[type_choisi_aleatoirement] != 0:
                hexagone_aleatoire = random.choice(self.hexagones)
                self.changer_mode(hexagone_aleatoire, type_choisi_aleatoirement, True)
                nom_vers_nombre_terrain[type_choisi_aleatoirement] -= 1

    def get_hex_points(self, x, y):
        """
        Calcule les 6 sommets d'un hexagone régulier (pointe vers le haut).

        Args:
            x (float): Coordonnée X du centre.
            y (float): Coordonnée Y du centre.

        Returns:
            list: Liste de tuples (x, y) pour les sommets.
        """
        return [
            (x + HEX_COTE * math.cos(math.radians(60 * i)),
             y + HEX_COTE * math.sin(math.radians(60 * i)))
            for i in range(6)
        ]

    def on_click(self, cell_id):
        """Gère le clic simple sur un hexagone."""
        hexagone = hexagone_id_vers_objet(self.hexagones, cell_id)
        if hexagone:
            mode = self.controleur.mode.get()
            self.changer_mode(hexagone, mode)

    def on_drag(self, event):
        items = self.canvas.find_overlapping(event.x, event.y, event.x, event.y)
        for item in items:
            if item in hexagones_id(self.hexagones):
                hexagone = hexagone_id_vers_objet(self.hexagones, item)
                mode = self.controleur.mode.get()
                self.changer_mode(hexagone, mode)

    def changer_mode(self, hexagone, mode, remplacer_mur=False):
        """
        Modifie l'état d'un hexagone (type de terrain, départ, arrivée ou gomme).

        Args:
            hexagone (Hexagone): L'objet hexagone à modifier.
            mode (str): Le type de terrain ou d'action ("mur", "debut", etc.).
            remplacer_mur (bool): Si True, permet d'écraser un mur existant.
        """
        if not hexagone:
            return
        if hexagone.couleur in [COULEUR_DEPART, COULEUR_ARRIVEE] and mode != "gomme":
            return

        if mode == "debut":
            ancien = trouver_hexagone_dernier_debut(self.hexagones)
            if ancien:
                self.canvas.itemconfig(ancien.id, fill=GRILLE_COULEUR_FOND)
                ancien.couleur = TYPE_DEFAUT

            hexagone.couleur = COULEUR_DEPART
            hexagone.type = TYPE_DEFAUT
            self.canvas.itemconfig(hexagone.id, fill=COULEUR_DEPART)
            self.point_de_depart = (hexagone.ligne, hexagone.colonne)

        elif mode == "fin":
            ancien = trouver_hexagone_dernier_arrivee(self.hexagones)
            if ancien:
                self.canvas.itemconfig(ancien.id, fill=GRILLE_COULEUR_FOND)
                ancien.couleur = TYPE_DEFAUT

            hexagone.couleur = COULEUR_ARRIVEE
            hexagone.type = TYPE_DEFAUT
            self.canvas.itemconfig(hexagone.id, fill=COULEUR_ARRIVEE)
            self.point_arrivee = (hexagone.ligne, hexagone.colonne)

        elif mode in ["mur", "eau", "sable", "foret", "glace"]:
            if remplacer_mur == False and hexagone.type == TYPE_MUR:
                return
            else:
                type_map = {
                    "mur": TYPE_MUR,
                    "eau": TYPE_EAU,
                    "sable": TYPE_SABLE,
                    "foret": TYPE_FORET,
                    "glace": TYPE_GLACE
                }
                color_map = {
                    "mur": COULEUR_MUR,
                    "eau": COULEUR_EAU,
                    "sable": COULEUR_SABLE,
                    "foret": COULEUR_FORET,
                    "glace": COULEUR_GLACE
                }
                hexagone.type = type_map[mode]
                hexagone.couleur = TYPE_DEFAUT
                self.canvas.itemconfig(hexagone.id, fill=color_map[mode])

        elif mode == "gomme":
            hexagone.type = TYPE_DEFAUT
            hexagone.couleur = TYPE_DEFAUT
            self.canvas.itemconfig(hexagone.id, fill=GRILLE_COULEUR_FOND)

    def recuperer_voisins(self, ligne, col):
        """
        Identifie les voisins d'un hexagone.

        Args:
            ligne (int): Index de la ligne.
            col (int): Index de la colonne.

        Returns:
            list: Liste de tuples (ligne, colonne) des voisins valides.
        """
        directions = [(0, -1), (0, 1), (-1, 0), (1, 0)]
        if col % 2 == 0:
            directions += [(-1, -1), (-1, 1)]
        else:
            directions += [(1, -1), (1, 1)]

        voisins = []
        for dl, dc in directions:
            voisin_l, voisin_c = ligne + dl, col + dc
            if 0 <= voisin_l < self.controleur.nombre_lignes and 0 <= voisin_c < self.controleur.grille.nombre_colonnes:
                voisins.append((voisin_l, voisin_c))
        return voisins

    def recuperer_coord(self, ligne, colonne):
        """Récupère l'objet Hexagone correspondant aux coordonnées logiques."""
        for hexa in self.hexagones:
            if hexa.ligne == ligne and hexa.colonne == colonne:
                return hexa
        return None

    def lancer_algo_visu(self, algo):
        """
        Lancement de l'algorithme choisi et l'animation du résultat.

        Gère les restrictions (ex: glace), appelle la fonction de calcul,
        puis dessine les flèches d'exploration et le chemin final.

        Args:
            algo (str): Nom de l'algorithme à exécuter.
        """
        if not self.point_de_depart or not self.point_arrivee:
            print("Départ ou arrivée non définis")
            return
        self.canvas.delete("ui_fleche")

        # On vérifie la présence de la grille pour pas que les algos crashent
        presence_glace = verifie_glace(self.hexagones)

        if algo == "Parcours en largeur":
            chemin, parents, couts = run_parcours_largeur(
                self, self.point_de_depart, self.point_arrivee, self.controleur)
            self.controleur.afficher_resultat(algo, chemin, couts.get(self.point_arrivee))

        elif algo == "Dijkstra":
            if not presence_glace:
                chemin, parents, couts = run_dijkstra(
                    self, self.point_de_depart, self.point_arrivee, self.controleur)
                self.controleur.afficher_resultat(algo, chemin, couts.get(self.point_arrivee))
            else:
                self.controleur.ajouter_label_resultat_bar(
                    "Tant que de la glace est présente sur la grille, cet algorithme ne peut pas se lancer")

        elif algo == "Dijkstra complet":
            if not presence_glace:
                chemin, parents, couts = run_dijkstra_complet(
                    self, self.point_de_depart, self.point_arrivee, self.controleur)
                self.controleur.afficher_resultat(algo, chemin, couts.get(self.point_arrivee))
            else:
                self.controleur.ajouter_label_resultat_bar(
                    "Tant que de la glace est présente sur la grille, cet algorithme ne peut pas se lancer")

        elif algo == "Parcours en profondeur":
            if not presence_glace:
                chemin, parents, couts = run_parcours_profondeur(
                    self, self.point_de_depart, self.point_arrivee, self.controleur)
                self.controleur.afficher_resultat(algo, chemin, couts.get(self.point_arrivee))
            else:
                self.controleur.ajouter_label_resultat_bar(
                    "Tant que de la glace est présente sur la grille, cet algorithme ne peut pas se lancer")

        elif algo == "A Star":
            if not presence_glace:
                chemin, parents, couts = run_a_star(
                    self, self.point_de_depart, self.point_arrivee, self.controleur)
                self.controleur.afficher_resultat(algo, chemin, couts.get(self.point_arrivee))
            else:
                self.controleur.ajouter_label_resultat_bar(
                    "Tant que de la glace est présente sur la grille, cet algorithme ne peut pas se lancer")

        elif algo == "Bellman-Ford":
            # Bellman-Ford retourne désormais 4 valeurs : chemin, parents, couts, ordre
            chemin, parents, couts, ordre = run_bellman_ford(
                self, self.point_de_depart, self.point_arrivee, self.controleur)
            self.controleur.afficher_resultat(algo, chemin, couts.get(self.point_arrivee))

        else:
            print("Algo non supporté")
            return

        if algo == "Bellman-Ford":
            for parent, node in ordre:
                self.dessiner_fleche(
                    parent,
                    node,
                    "ui_fleche",
                    cout=couts[node]
                )
                self.canvas.update()
                time.sleep(self.controleur.vitesse_tous_chemins.get() ** -1)

        else:
            for node, parent in parents.items():
                if parent:
                    self.dessiner_fleche(
                        parent,
                        node,
                        "ui_fleche",
                        cout=couts[node]
                    )
                    self.canvas.update()
                    time.sleep(self.controleur.vitesse_tous_chemins.get() ** -1)

        if chemin:
            for i in range(len(chemin) - 1):
                self.dessiner_fleche(
                    chemin[i],
                    chemin[i + 1],
                    "ui_fleche",
                    cout=couts[chemin[i + 1]],
                    color=COULEUR_CHEMIN_FINAL
                )
                self.canvas.update()
                time.sleep(self.controleur.vitesse_chemin_final.get() ** -1)

            cout_final = couts.get(self.point_arrivee)
            if cout_final is not None:
                print("Coût total du chemin :", cout_final)
            else:
                print("L'arrivée n'a pas été atteinte")

        else:
            print("Aucun chemin jusqu'à l'arrivée, exploration affichée")

    def dessiner_fleche(self, p1_coords, p2_coords, tag, cout=None, color=COULEUR_FLECHE):
        """
        Dessine une flèche directionnelle entre deux hexagones sur le canvas.

        Args:
            p1_coords (tuple): Tuple (ligne, colonne) de l'hexagone "parent" (celui d'où part la flèche).
            p2_coords (tuple): Tuple (ligne, colonne) de l'hexagone "enfant" (celui vers lequel pointe la flèche).
            tag (str): Tag Tkinter pour identification/suppression.
            cout (int, optional): Valeur du coût à afficher sur la flèche.
            color (str): Couleur de la ligne.
        """
        hexa1 = position_vers_hexagone(self.hexagones, p1_coords[0], p1_coords[1])
        hexa2 = position_vers_hexagone(self.hexagones, p2_coords[0], p2_coords[1])
        if not hexa1 or not hexa2:
            print("Erreur dessin fleche :", p1_coords, p2_coords)
            return

        self.canvas.create_line(
            hexa1.x, hexa1.y, hexa2.x, hexa2.y,
            arrow=tk.LAST, fill=color, width=2, tags=tag
        )

        if cout is not None:
            if hexa1.x == hexa2.x:
                mx = (hexa1.x + hexa2.x) / 2 - 10
                my = (hexa1.y + hexa2.y) / 2
            else:
                mx = (hexa1.x + hexa2.x) / 2
                my = (hexa1.y + hexa2.y) / 2 - 10
            self.canvas.create_text(mx, my, text=str(cout), fill=color, font=("Arial", 10, "bold"), tags=tag)
