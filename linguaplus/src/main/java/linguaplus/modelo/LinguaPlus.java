package linguaplus.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a la academia LinguaPlus como negocio: sus datos institucionales
 * y las colecciones raiz de estudiantes, programas y periodos academicos.
 */
public class LinguaPlus {

    private String nombreComercial;
    private String nit;
    private String direccion;
    private String telefono;
    private String email;
    private String sitioWeb;

    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<ProgramaFormacion> programas = new ArrayList<>();
    private final List<PeriodoAcademico> periodos = new ArrayList<>();

    public LinguaPlus(String nombreComercial, String nit, String direccion,
                       String telefono, String email, String sitioWeb) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.sitioWeb = sitioWeb;
    }

    public void registrarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public void registrarPrograma(ProgramaFormacion programa) {
        programas.add(programa);
    }

    public void abrirPeriodo(PeriodoAcademico periodo) {
        periodos.add(periodo);
    }

    public List<Estudiante> getEstudiantes() {
        return Collections.unmodifiableList(estudiantes);
    }

    public List<ProgramaFormacion> getProgramas() {
        return Collections.unmodifiableList(programas);
    }

    public List<PeriodoAcademico> getPeriodos() {
        return Collections.unmodifiableList(periodos);
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }
}
