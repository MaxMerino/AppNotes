/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxmerino.appnotes_maxmerino.model;

import at.favre.lib.crypto.bcrypt.BCrypt;
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

    public int InsertarUsuari(Connection c, String nom, String correu, String contrasenya) {
        if (!isBuit(nom) && !isBuit(correu) && !isBuit(contrasenya) && contrasenya.length() >= 8 && correuValid(correu)) {
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
                    return 1;
                }

            } catch (Exception ex) {
                return 0;
            }
        }
        return  -1;

    }

    public int ComprovarUsuari(Connection c, String correu, String contrasenya) {
        int count = 0;
        int id_usuari = 0;
        String hash = "";
        if (!isBuit(correu) && !isBuit(contrasenya) && contrasenya.length() >= 8 && correuValid(correu)) {
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

                return 0;
            }
        }
        return -1;
    }

    public ObservableList<Usuari> recollirLlistaUsuaris(Connection c, String query) {
        ObservableList<Usuari> usuaris = FXCollections.observableArrayList();

        PreparedStatement s;
        try {
            s = c.prepareStatement("SELECT id_usuari, nom FROM usuaris WHERE nom LIKE '%" + query + "%' AND id_usuari NOT IN(SELECT id_usuari FROM notes_usuaris WHERE id_nota = ?)");
           
            s.setInt(1, notaActual.getId());

            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_usuari");
                String nom = rs.getString("nom");
                usuaris.add(new Usuari(id,nom));
            }
            
           
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error amb el servidor de Bases de Dades"+ex.getMessage());
        }
        return usuaris;
    }
    public void eliminarUsuari(Connection c){
        PreparedStatement s;
        try {
            s = c.prepareStatement("DELETE FROM notes WHERE id_nota IN (SELECT id_nota FROM notes_usuaris WHERE id_usuari = ? AND id_nota IN  (SELECT id_nota FROM notes_usuaris GROUP BY id_nota HAVING COUNT(id_nota) = 1))");
            s.setInt(1, id_usuari);
            s.execute();
            s = c.prepareStatement("DELETE FROM usuaris WHERE id_usuari = ?");
            s.setInt(1, id_usuari);
            s.execute();
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error amb l'eliminació del compte");
        }
    }
    public void compartirNota(Connection c, Usuari usuari) {
        
        int count = 0;
        PreparedStatement s;
        try {
            s = c.prepareStatement("SELECT COUNT(*) as count FROM notes_usuaris WHERE id_nota = ? AND id_usuari = ?");
            s.setInt(1, notaActual.getId());
            s.setInt(2, usuari.getIdUsuari());

            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
            if (count == 0) {
                s = c.prepareStatement("INSERT INTO notes_usuaris VALUES (?,?,0)");
                s.setInt(1, notaActual.getId());
                s.setInt(2, usuari.getIdUsuari());
                s.execute();
            }
           
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error en compartir nota");
        }
        
    }

    public boolean isBuit(String cadena) {
        return cadena.length() == 0;
    }

    public boolean correuValid(String correu) {
        String patro = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(patro);
        java.util.regex.Matcher m = p.matcher(correu);
        return m.matches();
    }

    public Nota afegirNota(Connection c, Nota nota, boolean preferida) {
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
            nota.setId(idNota);
            return nota;
        } catch (Exception ex) {
            SistemaAlerta.alerta("Error en afegir la nota");
            return nota;
        }
    }

    public Nota notaPerId(Connection c, int id) {
        PreparedStatement s;
        Nota notaRetorn = new Nota();
        try {
            s = c.prepareStatement("SELECT notes.id_nota, titol, contingut, is_en_edicio, data_modificacio, is_preferida FROM notes INNER JOIN notes_usuaris ON notes.id_nota = notes_usuaris.id_nota WHERE notes.id_nota = ? AND notes_usuaris.id_usuari = ?");
            s.setInt(1, id);
            s.setInt(2, id_usuari);
            ResultSet resultat = s.executeQuery();
            if (resultat.next()) {
                notaRetorn = new Nota(resultat.getInt("id_nota"), resultat.getString("titol"), resultat.getString("contingut"), resultat.getBoolean("is_en_edicio"), resultat.getTimestamp("data_modificacio").toLocalDateTime(), resultat.getBoolean("is_preferida"));
            }
            
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error amb el servidor de Bases de Dades");

        }
        return notaRetorn;
    }

    public void canviarEstatEdicio(Connection c, boolean edicio, int id) {
        PreparedStatement s;
        Nota notaRetorn = new Nota();
        try {
            s = c.prepareStatement("UPDATE notes SET is_en_edicio = ? WHERE id_nota = ?");
            s.setBoolean(1, edicio);
            s.setInt(2, id);
            s.execute();
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error amb el servidor de Bases de Dades");

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
            SistemaAlerta.alerta("Error en carregar les notes");
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
            SistemaAlerta.alerta("Error en esborrar una nota");
        }

    }

    public void modificarNota(Connection c, Nota nota, boolean preferida) {

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
            SistemaAlerta.alerta("Error en modificar una nota");
        }
    }

    public void afegirEtiqueta(Connection c, String nomEtiqueta) {
        int idEtiqueta = -1;
        int count = 0;
        try {
            PreparedStatement s;
            s = c.prepareStatement("SELECT COUNT(*) AS countEtiqueta FROM etiquetes WHERE nom_etiqueta = ? AND id_usuari = ?");
            s.setString(1, nomEtiqueta);
            s.setInt(2, id_usuari);

            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                count = rs.getInt("countEtiqueta");
            }

            if (count == 0) {
                s = c.prepareStatement("INSERT INTO etiquetes(nom_etiqueta,id_usuari) VALUES (?,?)");
                s.setString(1, nomEtiqueta);
                s.setInt(2, id_usuari);
                s.execute();
                SistemaAlerta.alerta("Etiqueta afegida correctament");
            } else {
                SistemaAlerta.alerta("Aquesta etiqueta ja existeix!");
            }

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error a la inserció de etiqueta");
        }
    }

    public void vincularEtiqueta(Connection c, Etiqueta etiqueta) {
        int idEtiqueta = -1;
        PreparedStatement s;
        try {
            s = c.prepareStatement("SELECT id_etiqueta FROM etiquetes WHERE nom_etiqueta = ? AND id_usuari = ?");
            s.setString(1, etiqueta.getNom());
            s.setInt(2, id_usuari);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                idEtiqueta = rs.getInt("id_etiqueta");
            }

            s = c.prepareStatement("INSERT INTO notes_etiquetes(id_nota,id_etiqueta) VALUES (?,?)");
            s.setInt(1, notaActual.getId());
            s.setInt(2, idEtiqueta);

            s.execute();
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error a la vinculació de etiqueta");
        }

    }

    public ObservableList<Etiqueta> visualitzarEtiquetesTotals(Connection c) {
        ObservableList<Etiqueta> etiquetes = FXCollections.observableArrayList();
        PreparedStatement s;
        try {

            s = c.prepareStatement("SELECT id_etiqueta, nom_etiqueta FROM etiquetes WHERE id_usuari = ?");

            s.setInt(1, id_usuari);

            ResultSet resultat = s.executeQuery();
            while (resultat.next()) {
                etiquetes.add(new Etiqueta(resultat.getInt("id_etiqueta"), resultat.getString("nom_etiqueta")));
            }
            return etiquetes;

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error a la visualització d'etiquetes");
            return etiquetes;
        }

    }

    public ObservableList<Etiqueta> visualitzarEtiquetesNoVinculades(Connection c) {
        ObservableList<Etiqueta> etiquetes = FXCollections.observableArrayList();
        PreparedStatement s;
        try {

            s = c.prepareStatement("SELECT id_etiqueta, nom_etiqueta FROM etiquetes WHERE id_usuari = ? AND id_etiqueta NOT IN (SELECT id_etiqueta FROM notes_etiquetes WHERE id_nota = ?)");

            s.setInt(1, id_usuari);
            s.setInt(2, notaActual.getId());
            ResultSet resultat = s.executeQuery();
            while (resultat.next()) {
                etiquetes.add(new Etiqueta(resultat.getInt("id_etiqueta"), resultat.getString("nom_etiqueta")));
            }
            return etiquetes;

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error a la visualització d'etiquetes");
            return etiquetes;
        }

    }

    public ObservableList<Etiqueta> visualitzarEtiquetesNota(Connection c) {
        ObservableList<Etiqueta> etiquetes = FXCollections.observableArrayList();
        PreparedStatement s;
        try {

            s = c.prepareStatement("SELECT etiquetes.id_etiqueta, nom_etiqueta FROM etiquetes INNER JOIN notes_etiquetes ON etiquetes.id_etiqueta = notes_etiquetes.id_etiqueta WHERE id_nota = ? AND id_usuari = ?");

            s.setInt(1, notaActual.getId());
            s.setInt(2, id_usuari);
            ResultSet resultat = s.executeQuery();
            while (resultat.next()) {
                etiquetes.add(new Etiqueta(resultat.getInt("id_etiqueta"), resultat.getString("nom_etiqueta")));
            }
            return etiquetes;

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error a la visualització d'etiquetes"+ex.getMessage());
            return etiquetes;
        }

    }

    public void esborrarEtiquetaTotalment(Connection c, String nom_etiqueta) {
        PreparedStatement s;
        try {
            s = c.prepareStatement("DELETE FROM etiquetes WHERE id_usuari= ? AND nom_etiqueta = ?");
            s.setInt(1, id_usuari);
            s.setString(2, nom_etiqueta);
            s.execute();
            SistemaAlerta.alerta("Etiqueta eliminada correctament");
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error a la eliminació de etiqueta");
        }

    }

    public void desvincularEtiqueta(Connection c, Etiqueta etiqueta) {
        int idEtiqueta = -1;
        PreparedStatement s;
        try {
            s = c.prepareStatement("DELETE FROM notes_etiquetes WHERE id_etiqueta = ? AND id_nota = ?");
            s.setInt(1, etiqueta.getIdEtiqueta());
            s.setInt(2, notaActual.getId());
            s.execute();
        } catch (SQLException ex) {
            SistemaAlerta.alerta("Error a la desvinculació d'etiqueta");
        }

    }

    public ObservableList<Nota> filtrarNotes(Connection c, Etiqueta etiqueta) {
        ObservableList<Nota> notes = FXCollections.observableArrayList();
        PreparedStatement s;
        try {

            s = c.prepareStatement("SELECT notes.id_nota, titol, contingut, is_en_edicio, data_modificacio, is_preferida FROM notes INNER JOIN notes_usuaris ON notes.id_nota = notes_usuaris.id_nota\n" +
"INNER JOIN notes_etiquetes ON notes.id_nota = notes_etiquetes.id_nota WHERE notes_usuaris.id_usuari = ? AND notes_etiquetes.id_etiqueta = ?");

            s.setInt(1, id_usuari);
            s.setInt(2, etiqueta.getIdEtiqueta());
            ResultSet resultat = s.executeQuery();
            while (resultat.next()) {
                Nota notaTupla = new Nota(resultat.getInt("id_nota"), resultat.getString("titol"), resultat.getString("contingut"), resultat.getBoolean("is_en_edicio"), resultat.getTimestamp("data_modificacio").toLocalDateTime(), resultat.getBoolean("is_preferida"));
                notes.add(notaTupla);
            }
            return notes;

        } catch (Exception ex) {
            SistemaAlerta.alerta("Error al filtrat de notes:"+ex.getMessage());
            return notes;
        }

    }

}