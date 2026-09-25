package linguaplus.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Programa de formacion ofrecido por LinguaPlus (RF-02). */
public class ProgramaFormacion {

    private String codigo;
    private String nombre;
    private String idioma;
    private String descripcion;
    private int duracionMeses;
    private BigDecimal valorMensual;
    private EstadoPrograma estado;
    private TipoPrograma tipo;
    private final List<String> beneficios = new ArrayList<>();
    private Modalidad modalidad;

    public ProgramaFormacion(String codigo, String nombre, String idioma, String descripcion,
                              int duracionMeses, BigDecimal valorMensual, EstadoPrograma estado,
                              TipoPrograma tipo, Modalidad modalidad) {
        this.codigo = Objects.requireNonNull(codigo);
        this.nombre = Objects.requireNonNull(nombre);
        this.idioma = idioma;
        this.descripcion = descripcion;
        this.duracionMeses = duracionMeses;
        this.valorMensual = valorMensual == null ? BigDecimal.ZERO : valorMensual;
        this.estado = estado == null ? EstadoPrograma.ACTIVO : estado;
        this.tipo = tipo == null ? TipoPrograma.BASICO : tipo;
        this.modalidad = modalidad == null ? Modalidad.PRESENCIAL : modalidad;
    }

    /** Valor base del programa completo: valor mensual x duracion en meses. */
    public BigDecimal calcularValorBase() {
        return valorMensual.multiply(BigDecimal.valueOf(duracionMeses));
    }

    public boolean esPersonalizado() {
        return tipo == TipoPrograma.PERSONALIZADO;
    }

    public void agregarBeneficio(String beneficio) {
        if (beneficio != null && !beneficio.isBlank()) {
            beneficios.add(beneficio);
        }
    }

    public List<String> getBeneficios() {
        return Collections.unmodifiableList(beneficios);
    }

    public void setBeneficios(List<String> nuevos) {
        beneficios.clear();
        if (nuevos != null) {
            beneficios.addAll(nuevos);
        }
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

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(int duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public BigDecimal getValorMensual() {
        return valorMensual;
    }

    public void setValorMensual(BigDecimal valorMensual) {
        this.valorMensual = valorMensual;
    }

    public EstadoPrograma getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrograma estado) {
        this.estado = estado;
    }

    public TipoPrograma getTipo() {
        return tipo;
    }

    public void setTipo(TipoPrograma tipo) {
        this.tipo = tipo;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProgramaFormacion)) return false;
        ProgramaFormacion that = (ProgramaFormacion) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + idioma + ")";
    }
}
