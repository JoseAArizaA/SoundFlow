package org.jose.soundflow.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jose.soundflow.DAO.AudioDAO;
import org.jose.soundflow.model.*;

import java.io.IOException;
import java.util.List;


public class PantallaVerAudiosController {
    @FXML
    private TableView<Audio> tablaAudios;
    @FXML
    private TableColumn<Audio, String> colTitulo;
    @FXML
    private TableColumn<Audio, String> colArtista;
    @FXML
    private TableColumn<Audio, String> colDescripcion;
    @FXML
    private TableColumn<Audio, Integer> colDuracion;
    @FXML
    private TableColumn<Audio, String> colTipo;
    @FXML
    private TableColumn<Audio, String> colExtra;
    @FXML
    private Button botonAtras;
    @FXML
    private Button botonEditar;
    @FXML
    private Button botonBorrar;

    public void initialize() {
        // Asociar las columnas normales
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));
        colExtra.setCellValueFactory(cellData -> {
            Audio audio = cellData.getValue();
            StringProperty valor = new SimpleStringProperty();

            if (audio instanceof Cancion) {
                valor.set(((Cancion) audio).getGenero());
            } else if (audio instanceof Podcast) {
                valor.set(((Podcast) audio).getTematica());
            } else if (audio instanceof AudioLibro) {
                valor.set(((AudioLibro) audio).getIdioma());
            }
            return valor;
        });
        // Llenar la tabla con los audios del usuario
        cargarAudios();
    }

    private void cargarAudios() {
        Usuario usuario = Sesion.getUsuarioActual();
        List<Audio> lista = AudioDAO.findAudiosByUsuarioEager(usuario.getIdUsuario());
        ObservableList<Audio> datos = FXCollections.observableArrayList(lista);
        tablaAudios.setItems(datos);
    }





    public void botonAtras(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PrincipalAplicacion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 460);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void botonEditar(ActionEvent actionEvent) {
        Audio seleccionado = tablaAudios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaAñadirAudios.fxml"));
                Parent root = loader.load();

                // Enviar el audio al controlador
                org.jose.soundflow.viewController.PantallaAñadirAudiosController controller = loader.getController();
                controller.inicializarConAudio(seleccionado);

                // Mostrar la ventana
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void botonBorrar(ActionEvent actionEvent) {
        Audio seleccionado = tablaAudios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            AudioDAO.delete(seleccionado.getIdAudio());
            cargarAudios();
        }
    }
}
