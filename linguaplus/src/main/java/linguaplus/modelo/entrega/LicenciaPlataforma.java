package linguaplus.modelo.entrega;

/** Producto concreto virtual: licencia de plataforma (RN-06). */
public class LicenciaPlataforma implements MaterialEntrega {
    @Override
    public String descripcion() {
        return "Licencia de plataforma virtual";
    }
}
