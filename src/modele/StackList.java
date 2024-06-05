package modele;

import java.util.Stack;
import modele.exception.PoseImpossibleException;


public class StackList<T> extends Stack<T>{

    private int taille;

    public StackList(int taille){
        super();
        this.taille = taille;
    }

    public void pushItem(T item) throws PoseImpossibleException{
        if (this.size() >= this.taille){
            throw new PoseImpossibleException();
        }
        super.push(item);
    }

    public boolean isFull(){
        return this.size() >= this.taille;
    }
}
