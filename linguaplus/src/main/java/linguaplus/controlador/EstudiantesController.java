package linguaplus.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import linguaplus.app.AppContext;
import linguaplus.modelo.Estudiante;

import java.time.format.DateTimeFormatter;

/** Controlador de la pestaña Estudiantes (RF-01). Sin logica de negocio: solo captura y despliega. */
public class EstudiantesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtEdad;
    @FXML private Label lblMensaje;

    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colDocumento;
    @FXML private TableColumn<Estudiante, String> colTelefono;
    @FXML private TableColumn<Estudiante, String> colCorreo;
    @FXML private TableColumn<Estudiante, Integer> colEdad;
    @FXML private TableColumn<Estudiante, String> colFechaRegistro;

    private final AppContext ctx = AppContext.getInstancia();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colFechaRegistro.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tablaEstudiantes.setItems(ctx.getEstudiantes());
    }

    @FXML
    private void onRegistrar() {
        try {
            String nombre = requerido(txtNombre.getText(), "El nombre es obligatorio");
            String documento = requerido(txtDocumento.getText(), "El documento es obligatorio");
            String telefono = txtTelefono.getText();
            String correo = txtCorreo.getText();
            int edad = Integer.parseInt(txtEdad.getText().trim());

            Estudiante estudiante = new Estudiante(nombre, documento, telefono, correo, edad);
            ctx.getEstudiantes().add(estudiante);
            ctx.getLinguaPlus().registrarEstudiante(estudiante);

            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Estudiante registrado correctamente.");
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mostrarError("La edad debe ser un numero valido.");
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
        txtNombre.clear();
        txtDocumento.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtEdad.clear();
    }
}
