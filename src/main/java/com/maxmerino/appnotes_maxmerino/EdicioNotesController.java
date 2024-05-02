package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.Connexio;
import com.maxmerino.appnotes_maxmerino.model.Model;
import com.maxmerino.appnotes_maxmerino.model.Nota;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
    CheckBox checkboxPreferida;
    @FXML
    ComboBox comboBoxCategories;
    @FXML
    Button botoEsborrarCategoria;
    @FXML
    Button botoAfegirCategoria;
    @FXML
    ListView llistaCategories;
    
    @FXML
    public void initialize(){
        notaActual = model.getNotaActual();
        titol.setText(notaActual.getTitol());
        contingut.setText(notaActual.getContingut());
        checkboxPreferida.setSelected(notaActual.isPreferida());
        botoAfegirCategoria.disableProperty().bind(comboBoxCategories.getSelectionModel().selectedItemProperty().isNull());
        botoEsborrarCategoria.disableProperty().bind(llistaCategories.getSelectionModel().selectedItemProperty().isNull());
        actualitzarCategories();
    }
    
    @FXML
    public void guardar(){
        canviarDades();
        
        if(notaActual.getId() == -1) {
            
            model.afegirNota(connexio.connecta(), notaActual, checkboxPreferida.isSelected());
        }else{
            model.modificarNota(connexio.connecta(), notaActual, checkboxPreferida.isSelected());
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
            SistemaAlerta.alerta("Error!");
        }
    }
    
    @FXML
    private void actualitzarCategories(){
        comboBoxCategories.setItems(model.visualitzarCategoriesTotals(connexio.connecta()));
        llistaCategories.setItems(model.visualitzarCategoriesNota(connexio.connecta()));
        
    }
    @FXML
    private void vincularCategoria(){
        model.vincularCategoria(connexio.connecta(), (String)comboBoxCategories.getSelectionModel().getSelectedItem());
        actualitzarCategories();
    }
    
}