package Exception;

public class PoseImpossibleException extends Exception{
    public PoseImpossibleException(String message){
        super(message);
    }

    public PoseImpossibleException(){
        super("Impossible de poser un pions ici");
    }
}
