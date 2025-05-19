package org.jose.soundflow.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalAplicacionController {
    @FXML
    private Button cambiarVerAudios;
    @FXML
    private Button cambiarAñadirAudios;
    @FXML
    private Button cambiarListaReproduccion;
    @FXML
    private Button botonSalir;



    public void cambiarVerAudios(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaVerAudios.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = (Stage) cambiarVerAudios.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pantalla de Añadir Audios");

            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cambairAñadirAudios(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaAñadirAudios.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = (Stage) cambiarAñadirAudios.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pantalla de Añadir Audios");

            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cambiarListaReproduccion(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaListaReproduccion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = (Stage) cambiarListaReproduccion.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pantalla de Listas de Reproducción");

            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void botonSalir(ActionEvent actionEvent) {
        System.exit(0);
    }
}
