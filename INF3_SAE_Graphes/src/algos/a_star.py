from queue import PriorityQueue
from src.constantes import *


def calcul_estimation_couts_restants(coord_voisin, coord_fin):
    """
    Calcule l'heuristique (distance) entre une cellule et l'arrivée.

    Args:
        coord_voisin (tuple): Coordonnées (ligne, colonne) de la cellule actuelle.
        coord_fin (tuple): Coordonnées (ligne, colonne) de l'arrivée.

    Returns:
        float: Estimation du coût restant pour atteindre l'arrivée.
    """
    distance = math.sqrt((coord_voisin[0] - coord_fin[0])**2 + (coord_voisin[1] - coord_fin[1])**2)
    return distance * TYPE_DEFAUT


def run_a_star(grille, coord_depart, coord_fin, controleur):
    """
   L'algorithme utilise une file de priorité pour explorer les noeuds ayant le
    score `f = g + h` le plus bas, où `g` est le coût réel accumulé et `h`
    l'estimation heuristique.

    Args:
        grille (Grille): L'objet grille contenant les données des hexagones.
        coord_depart (tuple): Coordonnées (ligne, colonne) de départ.
        coord_fin (tuple): Coordonnées (ligne, colonne) d'arrivée.
        controleur (Controleur): Gère la reconstruction finale du chemin.

    Returns:
        - list: Le chemin solution (liste de coordonnées).
        - dict: Dictionnaire des noeuds parents {enfant: parent}.
        - dict: Dictionnaire des coûts cumulés {coordonnée: coût}.
    """
    if not coord_depart or not coord_fin:
        print("Erreur: coordonnées de départ ou arrivée manquantes")
        return [], {}, {}

    file_priorite = PriorityQueue()
    file_priorite.put((0, coord_depart))
    cellule_precedente = {coord_depart: None}
    cout = {coord_depart: 0}
    chemin_trouve = False

    while not file_priorite.empty():
        f_score, localisation = file_priorite.get()

        # si on arrive au point d'arrivée
        if localisation == coord_fin:
            chemin_trouve = True
            break

        # on récupère tous les voisins
        voisins = grille.recuperer_voisins(localisation[0], localisation[1])
        # on boucle sur les voisins pour comparer les coûts
        for voisin in voisins:
            hexagone_voisin = grille.recuperer_coord(voisin[0], voisin[1])
            # on évite les murs
            if hexagone_voisin and hexagone_voisin.type != TYPE_MUR:
                # calcul du coût selon le type de terrain
                cout_de_la_case = hexagone_voisin.type
                nouveau_cout = cout[localisation] + cout_de_la_case

                # si on trouve un chemin avec un cout plus bas
                if voisin not in cout or nouveau_cout < cout[voisin]:
                    cout[voisin] = nouveau_cout
                    cellule_precedente[voisin] = localisation
                    # f_score est la somme du cout actuel plus l'estimation des coûts restants jusqu'à l'arrrivée
                    f_score = nouveau_cout + calcul_estimation_couts_restants(voisin, coord_fin)
                    file_priorite.put((f_score, voisin))

    chemin_solution = controleur.reconstruction_chemin(chemin_trouve, coord_depart, coord_fin, cellule_precedente)

    return chemin_solution, cellule_precedente, cout
