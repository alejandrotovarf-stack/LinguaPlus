package linguaplus.modelo;

import linguaplus.excepcion.DescuentoInvalidoException;
import linguaplus.excepcion.MatriculaInvalidaException;
import linguaplus.excepcion.ServicioNoDisponibleException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Matricula de un {@link Estudiante} en un {@link ProgramaFormacion} (RF-04).
 * <p>
 * Se construye exclusivamente a traves de {@link Matricula.Builder}, que
 * aplica el patron <b>Builder</b>: concentra los campos obligatorios y
 * opcionales, y valida todas las reglas de negocio (RN-01 a RN-03) dentro de
 * {@link Builder#build()} antes de crear el objeto, de forma que nunca puede
 * existir una instancia de {@code Matricula} en un estado invalido.
 */
public final class Matricula {

    private static final BigDecimal DESCUENTO_MAXIMO = BigDecimal.valueOf(30);

    private final int numeroMatricula;
    private final Estudiante estudiante;
    private final ProgramaFormacion programa;
    private final LocalDate fechaInicio;
    private Docente tutor;
    private final List<ServicioAdicional> servicios;
    private final BigDecimal descuento;
    private final String observaciones;

    private Matricula(Builder builder) {
        this.numeroMatricula = ConsecutivoMatricula.getInstancia().siguiente();
        this.estudiante = builder.estudiante;
        this.programa = builder.programa;
        this.fechaInicio = builder.fechaInicio;
        this.tutor = builder.tutor;
        this.servicios = new ArrayList<>(builder.servicios);
        this.descuento = builder.descuento;
        this.observaciones = builder.observaciones;
    }

    /** Valor final de la matricula: valor del programa menos descuento, mas servicios (RF-08, RN-08). */
    public BigDecimal calcularValorTotal() {
        BigDecimal valorPrograma = programa.calcularValorBase();
        BigDecimal factorDescuento = descuento.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal montoDescuento = valorPrograma.multiply(factorDescuento);
        BigDecimal valorConDescuento = valorPrograma.subtract(montoDescuento);

        BigDecimal valorServicios = servicios.stream()
                .map(ServicioAdicional::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return valorConDescuento.add(valorServicios).setScale(2, RoundingMode.HALF_UP);
    }

    /** Agrega un servicio adicional ya validado como disponible (RN-08, RN-10). */
    public void agregarServicio(ServicioAdicional servicio) {
        Objects.requireNonNull(servicio, "El servicio no puede ser nulo");
        if (!servicio.estaDisponible()) {
            throw new ServicioNoDisponibleException(
                    "El servicio '" + servicio.getNombre() + "' no esta disponible");
        }
        servicios.add(servicio);
    }

    public void asignarTutor(Docente docente) {
        this.tutor = docente;
    }

    public int getNumeroMatricula() {
        return numeroMatricula;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public ProgramaFormacion getPrograma() {
        return programa;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public Docente getTutor() {
        return tutor;
    }

    public List<ServicioAdicional> getServicios() {
        return Collections.unmodifiableList(servicios);
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    @Override
    public String toString() {
        return "Matricula #" + numeroMatricula + " - " + estudiante.getNombreCompleto()
                + " - " + programa.getNombre();
    }

    /** Builder de {@link Matricula}: obligatorios + opcionales + validacion centralizada. */
    public static class Builder {
        private Estudiante estudiante;
        private ProgramaFormacion programa;
        private LocalDate fechaInicio;
        private Docente tutor;
        private final List<ServicioAdicional> servicios = new ArrayList<>();
        private BigDecimal descuento = BigDecimal.ZERO;
        private String observaciones;

        public Builder estudiante(Estudiante estudiante) {
            this.estudiante = estudiante;
            return this;
        }

        public Builder programa(ProgramaFormacion programa) {
            this.programa = programa;
            return this;
        }

        public Builder fechaInicio(LocalDate fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public Builder tutor(Docente tutor) {
            this.tutor = tutor;
            return this;
        }

        public Builder agregarServicio(ServicioAdicional servicio) {
            if (servicio != null) {
                if (!servicio.estaDisponible()) {
                    throw new ServicioNoDisponibleException(
                            "El servicio '" + servicio.getNombre() + "' no esta disponible");
                }
                this.servicios.add(servicio);
            }
            return this;
        }

        public Builder descuento(BigDecimal descuento) {
            this.descuento = descuento;
            return this;
        }

        public Builder observaciones(String observaciones) {
            this.observaciones = observaciones;
            return this;
        }

        /**
         * Valida las reglas de negocio obligatorias y construye la matricula.
         * Si algo no es valido, lanza una excepcion especifica y NO se
         * consume numero de matricula (la matricula invalida nunca llega a
         * construirse, ver RN-04 y la prueba correspondiente en MainConsola).
         */
        public Matricula build() {
            if (estudiante == null) {
                throw new MatriculaInvalidaException("La matricula debe tener un estudiante (RN-01)");
            }
            if (programa == null) {
                throw new MatriculaInvalidaException(
                        "No puede existir una matricula sin programa (RN-01 / RN-02)");
            }
            if (fechaInicio == null) {
                throw new MatriculaInvalidaException("La matricula debe tener fecha de inicio (RN-01)");
            }
            if (descuento == null) {
                descuento = BigDecimal.ZERO;
            }
            if (descuento.compareTo(BigDecimal.ZERO) < 0 || descuento.compareTo(DESCUENTO_MAXIMO) > 0) {
                throw new DescuentoInvalidoException(
                        "El descuento no puede superar el 30% del valor del programa (RN-03)");
            }
            return new Matricula(this);
        }
    }
}
