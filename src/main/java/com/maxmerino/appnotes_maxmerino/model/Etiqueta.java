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
    private int idEtiqueta;//Id de l'etiqueta a la base de dades
    private String nom;//Nom de l'etiqueta

    //Hi ha un constructor amb la id i el nom d'etiqueta
    public Etiqueta(int idEtiqueta, String nom) {
        this.idEtiqueta = idEtiqueta;
        this.nom = nom;
    }

    //El mètode toString retorna el nom d'etiqueta
    @Override
    public String toString() {
        return nom;
    }
    //Un getter per la id d'etiqueta
    public int getIdEtiqueta() {
        return idEtiqueta;
    }
    //Un getter pel nom d'etiqueta
    public String getNom() {
        return nom;
    }
    
}
