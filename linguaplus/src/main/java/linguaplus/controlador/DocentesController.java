package linguaplus.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import linguaplus.app.AppContext;
import linguaplus.modelo.Docente;

import java.math.BigDecimal;

/** Controlador de la pestaña Docentes (parte de RF-06: asignacion de tutores). */
public class DocentesController {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtIdioma;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtTarifa;
    @FXML private Label lblMensaje;

    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, String> colIdentificacion;
    @FXML private TableColumn<Docente, String> colNombre;
    @FXML private TableColumn<Docente, String> colIdioma;
    @FXML private TableColumn<Docente, String> colTelefono;
    @FXML private TableColumn<Docente, BigDecimal> colTarifa;

    private final AppContext ctx = AppContext.getInstancia();

    @FXML
    public void initialize() {
        colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdioma.setCellValueFactory(new PropertyValueFactory<>("idiomaEspecialidad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTarifa.setCellValueFactory(new PropertyValueFactory<>("tarifaSesion"));

        tablaDocentes.setItems(ctx.getDocentes());
    }

    @FXML
    private void onRegistrar() {
        try {
            String identificacion = requerido(txtIdentificacion.getText(), "La identificacion es obligatoria");
            String nombre = requerido(txtNombre.getText(), "El nombre es obligatorio");
            BigDecimal tarifa = new BigDecimal(txtTarifa.getText().trim());

            Docente docente = new Docente(identificacion, nombre, txtIdioma.getText(), txtTelefono.getText(), tarifa);
            ctx.getDocentes().add(docente);

            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Docente registrado correctamente.");
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mostrarError("La tarifa por sesion debe ser un numero valido.");
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
        txtIdentificacion.clear();
        txtNombre.clear();
        txtIdioma.clear();
        txtTelefono.clear();
        txtTarifa.clear();
    }
}
