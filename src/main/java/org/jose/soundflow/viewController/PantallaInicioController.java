package org.jose.soundflow.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;

public class PantallaInicioController {
    @FXML
    private Button cambiarRegistro;
    @FXML
    private Button cambiarInicioSesion;

    /**
     * Método para que al pulsar el boton de registrarse se cambie la pantalla a la de registro
     * @param actionEvent
     */
    public void cambiarRegistro(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaRegistrarse.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = (Stage) cambiarRegistro.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pantalla de Registro");

            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para que al pulsar el boton de iniciar sesion se cambie la pantalla a la de inicio de sesion
     * @param actionEvent
     */
    public void cambiarInicioSesion(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaInicioSesion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = (Stage) cambiarInicioSesion.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pantalla de Inicio de Sesión");

            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
