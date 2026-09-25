package linguaplus.servicio;

import linguaplus.excepcion.AsignacionTutorInvalidaException;
import linguaplus.modelo.AsignacionTutor;
import linguaplus.modelo.Docente;
import linguaplus.modelo.Matricula;
import linguaplus.modelo.OfertaPrograma;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Orquesta el ciclo de vida de las matriculas: registro (usando el
 * {@link Matricula.Builder}), calculo de valor total y asignacion de
 * tutores. Es el punto unico de entrada desde la interfaz grafica para todo
 * lo relacionado con matriculas (RF-04 a RF-08).
 */
public class ServicioMatricula {

    private final List<Matricula> matriculas;

    public ServicioMatricula() {
        this(new ArrayList<>());
    }

    /** Permite inyectar una lista externa (por ejemplo, una ObservableList de JavaFX). */
    public ServicioMatricula(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    /**
     * Registra una matricula ya construida (validada por el Builder) y, si se
     * indica una oferta del periodo, consume un cupo de ella (RN-05).
     */
    public void registrar(Matricula matricula, OfertaPrograma oferta) {
        Objects.requireNonNull(matricula, "La matricula no puede ser nula");
        validar(matricula);
        if (oferta != null) {
            oferta.consumirCupo();
        }
        matriculas.add(matricula);
    }

    /** Sobrecarga para registrar una matricula sin oferta asociada (p. ej. pruebas de consola). */
    public void registrar(Matricula matricula) {
        registrar(matricula, null);
    }

    /** Revalida una matricula ya construida (defensa adicional ante RN-01 a RN-03). */
    public void validar(Matricula matricula) {
        ReglasNegocio.validarMatricula(matricula.getEstudiante(), matricula.getPrograma(), matricula.getFechaInicio());
        ReglasNegocio.validarDescuento(matricula.getDescuento());
    }

    public BigDecimal calcularTotal(Matricula matricula) {
        return matricula.calcularValorTotal();
    }

    /**
     * Asigna un docente como tutor de la matricula, validando previamente la
     * coherencia estudiante-programa-docente (RN-09) mediante
     * {@link AsignacionTutor}.
     */
    public void asignarTutor(Matricula matricula, Docente docente) {
        AsignacionTutor asignacion = new AsignacionTutor(matricula.getEstudiante(), matricula.getPrograma(), docente);
        if (!asignacion.validar()) {
            throw new AsignacionTutorInvalidaException(
                    "El docente " + docente.getNombre() + " no puede ser tutor de "
                            + matricula.getEstudiante().getNombreCompleto()
                            + " en el programa " + matricula.getPrograma().getNombre());
        }
        asignacion.asignar();
        matricula.asignarTutor(docente);
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }
}
