package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.Connexio;
import com.maxmerino.appnotes_maxmerino.model.Model;
import com.maxmerino.appnotes_maxmerino.model.Nota;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

public class SecondaryController {
    Connexio connexio = new Connexio();
    Model model;
    public void injecta(Model model){
        this.model = model;
    }
    
    @FXML
    Label labelId;
    
    @FXML
    ListView llistaNotes;
    
    @FXML
    ListView llistaNotesPreferits;
    
    @FXML
    Button botoEsborrar;

    @FXML
    Button botoModificar;
    
    @FXML
    Button botoCompartir;
    @FXML
    TextField textCategoria;
    
   
    
    @FXML
    Tab tabTotesNotes;
    
    @FXML
    private void initialize(){
        botoEsborrar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        botoModificar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        botoCompartir.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        
        labelId.setText(String.valueOf(model.getId_usuari()));
        llistaNotes.setItems(model.visualitzarNotes(connexio.connecta(),false));
        
    }
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
    @FXML
    private void afegirNota(){
        Nota notaNova = new Nota();;
        if (!tabTotesNotes.isSelected()) {
            notaNova.setPreferida(true);
        }
        model.setNotaActual(notaNova);
        obrirPantallaEdicio();
    }
    
    @FXML
    private void editarNota(){
        Nota notaNova;
        if (tabTotesNotes.isSelected()) {
            notaNova = (Nota)llistaNotes.getSelectionModel().getSelectedItem();
        
        }else{
            notaNova = (Nota)llistaNotesPreferits.getSelectionModel().getSelectedItem();
        }
        model.setNotaActual(notaNova);
        obrirPantallaEdicio();
    }
     
    @FXML
    private void esborrarNotes(){
        if (tabTotesNotes.isSelected()) {
            model.esborrarNota(connexio.connecta(), (Nota)llistaNotes.getSelectionModel().getSelectedItem());
        }else{
            model.esborrarNota(connexio.connecta(), (Nota)llistaNotesPreferits.getSelectionModel().getSelectedItem());
        }
        actualitzarLlistes();
    }
    
    @FXML
    private void compartirNota(){
        
    }

    @FXML
    private void actualitzarLlistes(){
        if (tabTotesNotes.isSelected()) {
            llistaNotes.setItems(model.visualitzarNotes(connexio.connecta(),false));
            if (botoEsborrar != null && botoModificar != null && botoCompartir != null) {
                botoEsborrar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
                botoModificar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
                botoCompartir.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());

            }
        }else{
            
            llistaNotesPreferits.setItems(model.visualitzarNotes(connexio.connecta(),true));
            if (botoEsborrar != null && botoModificar != null && botoCompartir != null) {
                botoEsborrar.disableProperty().bind(llistaNotesPreferits.getSelectionModel().selectedItemProperty().isNull());
                botoModificar.disableProperty().bind(llistaNotesPreferits.getSelectionModel().selectedItemProperty().isNull());
                botoCompartir.disableProperty().bind(llistaNotesPreferits.getSelectionModel().selectedItemProperty().isNull());

            }
            
        }
    }
    
    @FXML
    private void obrirPantallaEdicio(){
        try {
            App.setRoot("edicio_notes");
        } catch (IOException ex) {
            System.out.println();
        }
    }
    
    @FXML
    private void tancarSessio(){
        try {
            App.setRoot("primary");
            model.setId_usuari(-1);
            
        } catch (IOException ex) {
            System.out.println();
        }
    }
    @FXML 
    private void crearCategoria(){
        if(textCategoria.getText().length() > 0){
            model.afegirCategoria(connexio.connecta(), textCategoria.getText());
        }
        
    }
    
    
}