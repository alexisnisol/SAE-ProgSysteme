package com.model;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Puissance4 game = new Puissance4();
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenue dans Puissance 4 !");
        System.out.println(game);

        boolean isFinished = false;

        while (!isFinished) {
            System.out.println("Joueur " + game.getJoueurActuel() + " : Entrez un numéro de colonne (0-" + (game.getLargeur() - 1) + ") : ");
            int col;
            try {
                col = sc.nextInt();
                Puissance4.Status status = game.poserPions(col);
                if (status == Puissance4.Status.CONTINUER) {
                    // Pions joueurActuel = game.getJoueurActuel();
                    // .switchPlayer(joueurActuel);
                    // TODO: switchPlayer(joueurActuel);
                } else if (status == Puissance4.Status.GAGNE) {
                    System.out.println("Le joueur " + game.getJoueurActuel() + " a gagné !");
                    isFinished = true;
                } else if (status == Puissance4.Status.NULL) {
                    System.out.println("Match nul !");
                    isFinished = true;
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    System.out.println("Erreur : " + e.getMessage());
                } else {
                    System.out.println("Erreur : veuillez entrer un nombre valide.");
                }
                sc.next();
            } 
            System.out.println(game);
            System.out.println();
        }

        sc.close();
        System.out.println("Merci d'avoir joué !");
    }
}
