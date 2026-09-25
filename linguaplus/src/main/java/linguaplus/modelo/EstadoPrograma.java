package linguaplus.modelo;

/** Estado del ciclo de vida de un {@link ProgramaFormacion}. */
public enum EstadoPrograma {
    ACTIVO("Activo"),
    SUSPENDIDO("Suspendido"),
    FINALIZADO("Finalizado");

    private final String etiqueta;

    EstadoPrograma(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
