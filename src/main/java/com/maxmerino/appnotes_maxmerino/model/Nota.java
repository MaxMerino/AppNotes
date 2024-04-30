/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author alumne
 */
public class Nota {
    private int id = -1;
    private String titol, contingut;
    private boolean isEnEdicio;
    private LocalDate dataModificacio = LocalDate.now();

    public Nota(String titol, String contingut, boolean isEnEdicio, LocalDate dataModificacio) {
        
        this.titol = titol;
        this.contingut = contingut;
        this.isEnEdicio = isEnEdicio;
        this.dataModificacio = dataModificacio;
    }
    
    public Nota(int id, String titol, String contingut, boolean isEnEdicio, LocalDate dataModificacio) {
        this(titol,contingut,isEnEdicio,dataModificacio);
        this.id = id;
        
    }
    
    public Nota() {
        this.titol = "";
        this.contingut = "";
        this.isEnEdicio = true;
        
    }
    
    public void actualitzarData(){
        dataModificacio = LocalDate.now();
    }
    
    public Date getSqlData(){
        return Date.valueOf(dataModificacio);
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

    public boolean isIsEnEdicio() {
        return isEnEdicio;
    }

    public void setIsEnEdicio(boolean isEnEdicio) {
        this.isEnEdicio = isEnEdicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return titol + "\n" + contingut;
    }
    
    
    
}
