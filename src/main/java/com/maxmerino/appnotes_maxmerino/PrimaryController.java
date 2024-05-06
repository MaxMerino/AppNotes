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
    Connexio connexio = new Connexio();
    Model model;
    
    @FXML
    TextField regNom;
    @FXML
    TextField regCorreu;
    @FXML
    PasswordField regContrasenya;
    
    @FXML
    TextField inCorreu;
    @FXML
    PasswordField inContrasenya;
    
    public void injecta(Model model){
        this.model = model;
    }
    
    
    @FXML
    private void initialize(){
       
    }
    
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
                SistemaAlerta.alerta("Correu o contrasenya incorrectes!\n Recorda, la contrasenya ha de tenir més de 8 caràcters");
            }
        }
    }
    
    
    
    
    @FXML
    private void canviarPantalla(){
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            SistemaAlerta.alerta("Error!"+ex.getMessage());
        }
    }
    
}
