package org.jose.soundflow.DAO;


import org.jose.soundflow.baseDatos.ConnectionDB;
import org.jose.soundflow.exceptions.EnumIncorrectoException;
import org.jose.soundflow.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AudioDAO {
    private static final String SQL_SELECT_ALL = "SELECT * FROM audio";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Audio WHERE idAudio = ?";
    private static final String SQL_UPDATE = "UPDATE audio SET titulo = ?, artista = ?, descripcion = ?, duracion = ?, idUsuario = ? WHERE idAudio = ?";
    private static final String SQL_DELETE = "DELETE FROM Audio WHERE idAudio = ?";

    private static final String SQL_INSERT_AUDIO = "INSERT INTO audio (titulo, artista, descripcion, duracion, idUsuario) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CANCION = "INSERT INTO cancion (idAudio, genero) VALUES (?, ?)";
    private static final String SQL_INSERT_PODCAST = "INSERT INTO podcast (idAudio, tematica) VALUES (?, ?)";
    private static final String SQL_INSERT_AUDIOLIBRO = "INSERT INTO audiolibro (idAudio, idioma) VALUES (?, ?)";

    private static final String SQL_SELECT_BY_USUARIO = "SELECT * FROM audio WHERE idUsuario = ?";

    //Cambios 28/05
    private static final String SQL_EXISTE_EN_TABLA_CANCION = "SELECT 1 FROM cancion WHERE idAudio = ?";
    private static final String SQL_EXISTE_EN_TABLA_PODCAST = "SELECT 1 FROM podcast WHERE idAudio = ?";
    private static final String SQL_EXISTE_EN_TABLA_AUDIOLIBRO = "SELECT 1 FROM audiolibro WHERE idAudio = ?";

    private static final String SQL_CAMPO_EXTRA_CANCION = "SELECT genero FROM cancion WHERE idAudio = ?";
    private static final String SQL_CAMPO_EXTRA_PODCAST = "SELECT tematica FROM podcast WHERE idAudio = ?";
    private static final String SQL_CAMPO_EXTRA_AUDIOLIBRO = "SELECT idioma FROM audiolibro WHERE idAudio = ?";




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
            pst.setInt(5, audio.getUsuario().getIdUsuario());

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
    // Cambios 28/05
    public static List<Audio> findAll() {
        List<Audio> audios = new ArrayList<>();
        Connection con = ConnectionDB.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                int idAudio = rs.getInt("idAudio");
                Audio audio = findById(idAudio);
                if (audio != null) {
                    audios.add(audio);
                }
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

                // Cargar el usuario asociado (EAGER)
                int idUsuario = rs.getInt("idUsuario");
                Usuario usuario = UsuarioDAO.findByIdEager(idUsuario);
                audio.setUsuario(usuario);

                //Cargar las relaciones
                ArrayList<RelacionListaAudio> relaciones = new ArrayList<>(RelacionListaAudioDAO.findByIdAudio(audio.getIdAudio()));
                audio.setRelaciones(relaciones);

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
                int idAudio = rs.getInt("idAudio");
                String titulo = rs.getString("titulo");
                String artista = rs.getString("artista");
                String descripcion = rs.getString("descripcion");
                int duracion = rs.getInt("duracion");

                Audio audio;

                if (existeEnLaTabla("cancion", idAudio)) {
                    Cancion c = new Cancion();
                    c.setGenero(getCampoExtra("cancion", "genero", idAudio));
                    audio = c;
                } else if (existeEnLaTabla("podcast", idAudio)) {
                    Podcast p = new Podcast();
                    p.setTematica(getCampoExtra("podcast", "tematica", idAudio));
                    audio = p;
                } else if (existeEnLaTabla("audiolibro", idAudio)) {
                    AudioLibro al = new AudioLibro();
                    al.setIdioma(getCampoExtra("audiolibro", "idioma", idAudio));
                    audio = al;
                } else {
                    audio = new Audio();
                }

                // Datos comunes
                audio.setIdAudio(idAudio);
                audio.setTitulo(titulo);
                audio.setArtista(artista);
                audio.setDescripcion(descripcion);
                audio.setDuracion(duracion);
                audio.setUsuario(UsuarioDAO.findById(idUsuarioBuscado));

                audios.add(audio);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return audios;
    }

    // Cambios 28/05
    private static boolean existeEnLaTabla(String tabla, int idAudio) {
        String sql;

        if (tabla.equals("cancion")) {
            sql = SQL_EXISTE_EN_TABLA_CANCION;
        } else if (tabla.equals("podcast")) {
            sql = SQL_EXISTE_EN_TABLA_PODCAST;
        } else if (tabla.equals("audiolibro")) {
            sql = SQL_EXISTE_EN_TABLA_AUDIOLIBRO;
        } else {
            throw new IllegalArgumentException("Tabla no válida: " + tabla);
        }

        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(sql)) {
            pst.setInt(1, idAudio);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Cambios 28/05
    private static String getCampoExtra(String tabla, String campo, int idAudio) {
        String sql;

        if (tabla.equals("cancion")) {
            sql = SQL_CAMPO_EXTRA_CANCION;
        } else if (tabla.equals("podcast")) {
            sql = SQL_CAMPO_EXTRA_PODCAST;
        } else if (tabla.equals("audiolibro")) {
            sql = SQL_CAMPO_EXTRA_AUDIOLIBRO;
        } else {
            throw new IllegalArgumentException("Tabla no válida: " + tabla);
        }

        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(sql)) {
            pst.setInt(1, idAudio);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
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
                audio.setUsuario(null); // Lazy: no se carga el usuario
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audio;
    }

    public static Audio findByIdEagerSinUsuario(int id) {
        Audio audio = null;
        Connection con = ConnectionDB.getConnection();

        try (PreparedStatement pst = con.prepareStatement(SQL_SELECT_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int idAudio = rs.getInt("idAudio");
                String titulo = rs.getString("titulo");
                String artista = rs.getString("artista");
                String descripcion = rs.getString("descripcion");
                int duracion = rs.getInt("duracion");

                if (existeEnLaTabla("cancion", idAudio)) {
                    Cancion c = new Cancion();
                    c.setGenero(getCampoExtra("cancion", "genero", idAudio));
                    audio = c;
                } else if (existeEnLaTabla("podcast", idAudio)) {
                    Podcast p = new Podcast();
                    p.setTematica(getCampoExtra("podcast", "tematica", idAudio));
                    audio = p;
                } else if (existeEnLaTabla("audiolibro", idAudio)) {
                    AudioLibro a = new AudioLibro();
                    a.setIdioma(getCampoExtra("audiolibro", "idioma", idAudio));
                    audio = a;
                } else {
                    audio = new Audio(); // tipo desconocido
                }

                // Setear atributos comunes
                audio.setIdAudio(idAudio);
                audio.setTitulo(titulo);
                audio.setArtista(artista);
                audio.setDescripcion(descripcion);
                audio.setDuracion(duracion);
                audio.setUsuario(null); // ⚠ NO cargar usuario para evitar bucles
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
            pst.setInt(5, audio.getUsuario().getIdUsuario());
            pst.setInt(6, audio.getIdAudio());
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


