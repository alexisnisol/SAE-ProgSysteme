package model;

import model.exception.PoseImpossibleException;
import network.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

    protected String id;
    protected List<Player> players;


    public Game() {
        this.id = java.util.UUID.randomUUID().toString();
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public String getId() {
        return this.id;
    }

    public abstract void start();

    public List<Player> getPlayers() {
        return this.players;
    }

    public abstract Player getPlayer(Pions pions);

    public abstract Pions getJoueurActuel();

    public abstract Puissance4.Status poserPions(int indice) throws PoseImpossibleException;

}