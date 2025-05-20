package org.jose.soundflow.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jose.soundflow.exceptions.CorreoInvalidoException;
import org.jose.soundflow.model.Usuario;

import java.io.IOException;

public class PantallaRegistrarseController {
    @FXML
    private Stage stage;
    @FXML
    private TextField usuarioText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField correoText;

    /**
     * Método para registrar un nuevo usuario y guardarlo en la base de datos. Muestra alertas si hay campos incompletos o errores
     * y muestra un mensaje si el usuario se registra
     * @param actionEvent
     */
    public void registrarUsuario(ActionEvent actionEvent) {
        String nombre = usuarioText.getText();
        String correo = correoText.getText();
        String password = passwordText.getText();
        boolean puedeRegistrar = true;

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, completa todos los campos");
            alert.showAndWait();
            puedeRegistrar = false;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        try {
            usuario.setCorreo(correo);
        } catch (CorreoInvalidoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Correo inválido");
            alert.setHeaderText(null);
            alert.setContentText("El correo electrónico no es válido");
            alert.showAndWait();
            puedeRegistrar = false;
        }
        usuario.setPassword(password);

        if (puedeRegistrar) {
            boolean exito = org.jose.soundflow.DAO.UsuarioDAO.insert(usuario);


            Alert alert;
            if (exito) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro exitoso");
                alert.setHeaderText(null);
                alert.setContentText("Usuario registrado");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de registro");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo registrar el usuario");
            }
            alert.showAndWait();
        }
    }

    /**
     * Método que al pulsar al boton de atras vuelve a la pantalla de inicio
     * @param actionEvent
     */
    public void volverAtrasBoton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaInicio.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 460);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pantalla de Inicio");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para cerrar la aplicación al pulsar el botón de salir
     * @param mouseEvent
     */
    public void botonSalir(MouseEvent mouseEvent) {
       stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
       stage.close();
    }
}
