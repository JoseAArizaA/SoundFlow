package org.jose.soundflow.utils;


import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jose.soundflow.exceptions.CorreoInvalidoException;
import org.jose.soundflow.model.TipoContenido;

public class Utilidades {

    public static boolean validarCorreo(String email) {
        boolean valido = false;
        if (!email.matches("^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new CorreoInvalidoException("El correo electrónico no es válido.");
        } else {
            valido = true;
        }
        return valido;
    }

    // Método para mostrar un mensaje temporal
    public static void mostrarMensajeTemporal(Stage stage, String mensaje) {
        Popup popup = new Popup();
        Label label = new Label(mensaje);
        label.setStyle("-fx-background-color: linear-gradient(to right, #4e54c8, #8f94fb)" +
                "-fx-text-fill: white;" +
                "-fx-padding: 15px 30px;" +
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-radius: 15px;" +
                "-fx-background-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);");
        popup.getContent().add(label);
        popup.setAutoHide(true);
        popup.show(stage);

        popup.setY(stage.getY() + 60);


        PauseTransition delay = new PauseTransition(Duration.seconds(2)); // 2 segundos
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

    public static TipoContenido corregirEnum(String tipo) {
        return TipoContenido.valueOf(
                tipo.toUpperCase()
                        .replace("Á", "A")
                        .replace("É", "E")
                        .replace("Í", "I")
                        .replace("Ó", "O")
                        .replace("Ú", "U")
                        .replace("Ñ", "N")
                        .replace(" ", "")
                        .trim()
        );
    }
}
