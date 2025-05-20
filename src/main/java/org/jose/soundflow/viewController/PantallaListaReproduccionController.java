package org.jose.soundflow.viewController;

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
import org.jose.soundflow.model.Audio;
import org.jose.soundflow.model.ListaReproduccion;
import org.jose.soundflow.DAO.ListaReproduccionDAO;
import javafx.scene.control.Button;
import org.jose.soundflow.model.Sesion;

import java.io.IOException;
import java.util.List;


public class PantallaListaReproduccionController {
    @FXML
    private TableView<ListaReproduccion> listaAudios;
    @FXML
    private TableColumn<ListaReproduccion, String> colNombre;
    @FXML
    private TableColumn<ListaReproduccion, Void> colVer;
    @FXML
    private TableColumn<ListaReproduccion, Void> colEliminar;
    @FXML
    private Button botonAtras;
    @FXML
    private Button botonCrearLista;

    /**
     * Se ejecuta al iniciar la pantalla.
     */
    public void initialize() {
        configurarColumnas();
        cargarListas();
    }

    /**
     * Configura las columnas de la tabla.
     */
    private void configurarColumnas() {
        // Nombre
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreLista"));

        // Ver canciones
        colVer.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Ver canciones");

            {
                btn.setOnAction(e -> {
                    ListaReproduccion lista = getTableView().getItems().get(getIndex());
                    abrirPantallaCanciones(lista);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Eliminar lista
        colEliminar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Eliminar");

            {
                btn.setOnAction(e -> {
                    ListaReproduccion lista = getTableView().getItems().get(getIndex());
                    ListaReproduccionDAO.delete(lista.getIdLista());
                    listaAudios.getItems().remove(lista);
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
     * Carga las listas de reproducción del usuario actual.
     */
    private void cargarListas() {
        int idUsuario = Sesion.getUsuarioActual().getIdUsuario();
        List<ListaReproduccion> listas = ListaReproduccionDAO.findByUsuario(idUsuario);
        ObservableList<ListaReproduccion> datos = FXCollections.observableArrayList(listas);
        listaAudios.setItems(datos);
    }

    /**
     * Abre la pantalla donde se muestran las canciones de una lista.
     */
    private void abrirPantallaCanciones(ListaReproduccion lista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jose/soundflow/view/PantallaAudiosLista.fxml"));
            Parent root = loader.load();

            PantallaAudiosListaController controller = loader.getController();
            controller.cargarCancionesDeLista(lista);

            Stage stage = (Stage) listaAudios.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void botonCrearLista(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Crear Lista");
        dialog.setHeaderText("Introduce el nombre de la nueva lista:");
        dialog.setContentText("Nombre:");

        dialog.showAndWait().ifPresent(nombre -> {
            if (!nombre.isBlank()) {
                ListaReproduccion nueva = new ListaReproduccion();
                nueva.setNombreLista(nombre);
                nueva.setUsuario(Sesion.getUsuarioActual());

                ListaReproduccionDAO.insert(nueva);
                cargarListas(); // refrescar la tabla
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Nombre vacío");
                alert.setHeaderText(null);
                alert.setContentText("El nombre de la lista no puede estar vacío.");
                alert.showAndWait();
            }
        });
    }




}
