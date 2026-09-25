package linguaplus.modelo;

/** Tipo de un {@link ProgramaFormacion}: basico, intensivo o personalizado. */
public enum TipoPrograma {
    BASICO("Basico"),
    INTENSIVO("Intensivo"),
    PERSONALIZADO("Personalizado");

    private final String etiqueta;

    TipoPrograma(String etiqueta) {
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
