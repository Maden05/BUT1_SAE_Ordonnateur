from src.constantes import *


class Hexagone:
    def __init__(self, lig, c, x, y):
        self.ligne = lig  # index ligne
        self.colonne = c  # index colonne
        self.x = x  # pixel X
        self.y = y  # pixel Y
        self.id = None  # ID Canvas
        self.type = TYPE_DEFAUT  # poids / type de terrain
        self.couleur = COULEUR_DEFAUT  # couleur affichage

    def position(self):
        return self.ligne, self.colonne


# Permet de verifier s'il existe un hexagone à la position ligne, colonne
def position_vers_hexagone(liste_hexagones, ligne, colonne):
    for hexagone in liste_hexagones:
        if hexagone.ligne == ligne and hexagone.colonne == colonne:
            return hexagone
    return None


# Vérifie si de la glace est présente sur la grille
def verifie_glace(liste_hexagones):
    for hexagone in liste_hexagones:
        if hexagone.type == TYPE_GLACE:
            return True
    return False


# Permet de verifier s'il existe un hexagone avec l'id donné
# Utilisé on_drag
def hexagone_id_vers_objet(liste_hexagone, id_hexagone: int):
    for hexa in liste_hexagone:
        if hexa.id == id_hexagone:
            return hexa
    return None


# Renvoie le pixel x et y de l'hexagone s'il existe
def hexagone_id_vers_coord(liste_hexagone, id_hexagone):
    hexa = hexagone_id_vers_objet(liste_hexagone, id_hexagone)
    if hexa:
        return hexa.x, hexa.y
    return None


# Renvoie une liste des id de tous les hexagones
# Utilisé on_drag
def hexagones_id(liste_hexagones):
    return [hexa.id for hexa in liste_hexagones]


# Renvoie l'hexagone si le dernier hexagone de la liste match avec la couleur par defaut.
# Permet de n'avoir qu'un seul début
def trouver_hexagone_dernier_debut(liste_hexagone):
    for hexa in reversed(liste_hexagone):
        if hexa.couleur == COULEUR_DEPART:
            return hexa
    return None


# Renvoie l'hexagone si le dernier hexagone de la liste match avec la couleur de l'arivée (rouge).
# Permet de n'avoir qu'une seule arrivée
def trouver_hexagone_dernier_arrivee(liste_hexagone):
    for hexa in reversed(liste_hexagone):
        if hexa.couleur == COULEUR_ARRIVEE:
            return hexa
    return None
