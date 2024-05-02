/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.maxmerino.appnotes_maxmerino.SistemaAlerta;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author alumne
 */
public class Model {

    private int id_usuari;
    private Nota notaActual;

    public Nota getNotaActual() {
        return notaActual;
    }

    public void setNotaActual(Nota notaActual) {
        this.notaActual = notaActual;
    }

    public int getId_usuari() {
        return id_usuari;
    }

    public void setId_usuari(int id_usuari) {
        this.id_usuari = id_usuari;
    }

    public boolean InsertarUsuari(Connection c, String nom, String correu, String contrasenya) {
        if (!isBuit(nom) && !isBuit(correu) && !isBuit(contrasenya) && correuValid(correu)) {
            try {

                PreparedStatement ss = c.prepareStatement("SELECT COUNT(*) AS count FROM usuaris WHERE correu = ? OR nom = ?");
                ss.setString(1, correu.toLowerCase());
                ss.setString(2, nom);
                ResultSet rs = ss.executeQuery();
                int count = 0;
                while (rs.next()) {
                    count = rs.getInt("count");
                }
                if (count == 0) {
                    String bcryptHashString = BCrypt.withDefaults().hashToString(12, contrasenya.toCharArray());
                    PreparedStatement s = c.prepareStatement("INSERT INTO usuaris(nom,correu,contrasenya) VALUES (?,?,?)");
                    s.setString(1, nom);
                    s.setString(2, correu);
                    s.setString(3, bcryptHashString);
                    s.execute();
                    return true;
                }

            } catch (Exception ex) {
                return false;
            }
        }
        return false;

    }

