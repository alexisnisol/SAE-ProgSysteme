package com.model.exception;


public class PoseImpossibleException extends Exception{
    public PoseImpossibleException(String message){
        super(message);
    }

    public PoseImpossibleException(){
        super("Impossible de poser un pions ici");
    }
}
