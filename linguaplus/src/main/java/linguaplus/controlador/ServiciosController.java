package linguaplus.controlador;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import linguaplus.app.AppContext;
import linguaplus.modelo.ServicioAdicional;

import java.math.BigDecimal;

/** Controlador de la pestaña Servicios adicionales (RF-07). */
public class ServiciosController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox chkDisponible;
    @FXML private Label lblMensaje;

    @FXML private TableView<ServicioAdicional> tablaServicios;
    @FXML private TableColumn<ServicioAdicional, String> colCodigo;
    @FXML private TableColumn<ServicioAdicional, String> colNombre;
    @FXML private TableColumn<ServicioAdicional, String> colDescripcion;
    @FXML private TableColumn<ServicioAdicional, String> colPrecio;
    @FXML private TableColumn<ServicioAdicional, String> colDisponible;

    private final AppContext ctx = AppContext.getInstancia();

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(c -> new SimpleStringProperty("$" + c.getValue().getPrecio()));
        colDisponible.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().estaDisponible() ? "Si" : "No"));

        tablaServicios.setItems(ctx.getServicios());
    }

    @FXML
    private void onRegistrar() {
        try {
            String codigo = requerido(txtCodigo.getText(), "El codigo es obligatorio");
            String nombre = requerido(txtNombre.getText(), "El nombre es obligatorio");
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());

            ServicioAdicional servicio = new ServicioAdicional(codigo, nombre, txtDescripcion.getText(),
                    precio, chkDisponible.isSelected());
            ctx.getServicios().add(servicio);

            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Servicio registrado correctamente.");
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mostrarError("El precio debe ser un numero valido.");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private String requerido(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    private void mostrarError(String mensaje) {
        lblMensaje.getStyleClass().setAll("helper-text", "tag-danger");
        lblMensaje.setText(mensaje);
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        chkDisponible.setSelected(true);
    }
}
