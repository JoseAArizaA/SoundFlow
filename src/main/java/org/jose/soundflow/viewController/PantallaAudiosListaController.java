package org.jose.soundflow.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jose.soundflow.DAO.AudioDAO;
import org.jose.soundflow.DAO.RelacionListaAudioDAO;
import org.jose.soundflow.model.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PantallaAudiosListaController {
    @FXML
    private Label labelTitulo;
    @FXML
    private TableView<Audio> tablaAudios;
    @FXML
    private TableColumn<Audio, String> colTitulo;
    @FXML
    private TableColumn<Audio, String> colArtista;
    @FXML
    private TableColumn<Audio, Integer> colDuracion;
    @FXML
    private TableColumn<Audio, String> colTipo;
    @FXML
    private TableColumn<Audio, Void>  colEliminar;
    @FXML
    private ComboBox<Audio> comboAudiosDisponibles;

    private ListaReproduccion listaSeleccionada;

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));
    }


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

    private void configurarColumnaEliminar() {
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("🗑");

            {
                btn.setOnAction(event -> {
                    Audio audio = getTableView().getItems().get(getIndex());
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


    public void botonAñadir(ActionEvent actionEvent) {
        Audio audioSeleccionado = comboAudiosDisponibles.getValue();
        if (audioSeleccionado != null) {
            boolean insertado = RelacionListaAudioDAO.insert(listaSeleccionada.getIdLista(), audioSeleccionado.getIdAudio());
            if (insertado) {
                cargarCancionesDeLista(listaSeleccionada);
                comboAudiosDisponibles.getSelectionModel().clearSelection();
            }
        }
    }


    public void cargarCancionesDeLista(ListaReproduccion lista) {
        this.listaSeleccionada = lista;
        labelTitulo.setText("Audios en la lista \" + lista.getNombreLista() + \"");

        List<Audio> audiosDeLista = RelacionListaAudioDAO.findAudiosByListaId(lista.getIdLista());
        tablaAudios.setItems(FXCollections.observableArrayList(audiosDeLista));

        configurarColumnaEliminar();
        cargarAudiosDisponibles();
    }

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
