package linguaplus.modelo;

/**
 * Patron <b>Singleton</b>: garantiza que el numero de matricula sea
 * consecutivo y unico sin importar la sede o el computador desde el que se
 * registre (RF-05, RN-04).
 * <p>
 * La instanciacion eager (constante estatica final) es segura para
 * inicializacion concurrente en JVM; el metodo {@link #siguiente()} esta
 * ademas sincronizado para proteger el contador ante accesos concurrentes.
 */
public final class ConsecutivoMatricula {

    private static final ConsecutivoMatricula INSTANCIA = new ConsecutivoMatricula();

    private int ultimo = 0;

    private ConsecutivoMatricula() {
        // Constructor privado: unica forma de obtener la instancia es getInstancia().
    }

    public static ConsecutivoMatricula getInstancia() {
        return INSTANCIA;
    }

    public synchronized int siguiente() {
        return ++ultimo;
    }

    /** Utilitario de solo lectura, principalmente para pruebas. */
    public synchronized int ultimoAsignado() {
        return ultimo;
    }
}
