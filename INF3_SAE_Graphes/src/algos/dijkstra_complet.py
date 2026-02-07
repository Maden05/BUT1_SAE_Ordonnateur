import heapq

from src.constantes import TYPE_MUR


def run_dijkstra_complet(grille, debut_coords, fin_coords, controleur):
    """
    Contrairement à la version standard, cet algorithme ne s'arrête pas lorsqu'il
    atteint `fin_coords`. Il continue d'explorer toutes les arêtes de la grille
    pour garantir que le dictionnaire `cout_visites` contient le coût minimal
    vers chaque case atteignable.

    Args:
        grille (Grille): L'objet contenant la topologie de la carte.
        debut_coords (tuple): Coordonnées (ligne, colonne) de départ.
        fin_coords (tuple): Coordonnées (ligne, colonne) d'arrivée (utilisées pour la reconstruction).
        controleur (Controleur): Gère la reconstruction du chemin.

    Returns:
        - list: Le chemin solution de départ à fin (tuples (ligne, colonne)).
        - dict: Dictionnaire des parents {enfant: parent} pour toute la zone explorée.
        - dict: Dictionnaire des coûts cumulés {case: coût_total}.
    """
    if not debut_coords or not fin_coords:
        print("Erreur: coordonnées manquantes")
        return [], {}, {}

    # (coût, coordonnées)
    file_priorite = [(0, debut_coords)]
    cout_visites = {debut_coords: 0}
    cellule_precedente = {debut_coords: None}
    chemin_trouve = True

    while file_priorite:
        cout_actuel, localisation = heapq.heappop(file_priorite)

        # Si on a déjà trouvé un meilleur chemin, on ignore
        if cout_actuel > cout_visites.get(localisation, float("inf")):
            continue

        voisins = grille.recuperer_voisins(localisation[0], localisation[1])
        for voisin in voisins:
            hex_voisin = grille.recuperer_coord(voisin[0], voisin[1])
            if hex_voisin.type == TYPE_MUR:
                continue

            cout_case = hex_voisin.type
            nouveau_cout = cout_actuel + cout_case

            if voisin not in cout_visites or nouveau_cout < cout_visites[voisin]:
                cout_visites[voisin] = nouveau_cout
                cellule_precedente[voisin] = localisation
                heapq.heappush(file_priorite, (nouveau_cout, voisin))

    chemin_solution = controleur.reconstruction_chemin(chemin_trouve, debut_coords, fin_coords, cellule_precedente)

    return chemin_solution, cellule_precedente, cout_visites
