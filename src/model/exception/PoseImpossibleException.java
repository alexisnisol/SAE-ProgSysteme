package model.exception;

/**
 * Cette exception est lancée lorsque l'action de poser un pion est impossible
 * dans le contexte du jeu. Elle est utilisée pour signaler des erreurs de placement,
 * comme essayer de poser un pion dans une case déjà occupée ou hors des limites du plateau.
 */
public class PoseImpossibleException extends Exception {

    /**
     * Constructeur de l'exception avec un message personnalisé.
     * 
     * @param message Le message d'erreur à afficher.
     */
    public PoseImpossibleException(String message) {
        super(message);
    }

    /**
     * Constructeur de l'exception avec un message par défaut.
     * Le message par défaut est "Impossible de poser un pion ici".
     */
    public PoseImpossibleException() {
        super("Impossible de poser un pion ici");
    }
}

