package linguaplus.modelo.entrega;

import linguaplus.excepcion.EntregaInvalidaException;
import linguaplus.modelo.Modalidad;

/**
 * Servicio que orquesta la entrega de materiales usando SIEMPRE una unica
 * fabrica concreta para ambos productos (material + identificacion), lo que
 * garantiza estructuralmente que las familias nunca se mezclen (RN-07).
 */
public class ServicioEntrega {

    /** Devuelve la fabrica de entrega correspondiente a la modalidad (RF-10). */
    public static FabricaEntrega obtenerFabrica(Modalidad modalidad) {
        if (modalidad == null) {
            throw new EntregaInvalidaException("La modalidad no puede ser nula");
        }
        return switch (modalidad) {
            case PRESENCIAL -> new FabricaPresencial();
            case VIRTUAL -> new FabricaVirtual();
        };
    }

    /**
     * Prepara la entrega completa (material + identificacion) usando una
     * unica fabrica, y valida ademas que ambas piezas pertenezcan a la misma
     * familia (segunda linea de defensa ademas de la garantia estructural
     * del Abstract Factory).
     */
    public String preparar(FabricaEntrega fabrica) {
        MaterialEntrega material = fabrica.crearMaterial();
        IdentificacionEntrega identificacion = fabrica.crearIdentificacion();
        validarMismaFamilia(material, identificacion);
        return "Entrega preparada -> " + material.descripcion() + " + " + identificacion.descripcion();
    }

    private void validarMismaFamilia(MaterialEntrega material, IdentificacionEntrega identificacion) {
        boolean presencial = material instanceof MaterialImpreso && identificacion instanceof CarneFisico;
        boolean virtual = material instanceof LicenciaPlataforma && identificacion instanceof CarneDigital;
        if (!presencial && !virtual) {
            throw new EntregaInvalidaException(
                    "No se pueden mezclar combinaciones de entrega de distintas modalidades (RN-07)");
        }
    }
}
