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
    Connexio connexio = new Connexio();
    Model model;
    
    
    
    public void injecta(Model model){
        this.model = model;
    }
    
    @FXML
    TextField textFieldBuscar;
    @FXML
    ListView llistaResultats;
    @FXML
    Button botoCompartir;
            
    @FXML
    private void initialize(){
       botoCompartir.disableProperty().bind(llistaResultats.getSelectionModel().selectedItemProperty().isNull());
    }
    @FXML
    private void buscar(){
        if (textFieldBuscar.getText().length() > 0) {
            llistaResultats.setItems(model.recollirLlistaUsuaris(connexio.connecta(), textFieldBuscar.getText()));
        }
        
    }
    @FXML
    private void compartir(){
        model.compartirNota(connexio.connecta(), (Usuari)llistaResultats.getSelectionModel().getSelectedItem());
        
    }
    
    
}
