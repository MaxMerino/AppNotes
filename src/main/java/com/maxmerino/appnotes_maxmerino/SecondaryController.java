package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.Connexio;
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

    Connexio connexio = new Connexio();
    Model model;

    public void injecta(Model model) {
        this.model = model;
    }

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

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void afegirNota() {
        Nota notaNova = new Nota();;

        notaNova = model.afegirNota(connexio.connecta(), notaNova, !tabTotesNotes.isSelected());
        model.setNotaActual(notaNova);

        obrirPantallaEdicio();
    }

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

    @FXML
    private void obrirPantallaEdicio() {
        try {
            App.setRoot("edicio_notes");
        } catch (IOException ex) {
            System.out.println();
        }
    }

    @FXML
    private void tancarSessio() {
        try {
            App.setRoot("primary");
            model.setId_usuari(-1);

        } catch (IOException ex) {
            System.out.println();
        }
    }

    @FXML
    private void crearEtiqueta() {
        if (textEtiqueta.getText().length() > 0) {
            model.afegirEtiqueta(connexio.connecta(), textEtiqueta.getText());
            textEtiqueta.setText("");
            actualitzarLlistes();
        }

    }

    @FXML
    private void eliminarEtiqueta() {
        if (textEtiqueta.getText().length() > 0 && SistemaAlerta.alertaConfirmacio("Segur que vols esborrar l'etiqueta?")) {
            model.esborrarEtiquetaTotalment(connexio.connecta(), textEtiqueta.getText());
            textEtiqueta.setText("");
            actualitzarLlistes();
        }

    }

    @FXML
    private void filtrarCategoria() {
        
        
        llistaNotes.setItems(model.filtrarNotes(connexio.connecta(), (String) comboBoxFiltre.getSelectionModel().getSelectedItem()));

    }
   

    @FXML
    private void sortirPrograma() {
        System.exit(0);
    }

    @FXML
    private void eliminarCompte() {
        if (SistemaAlerta.alertaConfirmacio("Segur que vols esborrar el compte de forma permanent?")) {
            model.eliminarUsuari(connexio.connecta());
            tancarSessio();
        }

    }

}
