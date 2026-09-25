package linguaplus.controlador;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import linguaplus.app.AppContext;
import linguaplus.modelo.EstadoPrograma;
import linguaplus.modelo.Modalidad;
import linguaplus.modelo.ProgramaFormacion;
import linguaplus.modelo.TipoPrograma;

import java.math.BigDecimal;
import java.util.Arrays;

/** Controlador de la pestaña Programas de formacion (RF-02). */
public class ProgramasController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtIdioma;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtValorMensual;
    @FXML private ComboBox<TipoPrograma> cmbTipo;
    @FXML private ComboBox<Modalidad> cmbModalidad;
    @FXML private ComboBox<EstadoPrograma> cmbEstado;
    @FXML private TextField txtBeneficios;
    @FXML private Label lblMensaje;

    @FXML private TableView<ProgramaFormacion> tablaProgramas;
    @FXML private TableColumn<ProgramaFormacion, String> colCodigo;
    @FXML private TableColumn<ProgramaFormacion, String> colNombre;
    @FXML private TableColumn<ProgramaFormacion, String> colIdioma;
    @FXML private TableColumn<ProgramaFormacion, TipoPrograma> colTipo;
    @FXML private TableColumn<ProgramaFormacion, Modalidad> colModalidad;
    @FXML private TableColumn<ProgramaFormacion, Integer> colDuracion;
    @FXML private TableColumn<ProgramaFormacion, String> colValor;
    @FXML private TableColumn<ProgramaFormacion, EstadoPrograma> colEstado;

    private final AppContext ctx = AppContext.getInstancia();

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoPrograma.values()));
        cmbModalidad.setItems(FXCollections.observableArrayList(Modalidad.values()));
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoPrograma.values()));
        cmbTipo.getSelectionModel().selectFirst();
        cmbModalidad.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().select(EstadoPrograma.ACTIVO);

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdioma.setCellValueFactory(new PropertyValueFactory<>("idioma"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colModalidad.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionMeses"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colValor.setCellValueFactory(c -> new SimpleStringProperty("$" + c.getValue().getValorMensual()));

        tablaProgramas.setItems(ctx.getProgramas());
    }

    @FXML
    private void onRegistrar() {
        try {
            String codigo = requerido(txtCodigo.getText(), "El codigo es obligatorio");
            String nombre = requerido(txtNombre.getText(), "El nombre es obligatorio");
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            BigDecimal valorMensual = new BigDecimal(txtValorMensual.getText().trim());

            ProgramaFormacion programa = new ProgramaFormacion(codigo, nombre, txtIdioma.getText(),
                    txtDescripcion.getText(), duracion, valorMensual,
                    cmbEstado.getValue(), cmbTipo.getValue(), cmbModalidad.getValue());

            if (!txtBeneficios.getText().isBlank()) {
                Arrays.stream(txtBeneficios.getText().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .forEach(programa::agregarBeneficio);
            }

            ctx.getProgramas().add(programa);
            ctx.getLinguaPlus().registrarPrograma(programa);

            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Programa registrado correctamente.");
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mostrarError("Duracion y valor mensual deben ser numeros validos.");
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
        txtIdioma.clear();
        txtDescripcion.clear();
        txtDuracion.clear();
        txtValorMensual.clear();
        txtBeneficios.clear();
    }
}
