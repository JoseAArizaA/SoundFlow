package org.jose.soundflow.DAO;

import org.jose.soundflow.baseDatos.ConnectionDB;
import org.jose.soundflow.model.Audio;
import org.jose.soundflow.model.ListaReproduccion;
import org.jose.soundflow.model.TipoContenido;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

     public class ListaReproduccionDAO {
          private static final String SQL_INSERT = "INSERT INTO ListaReproduccion (nombreLista, idUsuario) VALUES (?, ?)";
          private static final String SQL_UPDATE = "UPDATE ListaReproduccion SET nombreLista = ?, idUsuario = ? WHERE idLista = ?";
          private static final String SQL_DELETE = "DELETE FROM ListaReproduccion WHERE idLista = ?";
          private static final String SQL_SELECT_BY_USUARIO = "SELECT * FROM ListaReproduccion WHERE idUsuario = ?";
          private static final String SQL_SELECT_AUDIOS_BY_LISTA = "SELECT idAudio FROM RelacionListaAudio WHERE idLista = ?";


          // Insertar nueva lista
          public static boolean insert(ListaReproduccion lista) {
               boolean resultado = false;
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_INSERT)) {
                    pst.setString(1, lista.getNombreLista());
                    pst.setInt(2, lista.getUsuario().getIdUsuario());
                    resultado = pst.executeUpdate() > 0;
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return resultado;
          }

          // Actualizar lista
          public static boolean update(ListaReproduccion lista) {
               boolean resultado = false;
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_UPDATE)) {
                    pst.setString(1, lista.getNombreLista());
                    pst.setInt(2, lista.getUsuario().getIdUsuario());
                    pst.setInt(3, lista.getIdLista());
                    resultado = pst.executeUpdate() > 0;
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return resultado;
          }

          // Eliminar lista por ID
          public static boolean delete(int id) {
               boolean resultado = false;
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_DELETE)) {
                    pst.setInt(1, id);
                    resultado = pst.executeUpdate() > 0;
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return resultado;
          }

          // Obtener listas de un usuario
          public static List<ListaReproduccion> findByUsuario(int idUsuario) {
               List<ListaReproduccion> listas = new ArrayList<>();
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_BY_USUARIO)) {
                    pst.setInt(1, idUsuario);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                         ListaReproduccion lista = new ListaReproduccion();
                         lista.setIdLista(rs.getInt("idLista"));
                         lista.setNombreLista(rs.getString("nombreLista"));
                         lista.setUsuario(UsuarioDAO.findById(idUsuario));
                         listas.add(lista);
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return listas;
          }

     }
