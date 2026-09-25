package linguaplus.excepcion;

/**
 * Se lanza cuando una oferta de un periodo academico ya no tiene cupos
 * disponibles. Los cupos de un periodo son independientes de los de
 * otro periodo (RN-05), gracias al patron Prototype aplicado sobre
 * PeriodoAcademico / OfertaPrograma.
 */
public class CupoAgotadoException extends LinguaPlusException {
    public CupoAgotadoException(String mensaje) {
        super(mensaje);
    }
}
