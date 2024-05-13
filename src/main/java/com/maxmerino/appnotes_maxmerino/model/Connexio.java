/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author alumne
 */
public class Connexio {
    private final String URL = "jdbc:mysql://localhost/bd_blocnotes";//nom bd 

    private final String DRIVER = "com.mysql.cj.jdbc.Driver";//El driver de la bd
    private final String USER = "worker";//Nom d'usuari
    private final String PASSWD = "worker";//Contrasenya   
   
    /**
     * El mètode es connecta a la base de dades amb les credencials i dades anteriors
     * @return Retorna una connexió sql (Connection)
     */
    public Connection connecta() {
        Connection connexio = null;
        try {
                     
            Class.forName(DRIVER); 
            connexio = DriverManager.getConnection(URL, USER, PASSWD); 
        } catch (SQLException | ClassNotFoundException throwables) {
            SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades");
            
        }   
        return connexio;
    }
}
