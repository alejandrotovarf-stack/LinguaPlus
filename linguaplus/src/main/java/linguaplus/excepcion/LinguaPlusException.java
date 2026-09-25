package linguaplus.excepcion;

/**
 * Excepcion base de todas las excepciones de negocio del dominio LinguaPlus.
 * Se usa RuntimeException como base para no forzar try/catch en cada llamada
 * de la capa de modelo, pero SI se captura explicitamente en la capa de
 * servicio/controlador para informar al usuario sin detener la aplicacion.
 */
public class LinguaPlusException extends RuntimeException {

    public LinguaPlusException(String mensaje) {
        super(mensaje);
    }

    public LinguaPlusException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
