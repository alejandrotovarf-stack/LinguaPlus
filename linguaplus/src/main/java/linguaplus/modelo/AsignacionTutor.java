package linguaplus.modelo;

import java.util.Objects;

/**
 * Representa la asignacion de un {@link Docente} como tutor de un
 * {@link Estudiante} para un {@link ProgramaFormacion} especifico (RF-06),
 * preservando siempre la relacion estudiante-programa-docente (RN-09).
 */
public class AsignacionTutor {

    private final Estudiante estudiante;
    private final ProgramaFormacion programa;
    private final Docente docente;

    public AsignacionTutor(Estudiante estudiante, ProgramaFormacion programa, Docente docente) {
        this.estudiante = Objects.requireNonNull(estudiante);
        this.programa = Objects.requireNonNull(programa);
        this.docente = Objects.requireNonNull(docente);
    }

    /**
     * Una asignacion es valida si el docente puede ser tutor y su idioma de
     * especialidad coincide con el idioma del programa: mantiene coherente
     * la relacion estudiante-programa-docente (RN-09).
     */
    public boolean validar() {
        if (!docente.puedeSerTutor()) {
            return false;
        }
        if (docente.getIdiomaEspecialidad() == null || programa.getIdioma() == null) {
            return true; // sin informacion suficiente para descartar, se permite
        }
        return docente.getIdiomaEspecialidad().equalsIgnoreCase(programa.getIdioma());
    }

    /** No hace nada por si sola: la persistencia real ocurre en la Matricula (ver ServicioMatricula). */
    public void asignar() {
        // La asignacion efectiva la realiza ServicioMatricula.asignarTutor(),
        // que llama a Matricula.asignarTutor(docente) tras validar() == true.
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public ProgramaFormacion getPrograma() {
        return programa;
    }

    public Docente getDocente() {
        return docente;
    }
}
