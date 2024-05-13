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
    Connexio connexio = new Connexio();//La connexió a MySQL
    Model model;
    Stage stage;
    //Injecció del model 
    public void injecta(Model model){
        this.model = model;
    }
    //Injecció de l'Stage 
    public void injectaStage(Stage stage){
        this.stage = stage;
    }
    Nota notaActual; 
    //Elements del fxml
    @FXML
    TextField titol;
    @FXML
    TextArea contingut;
    
    
    
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
    
    /**
     * Quan s'inicia la pantalla es recull la nota actual, es canvia l'estat d'edicio de la nota actual a true, s'assignen les dades de la nota de text, contingut i preferit.
     * També es vinculen les etiquetes i es fa que els botons d'afegir i esborrar etiquetes només estiguin actius quan hi ha selecció. 
    */
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
    /**
     * El mètode comprova si les dades han canviat i són vàlides i modifica la nota i surt d'edició
     */
    @FXML
    public void guardar(){
        if (canviarDades()) {
            model.modificarNota(connexio.connecta(), notaActual, checkboxPreferida.isSelected());
            sortirEdicio();
        }

        
    }
    /**
     * El mètode comprova si la longitud del títol supera 50 caràcters i del contingut supera 2048 caràcters. 
     * Si ho fan, mostra una alerta per tallar el text. 
     * Si l'usuari confirma, talla el text a la longitud màxima. 
     * Finalment, actualitza les dades de la nota i retorna true si es fan canvis, sinó false
     */
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
    
    /**
     * El mètode canvia l'estat d'edició a fals i torna a la pantalla secundària
     */
    @FXML
    public void sortirEdicio(){
        model.canviarEstatEdicio(connexio.connecta(), false, notaActual.getId());
         try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            SistemaAlerta.alerta("Error!");
        }
    }
    
    /**
     * Actualitza el listview i combobox de etiquetes
     */
    @FXML
    private void actualitzarEtiquetes(){
        comboBoxEtiquetes.setItems(model.visualitzarEtiquetesNoVinculades(connexio.connecta()));
        llistaEtiquetes.setItems(model.visualitzarEtiquetesNota(connexio.connecta()));
        
    }
    /**
     * El mètode vincula una etiqueta a la nota actual
     */
    @FXML
    private void vincularEtiqueta(){
        model.vincularEtiqueta(connexio.connecta(), (Etiqueta)comboBoxEtiquetes.getSelectionModel().getSelectedItem());
        actualitzarEtiquetes();
    }
    /**
     * El mètode desvincula una etiqueta a la nota actual
     */
    @FXML
    private void desvincularEtiqueta(){
        model.desvincularEtiqueta(connexio.connecta(), (Etiqueta)llistaEtiquetes.getSelectionModel().getSelectedItem());
        actualitzarEtiquetes();
    }
    
}