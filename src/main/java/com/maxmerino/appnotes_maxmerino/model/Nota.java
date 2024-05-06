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
    private int id = -1;//Id a la base de dades de la nota
    private String titol = "";//Títol de la nota
    private String contingut = "";//Contingut de la nota
    private boolean enEdicio = true;//Per comprovar si un altre usuari no està editant la nota
    private boolean preferida = false;//Comprovar si la nota està assignada a preferits per l'usuari
    private LocalDateTime dataModificacio = LocalDateTime.now();//La data de modigficació de la nota

    //Hi ha 2 constructors, un amb totes les dades i un buit amb les dades per defecte
    
    
    public Nota(int id, String titol, String contingut, boolean isEnEdicio, LocalDateTime dataModificacio, boolean preferida) {
        this.titol = titol;
        this.contingut = contingut;
        this.enEdicio = isEnEdicio;
        this.dataModificacio = dataModificacio;
        this.preferida = preferida;
        this.id = id;
        
    }
    
    public Nota() { 
    }
    
    /**
     * Permet actualitzar la data a l'actual
     */
    public void actualitzarData(){
        dataModificacio = LocalDateTime.now();
    }
    
    /**
     * Converteix la data en format LocalDateTime a sql timestamp
     * @return 
     */
    public Timestamp getSqlData(){
        return Timestamp.valueOf(dataModificacio);
    }

    /**
     * Getters i setters del títol, contingut, enEdicio, id i preferida
     * 
     */
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
    
     public boolean isPreferida() {
        return preferida;
    }

    public void setPreferida(boolean preferida) {
        this.preferida = preferida;
    }
    
    /**
     * El mètode toString retorna 3 línies: el títol, contingut (si és més de 25 caràcters es mostra amb ...) i data de modificació
     * @return 
     */
    @Override
    public String toString() {
        String contingutVisualitzacio = contingut;
        if (contingut.length() > 25) {
            contingutVisualitzacio = contingut.substring(0,25)+"...";
        }
        return titol + "\n" + contingutVisualitzacio+"\n"+"Data Modificació: "+dataModificacio.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

   
    
    
    
}
