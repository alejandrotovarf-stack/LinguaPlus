package linguaplus.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import linguaplus.app.AppContext;
import linguaplus.modelo.Matricula;
import linguaplus.modelo.comprobante.Comprobante;
import linguaplus.modelo.comprobante.GeneradorComprobante;
import linguaplus.modelo.comprobante.GeneradorExcel;
import linguaplus.modelo.comprobante.GeneradorPDF;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Controlador de la pestaña Comprobantes (RF-09). Usa el patron
 * <b>Factory Method</b>: el proceso de generar el comprobante es el mismo
 * sin importar el formato, solo cambia el {@link GeneradorComprobante}
 * concreto que se instancia segun la seleccion del usuario.
 */
public class ComprobantesController {

    @FXML private ComboBox<Matricula> cmbMatricula;
    @FXML private RadioButton rbPdf;
    @FXML private RadioButton rbExcel;
    @FXML private TextArea txtResultado;
    @FXML private Label lblMensaje;

    private final AppContext ctx = AppContext.getInstancia();
    private Comprobante ultimoComprobante;

    @FXML
    public void initialize() {
        cmbMatricula.setItems(ctx.getMatriculas());
    }

    @FXML
    private void onGenerar() {
        try {
            Matricula matricula = cmbMatricula.getValue();
            if (matricula == null) {
                throw new IllegalArgumentException("Selecciona una matricula");
            }

            // Factory Method: el generador concreto varia, el proceso de emision no.
            GeneradorComprobante generador = rbPdf.isSelected() ? new GeneradorPDF() : new GeneradorExcel();
            ultimoComprobante = generador.emitir(matricula);

            txtResultado.setText(ultimoComprobante.generar());
            lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
            lblMensaje.setText("Comprobante generado en formato " + ultimoComprobante.getExtension().toUpperCase() + ".");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onGuardar() {
        if (ultimoComprobante == null) {
            mostrarError("Primero genera un comprobante.");
            return;
        }
        try {
            FileChooser chooser = new FileChooser();
            chooser.setInitialFileName("comprobante_" + ultimoComprobante.getMatricula().getNumeroMatricula()
                    + "." + ultimoComprobante.getExtension());
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "Comprobante", "*." + ultimoComprobante.getExtension()));

            Stage stage = (Stage) txtResultado.getScene().getWindow();
            java.io.File archivo = chooser.showSaveDialog(stage);
            if (archivo != null) {
                Files.writeString(archivo.toPath(), ultimoComprobante.generar(), StandardCharsets.UTF_8);
                lblMensaje.getStyleClass().setAll("helper-text", "tag-success");
                lblMensaje.setText("Guardado en " + archivo.getAbsolutePath());
            }
        } catch (IOException e) {
            mostrarError("No se pudo guardar el archivo: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.getStyleClass().setAll("helper-text", "tag-danger");
        lblMensaje.setText(mensaje);
    }
}
