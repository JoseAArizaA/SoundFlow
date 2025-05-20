package org.jose.soundflow.DAO;


import org.jose.soundflow.baseDatos.ConnectionDB;
import org.jose.soundflow.model.Audio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelacionListaAudioDAO {
    private static final String SQL_INSERT = "INSERT INTO relacionlistaaudio (idLista, idAudio, fechaAgregada) VALUES (?, ?, CURDATE())";
    private static final String SQL_DELETE = "DELETE FROM RelacionListaAudio WHERE idLista = ? AND idAudio = ?";
    private static final String SQL_SELECT_AUDIOS_BY_LISTA = "SELECT idAudio FROM RelacionListaAudio WHERE idLista = ?";


    /**
     * Insertar una nueva relación entre una lista de reproducción y un audio.
     * @param idLista: ID de la lista de reproducción.
     * @param idAudio: ID del audio.
     * @return: true si la inserción fue exitosa, false en caso contrario.
     */
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

    /**
     * Eliminar una relación entre una lista de reproducción y un audio.
     * @param idLista: ID de la lista de reproducción.
     * @param idAudio: ID del audio.
     * @return: true si la eliminación fue exitosa, false en caso contrario.
     */
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

    /**
     * Buscar todos los audios asociados a una lista de reproducción.
     * @param idLista: ID de la lista de reproducción.
     * @return: Lista de audios asociados a la lista de reproducción.
     */
    public static List<Audio> findAudiosByListaId(int idLista) {
        List<Audio> audios = new ArrayList<>();

        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_AUDIOS_BY_LISTA)) {
            pst.setInt(1, idLista);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int idAudio = rs.getInt("idAudio");
                Audio audio = AudioDAO.findById(idAudio);
                if (audio != null) {
                    audios.add(audio);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return audios;
    }
}