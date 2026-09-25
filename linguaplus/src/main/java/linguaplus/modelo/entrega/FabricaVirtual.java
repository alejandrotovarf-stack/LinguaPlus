package linguaplus.modelo.entrega;

/** Fabrica concreta: modalidad virtual => licencia de plataforma + carne digital (RN-06). */
public class FabricaVirtual implements FabricaEntrega {
    @Override
    public LicenciaPlataforma crearMaterial() {
        return new LicenciaPlataforma();
    }

    @Override
    public CarneDigital crearIdentificacion() {
        return new CarneDigital();
    }
}
