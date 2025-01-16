package com.controller;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import com.model.Puissance4.Status;
import com.model.exception.PoseImpossibleException;
import com.views.Appli;

public class BoutonControlleur implements EventHandler<ActionEvent>{

    private Appli vue;
    private int index;

    public BoutonControlleur(Appli vue, int index){
        this.vue = vue;
        this.index = index;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            Status st = this.vue.getModele().poserPions(index);
            this.vue.majAffichage(index);
            if(st != Status.CONTINUER){

                Optional<ButtonType> reponse = this.vue.popUpMessage(st).showAndWait(); // on lance la fenêtre popup et on attends la réponse
                if (reponse.isPresent() && reponse.get().equals(ButtonType.OK)){
                    this.vue.lancePartie();
                }
            }
        } catch (PoseImpossibleException poseImpossibleException) {
            System.out.println("Impossible de poser le pion");
        }
    }
    
}
