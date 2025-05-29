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

     public class ListaReproduccionDAO {
          private static final String SQL_INSERT = "INSERT INTO ListaReproduccion (nombreLista, idUsuario) VALUES (?, ?)";
          private static final String SQL_UPDATE = "UPDATE ListaReproduccion SET nombreLista = ?, idUsuario = ? WHERE idLista = ?";
          private static final String SQL_DELETE = "DELETE FROM ListaReproduccion WHERE idLista = ?";
          private static final String SQL_SELECT_BY_USUARIO = "SELECT * FROM ListaReproduccion WHERE idUsuario = ?";
          // Cambios 28/05
          private static final String SQL_SELECT_BY_ID_LISTA = "SELECT * FROM ListaReproduccion WHERE idLista = ?";
          private static final String SQL_SELECT_BY_ID_AUDIO = "SELECT * FROM relacionListaAudio WHERE idAudio = ?";


          /**
           * Insertar una nueva lista de reproducción en la base de datos.
           * @param lista: Lista que se va a insertar.
           * @return: true si la inserción fue exitosa, false en caso contrario.
           */
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

          /**
           * Actualizar una lista de reproducción existente en la base de datos.
           * @param lista: Lista que se va a actualizar.
           * @return: true si la actualización fue exitosa, false en caso contrario.
           */
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

          /**
           * Eliminar una lista de reproducción de la base de datos.
           * @param id: ID de la lista que se va a eliminar.
           * @return: true si la eliminación fue exitosa, false en caso contrario.
           */
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

          /**
           * Buscar listas de reproducción por ID de usuario.
           * @param idUsuario: ID del usuario cuyas listas se van a buscar.
           * @return: Lista de listas de reproducción asociadas al usuario.
           */
          public static List<ListaReproduccion> findByUsuario(int idUsuario) {
               List<ListaReproduccion> listas = new ArrayList<>();
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_BY_USUARIO)) {
                    pst.setInt(1, idUsuario);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                         ListaReproduccion lista = new ListaReproduccion();
                         //Cambios
                         int idLista = rs.getInt("idLista");
                         lista.setIdLista(idLista);

                         lista.setNombreLista(rs.getString("nombreLista"));
                         lista.setUsuario(UsuarioDAO.findById(idUsuario));

                         List<RelacionListaAudio> relaciones = RelacionListaAudioDAO.findByListaId(idLista);
                         lista.setRelaciones(new ArrayList<>(relaciones));

                         listas.add(lista);
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return listas;
          }

          public static ListaReproduccion findById(int idLista) {
               ListaReproduccion lista = null;
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_BY_ID_LISTA)) {
                    pst.setInt(1, idLista);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                         lista = new ListaReproduccion();
                         lista.setIdLista(rs.getInt("idLista"));
                         lista.setNombreLista(rs.getString("nombreLista"));
                         lista.setUsuario(UsuarioDAO.findById(rs.getInt("idUsuario")));

                         List<RelacionListaAudio> relaciones = RelacionListaAudioDAO.findByListaId(idLista);
                         lista.setRelaciones(new ArrayList<>(relaciones));
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
               }
               return lista;
          }

          public static List<RelacionListaAudio> findByIdAudio(int idAudio) {
               List<RelacionListaAudio> relaciones = new ArrayList<>();
               try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(SQL_SELECT_BY_ID_AUDIO)) {
                    pst.setInt(1, idAudio);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
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
