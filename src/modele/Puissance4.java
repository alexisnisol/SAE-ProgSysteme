package modele;

import java.util.ArrayList;
import java.util.List;
import modele.exception.PoseImpossibleException;

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

    public enum Status{
        CONTINUER,
        GAGNE,
        NULL;
    }

    /**
     * initialisation du plateau
     */
    private void initPlateau(){
        StackList<Pions> pile;
        for (int i = 0; i < this.largeur; ++i) {
            pile = new StackList<>(this.hauteur);
            this.plateau.add(pile);
        }
    }

    /**
     * Poser un pions, retourne si le joueur à gagné ou non
     * @param indice la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné, false sinon
     */
    public Status poserPions(int indice, Pions joueur) throws PoseImpossibleException{
        try {
            this.plateau.get(indice).pushItem(joueur);
            if (this.matchNul()){
                return Status.NULL;
            }
            return this.isWon(indice, joueur); // continuer ou est gagné
        } catch (PoseImpossibleException e) {
            throw e;
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

    /**
     * Réinitialise le plateau
     */
    public void reset(){
        this.initPlateau();
    }

    /**
     * vérifie si la colonne est pleine
     * @param indice l'indice de la colonne
     * @return true si la colonne est pleine, false sinon
     */
    public boolean isFull(int indice){
        return this.plateau.get(indice).isFull();
    }

    /**
     * vérifie si le plateau est plein
     * @return true si le plateau est plein, false sinon
     */
    public boolean matchNul(){
        for (int i = 0; i < this.largeur; i++) {
            if (!this.isFull(i)){
                return false;
            }
        }
        return true;
    }

    /**
     * vérifie si le joueur à gagné
     * @param lastPlayed la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné, false sinon
     */
    public Status isWon(int lastPlayed, Pions joueur){
        if (this.isWonLigne(lastPlayed, joueur) || this.isWonColonne(lastPlayed, joueur) || this.isWonDiag(lastPlayed, joueur)) {
            return Status.GAGNE;
        }
        return Status.CONTINUER;
    }

    /**
     * vérifie si le joueur à gagné en ligne
     * @param lastPlayed la ligne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné en ligne, false sinon
     */
    public boolean isWonLigne(int lastPlayed, Pions joueur){
        int nbPions = 1;
        int indice = this.plateau.get(lastPlayed).size() - 1;
        int indice_pile = lastPlayed + 1;
        boolean stop = false;
        StackList<Pions> pile;
        while (!stop && indice < this.largeur){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueur){
                nbPions++;
            } else {
                stop = true;
            }
            indice++;
        }
        indice_pile = lastPlayed - 1;
        stop = false;
        while (!stop && indice >= 0){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueur){
                nbPions++;
            } else {
                stop = true;
            }
            indice--;
        }
        return nbPions >= 4;
    }

    /**
     * vérifie si le joueur à gagné en colonne
     * @param lastPlayed la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné en colonne, false sinon
     */
    public boolean isWonColonne(int lastPlayed, Pions joueur){
        int nbPions = 1;
        StackList<Pions> pile = this.plateau.get(lastPlayed);
        boolean stop = false;
        int indice = pile.size();
        while (!stop && indice < pile.size()){
            if (pile.get(indice) == joueur){
                nbPions++;
            } else {
                stop = true;
            }
            indice--;
        }
        return nbPions >= 4;
    }

    /**
     * vérifie si le joueur à gagné en diagonale
     * @param lastPlayed la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné en diagonale, false sinon
     */
    public boolean isWonDiag(int lastPlayed, Pions joueur){
        return this.isWonDiag1(lastPlayed, joueur) || this.isWonDiag2(lastPlayed, joueur);
    }

    /**
     * vérifie si le joueur à gagné en diagonale 1
     * @param lastPlayed la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné en diagonale 1, false sinon
     */
    public boolean isWonDiag1(int lastPlayed, Pions joueur){
        int nbPions = 1;
        int indice = this.plateau.get(lastPlayed).size() - 2;
        int indice_pile = lastPlayed + 1;
        boolean stop = false;
        StackList<Pions> pile;
        while (!stop && indice_pile < this.largeur && indice >= 0){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueur){
                nbPions++;
            } else {
                stop = true;
            }
            indice--;
            indice_pile++;
        }
        indice_pile = lastPlayed - 1;
        indice = this.plateau.get(lastPlayed).size();
        stop = false;
        while (!stop && indice_pile >= 0 && indice < this.largeur){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueur){
                nbPions++;
            } else {
                stop = true;
            }
            indice++;
            indice_pile--;
        }
        return nbPions >= 4;
    }

    /**
     * vérifie si le joueur à gagné en diagonale 2
     * @param lastPlayed la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné en diagonale 2, false sinon
     */
    public boolean isWonDiag2(int lastPlayed, Pions joueur){
        int nbPions = 1;
        int indice = this.plateau.get(lastPlayed).size();
        int indice_pile = lastPlayed + 1;
        boolean stop = false;
        StackList<Pions> pile;
        while (!stop && indice_pile < this.largeur && indice < this.hauteur){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueur){ // si le pions existe et qu'il est le meme
                nbPions++; // on incrmente le nombre de pions
            } else {
                stop = true;
            }
            indice++;
            indice_pile++;
        }
        indice_pile = lastPlayed - 1;
        indice = this.plateau.get(lastPlayed).size() - 2;
        stop = false;
        while (!stop && indice_pile >= 0 && indice >= 0){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueur){
                nbPions++;
            } else {
                stop = true;
            }
            indice--;
            indice_pile--;
        }
        return nbPions >= 4;
    }
}