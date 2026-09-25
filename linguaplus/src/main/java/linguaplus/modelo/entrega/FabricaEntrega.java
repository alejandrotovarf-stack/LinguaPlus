package linguaplus.modelo.entrega;

/**
 * Fabrica abstracta del patron <b>Abstract Factory</b> (RN-06 / RN-07).
 * Cada implementacion concreta ({@link FabricaPresencial}, {@link FabricaVirtual})
 * crea un combo coherente de productos de entrega que nunca se mezcla con el
 * de la otra modalidad.
 */
public interface FabricaEntrega {
    MaterialEntrega crearMaterial();
    IdentificacionEntrega crearIdentificacion();
}
