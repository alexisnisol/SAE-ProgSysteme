package modele;

import java.util.Stack;

public class StackList<T> extends Stack<T>{

    private int taille;

    public StackList(int taille){
        super();
        this.taille = taille;
    }

    public boolean pushItem(T item) {
        if (this.size() >= this.taille){
            return false;
        }
        super.push(item);
        return true;
    }
}
