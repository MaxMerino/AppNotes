/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

/**
 *
 * @author max2442
 */
public class Etiqueta {
    private int idEtiqueta;
    private String nom;

    public Etiqueta(int idEtiqueta, String nom) {
        this.idEtiqueta = idEtiqueta;
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    public int getIdEtiqueta() {
        return idEtiqueta;
    }

    public String getNom() {
        return nom;
    }
    
}
