package linguaplus.modelo.entrega;

/** Producto concreto presencial: material impreso (RN-06). */
public class MaterialImpreso implements MaterialEntrega {
    @Override
    public String descripcion() {
        return "Material impreso";
    }
}
