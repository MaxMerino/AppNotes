package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.SistemaAlerta;
import com.maxmerino.appnotes_maxmerino.model.Connexio;
import com.maxmerino.appnotes_maxmerino.model.Etiqueta;
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
import javafx.stage.Stage;

public class EdicioNotesController {
    Connexio connexio = new Connexio();
    Model model;
    Stage stage;
    public void injecta(Model model){
        this.model = model;
    }
    public void injectaStage(Stage stage){
        this.stage = stage;
    }
    
    @FXML
    TextField titol;
    @FXML
    TextArea contingut;
    
    Nota notaActual; 
    
    @FXML
    CheckBox checkboxPreferida;
    @FXML
    ComboBox comboBoxEtiquetes;
    @FXML
    Button botoEsborrarEtiqueta;
    @FXML
    Button botoAfegirEtiqueta;
    @FXML
    ListView llistaEtiquetes;
    
    @FXML
    public void initialize(){
        notaActual = model.getNotaActual();
        
        model.canviarEstatEdicio(connexio.connecta(), true, notaActual.getId());
        titol.setText(notaActual.getTitol());
        contingut.setText(notaActual.getContingut());
        checkboxPreferida.setSelected(notaActual.isPreferida());
        botoAfegirEtiqueta.disableProperty().bind(comboBoxEtiquetes.getSelectionModel().selectedItemProperty().isNull());
        botoEsborrarEtiqueta.disableProperty().bind(llistaEtiquetes.getSelectionModel().selectedItemProperty().isNull());
        //Permet executar un mètode abans de que es tanqui el programa
        stage.setOnCloseRequest(event -> canviarEdicio());
        actualitzarEtiquetes();
    }
    
    @FXML
    public void guardar(){
        if (canviarDades()) {
            model.modificarNota(connexio.connecta(), notaActual, checkboxPreferida.isSelected());
            sortirEdicio();
        }

        
    }
    
    private boolean canviarDades(){
        boolean textRetallat = false;
        
        if (titol.getText().length() > 50) {
            
            if (SistemaAlerta.alertaConfirmacio("El títol excedeix el número de caràcters màxims (50). Vols que el text es talli a partir del caràcter 50?")) {
                titol.setText(titol.getText().substring(0,50));
                textRetallat = true;
            }else{
                textRetallat = false;
            }
            
        }
        if (contingut.getText().length() > 2048) {
            if (SistemaAlerta.alertaConfirmacio("El contingut excedeix el número de caràcters màxims (2048). Vols que el text es talli a partir del caràcter 2048?")) {
                contingut.setText(contingut.getText().substring(0,2048));
                textRetallat = true;
            }else{
                textRetallat = false;
            }
            
        }
        if (textRetallat ||(titol.getText().length() <= 50 && contingut.getText().length() <= 2048)) {
            model.getNotaActual().setTitol(titol.getText());
            model.getNotaActual().setContingut(contingut.getText());
            return true;
        }
        return false;
        
    }
    //Mètode per prevenir que es quedi en estat d'edició per sempre si es surt de l'aplicació amb la X del sistema operatiu
    private void canviarEdicio(){
        model.canviarEstatEdicio(connexio.connecta(), false, notaActual.getId());
        
    }
    
    @FXML
    public void sortirEdicio(){
        model.canviarEstatEdicio(connexio.connecta(), false, notaActual.getId());
         try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            SistemaAlerta.alerta("Error!");
        }
    }
    
    @FXML
    private void actualitzarEtiquetes(){
        comboBoxEtiquetes.setItems(model.visualitzarEtiquetesNoVinculades(connexio.connecta()));
        llistaEtiquetes.setItems(model.visualitzarEtiquetesNota(connexio.connecta()));
        
    }
    @FXML
    private void vincularEtiqueta(){
        model.vincularEtiqueta(connexio.connecta(), (Etiqueta)comboBoxEtiquetes.getSelectionModel().getSelectedItem());
        actualitzarEtiquetes();
    }
    @FXML
    private void desvincularEtiqueta(){
        model.desvincularEtiqueta(connexio.connecta(), (Etiqueta)llistaEtiquetes.getSelectionModel().getSelectedItem());
        actualitzarEtiquetes();
    }
    
}