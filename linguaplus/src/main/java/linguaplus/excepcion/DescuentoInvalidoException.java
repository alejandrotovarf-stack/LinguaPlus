package linguaplus.excepcion;

/**
 * Se lanza cuando el descuento aplicado supera el maximo permitido (RN-03: 30%)
 * o es un valor negativo.
 */
public class DescuentoInvalidoException extends LinguaPlusException {
    public DescuentoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
