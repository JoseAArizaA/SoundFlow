package org.jose.soundflow.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jose.soundflow.DAO.UsuarioDAO;
import org.jose.soundflow.model.Sesion;
import org.jose.soundflow.model.Usuario;

import java.io.IOException;

public class PantallaInicioSesionController {

    @FXML
    private Stage stage;
    @FXML
    private TextField usuarioText;
    @FXML
    private TextField passwordText;

    /**
     * Método que al pulsar al boton de atras vuelve a la pantalla de inicio
     * @param actionEvent
     */
    public void volverAtrasIniciarSesionBoton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaInicio.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 460); // Ajusta el tamaño si es necesario
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pantalla de Inicio");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para iniciar sesión, muestra alertas si hay campos incompletos o si el usuario o la contrasñea son incorrectos.
     * Si esta todo correcto se inicia la sesion y se cambia a la pantalla principal
     * @param actionEvent
     */
    public void iniciarSesion(ActionEvent actionEvent) {
        String nombre = usuarioText.getText();
        String password = passwordText.getText();
        boolean puedeIniciar = true;

        if (nombre.isEmpty() || password.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, completa todos los campos");
            alert.showAndWait();
            puedeIniciar = false;
        }

        Usuario usuario = null;

        if (puedeIniciar) {
            usuario = UsuarioDAO.findByNombreYPassword(nombre, password);
            if (usuario == null) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error de inicio de sesión");
                alert.setHeaderText(null);
                alert.setContentText("Usuario o contraseña incorrectos");
                alert.showAndWait();
                puedeIniciar = false;
            }
        }

        if (puedeIniciar && usuario != null) {
            try {
                Sesion.setUsuarioActual(usuario);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PrincipalAplicacion.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 640, 480);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("SoundFlow - Principal");
                stage.show();

                org.jose.soundflow.utils.Utilidades.mostrarMensajeTemporal(stage, "Bienvenido a SoundFlow");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

