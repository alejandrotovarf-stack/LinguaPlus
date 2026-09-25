package linguaplus.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import linguaplus.app.AppContext;
import linguaplus.modelo.Estudiante;
import linguaplus.modelo.Matricula;
import linguaplus.modelo.PeriodoAcademico;
import linguaplus.servicio.AnalizadorNumero;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/** Controlador de la pestaña Consultas (RF-11, RF-12, RF-13). */
public class ConsultasController {

    @FXML private TextField txtTelefono;
    @FXML private Label lblResultadoTelefono;

    @FXML private TextField txtNumero;
    @FXML private Label lblResultadoNumero;

    @FXML private ComboBox<PeriodoAcademico> cmbPeriodo;
    @FXML private Label lblCantidadMatriculas;
    @FXML private Label lblTotalIngresos;

    private final AppContext ctx = AppContext.getInstancia();
    private final AnalizadorNumero analizadorNumero = new AnalizadorNumero();

    @FXML
    public void initialize() {
        cmbPeriodo.setItems(ctx.getPeriodos());
    }

    @FXML
    private void onBuscarTelefono() {
        String telefono = txtTelefono.getText();
        Optional<Estudiante> estudiante = ctx.getServicioConsulta().buscarPorTelefono(telefono);
        if (estudiante.isPresent()) {
            lblResultadoTelefono.getStyleClass().setAll("helper-text", "tag-success");
            lblResultadoTelefono.setText("Encontrado: " + estudiante.get().getNombreCompleto()
                    + " (" + estudiante.get().getDocumento() + ")");
        } else {
            lblResultadoTelefono.getStyleClass().setAll("helper-text", "tag-danger");
            lblResultadoTelefono.setText("No se encontro ningun estudiante con ese telefono.");
        }
    }

    @FXML
    private void onVerificarNumero() {
        try {
            int numero = Integer.parseInt(txtNumero.getText().trim());
            boolean esPerfecto = ctx.getServicioConsulta().esNumeroPerfecto(numero);
            int suma = analizadorNumero.sumaDivisores(numero);
            lblResultadoNumero.getStyleClass().setAll("helper-text", esPerfecto ? "tag-success" : "tag-danger");
            lblResultadoNumero.setText(numero + (esPerfecto ? " SI es un numero perfecto" : " NO es un numero perfecto")
                    + " (suma de divisores propios: " + suma + ")");
        } catch (NumberFormatException e) {
            lblResultadoNumero.getStyleClass().setAll("helper-text", "tag-danger");
            lblResultadoNumero.setText("Ingresa un numero entero valido.");
        }
    }

    @FXML
    private void onCalcularIngresos() {
        PeriodoAcademico periodo = cmbPeriodo.getValue();
        if (periodo == null) {
            lblCantidadMatriculas.setText("Selecciona un periodo.");
            lblTotalIngresos.setText("");
            return;
        }
        List<Matricula> matriculasPeriodo = ctx.getCalculadorIngresos()
                .filtrarPorPeriodo(ctx.getMatriculas(), periodo);
        BigDecimal total = ctx.getCalculadorIngresos().calcular(periodo, ctx.getMatriculas());

        lblCantidadMatriculas.setText("Matriculas en el periodo: " + matriculasPeriodo.size());
        lblTotalIngresos.setText("Ingresos totales: $" + total);
    }
}
