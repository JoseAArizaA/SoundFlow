package org.jose.soundflow.DAO;


import org.jose.soundflow.baseDatos.ConnectionDB;
import org.jose.soundflow.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final String SQL_INSERT = "INSERT INTO Usuario (nombre, correo, password) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM Usuario";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Usuario WHERE idUsuario = ?";
    private static final String SQL_UPDATE = "UPDATE Usuario SET nombre = ?, correo = ? WHERE idUsuario = ?";
    private static final String SQL_DELETE = "DELETE FROM Usuario WHERE idUsuario = ?";
    private static final String SQL_VALIDAR_USUARIO_COMPLETO = "SELECT * FROM Usuario WHERE nombre = ? AND password = ?";


    // Crear (Insertar)
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


    // Leer todos
    public static List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = ConnectionDB.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public static List<Usuario> findAllEager() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = ConnectionDB.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                int idUsuario = rs.getInt("idUsuario");
                usuario.setIdUsuario(idUsuario);
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));

                // Cargar relaciones asociadas (EAGER)
                usuario.setMisAudios(new ArrayList<>(AudioDAO.findAudiosByUsuarioEager(idUsuario)));
                usuario.setMisListasReproduccion(new ArrayList<>(ListaReproduccionDAO.findByUsuario(idUsuario)));

                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }


    //METODO PARA BUSCAR POR ID VERSION LAZY
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


    //METODO PARA BUSCAR POR ID VERSION EAGER
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

    // Eliminar
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
