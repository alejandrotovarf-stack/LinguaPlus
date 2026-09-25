package linguaplus.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguaplus.modelo.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase principal de la aplicacion JavaFX (RF-14). Arranca la interfaz
 * grafica organizada en MVC: carga {@code MainView.fxml}, aplica la hoja de
 * estilos y muestra la ventana principal.
 * <p>
 * Ejecutar con: {@code mvn javafx:run}
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        sembrarDatosDemo();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/linguaplus/vista/MainView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1180, 760);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/linguaplus/css/styles.css")).toExternalForm());

        stage.setTitle("LinguaPlus - Sistema de Gestion Academica");
        stage.setScene(scene);
        stage.setMinWidth(980);
        stage.setMinHeight(640);
        stage.show();
    }

    /** Carga algunos datos de ejemplo para que la GUI no se vea vacia al arrancar. */
    private void sembrarDatosDemo() {
        AppContext ctx = AppContext.getInstancia();

        Estudiante ana = new Estudiante("Ana Torres", "1001", "3001234567", "ana.torres@mail.com", 22);
        Estudiante luis = new Estudiante("Luis Perez", "1002", "3009876543", "luis.perez@mail.com", 30);
        ctx.getEstudiantes().addAll(ana, luis);
        ctx.getLinguaPlus().registrarEstudiante(ana);
        ctx.getLinguaPlus().registrarEstudiante(luis);

        ProgramaFormacion ingles = new ProgramaFormacion("ING-B1", "Ingles Intensivo B1", "Ingles",
                "Curso intensivo de ingles nivel B1", 6, BigDecimal.valueOf(250000),
                EstadoPrograma.ACTIVO, TipoPrograma.INTENSIVO, Modalidad.PRESENCIAL);
        ingles.agregarBeneficio("Plataforma virtual");
        ingles.agregarBeneficio("Club de conversacion");

        ProgramaFormacion frances = new ProgramaFormacion("FRA-A1", "Frances Basico A1", "Frances",
                "Curso basico de frances para principiantes", 4, BigDecimal.valueOf(180000),
                EstadoPrograma.ACTIVO, TipoPrograma.BASICO, Modalidad.VIRTUAL);
        frances.agregarBeneficio("Plataforma virtual");

        ctx.getProgramas().addAll(ingles, frances);
        ctx.getLinguaPlus().registrarPrograma(ingles);
        ctx.getLinguaPlus().registrarPrograma(frances);

        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2027, 12, 31);
        
        PeriodoAcademico periodo1=new PeriodoAcademico("1",inicio,fin);

        Docente carlos = new Docente("D-01", "Carlos Ramirez", "Ingles", "3151234567", BigDecimal.valueOf(45000));
        Docente marie = new Docente("D-02", "Marie Dubois", "Frances", "3157654321", BigDecimal.valueOf(50000));
        ctx.getDocentes().addAll(carlos, marie);

        ServicioAdicional tutorias = new ServicioAdicional("SRV-01", "Tutorias personalizadas",
                "Sesiones extra de acompanamiento", BigDecimal.valueOf(60000), true);
        ServicioAdicional certificado = new ServicioAdicional("SRV-02", "Certificado internacional",
                "Emision de certificado con validez internacional", BigDecimal.valueOf(90000), true);
        ctx.getServicios().addAll(tutorias, certificado);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
