/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author alumne
 */
public class SistemaAlerta {
    
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
}
