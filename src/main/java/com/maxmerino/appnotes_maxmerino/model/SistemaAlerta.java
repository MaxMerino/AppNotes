/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author alumne
 */
public class SistemaAlerta {
    
    /**
     * El mètode d'alerta fa aparèixer en pantalla un popup amb el paràmetre text com informació
     * @param text 
     */
    @FXML
    public static void alerta(String text){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Informació");
        alerta.setContentText(text);
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alerta.show();
    }
    /**
     * El mètode d'alertaConfirmació fa aparèixer en pantalla un popup amb el paràmetre text com informació
     * i retorna en forma de booleà si s'ha clicat "Si"
     * @param text
     * @return Retorna si s'ha clicat "Sí"
     */
    @FXML
    public static boolean alertaConfirmacio(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació");
        alert.setContentText(text);
        ButtonType botoSi = new ButtonType("Sí");
        ButtonType botoNo = new ButtonType("No");
        alert.getButtonTypes().setAll(botoSi, botoNo);
        
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        
        alert.showAndWait();

        
        return alert.getResult() == botoSi;
    }
}
