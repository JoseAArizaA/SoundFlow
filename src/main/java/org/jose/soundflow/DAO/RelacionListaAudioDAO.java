package org.jose.soundflow.DAO;


import org.jose.soundflow.baseDatos.ConnectionDB;
import org.jose.soundflow.model.Audio;
import org.jose.soundflow.model.ListaReproduccion;
import org.jose.soundflow.model.RelacionListaAudio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelacionListaAudioDAO {
    private static final String SQL_INSERT = "INSERT INTO relacionlistaaudio (idLista, idAudio, fechaAgregada) VALUES (?, ?, CURDATE())";
    private static final String SQL_DELETE = "DELETE FROM RelacionListaAudio WHERE idLista = ? AND idAudio = ?";
    private static final String SQL_SELECT_AUDIOS_BY_LISTA = "SELECT idAudio FROM RelacionListaAudio WHERE idLista = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM relacionListaAudio WHERE idLista = ?";

    private static final String SQL_SELECT_ALL_ID_AUDIO = "SELECT * FROM relacionListaAudio WHERE idAudio = ?";

    /**
     * Insertar una nueva relación entre una lista de reproducción y un audio.
     *
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
     *
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
     *
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

    //Nuevo metoto
    public static List<RelacionListaAudio> findByListaId(int idLista) {
        List<RelacionListaAudio> relaciones = new ArrayList<>();

        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_ALL)) {
            pst.setInt(1, idLista);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int idAudio = rs.getInt("idAudio");
                Audio audio = AudioDAO.findByIdEagerSinUsuario(idAudio);
                LocalDate fechaAgregada = rs.getDate("fechaAgregada").toLocalDate();

                ListaReproduccion lista = new ListaReproduccion();
                lista.setIdLista(idLista);

                RelacionListaAudio relacion = new RelacionListaAudio(lista, audio, fechaAgregada);
                relaciones.add(relacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relaciones;
    }

    public static List<RelacionListaAudio> findByIdAudio(int idAudio) {
        List<RelacionListaAudio> relaciones = new ArrayList<>();

        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_ALL_ID_AUDIO)) {
            pst.setInt(1, idAudio);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int idLista = rs.getInt("idLista");
                ListaReproduccion lista = new ListaReproduccion();
                lista.setIdLista(idLista);

                Audio audio = AudioDAO.findByIdEagerSinUsuario(idAudio);
                LocalDate fechaAgregada = rs.getDate("fechaAgregada").toLocalDate();

                RelacionListaAudio relacion = new RelacionListaAudio(lista, audio, fechaAgregada);
                relaciones.add(relacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relaciones;

    }

}
