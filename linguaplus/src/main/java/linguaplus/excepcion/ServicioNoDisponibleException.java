package linguaplus.excepcion;

/**
 * Se lanza al intentar usar un servicio adicional que no esta disponible (RN-10).
 */
public class ServicioNoDisponibleException extends LinguaPlusException {
    public ServicioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
