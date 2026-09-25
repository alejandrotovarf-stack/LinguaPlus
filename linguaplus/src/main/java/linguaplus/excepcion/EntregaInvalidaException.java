package linguaplus.excepcion;

/**
 * Se lanza cuando se intenta combinar piezas de entrega de distintas
 * familias (por ejemplo material impreso con carne digital), violando
 * RN-06 / RN-07. Protege la coherencia que ya garantiza estructuralmente
 * el patron Abstract Factory, como segunda linea de defensa.
 */
public class EntregaInvalidaException extends LinguaPlusException {
    public EntregaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
