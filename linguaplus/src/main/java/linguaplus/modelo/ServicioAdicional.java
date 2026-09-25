package linguaplus.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/** Servicio adicional asociable a un programa dentro de una matricula (RF-07, RN-08, RN-10). */
public class ServicioAdicional {

    private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private boolean disponible;

    public ServicioAdicional(String codigo, String nombre, String descripcion,
                              BigDecimal precio, boolean disponible) {
        this.codigo = Objects.requireNonNull(codigo);
        this.nombre = Objects.requireNonNull(nombre);
        this.descripcion = descripcion;
        this.precio = precio == null ? BigDecimal.ZERO : precio;
        this.disponible = disponible;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicioAdicional)) return false;
        ServicioAdicional that = (ServicioAdicional) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")" + (disponible ? "" : " [NO DISPONIBLE]");
    }
}
