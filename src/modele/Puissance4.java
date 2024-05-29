package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Puissance4{
    private List<Stack<Pions>> plateau;
    private int hauteur;
    private int largeur;

    /**
     * 
     * @param indice
     * @param joueur
     */
    public Puissance4(){
        this.plateau = new ArrayList<>();
        this.largeur = 7;
        this.hauteur = 6;
        this.initPlateau();
    }

    public Puissance4(int largeur, int hauteur){
        this.plateau = new ArrayList<>();
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.initPlateau();
        
    }

    private void initPlateau(){
        Stack<Pions> pile;
        for (int i = 0; i < this.largeur; ++i) {
            pile = new Stack<>();
            for (int j = 0; j < this.hauteur; j++) {
                pile.push(Pions.VIDE);
            }
            this.plateau.add(pile);
        }
    }

    public void poserPions(int indice, Pions joueur) throws PoseImpossibleException{
        this.plateau.get(indice).push(joueur);
    }

}