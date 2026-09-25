package linguaplus.modelo;

import linguaplus.excepcion.CupoAgotadoException;

import java.util.Objects;

/**
 * Oferta de un {@link ProgramaFormacion} dentro de un periodo: horario y cupos.
 * <p>
 * Implementa {@link Cloneable} porque es la pieza que el patron <b>Prototype</b>
 * clona para cada {@link PeriodoAcademico}: cada clon obtiene su propio contador
 * de cupos disponibles, totalmente independiente del periodo del que se clono
 * (RN-05).
 */
public class OfertaPrograma implements Cloneable {

    private ProgramaFormacion programa;
    private String horario;
    private int cuposDisponibles;

    public OfertaPrograma(ProgramaFormacion programa, String horario, int cuposDisponibles) {
        this.programa = Objects.requireNonNull(programa, "La oferta requiere un programa");
        this.horario = horario;
        this.cuposDisponibles = cuposDisponibles;
    }

    /** Consume un cupo de esta oferta. Lanza excepcion si ya no hay disponibles. */
    public void consumirCupo() {
        if (cuposDisponibles <= 0) {
            throw new CupoAgotadoException(
                    "No hay cupos disponibles para " + programa.getNombre() + " en el horario " + horario);
        }
        cuposDisponibles--;
    }

    public int getCupos() {
        return cuposDisponibles;
    }

    /**
     * Clon superficial en cuanto al programa (se comparte, es un catalogo),
     * pero con un contador de cupos totalmente nuevo e independiente:
     * exactamente lo que exige el patron Prototype para esta clase.
     */
    @Override
    public OfertaPrograma clone() {
        try {
            return (OfertaPrograma) super.clone();
        } catch (CloneNotSupportedException e) {
            // No deberia ocurrir: OfertaPrograma implementa Cloneable.
            throw new AssertionError("Clonacion de OfertaPrograma no soportada", e);
        }
    }

    public ProgramaFormacion getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaFormacion programa) {
        this.programa = programa;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    @Override
    public String toString() {
        return programa.getNombre() + " - " + horario + " (" + cuposDisponibles + " cupos)";
    }
}
