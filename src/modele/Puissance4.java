package modele;

import java.util.ArrayList;
import java.util.List;

public class Puissance4{
    private List<StackList<Pions>> plateau;
    private int hauteur;
    private int largeur;

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

    /**
     * initialisation du plateau
     */
    private void initPlateau(){
        StackList<Pions> pile;
        for (int i = 0; i < this.largeur; ++i) {
            pile = new StackList<>(this.hauteur);
            for (int j = 0; j < this.hauteur; j++) {
                pile.push(Pions.VIDE);
            }
            this.plateau.add(pile);
        }
    }

    /**
     * Poser un pions, retourne si le joueur à gagné ou non
     * @param indice la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné, false sinon
     */
    public boolean poserPions(int indice, Pions joueur) {
        if(this.plateau.get(indice).pushItem(joueur)){
            return this.isWon(indice, joueur);
        }

    }

    public Pions getCase(int x, int y){
        return this.plateau.get(x).get(y);
    }

    public int getHauteur(){
        return this.hauteur;
    }

    public int getLargeur(){
        return this.largeur;
    }

    public List<StackList<Pions>> getPlateau(){
        return this.plateau;
    }

    public void reset(){
        this.initPlateau();
    }

    public boolean isFull(int indice){
        return this.plateau.get(indice).isFull();
    }

    public boolean matchNul(){
        for (int i = 0; i < this.largeur; i++) {
            if (!this.isFull(i)){
                return false;
            }
        }
        return true;
    }

    public boolean isWon(int lastPlayed, Pions joueur){
        if (this.isWonLigne(lastPlayed, joueur) || this.isWonColonne(lastPlayed, joueur) || this.isWonDiag(lastPlayed, joueur)) {
            return true;
        }
        return false;
    }

    public boolean isWonLigne(){

    }
}