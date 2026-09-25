package linguaplus.modelo.comprobante;

import linguaplus.modelo.Matricula;

/**
 * Creador abstracto del patron <b>Factory Method</b> (RF-09).
 * <p>
 * El proceso de emision ({@link #emitir(Matricula)}) permanece fijo y estable;
 * cada subclase concreta ({@link GeneradorPDF}, {@link GeneradorExcel}, o un
 * futuro tercer formato) decide unicamente que tipo concreto de
 * {@link Comprobante} crear en {@link #crearComprobante(Matricula)}.
 */
public abstract class GeneradorComprobante {

    /** Factory Method: cada subclase decide el tipo concreto de comprobante. */
    protected abstract Comprobante crearComprobante(Matricula matricula);

    /** Proceso fijo de emision: calcula el total y delega la creacion del producto concreto. */
    public final Comprobante emitir(Matricula matricula) {
        return crearComprobante(matricula);
    }
}
