from src.constantes import TYPE_MUR, TYPE_DEFAUT
import heapq  # pour la priority queue


def run_dijkstra(grille, debut_coords, fin_coords, controleur):
    """
    L'algorithme utilise une file de priorité (min-heap) pour toujours explorer
    l'hexagone ayant le coût cumulé le plus bas. Contrairement à A*, il n'utilise
    pas d'heuristique et explore de manière circulaire autour du départ.

    Args:
        grille (Grille): L'objet représentant la carte et ses obstacles.
        debut_coords (tuple): Coordonnées (ligne, colonne) du point de départ.
        fin_coords (tuple): Coordonnées (ligne, colonne) du point d'arrivée.
        controleur (Controleur): Objet responsable de la reconstruction du chemin final.

    Returns:
        - list: Le chemin optimal (liste de tuples (ligne, colonne)).
        - dict: Les relations de parenté {enfant: parent} pour reconstruire le parcours.
        - dict: Les coûts minimaux trouvés pour chaque case {coordonnees: coût}.
    """

    if not debut_coords or not fin_coords:
        print("Erreur: coordonnées de départ ou arrivée manquantes")
        return [], {}, {}

    # file de priorité : (coût_cumulé, (ligne, colonne))
    file_priorite = [(0, debut_coords)]
    cellule_precedente = {debut_coords: None}

    # Dictionnaire avec comme clé les coordonnée d'un exagone et comme valeur son cout d'exploration depuis l'origine
    cout_visites = {debut_coords: 0}
    chemin_trouve = False

    # On regarde si il y a encore des chemins en cours de découverte (extrémités qui ont encore des voisins non explorés)
    while file_priorite:
        # On récupère le cout et la localisation de l'hexagone avec des voisins non découverts et de plus faible coût
        cout_actuel, localisation = heapq.heappop(file_priorite)

        if localisation == fin_coords:
            chemin_trouve = True
            break

        voisins = grille.recuperer_voisins(localisation[0], localisation[1])
        for voisin in voisins:
            voisins_hexagone = grille.recuperer_coord(voisin[0], voisin[1])
            if voisins_hexagone.type != TYPE_MUR:
                cout_case = voisins_hexagone.type # Le cout d'accès à l'hexagone
                nouveau_cout = cout_visites[localisation] + cout_case # Le cout du parcours jusqu'à la case

                if voisin not in cout_visites or nouveau_cout < cout_visites[voisin]:
                    cout_visites[voisin] = nouveau_cout
                    cellule_precedente[voisin] = localisation
                    heapq.heappush(file_priorite, (nouveau_cout, voisin))

    chemin_solution = controleur.reconstruction_chemin(chemin_trouve, debut_coords, fin_coords, cellule_precedente)

    return chemin_solution, cellule_precedente, cout_visites
