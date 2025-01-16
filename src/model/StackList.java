package model;

import java.util.Stack;
import model.exception.PoseImpossibleException;

/**
 * Classe représentant une pile (stack) avec une taille limitée.
 * Cette classe étend {@link Stack} et ajoute une contrainte sur la taille de la pile,
 * ainsi que des méthodes pour vérifier si la pile est pleine et pour ajouter des éléments avec gestion d'exception.
 *
 * @param <T> Le type des éléments stockés dans la pile.
 */
public class StackList<T> extends Stack<T> {

    // La taille maximale de la pile
    private int taille;

    /**
     * Constructeur de la pile avec une taille spécifiée.
     * 
     * @param taille La taille maximale de la pile.
     */
    public StackList(int taille) {
        super();
        this.taille = taille;
    }

    /**
     * Ajoute un élément à la pile.
     * Si la pile est pleine, une exception {@link PoseImpossibleException} est levée.
     * 
     * @param item L'élément à ajouter à la pile.
     * @throws PoseImpossibleException Si la pile est pleine et qu'un élément ne peut pas y être ajouté.
     */
    public void pushItem(T item) throws PoseImpossibleException {
        if (this.size() >= this.taille) {
            throw new PoseImpossibleException();
        }
        super.push(item);
    }

    /**
     * Vérifie si la pile est pleine.
     * 
     * @return {@code true} si la pile est pleine, sinon {@code false}.
     */
    public boolean isFull() {
        return this.size() >= this.taille;
    }
}
