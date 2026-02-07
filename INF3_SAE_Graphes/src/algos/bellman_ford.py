from src.constantes import TYPE_MUR, TYPE_DEFAUT


def run_bellman_ford(grille, debut_coords, fin_coords, controleur):
    """
    Bellman-Ford sur une grille hexagonale :
        - Empêche la création de cycles (un hexagone ne peut être visité qu'une seule fois par chemin)
        - Gère les coûts négatifs
    Args:
        grille (Grille): L'objet contenant la structure de la carte.
        debut_coords (tuple): Coordonnées (ligne, colonne) de départ.
        fin_coords (tuple): Coordonnées (ligne, colonne) d'arrivée.
        controleur (Controleur): Gère la reconstruction du chemin.

    Returns:
        - list: Le chemin solution (coordonnées (ligne, colonne)).
        - dict: Dictionnaire des parents {enfant: parent}.
        - dict: Dictionnaire des coûts cumulés {case: coût}.
        - list: Liste de tuples (parent, enfant) définissant l'ordre d'affichage.
    """
    if not debut_coords or not fin_coords:
        print("Erreur : départ ou arrivée manquants")
        return [], {}, {}, []

    couts = {debut_coords: 0}  # coût pour atteindre chaque hexagone depuis le départ
    parents = {debut_coords: None}  # parent de chaque hexagone (None pour le départ)

    a_traiter = [debut_coords]  # liste des hexagones à explorer
    toutes_cases = set(a_traiter)  # ensemble de toutes les cases atteignables

    while a_traiter:
        case = a_traiter.pop(0)
        voisins = grille.recuperer_voisins(case[0], case[1])

        for v in voisins:
            hex_v = grille.recuperer_coord(v[0], v[1])
            # si le voisin existe, n'est pas un mur et n'a pas encore été ajouté
            if hex_v and hex_v.type != TYPE_MUR and v not in toutes_cases:
                toutes_cases.add(v)
                a_traiter.append(v)  # ajout pour exploration future

    # Initialisation des coûts et parents pour toutes les cases
    for case in toutes_cases:
        if case not in couts:
            couts[case] = float("inf")  # coût infini initial
            parents[case] = None  # pas encore de parent

    for _ in range(len(toutes_cases) - 1):
        modification = False  # pour détecter si une mise à jour a eu lieu

        for u in toutes_cases:
            if couts[u] == float("inf"):
                continue  # on ne peut pas partir d'une case non atteignable

            voisins = grille.recuperer_voisins(u[0], u[1])
            for v in voisins:
                hex_v = grille.recuperer_coord(v[0], v[1])
                if not hex_v or hex_v.type == TYPE_MUR:
                    continue  # on ignore les murs

                if contient_cycle(parents, u, v):
                    continue  # on empêche les cycles

                # calcul du coût cumulatif pour atteindre enfant depuis parent
                cout_case = hex_v.type if hex_v.type else TYPE_DEFAUT
                nouveau_cout = couts[u] + cout_case

                # mise à jour si on a trouvé un chemin moins coûteux
                if nouveau_cout < couts[v]:
                    couts[v] = nouveau_cout
                    parents[v] = u
                    modification = True

        if not modification:
            break  # si plus de maj, on s'arrete

    chemin_solution = controleur.reconstruction_chemin(
        couts[fin_coords] != float("inf"),
        debut_coords,
        fin_coords,
        parents
    )

    ordre = ordre_affichage_bellman_ford(debut_coords, parents)

    return chemin_solution, parents, couts, ordre


def contient_cycle(parents, parent, enfant):
    """
    Vérifie si ajouter 'enfant' comme enfant de 'parent' crée un cycle.
    (un hexagone ne peut être parcouru qu'une seule fois sur un chemin)

    Args:
        parents (dict): Dictionnaire des relations {hexagone: parent}.
        parent (tuple): Coordonnées (ligne, colonne) du parent potentiel.
        enfant (tuple): Coordonnées (ligne, colonne) du voisin à tester.

    Returns:
        bool: True si un cycle est détecté, False sinon.
    """
    actuel = parent
    while actuel is not None:
        if actuel == enfant:  # si on retrouve l'enfant dans la chaîne des parents => cycle
            return True
        actuel = parents.get(actuel)  # remonte d'un parent
    return False  # pas de cycle détecté


def ordre_affichage_bellman_ford(debut, parents):
    """
    Renvoie l'ordre dans lequel afficher les flèches pour Bellman-Ford.
    Utilise un parcours en largeur (BFS) depuis le départ pour que les flèches
    se propagent correctement.

    Args:
        debut (tuple): Coordonnées (ligne, colonne) de départ.
        parents (dict): Collection des relations finales {enfant: parent}.

    Returns:
        list: Liste de tuples ((ligne_p, col_p), (ligne_e, col_e)) ordonnée.
    """
    ordre = []
    vus = set([debut])
    file = [debut]

    while file:
        parent = file.pop(0)  # on prend le prochain hexagone à traiter
        for enfant, p in parents.items():
            # si l'hexagone enfant a pour parent parent et n'a pas encore été affiché
            if p == parent and enfant not in vus:
                ordre.append((parent, enfant))  # on ajoute la flèche parent -> enfant
                vus.add(enfant)
                file.append(enfant)  # on continue depuis enfant

    return ordre  # retourne la liste des flèches à afficher dans le bon ordre
