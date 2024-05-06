package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.SistemaAlerta;
import com.maxmerino.appnotes_maxmerino.model.Connexio;
import com.maxmerino.appnotes_maxmerino.model.Model;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PrimaryController {
    Connexio connexio = new Connexio();//La connexió a MySQL
    Model model;
    
    //Variables registre
    @FXML
    TextField regNom;//El nom d'usuari
    @FXML
    TextField regCorreu;//Correu
    @FXML
    PasswordField regContrasenya;//Contrasenya
    //Variables inici sessió
    @FXML
    TextField inCorreu;//Correu
    @FXML
    PasswordField inContrasenya;//Contrasenya
    
    //Injecció del model 
    public void injecta(Model model){
        this.model = model;
    }
    
    
    /**
     * El mètode registrar-se crida el mètode d'InsertarUsuari del model amb les dades dels textField i passwordField i emmagatzema el resultat el forma de int
     * depenent del resultat, s'inicia sessió(model.ComprovarUsuari) i es mostra un missatge  de registre correcte i s'assigna al model la id de l'usuari, mostra missatge d'error de bases de dades si
     * no hi ha connexió o mostra un missatge si les credencials no són vàlides
     */
    @FXML
    public void registrarse(){
        
        int resultat =  model.InsertarUsuari(connexio.connecta(), regNom.getText(), regCorreu.getText(), regContrasenya.getText());
        
        if (resultat == 1) {
            
            int idSessio = model.ComprovarUsuari(connexio.connecta(), regCorreu.getText(), regContrasenya.getText());
            SistemaAlerta.alerta("Registrat correctament");
            model.setId_usuari(idSessio);
            
            canviarPantalla();
            
        }else{
            if (resultat == 0) {
                SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades!");
            }else{
                SistemaAlerta.alerta("Revisa el format de les credencials!\nEl nom d'usuari ha de ser únic, el correu ha de ser vàlid i contrasenya de més de 8 caràcters");
            }
        }
    }
    
    
    /**
     * El mètode iniciar sessió crida el mètode de ComprovarUsuari del model amb les dades dels textField i passwordField 
     * i emmagatzema el resultat el forma de int
     * depenent del resultat, es mostra un missatge d'inici de sessió correcte i s'assigna l'id d'usuari al model, mostra
     * un missatge d'error de connexió amb el servidor de bases de dades o mostra un missatge de credencials invàlides
     */
    @FXML
    public void iniciarSessio(){
        int idSessio = model.ComprovarUsuari(connexio.connecta(), inCorreu.getText(), inContrasenya.getText());
        if (idSessio != 0 && idSessio != -1) {
            SistemaAlerta.alerta("Credencials vàlides, s'ha iniciat sessió");
            model.setId_usuari(idSessio);
            canviarPantalla();
        }else{
            if (idSessio == 0) {
                SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades!");
            }else{
                SistemaAlerta.alerta("Correu o contrasenya incorrectes!");
            }
        }
    }
    
    
    
    /**
     * El mètode de canviar pantalla crida a App per canviar la pantalla
     */
    @FXML
    private void canviarPantalla(){
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            SistemaAlerta.alerta("Error!"+ex.getMessage());
        }
    }
    
}
