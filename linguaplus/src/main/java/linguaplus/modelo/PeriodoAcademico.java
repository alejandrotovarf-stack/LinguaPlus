package linguaplus.modelo;

import linguaplus.excepcion.LinguaPlusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Periodo academico (RF-03). Contiene la oferta de programas disponible
 * para ese periodo especifico.
 * <p>
 * Patron <b>Prototype</b>: {@link #clonarDesdeBase(String, LocalDate, LocalDate)}
 * crea un nuevo periodo copiando la oferta de un periodo base, pero clonando
 * cada {@link OfertaPrograma} individualmente para que los cupos consumidos
 * en un periodo NUNCA afecten a otro periodo (RN-05).
 */
public class PeriodoAcademico implements Cloneable {

    private String identificador;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private final List<OfertaPrograma> oferta = new ArrayList<>();

    public PeriodoAcademico(String identificador, LocalDate fechaInicio, LocalDate fechaFin) {
        this.identificador = Objects.requireNonNull(identificador);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public void addOferta(OfertaPrograma ofertaPrograma) {
        oferta.add(Objects.requireNonNull(ofertaPrograma));
    }

    /**
     * Crea un nuevo periodo academico a partir de ESTE periodo, usado como base,
     * clonando cada oferta (Prototype) para que el nuevo periodo tenga cupos
     * propios e independientes.
     */
    public PeriodoAcademico clonarDesdeBase(String nuevoIdentificador, LocalDate nuevaFechaInicio,
                                             LocalDate nuevaFechaFin) {
        PeriodoAcademico nuevo = new PeriodoAcademico(nuevoIdentificador, nuevaFechaInicio, nuevaFechaFin);
        for (OfertaPrograma o : this.oferta) {
            nuevo.addOferta(o.clone());
        }
        return nuevo;
    }

    /** Busca la oferta de un programa por su codigo dentro de este periodo. */
    private OfertaPrograma buscarOferta(String codigoPrograma) {
        return oferta.stream()
                .filter(o -> o.getPrograma().getCodigo().equalsIgnoreCase(codigoPrograma))
                .findFirst()
                .orElseThrow(() -> new LinguaPlusException(
                        "El programa " + codigoPrograma + " no esta ofertado en el periodo " + identificador));
    }

    public void consumirCupo(String codigoPrograma) {
        buscarOferta(codigoPrograma).consumirCupo();
    }

    public int getCupo(String codigoPrograma) {
        return buscarOferta(codigoPrograma).getCupos();
    }

    public List<OfertaPrograma> getOferta() {
        return Collections.unmodifiableList(oferta);
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    /** True si la fecha dada cae dentro del rango [fechaInicio, fechaFin] del periodo. */
    public boolean contiene(LocalDate fecha) {
        if (fecha == null) return false;
        boolean despuesDeInicio = fechaInicio == null || !fecha.isBefore(fechaInicio);
        boolean antesDeFin = fechaFin == null || !fecha.isAfter(fechaFin);
        return despuesDeInicio && antesDeFin;
    }

    @Override
    public String toString() {
        return identificador + " (" + fechaInicio + " a " + fechaFin + ")";
    }
}
