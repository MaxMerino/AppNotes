/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author alumne
 */
public class Nota {
    private int id = -1;
    private String titol = "";
    private String contingut = "";
    private boolean enEdicio = true;
    private boolean preferida = false;
    private LocalDateTime dataModificacio = LocalDateTime.now();

    public Nota(String titol, String contingut, boolean isEnEdicio, LocalDateTime dataModificacio, boolean preferida) {
        
        this.titol = titol;
        this.contingut = contingut;
        this.enEdicio = isEnEdicio;
        this.dataModificacio = dataModificacio;
        this.preferida = preferida;
    }
    
    public Nota(int id, String titol, String contingut, boolean isEnEdicio, LocalDateTime dataModificacio, boolean preferida) {
        this(titol,contingut,isEnEdicio,dataModificacio,preferida);
        this.id = id;
        
    }
    
    public Nota() { 
    }
    
    public void actualitzarData(){
        dataModificacio = LocalDateTime.now();
    }
    
    public Timestamp getSqlData(){
        return Timestamp.valueOf(dataModificacio);
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getContingut() {
        return contingut;
    }

    public void setContingut(String contingut) {
        this.contingut = contingut;
    }

    public boolean isEnEdicio() {
        return enEdicio;
    }

    public void setEnEdicio(boolean enEdicio) {
        this.enEdicio = enEdicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String contingutVisualitzacio = contingut;
        if (contingut.length() > 25) {
            contingutVisualitzacio = contingut.substring(0,25)+"...";
        }
        return titol + "\n" + contingutVisualitzacio+"\n"+"Data Modificació: "+dataModificacio.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public boolean isPreferida() {
        return preferida;
    }

    public void setPreferida(boolean preferida) {
        this.preferida = preferida;
    }
    
    
    
}
