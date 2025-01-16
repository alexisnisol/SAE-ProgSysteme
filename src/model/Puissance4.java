package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.exception.PoseImpossibleException;
import network.Player;

public class Puissance4 extends Game {
    private List<StackList<Pions>> plateau;
    private int hauteur;
    private int largeur;

    private Pions joueurActuel;
    private Map<Pions, Integer> nbPions;

    private Map<Pions, Player> playersPions;

    public Puissance4(){
        this(7, 6);
    }

    public Puissance4(int largeur, int hauteur){
        super();
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.playersPions = new HashMap<>();
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
        this.plateau = new ArrayList<>();

        this.joueurActuel = Pions.JOUEUR1;
        this.nbPions = new HashMap<>();
        this.nbPions.put(Pions.JOUEUR1, 21);
        this.nbPions.put(Pions.JOUEUR2, 21);
        StackList<Pions> pile;
        for (int i = 0; i < this.largeur; ++i) {
            pile = new StackList<>(this.hauteur);
            this.plateau.add(pile);
        }
    }

    @Override
    public void start(){

        Player player;
        for (int j = 0; j < this.getPlayers().size(); j++) {
            try {
                player = this.getPlayers().get(j);
                this.playersPions.put(Pions.values()[j], player);
                System.out.println(this.playersPions);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Erreur : il n'y a pas assez de joueurs");
                break;
            }
        }

    }

    /**
     * Poser un pions, retourne si le joueur à gagné ou non
     * @param indice la colonne où le joueur à joué
     * @return true si le joueur à gagné, false sinon
     */
    @Override
    public Status poserPions(int indice) throws PoseImpossibleException{
        try {
            this.plateau.get(indice).pushItem(joueurActuel);

            int nbPionsActuel = this.nbPions.get(joueurActuel);
            this.nbPions.put(joueurActuel, nbPionsActuel-1);

            Status status = this.isWon(indice);
            if(status == Status.CONTINUER){
                if(this.matchNul()){
                    return Status.NULL;
                }
                this.joueurActuel = switchPlayer(this.joueurActuel);
            }
            return status; // continuer ou est gagné
        } catch (PoseImpossibleException e) {
            throw e;
        }
    }

    public static Pions switchPlayer(Pions joueurActuel){
        return joueurActuel == Pions.JOUEUR1 ? Pions.JOUEUR2 : Pions.JOUEUR1;
    }

    public Map<Pions, Integer> getNbPions(){
        return this.nbPions;
    }

    @Override
    public Player getPlayer(Pions pions){
        return this.playersPions.get(pions);
    }

    @Override
    public Pions getJoueurActuel(){
        return this.joueurActuel;
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
     * @return Status
     */
    public Status isWon(int lastPlayed){
        if (this.isWonLigne(lastPlayed) || this.isWonColonne(lastPlayed) || this.isWonDiag(lastPlayed)) {
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
    public boolean isWonLigne(int lastPlayed){
        int nbPions = 1;
        int indice = this.plateau.get(lastPlayed).size() - 1;
        int indice_pile = lastPlayed + 1;
        boolean stop = false;
        StackList<Pions> pile;
        while (!stop && indice_pile < this.largeur){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueurActuel){
                nbPions++;
            } else {
                stop = true;
            }
            indice_pile++;
        }
        indice_pile = lastPlayed - 1;
        stop = false;
        while (!stop && indice_pile >= 0){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueurActuel){
                nbPions++;
            } else {
                stop = true;
            }
            indice_pile--;
        }
        return nbPions >= 4;
    }

    /**
     * vérifie si le joueur à gagné en colonne
     * @param lastPlayed la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné en colonne, false sinon
     */
    public boolean isWonColonne(int lastPlayed){
        int nbPions = 0;
        StackList<Pions> pile = this.plateau.get(lastPlayed);
        boolean stop = false;
        int indice = pile.size()-1;
        while (!stop && indice >= 0){
            if (pile.get(indice) == joueurActuel){
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
    public boolean isWonDiag(int lastPlayed){
        return this.isWonDiag1(lastPlayed) || this.isWonDiag2(lastPlayed);
    }

    /**
     * vérifie si le joueur à gagné en diagonale 1
     * @param lastPlayed la colonne où le joueur à joué
     * @param joueur le joueur qui a joué
     * @return true si le joueur à gagné en diagonale 1, false sinon
     */
    public boolean isWonDiag1(int lastPlayed){
        int nbPions = 1;
        int indice = this.plateau.get(lastPlayed).size() - 2;
        int indice_pile = lastPlayed + 1;
        boolean stop = false;
        StackList<Pions> pile;
        while (!stop && indice_pile < this.largeur && indice >= 0){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueurActuel){
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
            if (indice < pile.size() && pile.get(indice) == joueurActuel){
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
    public boolean isWonDiag2(int lastPlayed){
        int nbPions = 1;
        int indice = this.plateau.get(lastPlayed).size();
        int indice_pile = lastPlayed + 1;
        boolean stop = false;
        StackList<Pions> pile;
        while (!stop && indice_pile < this.largeur && indice < this.hauteur){
            pile = this.plateau.get(indice_pile); // la pile actuelle
            if (indice < pile.size() && pile.get(indice) == joueurActuel){ // si le pions existe et qu'il est le meme
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
            if (indice < pile.size() && pile.get(indice) == joueurActuel){
                nbPions++;
            } else {
                stop = true;
            }
            indice--;
            indice_pile--;
        }
        return nbPions >= 4;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("PUISSANCE4 :");
        //affiche le puissance 4 avec des cases et des 0 et 1 pour les pions
        for (int i = this.hauteur - 1; i >= 0; i--) {
            str.append("\n");
            for (int j = 0; j < this.largeur; j++) {
                if (this.plateau.get(j).size() > i){
                    str.append(this.plateau.get(j).get(i) == Pions.JOUEUR1 ? "0" : "1");
                } else {
                    str.append(".");
                }
            }
        }
        str.append("\n");
        return str.toString();
    }
}