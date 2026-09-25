package linguaplus.servicio;

import linguaplus.excepcion.DescuentoInvalidoException;
import linguaplus.excepcion.MatriculaInvalidaException;
import linguaplus.excepcion.ServicioNoDisponibleException;
import linguaplus.modelo.Estudiante;
import linguaplus.modelo.ProgramaFormacion;
import linguaplus.modelo.ServicioAdicional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase de utilidad (no instanciable) que centraliza las validaciones de
 * reglas de negocio (RN-01 a RN-10), evitando que la interfaz grafica u
 * otras clases dupliquen esta logica. Las clases de dominio (Matricula.Builder,
 * ServicioAdicional, etc.) tambien la usan internamente donde aplica.
 */
public final class ReglasNegocio {

    private static final BigDecimal DESCUENTO_MAXIMO = BigDecimal.valueOf(30);

    private ReglasNegocio() {
        // Utilitario: no se instancia.
    }

    public static void validarDescuento(BigDecimal descuento) {
        if (descuento == null || descuento.compareTo(BigDecimal.ZERO) < 0
                || descuento.compareTo(DESCUENTO_MAXIMO) > 0) {
            throw new DescuentoInvalidoException(
                    "El descuento no puede superar el 30% del valor del programa (RN-03)");
        }
    }

    public static void validarMatricula(Estudiante estudiante, ProgramaFormacion programa, LocalDate fechaInicio) {
        if (estudiante == null) {
            throw new MatriculaInvalidaException("La matricula debe tener un estudiante (RN-01)");
        }
        if (programa == null) {
            throw new MatriculaInvalidaException("No puede existir una matricula sin programa (RN-02)");
        }
        if (fechaInicio == null) {
            throw new MatriculaInvalidaException("La matricula debe tener fecha de inicio (RN-01)");
        }
    }

    public static void validarServicio(ServicioAdicional servicio) {
        if (servicio == null) {
            throw new ServicioNoDisponibleException("El servicio no puede ser nulo");
        }
        if (!servicio.estaDisponible()) {
            throw new ServicioNoDisponibleException(
                    "El servicio '" + servicio.getNombre() + "' no esta disponible (RN-10)");
        }
    }

    /** Verifica que la modalidad de origen y destino de una entrega coincidan (RN-06 / RN-07). */
    public static void validarModalidad(boolean coinciden) {
        if (!coinciden) {
            throw new linguaplus.excepcion.EntregaInvalidaException(
                    "Las combinaciones de entrega no se pueden mezclar (RN-07)");
        }
    }
}
