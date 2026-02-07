from src.constantes import TYPE_MUR, TYPE_DEFAUT


def run_parcours_profondeur(grille, debut_coords, fin_coords, controleur):
    """
    L'algorithme utilise une pile pour stocker les cellules à explorer.
    En retirant systématiquement le dernier élément ajouté,
    il progresse dans une direction jusqu'au bout avant d'explorer les autres options.

    Args:
        grille (Grille): L'objet contenant la structure et les types de cases.
        debut_coords (tuple): Coordonnées (ligne, colonne) de départ.
        fin_coords (tuple): Coordonnées (ligne, colonne) d'arrivée.
        controleur (Controleur): Instance responsable de la reconstruction du chemin.

    Returns:
        - list: Le chemin trouvé (liste de tuples (ligne, colonne)).
        - dict: Dictionnaire des parents {enfant: parent}.
        - dict: Dictionnaire des coûts cumulés {case: coût}.
    """

    if not debut_coords or not fin_coords:
        print("Erreur: coordonnées de départ ou arrivée manquantes")
        return [], {}, {}

    # pile (LIFO) / on ajoute les voisins de la case actuelle et on explore le dernier ajouté
    pile = [debut_coords]

    # pour reconstruire le chemin
    cellule_precedente = {debut_coords: None}

    # pour calculer le coût si besoin
    cout_visites = {debut_coords: 0}
    chemin_trouve = False


    # A B C / Départ : A → pile = [A]
    # D E F / Pop A → pile = []
    # G H I / Voisins de A = B, D (pas de murs, pas visités)
    #         Ajouter à pile → pile = [B, D]
    #         Pop D → pile = [B]
    #         Voisins de D = E, G (non visités) → ajouter à pile → pile = [B, E, G]
    #         Pop G → pile = [B, E] etc...

    # Tant que pile est pas vide, on prend la dernière case ajoutée pour aller profondément
    while pile:
        localisation = pile.pop()  # <-- ici LIFO

        # Si la case actuelle est l'arrivée, le chemin est trouvé, on sort de la boucle
        if localisation == fin_coords:
            chemin_trouve = True
            break

        # On récup tous les voisins de la case actuelle
        voisins = grille.recuperer_voisins(localisation[0], localisation[1])

        # Pour chaque voisin, on récup l'objet hexagone
        for voisin in voisins:
            hex_voisin = grille.recuperer_coord(voisin[0], voisin[1])

            # On ignore les voisins qui n'existent pas ou les murs
            if hex_voisin and hex_voisin.type != TYPE_MUR:
                cout_case = hex_voisin.type
                # cout cumulé avec la case actuelle pour arriver la case
                nouveau_cout = cout_visites[localisation] + cout_case

                # si le voisin n'a pas été visité, on stocke son cout cumulé
                if voisin not in cout_visites:  # DFS ne revisite pas
                    cout_visites[voisin] = nouveau_cout
                    cellule_precedente[voisin] = localisation
                    pile.append(voisin)  # on ajoute à la pile pour continuer en profondeur

    chemin_solution = controleur.reconstruction_chemin(chemin_trouve, debut_coords, fin_coords, cellule_precedente)

    return chemin_solution, cellule_precedente, cout_visites