    public void SeleccionarUsuaris(Connection c) {
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM usuaris;");

            while (rs.next()) {
                System.out.println("Id:" + rs.getInt("id_usuari") + " Nom:" + rs.getString("nom") + " Correu:" + rs.getString("correu") + " Contrasenya:" + rs.getString("contrasenya"));
            }

        } catch (SQLException ex) {

        }
    }

    public int ComprovarUsuari(Connection c, String correu, String contrasenya) {
        int count = 0;
        int id_usuari = 0;
        String hash = "";
        if (!isBuit(correu) && !isBuit(contrasenya) && correuValid(correu)) {
            try {
                PreparedStatement ss = c.prepareStatement("SELECT COUNT(*) AS count, id_usuari, contrasenya  FROM usuaris WHERE correu = ?");
                ss.setString(1, correu);

                ResultSet rs = ss.executeQuery();
                while (rs.next()) {
                    count = rs.getInt("count");
                    id_usuari = rs.getInt("id_usuari");
                    hash = rs.getString("contrasenya");
                }
                if (count > 0) {
                    BCrypt.Result result = BCrypt.verifyer().verify(contrasenya.toCharArray(), hash);
                    if (result.verified) {

                        return id_usuari;
                    }
                }
            } catch (Exception ex) {

                return -1;
            }
        }
        return -1;
    }

    public void recollirLlistaUsuaris(Connection c, String query, int id_actual) throws SQLException {
        HashMap<String, Integer> mapaUsuaris = new HashMap();
        ArrayList<String> nomsUsuaris = new ArrayList();

        PreparedStatement s = c.prepareStatement("SELECT id_usuari, nom FROM usuaris WHERE id_usuari != ? AND nom LIKE '%" + query + "%';");
        s.setInt(1, id_actual);

        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id_usuari");
            String nom = rs.getString("nom");
            nomsUsuaris.add(nom);
            mapaUsuaris.put(nom, id);
        }

        for (String nomUsuari : nomsUsuaris) {
            System.out.println(nomUsuari + ": " + mapaUsuaris.get(nomUsuari));

        }
    }

    public boolean isBuit(String cadena) {
        return cadena.length() == 0;
    }

    public boolean correuValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void afegirNota(Connection c, Nota nota, boolean preferida) {
        String titol = nota.getTitol();
        String contingut = nota.getContingut();
        boolean enEdicio = nota.isEnEdicio();
        Timestamp data = nota.getSqlData();
        int idNota = 0;

        try {
            PreparedStatement s;
            s = c.prepareStatement("INSERT INTO notes(titol,contingut,is_en_edicio,data_modificacio) VALUES (?,?,?,?)");
            s.setString(1, titol);
            s.setString(2, contingut);
            s.setBoolean(3, enEdicio);
            s.setTimestamp(4, data);
            s.execute();

            s = c.prepareStatement("SELECT LAST_INSERT_ID() as id FROM notes");
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                idNota = rs.getInt("id");
            }

            s = c.prepareStatement("INSERT INTO notes_usuaris(id_nota,id_usuari,is_preferida) VALUES (?,?,?)");
            s.setInt(1, idNota);
            s.setInt(2, id_usuari);
            s.setBoolean(3, preferida);

            s.execute();
        } catch (Exception ex) {
            SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades");
        }
    }

    public ObservableList<Nota> visualitzarNotes(Connection c, boolean nomesPreferides) {
        ObservableList<Nota> notes = FXCollections.observableArrayList();
        PreparedStatement s;
        try {
            String consultaBase = "SELECT notes.id_nota, titol, contingut, is_en_edicio, data_modificacio, is_preferida FROM notes INNER JOIN notes_usuaris ON notes.id_nota = notes_usuaris.id_nota WHERE id_usuari = ?";
            if (nomesPreferides) {
                s = c.prepareStatement(consultaBase + " AND is_preferida");
            } else {
                s = c.prepareStatement(consultaBase);
            }
            s.setInt(1, id_usuari);
            ResultSet resultat = s.executeQuery();
            while (resultat.next()) {
                Nota notaTupla = new Nota(resultat.getInt("id_nota"), resultat.getString("titol"), resultat.getString("contingut"), resultat.getBoolean("is_en_edicio"), resultat.getTimestamp("data_modificacio").toLocalDateTime(), resultat.getBoolean("is_preferida"));
                notes.add(notaTupla);
            }
            return notes;

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades");
            return notes;
        }

    }

    public void esborrarNota(Connection c, Nota nota) {
        int countCompartit = 0;
        PreparedStatement s;
        try {
            s = c.prepareStatement("SELECT COUNT(id_nota) as count FROM notes_usuaris WHERE id_nota = ?");
            s.setInt(1, nota.getId());
            ResultSet resultat = s.executeQuery();
            if (resultat.next()) {
                countCompartit = resultat.getInt("count");
            }
            if (countCompartit > 1) {
                s = c.prepareStatement("DELETE FROM notes_usuaris WHERE id_nota = ? AND id_usuari = ?");
                s.setInt(1, nota.getId());
                s.setInt(2, id_usuari);
                s.execute();
            } else {
                s = c.prepareStatement("DELETE FROM notes WHERE id_nota = ?");
                s.setInt(1, nota.getId());

                s.execute();
            }

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades");
        }

    }

    public void modificarNota(Connection c, Nota nota, boolean preferida) {
        //TODO: Programar data i en edicio
        PreparedStatement s;
        try {
            s = c.prepareStatement("UPDATE notes SET titol = ?, contingut = ?, data_modificacio= ? WHERE id_nota = ?");
            s.setString(1, nota.getTitol());
            s.setString(2, nota.getContingut());
            nota.actualitzarData();
            s.setTimestamp(3, nota.getSqlData());
            s.setInt(4, nota.getId());
            s.execute();

            s = c.prepareStatement("UPDATE notes_usuaris SET is_preferida = ? WHERE id_usuari = ? AND id_nota = ?");
            s.setBoolean(1, preferida);
            s.setInt(2, id_usuari);
            s.setInt(3, nota.getId());
            s.execute();
        } catch (Exception ex) {
            SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades");
        }
    }

    public void afegirCategoria(Connection c, String nomCategoria) {
        int idCategoria = -1;
        int count = 0;
        try {
            PreparedStatement s;
            s = c.prepareStatement("SELECT COUNT(*) AS countCategoria FROM categories WHERE nom_categoria = ? AND id_usuari = ?");
            s.setString(1, nomCategoria);
            s.setInt(2, id_usuari);

            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                count = rs.getInt("countCategoria");
            }

            if (count == 0) {
                s = c.prepareStatement("INSERT INTO categories(nom_categoria,id_usuari) VALUES (?,?)");
                s.setString(1, nomCategoria);
                s.setInt(2, id_usuari);
                s.execute();
            } else {
                SistemaAlerta.alerta("Aquesta categoria ja existeix!");
            }

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error a la inserció de categoria" + ": " + ex.getMessage());
        }
    }

    public void vincularCategoria(Connection c, String nomCategoria) {
        int idCategoria = -1;
        PreparedStatement s;
        try {
            s = c.prepareStatement("SELECT id_categoria FROM categories WHERE nom_categoria = ? AND id_usuari = ?");
            s.setString(1, nomCategoria);
            s.setInt(2, id_usuari);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                idCategoria = rs.getInt("id_categoria");
            }
            
            s = c.prepareStatement("INSERT INTO notes_categories(id_nota,id_categoria) VALUES (?,?)");
            s.setInt(1, notaActual.getId());
            s.setInt(2, idCategoria);

            s.execute();
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error a la vinculació de categoria" + ": " + ex.getMessage());
        }

    }
    
    public ObservableList<String> visualitzarCategoriesTotals(Connection c) {
        ObservableList<String> categories = FXCollections.observableArrayList();
        PreparedStatement s;
        try {
            
            s = c.prepareStatement("SELECT nom_categoria FROM categories WHERE id_usuari = ?");
           
            s.setInt(1, id_usuari);
            ResultSet resultat = s.executeQuery();
            while (resultat.next()) {
                categories.add(resultat.getString("nom_categoria"));
            }
            return categories;

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades");
            return categories;
        }

    }
    
    public ObservableList<String> visualitzarCategoriesNota(Connection c) {
        ObservableList<String> categories = FXCollections.observableArrayList();
        PreparedStatement s;
        try {
            
            s = c.prepareStatement("SELECT nom_categoria FROM categories INNER JOIN notes_categories ON categories.id_categoria = notes_categories.id_categoria WHERE id_nota = ? AND id_usuari = ?");
           
            s.setInt(1, notaActual.getId());
            s.setInt(2, id_usuari);
            ResultSet resultat = s.executeQuery();
            while (resultat.next()) {
                categories.add(resultat.getString("nom_categoria"));
            }
            return categories;

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error de connexió amb el servidor de Bases de Dades");
            return categories;
        }

    }

}
