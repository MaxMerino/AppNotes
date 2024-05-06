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
    private int idUsuari;//Id de l'usuari de la base de dades
    private String nom;//Nom de l'usuari

    //Hi ha un constructor amb la id i el nom d'usuari
    public Usuari(int idUsuari, String nom) {
        this.idUsuari = idUsuari;
        this.nom = nom;
    }

    //El mètode toString retorna el nom d'usuari
    @Override
    public String toString() {
        return nom;
    }

    //Un getter per la id d'usuari
    public int getIdUsuari() {
        return idUsuari;
    }
    
    
    
}
