from src.constantes import TYPE_MUR, TYPE_DEFAUT


def run_parcours_largeur(grille, debut_coords, fin_coords, controleur):
    """
    L'algorithme utilise une file pour explorer les voisins immédiats avant de
    passer aux voisins de second niveau. Il s'arrête dès que la coordonnée de fin
    est extraite de la file.

    Args:
        grille (Grille): L'objet contenant la matrice et les données des hexagones.
        debut_coords (tuple): Coordonnées (ligne, colonne) de départ.
        fin_coords (tuple): Coordonnées (ligne, colonne) d'arrivée.
        controleur (Controleur): Gère la reconstruction du chemin.

    Returns:
        - list: Le chemin solution (coordonnées (ligne, colonne)).
        - dict: Dictionnaire des parents {enfant: parent}.
        - dict: Dictionnaire des coûts cumulés {case: distance_accumulée}.
    """
    if not debut_coords or not fin_coords:
        print("Erreur: coordonnées de départ ou arrivée manquantes")
        return [], {}, {}

    liste_des_extremites = [debut_coords]  # à pour but de stocker les extrémités des chemins
    cellule_precedente = {debut_coords: None}  # correspond donc à l'origine de la flèche et vaut None pour l'hexagone de départ
    cout_visites = {debut_coords: 0}  # correspond à un dictionnaire avec comme clé un hexagone et comme valeur la distance de sa découverte depuis l'origine
    chemin_trouve = False

    while liste_des_extremites:
        """
            La boucle vérifie si le parcours est toujours en cours et sert de condition d'arrêt.
            Pour simplifier à chaque étape on supprime l'extrémité en question puis nous rajoutons la nouvelle extrémité si elle existe
            Si l'arrivée n'est pas atteinte, la liste seras donc vide, ce qui permet de s'arreter, si l'arrivée est atteinte, 
            il y aura les extrémites des derniers chemins créer
        """
        localisation = liste_des_extremites.pop(0)
        if localisation == fin_coords:
            # si on arrive à l'arrivée, on le signale puis on sors de la boucle while
            chemin_trouve = True
            break

        voisins = grille.recuperer_voisins(localisation[0], localisation[1])

        for voisin in voisins:
            # Boucles sur l'ensemble des voisins de l'hexagone séléctionné
            hex_voisin = grille.recuperer_coord(voisin[0], voisin[1])
            # On vérifie si c'est ni un mur, ni un exagone déjà visité
            if hex_voisin and hex_voisin.type != TYPE_MUR and voisin not in cout_visites:

                cout_visites[voisin] = cout_visites[localisation] + hex_voisin.type
                cellule_precedente[voisin] = localisation
                liste_des_extremites.append(voisin)

    chemin_solution = controleur.reconstruction_chemin(chemin_trouve, debut_coords, fin_coords, cellule_precedente)

    return chemin_solution, cellule_precedente, cout_visites
