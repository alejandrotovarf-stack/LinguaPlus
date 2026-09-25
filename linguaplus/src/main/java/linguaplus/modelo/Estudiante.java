package linguaplus.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa a un estudiante de LinguaPlus (RF-01).
 * El documento de identidad es el identificador natural de negocio.
 */
public class Estudiante {

    private String nombreCompleto;
    private String documento;
    private String telefono;
    private String correo;
    private int edad;
    private final LocalDate fechaRegistro;

    private final List<ProgramaFormacion> programasAdquiridos = new ArrayList<>();

    public Estudiante(String nombreCompleto, String documento, String telefono,
                       String correo, int edad) {
        this(nombreCompleto, documento, telefono, correo, edad, LocalDate.now());
    }

    public Estudiante(String nombreCompleto, String documento, String telefono,
                       String correo, int edad, LocalDate fechaRegistro) {
        this.nombreCompleto = Objects.requireNonNull(nombreCompleto, "El nombre es obligatorio");
        this.documento = Objects.requireNonNull(documento, "El documento es obligatorio");
        this.telefono = telefono;
        this.correo = correo;
        this.edad = edad;
        this.fechaRegistro = fechaRegistro == null ? LocalDate.now() : fechaRegistro;
    }

    /** Vincula un programa como adquirido por el estudiante (uso desde la GUI/servicios). */
    public void adquirirPrograma(ProgramaFormacion programa) {
        Objects.requireNonNull(programa, "El programa no puede ser nulo");
        programasAdquiridos.add(programa);
    }

    public List<ProgramaFormacion> getProgramasAdquiridos() {
        return Collections.unmodifiableList(programasAdquiridos);
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estudiante)) return false;
        Estudiante that = (Estudiante) o;
        return Objects.equals(documento, that.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return nombreCompleto + " (" + documento + ")";
    }
}
