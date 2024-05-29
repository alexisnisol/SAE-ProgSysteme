import java.util.Stack;

public class StackList<T> extends Stack<T>{

    private int taille;

    public StackList(int taille){
        super();
        this.taille = taille;
    }

    @Override
    public T push(T item) {
        if (this.size() >= this.taille){
            return null;
        }
        return super.push(item);
    }
}
