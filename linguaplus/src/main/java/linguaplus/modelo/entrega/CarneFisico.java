package linguaplus.modelo.entrega;

/** Producto concreto presencial: carne fisico (RN-06). */
public class CarneFisico implements IdentificacionEntrega {
    @Override
    public String descripcion() {
        return "Carne fisico";
    }
}
