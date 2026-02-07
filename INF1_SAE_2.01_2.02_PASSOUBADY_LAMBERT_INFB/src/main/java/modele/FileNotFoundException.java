package modele;

/**
 * Une exception pour la gestion des fichiers non trouvés.
 */
public class FileNotFoundException extends Exception{
    /**
     * Code d'erreur associé à l'exception
     * */
    private int chCodeErreur;

    /**
     * Constructeur de l'exception avec un code d'erreur spécifié.
     *
     * @param parCodeErreur Le code d'erreur associé à l'exception.
     */
    public FileNotFoundException (int parCodeErreur){
        chCodeErreur = parCodeErreur;
    }

}
