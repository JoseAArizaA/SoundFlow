package org.jose.soundflow.DAO;


import org.jose.soundflow.baseDatos.ConnectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelacionListaAudioDAO {
    private static final String SQL_INSERT = "INSERT INTO RelacionListaAudio (idLista, idAudio) VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM RelacionListaAudio WHERE idLista = ? AND idAudio = ?";
    private static final String SQL_SELECT_AUDIOS_BY_LISTA = "SELECT idAudio FROM RelacionListaAudio WHERE idLista = ?";
    private static final String SQL_SELECT_LISTAS_BY_AUDIO = "SELECT idLista FROM RelacionListaAudio WHERE idAudio = ?";


    // Insertar relación
    public static boolean insert(int idLista, int idAudio) {
        boolean resultado = false;
        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_INSERT)) {
            pst.setInt(1, idLista);
            pst.setInt(2, idAudio);
            resultado = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    // Eliminar relación
    public static boolean delete(int idLista, int idAudio) {
        boolean resultado = false;
        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_DELETE)) {
            pst.setInt(1, idLista);
            pst.setInt(2, idAudio);
            resultado = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    // Buscar audios por lista
    public static List<Integer> findAudiosByLista(int idLista) {
        List<Integer> audios = new ArrayList<>();
        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_AUDIOS_BY_LISTA)) {
            pst.setInt(1, idLista);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                audios.add(rs.getInt("idAudio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audios;
    }

    // Buscar listas por audio
    public static List<Integer> findListasByAudio(int idAudio) {
        List<Integer> listas = new ArrayList<>();
        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_LISTAS_BY_AUDIO)) {
            pst.setInt(1, idAudio);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                listas.add(rs.getInt("idLista"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listas;
    }
}