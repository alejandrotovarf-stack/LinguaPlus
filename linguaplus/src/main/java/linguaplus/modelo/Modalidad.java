package linguaplus.modelo;

/**
 * Modalidad de un programa. Determina, a traves del patron Abstract Factory
 * (paquete {@code linguaplus.modelo.entrega}), que familia de productos de
 * entrega (material + identificacion) corresponde (RN-06 / RN-07).
 */
public enum Modalidad {
    PRESENCIAL("Presencial"),
    VIRTUAL("Virtual");

    private final String etiqueta;

    Modalidad(String etiqueta) {
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
