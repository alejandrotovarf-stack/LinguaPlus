package linguaplus.excepcion;

/**
 * Se lanza cuando una matricula no cumple sus reglas obligatorias
 * (RN-01: numero, estudiante, programa y fecha de inicio; RN-02: sin programa).
 */
public class MatriculaInvalidaException extends LinguaPlusException {
    public MatriculaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
