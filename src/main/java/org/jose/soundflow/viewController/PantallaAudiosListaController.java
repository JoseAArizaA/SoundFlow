package org.jose.soundflow.viewController;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jose.soundflow.DAO.AudioDAO;
import org.jose.soundflow.DAO.ListaReproduccionDAO;
import org.jose.soundflow.DAO.RelacionListaAudioDAO;
import org.jose.soundflow.model.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PantallaAudiosListaController {
    @FXML
    private Label labelTitulo;
    @FXML
    private TableView<RelacionListaAudio> tablaAudios;
    @FXML
    private TableColumn<RelacionListaAudio, String> colTitulo;
    @FXML
    private TableColumn<RelacionListaAudio, String> colArtista;
    @FXML
    private TableColumn<RelacionListaAudio, Integer> colDuracion;
    @FXML
    private TableColumn<RelacionListaAudio, String> colTipo;
    @FXML
    private TableColumn<RelacionListaAudio, Void>  colEliminar;
    @FXML
    private ComboBox<Audio> comboAudiosDisponibles;

    private ListaReproduccion listaSeleccionada;

    @FXML
    /**
     * // Método que se ejecuta al inicializar el controlador.
     * Configura las columnas de la tabla para mostrar los datos de los audios
     */
        // Cambios 28/05
        public void initialize() {
        colTitulo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAudio().getTitulo()));
        colArtista.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAudio().getArtista()));
        colDuracion.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAudio().getDuracion()).asObject());
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAudio().getTipoComoTexto()));
    }


    /**
     * Método que al pulsar al boton de atras vuelve a la pantalla de listas de reproduccion
     * @param actionEvent
     */
    public void botonAtras(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaListaReproduccion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 460);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configura la columna `colEliminar` para cada fila de la tabla.
     * Crea un botón con un icono de papelera que permite eliminar el audio de la lista actual.
     * Al pulsarlo, llama al DAO para borrar la relación entre audio y lista,
     * y recarga la tabla si la eliminación fue exitosa.
     */
    private void configurarColumnaEliminar() {
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("🗑");
            {
                btn.setOnAction(event -> {
                    // Cambios 28/05
                    RelacionListaAudio relacion = getTableView().getItems().get(getIndex());
                    Audio audio = relacion.getAudio();
                    if (audio != null) {
                        boolean eliminado = RelacionListaAudioDAO.delete(listaSeleccionada.getIdLista(), audio.getIdAudio());
                        if (eliminado) cargarCancionesDeLista(listaSeleccionada);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }


    /**
     * Método ejecutado al pulsar el botón "Añadir".
     * Añade el audio seleccionado en el ComboBox `comboAudiosDisponibles` a la lista de reproducción actual.
     * Si se añade correctamente, recarga la tabla y limpia la selección del ComboBox.
     * @param actionEvent
     */
    public void botonAñadir(ActionEvent actionEvent) {
        Audio audioSeleccionado = comboAudiosDisponibles.getValue();
        if (audioSeleccionado != null) {
            boolean insertado = RelacionListaAudioDAO.insert(listaSeleccionada.getIdLista(), audioSeleccionado.getIdAudio());
            if (insertado) {
                // Cambios 28/05
                ListaReproduccion actualizada = ListaReproduccionDAO.findById(listaSeleccionada.getIdLista());
                cargarCancionesDeLista(actualizada);
                comboAudiosDisponibles.getSelectionModel().clearSelection();
            }
        }
    }


    /**
     * Carga los audios pertenecientes a una lista de reproducción específica.
     * - Llama a `cargarAudiosDisponibles()` para actualizar el ComboBox con los audios que no están en la lista.
     * @param lista la lista de reproducción seleccionada
     */
    public void cargarCancionesDeLista(ListaReproduccion lista) {
        this.listaSeleccionada = lista;
        labelTitulo.setText("Audios en la lista: " + lista.getNombreLista());

        // Cambios 28/05
        List<RelacionListaAudio> relaciones = RelacionListaAudioDAO.findByListaId(lista.getIdLista());
        lista.setRelaciones(new ArrayList<>(relaciones));
        tablaAudios.setItems(FXCollections.observableArrayList(relaciones));

        configurarColumnaEliminar();
        cargarAudiosDisponibles();
    }

    /**
     * Carga en el ComboBox los audios que no están actualmente en la lista seleccionada.
     * Configura cómo se visualizan los elementos del ComboBox.
     */
    private void cargarAudiosDisponibles() {
        List<Audio> todos = AudioDAO.findAll();
        List<Audio> enLista = RelacionListaAudioDAO.findAudiosByListaId(listaSeleccionada.getIdLista());

        List<Audio> disponibles = new ArrayList<>();
        for (Audio audio : todos) {
            boolean esta = false;
            for (Audio en : enLista) {
                if (audio.getIdAudio() == en.getIdAudio()) {
                    esta = true;
                    break;
                }
            }
            if (!esta) disponibles.add(audio);
        }

        comboAudiosDisponibles.setItems(FXCollections.observableArrayList(disponibles));

        comboAudiosDisponibles.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Audio item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });

        comboAudiosDisponibles.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Audio item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });
    }
}
