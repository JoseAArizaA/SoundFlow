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
          private static final String SQL_INSERT = "INSERT INTO ListaReproduccion (nombre, descripcion, fechaCreacion, idUsuario) VALUES (?, ?, ?, ?)";
          private static final String SQL_SELECT_BY_ID = "SELECT * FROM ListaReproduccion WHERE idLista = ?";
          private static final String SQL_UPDATE = "UPDATE ListaReproduccion SET nombre = ?, descripcion = ?, fechaCreacion = ?, idUsuario = ? WHERE idLista = ?";
          private static final String SQL_DELETE = "DELETE FROM ListaReproduccion WHERE idLista = ?";
          private static final String SQL_SELECT_BY_USUARIO = "SELECT * FROM ListaReproduccion WHERE idUsuario = ?";
          private static final String SQL_SELECT_ALL = "SELECT * FROM ListaReproduccion";
          private static final String SQL_SELECT_AUDIOS_BY_LISTA = "SELECT * FROM Audio WHERE idLista = ?";


          // Método INSERT
          public static boolean insert(ListaReproduccion lista) {
               boolean resultado = false;
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_INSERT)) {
                    pst.setString(1, lista.getNombreLista());
                    pst.setInt(2, lista.getUsuario().getIdUsuario()); // Usar idUsuario del objeto Usuario
                    resultado = pst.executeUpdate() > 0;
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return resultado;
          }

          // Método FIND BY ID
          public static ListaReproduccion findById(int id) {
               ListaReproduccion lista = null;
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_BY_ID)) {
                    pst.setInt(1, id);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                         lista = new ListaReproduccion();
                         lista.setIdLista(rs.getInt("idLista"));
                         lista.setNombreLista(rs.getString("nombre"));
                         lista.setUsuario(UsuarioDAO.findById(rs.getInt("idUsuario"))); // Cargar Usuario
                         lista.setAudios(new ArrayList<>(findAudiosByListaId(lista.getIdLista()))); // Cargar audios
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return lista;
          }

          // Método para obtener audios de una lista
          public static List<Audio> findAudiosByListaId(int idLista) {
               List<Audio> audios = new ArrayList<>();
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_AUDIOS_BY_LISTA)) {
                    pst.setInt(1, idLista);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                         Audio audio = new Audio();
                         audio.setIdAudio(rs.getInt("idAudio"));
                         audio.setTitulo(rs.getString("titulo"));
                         audio.setArtista(rs.getString("artista"));
                         audio.setDescripcion(rs.getString("descripcion"));
                         audio.setDuracion(rs.getInt("duracion"));
                         audio.setTipoAudio(TipoContenido.valueOf(rs.getString("tipoAudio")));
                         audios.add(audio);
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return audios;
          }

          // Método UPDATE
          public static boolean update(ListaReproduccion lista) {
               boolean resultado = false;
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_UPDATE)) {
                    pst.setString(1, lista.getNombreLista());
                    pst.setInt(2, lista.getUsuario().getIdUsuario()); // Usar idUsuario del objeto Usuario
                    pst.setInt(3, lista.getIdLista());
                    resultado = pst.executeUpdate() > 0;
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return resultado;
          }



          // Método DELETE
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


          public static List<ListaReproduccion> findAllEager() {
               List<ListaReproduccion> listas = new ArrayList<>();
               try (Statement stmt = ConnectionDB.getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
                    while (rs.next()) {
                         ListaReproduccion lista = new ListaReproduccion();
                         lista.setIdLista(rs.getInt("idLista"));
                         lista.setNombreLista(rs.getString("nombreLista"));
                         lista.setUsuario(UsuarioDAO.findById(rs.getInt("idUsuario"))); // Cargar Usuario
                         lista.setAudios(new ArrayList<>(findAudiosByListaId(lista.getIdLista()))); // Cargar audios
                         listas.add(lista);
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return listas;
          }


          public static List<ListaReproduccion> findByUsuarioId(int idUsuario) {
               List<ListaReproduccion> listas = new ArrayList<>();
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_BY_USUARIO)) {
                    pst.setInt(1, idUsuario);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                         ListaReproduccion lista = new ListaReproduccion();
                         lista.setIdLista(rs.getInt("idLista"));
                         lista.setNombreLista(rs.getString("nombre"));
                         lista.setUsuario(UsuarioDAO.findById(idUsuario)); // Cargar usuario completo
                         lista.setAudios(new ArrayList<>(findAudiosByListaId(lista.getIdLista()))); // Cargar audios
                         listas.add(lista);
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return listas;
          }

     }
