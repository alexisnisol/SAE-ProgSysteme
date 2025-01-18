package controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;


public class BoutonHoverProperty implements ChangeListener<Boolean>{
    private Button button;

    public BoutonHoverProperty(Button btn){
        this.button = btn;
    }
    
    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
    {   
        if (newValue) { // si on a perdu le hover
            this.button.setStyle("-fx-background-color: red;");
        }else{
            this.button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");
        }
    }
}

