package linguaplus.excepcion;

/**
 * Se lanza cuando no es posible asignar un docente como tutor de un
 * estudiante para un programa determinado (RN-09).
 */
public class AsignacionTutorInvalidaException extends LinguaPlusException {
    public AsignacionTutorInvalidaException(String mensaje) {
        super(mensaje);
    }
}
