package org.jose.soundflow.DAO;


import org.jose.soundflow.baseDatos.ConnectionDB;
import org.jose.soundflow.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final String SQL_INSERT = "INSERT INTO Usuario (nombre, correo, password) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Usuario WHERE idUsuario = ?";
    private static final String SQL_UPDATE = "UPDATE Usuario SET nombre = ?, correo = ? WHERE idUsuario = ?";
    private static final String SQL_DELETE = "DELETE FROM Usuario WHERE idUsuario = ?";
    private static final String SQL_VALIDAR_USUARIO_COMPLETO = "SELECT * FROM Usuario WHERE nombre = ? AND password = ?";


    /**
     * Insertar un nuevo usuario en la base de datos.
     * @param usuario: Usuario que se va a insertar.
     * @return: true si la inserción fue exitosa, false en caso contrario.
     */
    public static boolean insert(Usuario usuario) {
        boolean resultado = false;
        Connection con = ConnectionDB.getConnection();
        try (PreparedStatement pst = con.prepareStatement(SQL_INSERT)) {
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getCorreo());
            pst.setString(3, usuario.getPassword());
            resultado = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    /**
     *  Buscar a un usuario por su ID VERSION LAZY.
     * @param id: id del usuario
     * @return: Usuario que se va a buscar
     */
    public static Usuario findById(int id) {
        Usuario usuario = null;
        Connection con = ConnectionDB.getConnection();
        try (PreparedStatement pst = con.prepareStatement(SQL_SELECT_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }


    /**
     * Buscar a un usuario por su ID VERSION EAGER.
     * @return: Lista de usuarios.
     */
    public static Usuario findByIdEager(int id) {
        Usuario usuario = null;
        Connection con = ConnectionDB.getConnection();

        try (PreparedStatement pstmt = con.prepareStatement(SQL_SELECT_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));

                // Cargar relaciones asociadas (EAGER)
                usuario.setMisAudios(new ArrayList<>(AudioDAO.findAudiosByUsuarioEager(id)));
                usuario.setMisListasReproduccion(new ArrayList<>(ListaReproduccionDAO.findByUsuario(id)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    // Actualizar
    public static boolean update(Usuario usuario) {
        boolean resultado = false;
        Connection con = ConnectionDB.getConnection();
        try (PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getCorreo());
            pst.setString(3, usuario.getPassword());
            pst.setInt(4, usuario.getIdUsuario());

            resultado = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Eliminar un usuario de la base de datos.
     * @param id: id del usuario que se va a eliminar
     * @return: true si la eliminación fue exitosa, false en caso contrario.
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


    public static Usuario findByNombreYPassword(String nombre, String password) {
        Usuario usuario = null;

        Connection con = ConnectionDB.getConnection();
        try (PreparedStatement pst = con.prepareStatement(SQL_VALIDAR_USUARIO_COMPLETO)) {
            pst.setString(1, nombre);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

}
