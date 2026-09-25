package linguaplus.controlador;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import linguaplus.app.AppContext;
import linguaplus.modelo.OfertaPrograma;
import linguaplus.modelo.PeriodoAcademico;
import linguaplus.modelo.ProgramaFormacion;

/**
 * Controlador de la pestaña Periodos y Ofertas (RF-03). Ilustra el patron
 * <b>Prototype</b> desde la GUI: las ofertas se arman primero en un periodo
 * "base" y luego se clonan hacia cada periodo real mediante
 * {@link PeriodoAcademico#clonarDesdeBase}, garantizando cupos
 * independientes entre periodos (RN-05).
 */
public class PeriodosController {

    @FXML private ComboBox<ProgramaFormacion> cmbPrograma;
    @FXML private TextField txtHorario;
    @FXML private TextField txtCupos;
    @FXML private ListView<String> listaOfertaBase;

    @FXML private TextField txtIdentificador;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblMensaje;

    @FXML private TableView<PeriodoAcademico> tablaPeriodos;
    @FXML private TableColumn<PeriodoAcademico, String> colIdentificador;
    @FXML private TableColumn<PeriodoAcademico, String> colFechaInicio;
    @FXML private TableColumn<PeriodoAcademico, String> colFechaFin;
    @FXML private TableColumn<PeriodoAcademico, Number> colNumOfertas;

    @FXML private TableView<OfertaPrograma> tablaOfertas;
    @FXML private TableColumn<OfertaPrograma, String> colOfertaPrograma;
    @FXML private TableColumn<OfertaPrograma, String> colOfertaHorario;
    @FXML private TableColumn<OfertaPrograma, Number> colOfertaCupos;

    private final AppContext ctx = AppContext.getInstancia();

    @FXML
    public void initialize() {
        cmbPrograma.setItems(ctx.getProgramas());

        colIdentificador.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdentificador()));
        colFechaInicio.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getFechaInicio())));
        colFechaFin.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getFechaFin())));
        colNumOfertas.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                c.getValue().getOferta().size()));

        colOfertaPrograma.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPrograma().getNombre()));
        colOfertaHorario.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHorario()));
        colOfertaCupos.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCupos()));

        tablaPeriodos.setItems(ctx.getPeriodos());
        tablaPeriodos.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            tablaOfertas.setItems(nuevo == null ? FXCollections.observableArrayList()
                    : FXCollections.observableArrayList(nuevo.getOferta()));
        });

        refrescarListaBase();
    }

    @FXML
    private void onAgregarOferta() {
        try {
            ProgramaFormacion programa = cmbPrograma.getValue();
            if (programa == null) {
                throw new IllegalArgumentException("Selecciona un programa");
            }
            int cupos = Integer.parseInt(txtCupos.getText().trim());
            OfertaPrograma oferta = new OfertaPrograma(programa, txtHorario.getText(), cupos);
            ctx.getPeriodoBase().addOferta(oferta);
            refrescarListaBase();

            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Oferta agregada a la base.");
            txtHorario.clear();
            txtCupos.clear();
        } catch (NumberFormatException e) {
            mostrarError("Los cupos deben ser un numero entero valido.");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onCrearPeriodo() {
        try {
            String identificador = txtIdentificador.getText();
            if (identificador == null || identificador.isBlank()) {
                throw new IllegalArgumentException("El identificador del periodo es obligatorio");
            }
            if (ctx.getPeriodoBase().getOferta().isEmpty()) {
                throw new IllegalArgumentException("Agrega al menos una oferta a la base antes de crear el periodo");
            }
            PeriodoAcademico nuevo = ctx.getPeriodoBase().clonarDesdeBase(
                    identificador.trim(), dpFechaInicio.getValue(), dpFechaFin.getValue());
            ctx.getPeriodos().add(nuevo);
            ctx.getLinguaPlus().abrirPeriodo(nuevo);

            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Periodo '" + identificador + "' creado con cupos independientes de la base.");
            txtIdentificador.clear();
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void refrescarListaBase() {
        listaOfertaBase.setItems(FXCollections.observableArrayList(
                ctx.getPeriodoBase().getOferta().stream().map(Object::toString).toList()));
    }

    private void mostrarError(String mensaje) {
        lblMensaje.getStyleClass().setAll("helper-text", "tag-danger");
        lblMensaje.setText(mensaje);
    }
}
