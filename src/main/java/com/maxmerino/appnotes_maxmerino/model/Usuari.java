/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

/**
 *
 * @author alumne
 */
public class Usuari {
    private int idUsuari;
    private String nom;

    public Usuari(int idUsuari, String nom) {
        this.idUsuari = idUsuari;
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    public int getIdUsuari() {
        return idUsuari;
    }
    
    
    
}
