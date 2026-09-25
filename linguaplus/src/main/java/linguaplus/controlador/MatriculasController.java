package linguaplus.controlador;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import linguaplus.app.AppContext;
import linguaplus.modelo.Docente;
import linguaplus.modelo.Estudiante;
import linguaplus.modelo.Matricula;
import linguaplus.modelo.OfertaPrograma;
import linguaplus.modelo.PeriodoAcademico;
import linguaplus.modelo.ProgramaFormacion;
import linguaplus.modelo.ServicioAdicional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de la pestaña Matriculas (RF-04 a RF-08). Traduce la
 * seleccion del usuario en llamadas a {@link Matricula.Builder} y a
 * {@link linguaplus.servicio.ServicioMatricula}; la interfaz no valida
 * reglas de negocio por si misma, solo captura datos y muestra el
 * resultado o el error devuelto por la capa de servicio/modelo.
 */
public class MatriculasController {

    @FXML private ComboBox<Estudiante> cmbEstudiante;
    @FXML private ComboBox<ProgramaFormacion> cmbPrograma;
    @FXML private DatePicker dpFechaInicio;
    @FXML private ComboBox<PeriodoAcademico> cmbPeriodo;
    @FXML private ComboBox<Docente> cmbTutor;
    @FXML private ListView<ServicioAdicional> listaServicios;
    @FXML private TextField txtDescuento;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblMensaje;

    @FXML private TableView<Matricula> tablaMatriculas;
    @FXML private TableColumn<Matricula, Number> colNumero;
    @FXML private TableColumn<Matricula, String> colEstudiante;
    @FXML private TableColumn<Matricula, String> colPrograma;
    @FXML private TableColumn<Matricula, String> colFechaInicio;
    @FXML private TableColumn<Matricula, String> colTutor;
    @FXML private TableColumn<Matricula, String> colDescuento;
    @FXML private TableColumn<Matricula, String> colTotal;

    private final AppContext ctx = AppContext.getInstancia();

    @FXML
    public void initialize() {
        cmbEstudiante.setItems(ctx.getEstudiantes());
        cmbPrograma.setItems(ctx.getProgramas());
        cmbPeriodo.setItems(ctx.getPeriodos());
        cmbTutor.setItems(ctx.getDocentes());

        listaServicios.setItems(ctx.getServicios());
        listaServicios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        colNumero.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                c.getValue().getNumeroMatricula()));
        colEstudiante.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEstudiante().getNombreCompleto()));
        colPrograma.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPrograma().getNombre()));
        colFechaInicio.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getFechaInicio())));
        colTutor.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTutor() == null ? "-" : c.getValue().getTutor().getNombre()));
        colDescuento.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getDescuento())));
        colTotal.setCellValueFactory(c -> new SimpleStringProperty("$" + c.getValue().calcularValorTotal()));

        tablaMatriculas.setItems(ctx.getMatriculas());
    }

    @FXML
    private void onRegistrar() {
        try {
            Estudiante estudiante = requerido(cmbEstudiante.getValue(), "Selecciona un estudiante");
            ProgramaFormacion programa = requerido(cmbPrograma.getValue(), "Selecciona un programa");
            if (dpFechaInicio.getValue() == null) {
                throw new IllegalArgumentException("Selecciona la fecha de inicio");
            }
            BigDecimal descuento = txtDescuento.getText() == null || txtDescuento.getText().isBlank()
                    ? BigDecimal.ZERO : new BigDecimal(txtDescuento.getText().trim());

            Matricula.Builder builder = new Matricula.Builder()
                    .estudiante(estudiante)
                    .programa(programa)
                    .fechaInicio(dpFechaInicio.getValue())
                    .descuento(descuento)
                    .observaciones(txtObservaciones.getText());

            List<ServicioAdicional> serviciosSeleccionados = listaServicios.getSelectionModel().getSelectedItems();
            for (ServicioAdicional servicio : serviciosSeleccionados) {
                builder.agregarServicio(servicio);
            }

            Matricula matricula = builder.build();

            OfertaPrograma oferta = buscarOfertaDelPeriodo(programa);
            ctx.getServicioMatricula().registrar(matricula, oferta);

            Docente tutor = cmbTutor.getValue();
            if (tutor != null) {
                ctx.getServicioMatricula().asignarTutor(matricula, tutor);
            }

            estudiante.adquirirPrograma(programa);

            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Matricula #" + matricula.getNumeroMatricula() + " registrada. Total: $"
                    + matricula.calcularValorTotal());
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mostrarError("El descuento debe ser un numero valido.");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    /** Busca, en el periodo seleccionado (si hay), la oferta que corresponde al programa elegido. */
    private OfertaPrograma buscarOfertaDelPeriodo(ProgramaFormacion programa) {
        PeriodoAcademico periodo = cmbPeriodo.getValue();
        if (periodo == null) {
            return null;
        }
        Optional<OfertaPrograma> oferta = periodo.getOferta().stream()
                .filter(o -> o.getPrograma().getCodigo().equals(programa.getCodigo()))
                .findFirst();
        return oferta.orElse(null);
    }

    private <T> T requerido(T valor, String mensaje) {
        if (valor == null) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor;
    }

    private void mostrarError(String mensaje) {
        lblMensaje.getStyleClass().setAll("helper-text", "tag-danger");
        lblMensaje.setText(mensaje);
    }

    private void limpiarFormulario() {
        cmbEstudiante.getSelectionModel().clearSelection();
        cmbPrograma.getSelectionModel().clearSelection();
        cmbPeriodo.getSelectionModel().clearSelection();
        cmbTutor.getSelectionModel().clearSelection();
        dpFechaInicio.setValue(null);
        listaServicios.getSelectionModel().clearSelection();
        txtDescuento.clear();
        txtObservaciones.clear();
    }
}
