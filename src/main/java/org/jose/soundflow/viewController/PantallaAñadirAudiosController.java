package org.jose.soundflow.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jose.soundflow.DAO.AudioDAO;
import org.jose.soundflow.model.*;

import java.io.IOException;

public class PantallaAñadirAudiosController {
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtArtista;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtDuracion;
    @FXML
    private ComboBox<String> tipoComboBox;
    @FXML
    private TextField txtExtra;
    @FXML
    private Button botonAtras;
    @FXML
    private Button botonGuardar;
    @FXML
    private Label labelExtra;

    public void initialize() {
        labelExtra.setVisible(false);
        txtExtra.setVisible(false);

        tipoComboBox.getItems().addAll("Canción", "Podcast", "Audiolibro");
        tipoComboBox.setPromptText("Selecciona un tipo");

        tipoComboBox.setOnAction(event -> {
            String tipo = tipoComboBox.getValue();

            if ("Canción".equals(tipo)) {
                labelExtra.setText("Género");
            } else if ("Podcast".equals(tipo)) {
                labelExtra.setText("Temática");
            } else if ("Audiolibro".equals(tipo)) {
                labelExtra.setText("Idioma");
            }

            // Mostrar los campos extra solo si se eligió un tipo
            labelExtra.setVisible(true);
            txtExtra.setVisible(true);
        });
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

    public void botonGuardar(ActionEvent actionEvent) {
        String titulo = txtTitulo.getText();
        String artista = txtArtista.getText();
        String descripcion = txtDescripcion.getText();
        String duracionTexto = txtDuracion.getText();
        String tipo = tipoComboBox.getValue();
        String extra = txtExtra.getText();
        boolean puedeGuardar = true;

        if (titulo.isEmpty() || artista.isEmpty() || descripcion.isEmpty() || duracionTexto.isEmpty() || tipo == null || extra.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, completa todos los campos");
            alert.showAndWait();
            puedeGuardar = false;
        }

        int duracion = 0;
        try {
            duracion = Integer.parseInt(duracionTexto);
            if (duracion <= 0 || duracion > 100) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duración inválida");
                alert.setHeaderText(null);
                alert.setContentText("La duración debe ser un número positivo");
                alert.showAndWait();
                puedeGuardar = false;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Duración inválida");
            alert.setHeaderText(null);
            alert.setContentText("La duración debe ser un número válido");
            alert.showAndWait();
            puedeGuardar = false;
        }

        Usuario usuario = Sesion.getUsuarioActual();

        Audio audio;

        if (audioEditar != null) {
            audio = audioEditar;
        } else {
            switch (tipo) {
                case "Canción":
                    audio = new Cancion();
                    ((Cancion) audio).setGenero(extra);
                    break;
                case "Podcast":
                    audio = new Podcast();
                    ((Podcast) audio).setTematica(extra);
                    break;
                case "Audiolibro":
                    audio = new AudioLibro();
                    ((AudioLibro) audio).setIdioma(extra);
                    break;
                default:
                    Alert alertTipo = new Alert(Alert.AlertType.ERROR);
                    alertTipo.setTitle("Tipo no reconocido");
                    alertTipo.setHeaderText(null);
                    alertTipo.setContentText("El tipo de audio no es válido.");
                    alertTipo.showAndWait();
                    return;
            }
        }

        audio.setTitulo(titulo);
        audio.setArtista(artista);
        audio.setDescripcion(descripcion);
        audio.setDuracion(duracion);
        audio.setTipoAudio(org.jose.soundflow.utils.Utilidades.corregirEnum(tipo));
        audio.setUsuario(usuario);

        boolean exito;
        if (audioEditar != null) {
            exito = AudioDAO.update(audio);
        } else {
            exito = AudioDAO.insertAudio(audio);
        }

        Alert alert;
        if (exito) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Guardado exitoso");
            alert.setHeaderText(null);
            alert.setContentText("Audio guardado correctamente");
            limpiarCampos();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al guardar");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo guardar el audio");
        }
        alert.showAndWait();
    }


    private void limpiarCampos() {
        txtTitulo.clear();
        txtArtista.clear();
        txtDescripcion.clear();
        txtDuracion.clear();
        tipoComboBox.getSelectionModel().clearSelection();
        txtExtra.clear();
        txtExtra.setVisible(false);
        labelExtra.setVisible(false);
    }

    private Audio audioEditar;

    public void inicializarConAudio(Audio audio) {
        this.audioEditar = audio;

        txtTitulo.setText(audio.getTitulo());
        txtArtista.setText(audio.getArtista());
        txtDescripcion.setText(audio.getDescripcion());
        txtDuracion.setText(String.valueOf(audio.getDuracion()));
        tipoComboBox.setValue(audio.getTipoAudio().toString());

        // Mostrar los campos extra según el tipo
        labelExtra.setVisible(true);
        txtExtra.setVisible(true);

        if (audio instanceof Cancion) {
            labelExtra.setText("Género");
            txtExtra.setText(((Cancion) audio).getGenero());
        } else if (audio instanceof Podcast) {
            labelExtra.setText("Temática");
            txtExtra.setText(((Podcast) audio).getTematica());
        } else if (audio instanceof AudioLibro) {
            labelExtra.setText("Idioma");
            txtExtra.setText(((AudioLibro) audio).getIdioma());
        }
    }
}
