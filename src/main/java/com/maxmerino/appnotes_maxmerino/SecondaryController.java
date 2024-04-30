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
    Button botoEsborrar;
    @FXML
    Button botoModificar;
    
    @FXML
    private void initialize(){
        botoEsborrar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        botoModificar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        labelId.setText(String.valueOf(model.getId_usuari()));
        actualitzarVistaNotes();
    }
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
    @FXML
    private void editarNota(){
        Nota notaNova = (Nota)llistaNotes.getSelectionModel().getSelectedItem();
        model.setNotaActual(notaNova);
        obrirPantallaEdicio();
        
    }
    @FXML
    private void afegirNota(){
        Nota notaNova = new Nota();
        model.setNotaActual(notaNova);
        obrirPantallaEdicio();
        
    }
    
    @FXML
    private void actualitzarVistaNotes(){
        llistaNotes.setItems(model.visualitzarNotes(connexio.connecta()));
    }
    
    @FXML
    private void esborrarNotes(){
        
        model.esborrarNota(connexio.connecta(), (Nota)llistaNotes.getSelectionModel().getSelectedItem());
        actualitzarVistaNotes();
        
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
    
    
}