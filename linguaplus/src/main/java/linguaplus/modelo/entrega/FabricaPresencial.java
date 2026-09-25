package linguaplus.modelo.entrega;

/** Fabrica concreta: modalidad presencial => material impreso + carne fisico (RN-06). */
public class FabricaPresencial implements FabricaEntrega {
    @Override
    public MaterialImpreso crearMaterial() {
        return new MaterialImpreso();
    }

    @Override
    public CarneFisico crearIdentificacion() {
        return new CarneFisico();
    }
}
