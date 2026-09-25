package linguaplus.modelo.entrega;

/** Producto concreto virtual: carne digital (RN-06). */
public class CarneDigital implements IdentificacionEntrega {
    @Override
    public String descripcion() {
        return "Carne digital";
    }
}
