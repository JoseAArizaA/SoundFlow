package org.jose.soundflow.DAO;


import org.jose.soundflow.baseDatos.ConnectionDB;
import org.jose.soundflow.exceptions.EnumIncorrectoException;
import org.jose.soundflow.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AudioDAO {
    private static final String SQL_SELECT_ALL = "SELECT * FROM Audio";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Audio WHERE idAudio = ?";
    private static final String SQL_UPDATE = "UPDATE Audio SET titulo = ?, artista = ?, descripcion = ?, duracion = ?, tipoAudio = ?, idUsuario = ? WHERE idAudio = ?";
    private static final String SQL_DELETE = "DELETE FROM Audio WHERE idAudio = ?";

    private static final String SQL_INSERT_AUDIO = "INSERT INTO audio (titulo, artista, descripcion, duracion, tipoAudio, idUsuario) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CANCION = "INSERT INTO cancion (idAudio, genero) VALUES (?, ?)";
    private static final String SQL_INSERT_PODCAST = "INSERT INTO podcast (idAudio, tematica) VALUES (?, ?)";
    private static final String SQL_INSERT_AUDIOLIBRO = "INSERT INTO audiolibro (idAudio, idioma) VALUES (?, ?)";

    private static final String SQL_SELECT_BY_USUARIO = "SELECT * FROM audio WHERE idUsuario = ?";
    private static final String SQL_SELECT_GENERO = "SELECT genero FROM cancion WHERE idAudio = ?";
    private static final String SQL_SELECT_TEMATICA = "SELECT tematica FROM podcast WHERE idAudio = ?";
    private static final String SQL_SELECT_IDIOMA = "SELECT idioma FROM audiolibro WHERE idAudio = ?";


    /**
     * Insertar un nuevo audio y su tipo específico (canción, podcast o audiolibro) con su atributo correspondiente
     * @param audio: objeto Audio a insertar
     * @return: true si se ha insertado correctamente, false en caso contrario
     **/
    public static boolean insertAudio(Audio audio) {
        boolean resultado = false;
        Connection con = ConnectionDB.getConnection();

        try (PreparedStatement pst = con.prepareStatement(SQL_INSERT_AUDIO, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, audio.getTitulo());
            pst.setString(2, audio.getArtista());
            pst.setString(3, audio.getDescripcion());
            pst.setInt(4, audio.getDuracion());
            pst.setString(5, audio.getTipoAudio().name());
            pst.setInt(6, audio.getUsuario().getIdUsuario());

            int filas = pst.executeUpdate();

            if (filas > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int idAudio = rs.getInt(1);

                    if (audio instanceof Cancion) {
                        try (PreparedStatement pstExtra = con.prepareStatement(SQL_INSERT_CANCION)) {
                            pstExtra.setInt(1, idAudio);
                            pstExtra.setString(2, ((Cancion) audio).getGenero());
                            resultado = pstExtra.executeUpdate() > 0;
                        }
                    } else if (audio instanceof Podcast) {
                        try (PreparedStatement pstExtra = con.prepareStatement(SQL_INSERT_PODCAST)) {
                            pstExtra.setInt(1, idAudio);
                            pstExtra.setString(2, ((Podcast) audio).getTematica());
                            resultado = pstExtra.executeUpdate() > 0;
                        }
                    } else if (audio instanceof AudioLibro) {
                        try (PreparedStatement pstExtra = con.prepareStatement(SQL_INSERT_AUDIOLIBRO)) {
                            pstExtra.setInt(1, idAudio);
                            pstExtra.setString(2, ((AudioLibro) audio).getIdioma());
                            resultado = pstExtra.executeUpdate() > 0;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }


    /**
     * Buscar todos los audios
     * @return:Lista de Audio con los datos de todos los audios
     */
    public static List<Audio> findAll() {
        List<Audio> audios = new ArrayList<>();
        Connection con = ConnectionDB.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                Audio audio = new Audio();
                audio.setIdAudio(rs.getInt("idAudio"));
                audio.setTitulo(rs.getString("titulo"));
                audio.setArtista(rs.getString("artista"));
                audio.setDescripcion(rs.getString("descripcion"));
                audio.setDuracion(rs.getInt("duracion"));
                audio.setTipoAudio(TipoContenido.valueOf(rs.getString("tipoAudio")));
                audio.setUsuario(null); // Lazy: no se carga el usuario
                audios.add(audio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audios;
    }

    // Leer todos VERSION EAGER
    public static List<Audio> findAllEager() {
        List<Audio> audios = new ArrayList<>();
        Connection con = ConnectionDB.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                Audio audio = new Audio();
                audio.setIdAudio(rs.getInt("idAudio"));
                audio.setTitulo(rs.getString("titulo"));
                audio.setArtista(rs.getString("artista"));
                audio.setDescripcion(rs.getString("descripcion"));
                audio.setDuracion(rs.getInt("duracion"));
                audio.setTipoAudio(TipoContenido.valueOf(rs.getString("tipoAudio")));

                // Cargar el usuario asociado (EAGER)
                int idUsuario = rs.getInt("idUsuario");
                Usuario usuario = UsuarioDAO.findByIdEager(idUsuario);
                audio.setUsuario(usuario);

                audios.add(audio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audios;
    }

    /**
     * Buscar por ID VERSION EAGER
     * @param idUsuarioBuscado: id del audio a buscar
     * @return: objeto Audio con los datos del audio encontrado, o null si no se encuentra
     **/
    public static List<Audio> findAudiosByUsuarioEager(int idUsuarioBuscado) {
        List<Audio> audios = new ArrayList<>();
        Connection con = ConnectionDB.getConnection();

        try (PreparedStatement pst = con.prepareStatement(SQL_SELECT_BY_USUARIO)) {
            pst.setInt(1, idUsuarioBuscado);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Audio audio;
                String tipo = rs.getString("tipoAudio");
                int idAudio = rs.getInt("idAudio");

                switch (tipo.toUpperCase()) {
                    case "CANCION":
                        audio = new Cancion();
                        try (PreparedStatement pst2 = con.prepareStatement(SQL_SELECT_GENERO)) {
                            pst2.setInt(1, idAudio);
                            ResultSet rs2 = pst2.executeQuery();
                            if (rs2.next()) {
                                ((Cancion) audio).setGenero(rs2.getString("genero"));
                            }
                        }
                        break;

                    case "PODCAST":
                        audio = new Podcast();
                        try (PreparedStatement pst2 = con.prepareStatement(SQL_SELECT_TEMATICA)) {
                            pst2.setInt(1, idAudio);
                            ResultSet rs2 = pst2.executeQuery();
                            if (rs2.next()) {
                                ((Podcast) audio).setTematica(rs2.getString("tematica"));
                            }
                        }
                        break;

                    case "AUDIOLIBRO":
                        audio = new AudioLibro();
                        try (PreparedStatement pst2 = con.prepareStatement(SQL_SELECT_IDIOMA)) {
                            pst2.setInt(1, idAudio);
                            ResultSet rs2 = pst2.executeQuery();
                            if (rs2.next()) {
                                ((AudioLibro) audio).setIdioma(rs2.getString("idioma"));
                            }
                        }
                        break;

                    default:
                        audio = new Audio();
                        break;
                }

                // Campos comunes
                audio.setIdAudio(idAudio);
                audio.setTitulo(rs.getString("titulo"));
                audio.setArtista(rs.getString("artista"));
                audio.setDescripcion(rs.getString("descripcion"));
                audio.setDuracion(rs.getInt("duracion"));

                try {
                    audio.setTipoAudio(TipoContenido.valueOf(tipo));
                } catch (IllegalArgumentException e) {
                    throw new EnumIncorrectoException("El tipo tiene que ser CANCION, PODCAST o AUDIOLIBRO y estar en mayúsculas.");
                }

                audios.add(audio);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return audios;
    }

    /**
     * Buscar por ID VERSION LAZY
     * @param id: id del audio a buscar
     * @return: objeto Audio con los datos del audio encontrado, o null si no se encuentra
     */
    public static Audio findById(int id) {
        Audio audio = null;
        Connection con = ConnectionDB.getConnection();
        try (PreparedStatement pst = con.prepareStatement(SQL_SELECT_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                audio = new Audio();
                audio.setIdAudio(rs.getInt("idAudio"));
                audio.setTitulo(rs.getString("titulo"));
                audio.setArtista(rs.getString("artista"));
                audio.setDescripcion(rs.getString("descripcion"));
                audio.setDuracion(rs.getInt("duracion"));
                audio.setTipoAudio(TipoContenido.valueOf(rs.getString("tipoAudio")));
                audio.setUsuario(null); // Lazy: no se carga el usuario
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audio;
    }

    /**
     * Actualizar
     * @param audio: objeto Audio a actualizar
     * @return: true si se ha actualizado correctamente, false en caso contrario
     **/
    public static boolean update(Audio audio) {
        boolean resultado = false;
        Connection con = ConnectionDB.getConnection();
        try (PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
            pst.setString(1, audio.getTitulo());
            pst.setString(2, audio.getArtista());
            pst.setString(3, audio.getDescripcion());
            pst.setInt(4, audio.getDuracion());
            pst.setString(5, audio.getTipoAudio().name());
            pst.setInt(6, audio.getUsuario().getIdUsuario());
            pst.setInt(7, audio.getIdAudio());
            resultado = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    /**
     * Eliminar por ID
     * @param id: ide del audio a eliminar
     * @return: true si se ha eliminado correctamente, false en caso contrario
     */
    public static boolean delete(int id) {
        boolean resultado = false;
        Connection con = ConnectionDB.getConnection();
        try (PreparedStatement pst = con.prepareStatement(SQL_DELETE)) {
            pst.setInt(1, id);
            resultado = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


}


