package linguaplus.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/** Docente de LinguaPlus, potencial tutor de un {@link ProgramaFormacion} (RF-06). */
public class Docente {

    private String identificacion;
    private String nombre;
    private String idiomaEspecialidad;
    private String telefono;
    private BigDecimal tarifaSesion;

    public Docente(String identificacion, String nombre, String idiomaEspecialidad,
                    String telefono, BigDecimal tarifaSesion) {
        this.identificacion = Objects.requireNonNull(identificacion);
        this.nombre = Objects.requireNonNull(nombre);
        this.idiomaEspecialidad = idiomaEspecialidad;
        this.telefono = telefono;
        this.tarifaSesion = tarifaSesion == null ? BigDecimal.ZERO : tarifaSesion;
    }

    /** Un docente puede ser tutor si tiene una tarifa por sesion valida (>= 0). */
    public boolean puedeSerTutor() {
        return tarifaSesion != null && tarifaSesion.compareTo(BigDecimal.ZERO) >= 0;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdiomaEspecialidad() {
        return idiomaEspecialidad;
    }

    public void setIdiomaEspecialidad(String idiomaEspecialidad) {
        this.idiomaEspecialidad = idiomaEspecialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getTarifaSesion() {
        return tarifaSesion;
    }

    public void setTarifaSesion(BigDecimal tarifaSesion) {
        this.tarifaSesion = tarifaSesion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Docente)) return false;
        Docente docente = (Docente) o;
        return Objects.equals(identificacion, docente.identificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacion);
    }

    @Override
    public String toString() {
        return nombre + " (" + idiomaEspecialidad + ")";
    }
}
