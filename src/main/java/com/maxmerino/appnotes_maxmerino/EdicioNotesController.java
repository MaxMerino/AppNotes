package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.Connexio;
import com.maxmerino.appnotes_maxmerino.model.Model;
import com.maxmerino.appnotes_maxmerino.model.Nota;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EdicioNotesController {
    Connexio connexio = new Connexio();
    Model model;
    public void injecta(Model model){
        this.model = model;
    }
    
    @FXML
    TextField titol;
    @FXML
    TextArea contingut;
    Nota notaActual; 
    
    @FXML
    public void initialize(){
        notaActual = model.getNotaActual();
        titol.setText(notaActual.getTitol());
        contingut.setText(notaActual.getContingut());
        
    }
    
    @FXML
    public void guardar(){
        canviarDades();
        
        if(notaActual.getId() == -1) {
            
            model.afegirNota(connexio.connecta(), notaActual, true);
        }else{
            model.modificarNota(connexio.connecta(), notaActual, true);
        }
        
        sortirEdicio();
    }
    
    private void canviarDades(){
        model.getNotaActual().setTitol(titol.getText());
        model.getNotaActual().setContingut(contingut.getText());
    }
    
    @FXML
    public void sortirEdicio(){
         try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            System.out.println();
        }
    }
    
    
}