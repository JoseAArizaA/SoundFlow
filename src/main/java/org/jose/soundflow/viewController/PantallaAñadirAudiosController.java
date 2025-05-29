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

    /**
     * MÉtodo para que al cargar la pantalla para añadir audios el label y el campo de texto extra no se vean, tambien se pone el texto en el comboBox
     * y segun el tipo que se seleccione se cambia el texto del label y se muestra el campo de texto extra
     */
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

    /**
     * Método para volver a la pantalla anterior
     * @param actionEvent
     */
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

    /**
     * Método para guardar el audio en la base de datos
     * @param actionEvent: evento de acción al pulsar el botón, tambien hay alertas si no se rellena correctamente o se dejan campos vacíos
     */
    // Cambios 28/05
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
            if (duracion <= 0) {
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
        Audio audio = null;

        if (puedeGuardar) {
            if (audioEditar != null) {
                audio = audioEditar;
            } else {
                // Cambios 28/05
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
                    }
            }
        }

        if (puedeGuardar && audio != null) {
            audio.setTitulo(titulo);
            audio.setArtista(artista);
            audio.setDescripcion(descripcion);
            audio.setDuracion(duracion);
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
    }

    /**
     * Método para limpiar los campos de texto cuando se añade un nuevo audio
     */
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
    /**
     * Método para que al pulsar en editar se carguen los datos del audio en los campos
     * @param audio: audio que se va a editar
     */
    // Cambios 28/05
    public void inicializarConAudio(Audio audio) {
        this.audioEditar = audio;

        txtTitulo.setText(audio.getTitulo());
        txtArtista.setText(audio.getArtista());
        txtDescripcion.setText(audio.getDescripcion());
        txtDuracion.setText(String.valueOf(audio.getDuracion()));

        if (audio instanceof Cancion) {
            tipoComboBox.setValue("Canción");
        } else if (audio instanceof Podcast) {
            tipoComboBox.setValue("Podcast");
        } else if (audio instanceof AudioLibro) {
            tipoComboBox.setValue("Audiolibro");
        }

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
