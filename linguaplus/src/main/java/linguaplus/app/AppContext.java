package linguaplus.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import linguaplus.modelo.Docente;
import linguaplus.modelo.Estudiante;
import linguaplus.modelo.LinguaPlus;
import linguaplus.modelo.Matricula;
import linguaplus.modelo.PeriodoAcademico;
import linguaplus.modelo.ProgramaFormacion;
import linguaplus.modelo.ServicioAdicional;
import linguaplus.servicio.CalculadorIngresos;
import linguaplus.servicio.ServicioConsulta;
import linguaplus.servicio.ServicioMatricula;

/**
 * Contenedor de estado compartido de la aplicacion JavaFX. Actua como
 * fachada simple entre la GUI (vista/controlador) y la capa de
 * modelo/servicio, evitando que los controladores dupliquen listas o logica
 * de negocio (que debe vivir exclusivamente en la capa de servicio, ver
 * documento de pensamiento computacional, modulo "5. Entrega e interfaz").
 * <p>
 * Se implementa como instancia unica de aplicacion (no es el patron Singleton
 * del dominio, que es exclusivamente {@link linguaplus.modelo.ConsecutivoMatricula});
 * simplemente centraliza el estado en memoria requerido por la GUI.
 */
public final class AppContext {

    private static final AppContext INSTANCIA = new AppContext();

    private final LinguaPlus linguaPlus;

    private final ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();
    private final ObservableList<ProgramaFormacion> programas = FXCollections.observableArrayList();
    private final ObservableList<Docente> docentes = FXCollections.observableArrayList();
    private final ObservableList<ServicioAdicional> servicios = FXCollections.observableArrayList();
    private final ObservableList<PeriodoAcademico> periodos = FXCollections.observableArrayList();
    private final ObservableList<Matricula> matriculas = FXCollections.observableArrayList();

    private final ServicioMatricula servicioMatricula;
    private final ServicioConsulta servicioConsulta;
    private final CalculadorIngresos calculadorIngresos;

    /**
     * Periodo "base" usado como plantilla de staging: aqui se agregan las
     * ofertas (programa + horario + cupos) que luego se clonan (Prototype)
     * hacia cada periodo academico real mediante
     * {@link PeriodoAcademico#clonarDesdeBase}, garantizando que los cupos
     * consumidos en un periodo nunca afecten a otro (RN-05).
     */
    private final PeriodoAcademico periodoBase;

    private AppContext() {
        this.linguaPlus = new LinguaPlus(
                "LinguaPlus",
                "900.123.456-7",
                "Calle 10 # 20-30, Cali, Colombia",
                "+57 602 555 0100",
                "contacto@linguaplus.edu.co",
                "www.linguaplus.edu.co");
        this.servicioMatricula = new ServicioMatricula(matriculas);
        this.servicioConsulta = new ServicioConsulta(estudiantes);
        this.calculadorIngresos = new CalculadorIngresos();
        this.periodoBase = new PeriodoAcademico("BASE", null, null);
    }

    public PeriodoAcademico getPeriodoBase() {
        return periodoBase;
    }

    public static AppContext getInstancia() {
        return INSTANCIA;
    }

    public LinguaPlus getLinguaPlus() {
        return linguaPlus;
    }

    public ObservableList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public ObservableList<ProgramaFormacion> getProgramas() {
        return programas;
    }

    public ObservableList<Docente> getDocentes() {
        return docentes;
    }

    public ObservableList<ServicioAdicional> getServicios() {
        return servicios;
    }

    public ObservableList<PeriodoAcademico> getPeriodos() {
        return periodos;
    }

    public ObservableList<Matricula> getMatriculas() {
        return matriculas;
    }

    public ServicioMatricula getServicioMatricula() {
        return servicioMatricula;
    }

    public ServicioConsulta getServicioConsulta() {
        return servicioConsulta;
    }

    public CalculadorIngresos getCalculadorIngresos() {
        return calculadorIngresos;
    }
}
