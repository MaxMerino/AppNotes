package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.SistemaAlerta;
import com.maxmerino.appnotes_maxmerino.model.Connexio;
import com.maxmerino.appnotes_maxmerino.model.Etiqueta;
import com.maxmerino.appnotes_maxmerino.model.Model;
import com.maxmerino.appnotes_maxmerino.model.Nota;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class SecondaryController {
    //Objecte de classe Connexio
    Connexio connexio = new Connexio();
    Model model;

    //Injecció del model
    public void injecta(Model model) {
        this.model = model;
    }
    //Elements del fxml
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
    Button botoFiltrar;
    @FXML
    Button botoNetejar;
    @FXML
    TextField textEtiqueta;

    @FXML
    Tab tabTotesNotes;
    @FXML
    TabPane tabPane;

    @FXML
    ComboBox comboBoxFiltre;
    
    /**
     * Quan la pantalla s'inicia es vincula la propietat de desactivar els botons d'esborrar, modificar i compartir al list view per evitar
     * clicar quan no hi ha cap element seleccionat.
     * També es fa el mateix amb el botó de filtar i netejar amb el combobox de selecció d'etiqueta.
     * El combobox també es vincula comprovant si està a la pantalla de notes totals
     * Finalment es recullen al list view i combobox les dades de la base de dades
     */
    @FXML
    private void initialize() {
        botoEsborrar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        botoModificar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        botoCompartir.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
        botoFiltrar.disableProperty().bind(comboBoxFiltre.getSelectionModel().selectedItemProperty().isNull());
        botoNetejar.disableProperty().bind(comboBoxFiltre.getSelectionModel().selectedItemProperty().isNull());
        comboBoxFiltre.disableProperty().bind(tabPane.getSelectionModel().selectedItemProperty().isNotEqualTo(tabTotesNotes));
        llistaNotes.setItems(model.visualitzarNotes(connexio.connecta(), false));
       
        comboBoxFiltre.setItems(model.visualitzarEtiquetesTotals(connexio.connecta()));
        
        

    }
    
    /**
     * El mètode d'afegir nota crida al model per crear una nota buida, es posa com nota actual aquesta i s'obre la pantalla d'edició 
     */
    @FXML
    private void afegirNota() {
        Nota notaNova = new Nota();

        notaNova = model.afegirNota(connexio.connecta(), notaNova, !tabTotesNotes.isSelected());
        model.setNotaActual(notaNova);

        obrirPantallaEdicio();
    }

    /**
     * El mètode d'editar nota crea un objecte nota amb el element seleccionat.
     * D'aquest es treu la id i amb el model es recupera l'objecte nota a partir de la id de la base de dades.
     * Si el booleà enEdició es fals s'assigna com nota actual i s'obre la pantalla d'edició sinó mostra un missatge que un altre usuari està editant la nota
     */
    @FXML
    private void editarNota() {
        Nota notaNova;

        if (tabTotesNotes.isSelected()) {
            notaNova = (Nota) llistaNotes.getSelectionModel().getSelectedItem();

        } else {
            notaNova = (Nota) llistaNotesPreferits.getSelectionModel().getSelectedItem();
        }
        int idNota = notaNova.getId();
        
        //Per recuperar la versió que hi ha a la base de dades per evitar editar una versió anterior
        notaNova = model.notaPerId(connexio.connecta(), idNota);
        if (!notaNova.isEnEdicio()) {
            model.setNotaActual(notaNova);

            obrirPantallaEdicio();
        } else {
            SistemaAlerta.alerta("Un altre usuari està editant la nota.");
        }

    }
    
    /**
     * El mètode d'esborrar nota primer demana confirmació de l'usuari, si s'accepta es crida el model per eliminar l'element seleccionat
     * i s'actualitzen les llistes
     * 
     */
    @FXML
    private void esborrarNotes() {
        if (SistemaAlerta.alertaConfirmacio("Segur que vols esborrar la nota?")) {
            if (tabTotesNotes.isSelected()) {
                model.esborrarNota(connexio.connecta(), (Nota) llistaNotes.getSelectionModel().getSelectedItem());
            } else {
                model.esborrarNota(connexio.connecta(), (Nota) llistaNotesPreferits.getSelectionModel().getSelectedItem());
            }
            actualitzarLlistes();
        }

    }

    /**
     * El mètode de compartirNota guarda en un objecte nota l'element seleccionat, s'assigna com nota actual al model i es canvia a la pantalla de compartir
     */
    @FXML
    private void compartirNota() {
        Nota notaNova;

        if (tabTotesNotes.isSelected()) {
            notaNova = (Nota) llistaNotes.getSelectionModel().getSelectedItem();

        } else {
            notaNova = (Nota) llistaNotesPreferits.getSelectionModel().getSelectedItem();
        }
        model.setNotaActual(notaNova);
        try {
            App.setRoot("compartir");

        } catch (IOException ex) {
            System.out.println();
        }

    }
    /**
     * El mètode d'actualitzarLlistes recupera les llistes de la base de dades i les reassigna als list views i comboBox
     */
    @FXML
    private void actualitzarLlistes() {
        if (comboBoxFiltre != null) {
            comboBoxFiltre.setItems(model.visualitzarEtiquetesTotals(connexio.connecta()));
            comboBoxFiltre.getSelectionModel().select(null);
            
        }

        if (tabTotesNotes.isSelected()) {

            llistaNotes.setItems(model.visualitzarNotes(connexio.connecta(), false));
            if (botoEsborrar != null && botoModificar != null && botoCompartir != null) {
                botoEsborrar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
                botoModificar.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());
                botoCompartir.disableProperty().bind(llistaNotes.getSelectionModel().selectedItemProperty().isNull());

            }
        } else {

            llistaNotesPreferits.setItems(model.visualitzarNotes(connexio.connecta(), true));
            if (botoEsborrar != null && botoModificar != null && botoCompartir != null) {
                botoEsborrar.disableProperty().bind(llistaNotesPreferits.getSelectionModel().selectedItemProperty().isNull());
                botoModificar.disableProperty().bind(llistaNotesPreferits.getSelectionModel().selectedItemProperty().isNull());
                botoCompartir.disableProperty().bind(llistaNotesPreferits.getSelectionModel().selectedItemProperty().isNull());

            }

        }
    }
    /**
     * El mètode obre la pantalla d'edició de notes
     */
    @FXML
    private void obrirPantallaEdicio() {
        try {
            App.setRoot("edicio_notes");
        } catch (IOException ex) {
            System.out.println();
        }
    }
    /**
     * El mètode fixa la id d'usuari del model a -1, deixa com null la nota actual i va a la pantalla de registre / inici sessió
     */
    @FXML
    private void tancarSessio() {
        try {
            model.setId_usuari(-1);
            model.setNotaActual(null);
            App.setRoot("primary");
        } catch (IOException ex) {
            System.out.println();
        }
    }

    /**
     * El mètode primer comprova si el nom de l'etiqueta no està buit o si és més petit de 50 caràcters
     * desprès es crida el mètode d'afegir etiqueta del model
     */
    @FXML
    private void crearEtiqueta() {
        if (textEtiqueta.getText().length() > 0) {
            if (textEtiqueta.getText().length() < 50) {
                model.afegirEtiqueta(connexio.connecta(), textEtiqueta.getText());
                textEtiqueta.setText("");
                actualitzarLlistes();
            }else{
                SistemaAlerta.alerta("El nom d'etiqueta no pot ser superior a 50 caràcters");
            }
            
        }

    }
    /**
     * El mètode primer comprova si el nom de l'etiqueta no està buit i fa una confirmació a l'usuari
     * seguidament es crida el mètode per esborrar l'etiqueta a partir del nom
     */
    @FXML
    private void eliminarEtiqueta() {
        if (textEtiqueta.getText().length() > 0 && SistemaAlerta.alertaConfirmacio("Segur que vols esborrar l'etiqueta?")) {
            model.esborrarEtiquetaTotalment(connexio.connecta(), textEtiqueta.getText());
            textEtiqueta.setText("");
            actualitzarLlistes();
        }

    }
    /**
     * assigna com valors del listview de totes les notes els valors del retorn del mètode del model
     */
    @FXML
    private void filtrarEtiqueta() {
        
        
        llistaNotes.setItems(model.filtrarNotes(connexio.connecta(), (Etiqueta) comboBoxFiltre.getSelectionModel().getSelectedItem()));

    }
   
    /**
     * El mètode crida system exit per sortir del programa
     */
    @FXML
    private void sortirPrograma() {
        System.exit(0);
    }
    /**
     * El mètode demana confirmació, crida el mètode del model per eliminar usuaris i tanca sessió
     */
    @FXML
    private void eliminarCompte() {
        if (SistemaAlerta.alertaConfirmacio("Segur que vols esborrar el compte de forma permanent?")) {
            model.eliminarUsuari(connexio.connecta());
            tancarSessio();
        }

    }

}
