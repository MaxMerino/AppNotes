package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.Connexio;
import com.maxmerino.appnotes_maxmerino.model.Model;
import com.maxmerino.appnotes_maxmerino.model.Usuari;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CompartirController {
    //Objecte de classe Connexio
    Connexio connexio = new Connexio();
    Model model;
    
    
    //Injecció del model
    public void injecta(Model model){
        this.model = model;
    }
    //Elements del fxml
    @FXML
    TextField textFieldBuscar;
    @FXML
    ListView llistaResultats;
    @FXML
    Button botoCompartir;
    /**
     * Quan la pantalla inicia la propietat de desactivat del botó de compartir es vincula amb si hi ha selecció al listView de resultats
     */       
    @FXML
    private void initialize(){
       botoCompartir.disableProperty().bind(llistaResultats.getSelectionModel().selectedItemProperty().isNull());
    }
    /**
     * El mètode de buscar comprova que la longitud del text a buscar no sigui buit i assigna al listview el resultat del mètode del model de recollirLlistaUsuaris
     */
    @FXML
    private void buscar(){
        if (textFieldBuscar.getText().length() > 0) {
            llistaResultats.setItems(model.recollirLlistaUsuaris(connexio.connecta(), textFieldBuscar.getText()));
        }
        
    }
    /**
     * El mètode de compartir crida el mètode del model per compartirNota i canvia la pantalla
     */
    @FXML
    private void compartir(){
        model.compartirNota(connexio.connecta(), (Usuari)llistaResultats.getSelectionModel().getSelectedItem());
        canviarPantalla();
    }
    
    /**
     * El mètode canvia a la pantalla secundària
     */
    @FXML
    private void canviarPantalla(){
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            System.out.println();
        }
    }
    
}
