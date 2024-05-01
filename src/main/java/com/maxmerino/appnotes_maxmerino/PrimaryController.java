package com.maxmerino.appnotes_maxmerino;

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
        
        boolean resultat =  model.InsertarUsuari(connexio.connecta(), regNom.getText(), regCorreu.getText(), regContrasenya.getText());
        if (resultat) {
            
            int idSessio = model.ComprovarUsuari(connexio.connecta(), regCorreu.getText(), regContrasenya.getText());
            SistemaAlerta.alerta("Compte creat correctament");
            model.setId_usuari(idSessio);
            
            canviarPantalla();
            
        }else{
            SistemaAlerta.alerta("Compte no creat");
        }
    }
    
    @FXML
    public void iniciarSessio(){
        int idSessio = model.ComprovarUsuari(connexio.connecta(), inCorreu.getText(), inContrasenya.getText());
        if (idSessio != -1) {
            SistemaAlerta.alerta("Credencials vàlides!");
            model.setId_usuari(idSessio);
            canviarPantalla();
        }else{
            SistemaAlerta.alerta("Credencials invàlides!");
        }
    }
    
    
    
    
    @FXML
    private void canviarPantalla(){
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            SistemaAlerta.alerta("Error!");
        }
    }
    
}
