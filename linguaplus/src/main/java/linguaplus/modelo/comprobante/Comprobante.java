package linguaplus.modelo.comprobante;

import linguaplus.modelo.Matricula;
import linguaplus.modelo.ServicioAdicional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Producto abstracto del patron <b>Factory Method</b> (RF-09).
 * <p>
 * Representa el comprobante de pago de una {@link Matricula}. La
 * informacion comun se construye aqui en {@link #obtenerDatos()}; cada
 * subclase concreta decide unicamente el formato final en {@link #generar()}.
 * <p>
 * Nota de implementacion: por decision explicita del cliente, este proyecto
 * NO usa librerias externas (iText / Apache POI) para generar binarios PDF o
 * XLSX reales. En su lugar, {@code generar()} produce una representacion en
 * texto plano que simula cada formato. Gracias al Factory Method, sustituir
 * esta simulacion por una libreria real en el futuro solo implica cambiar el
 * cuerpo de {@code generar()} en {@link ComprobantePDF} / {@link ComprobanteExcel}
 * (o anadir un tercer formato), sin tocar el resto del sistema.
 */
public abstract class Comprobante {

    protected static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected final Matricula matricula;
    protected final BigDecimal valorTotal;

    protected Comprobante(Matricula matricula, BigDecimal valorTotal) {
        this.matricula = matricula;
        this.valorTotal = valorTotal;
    }

    /** Genera el contenido del comprobante en el formato concreto de la subclase. */
    public abstract String generar();

    /** Extension de archivo sugerida para este formato (sin punto), ej. "pdf" o "xlsx". */
    public abstract String getExtension();

    /** Datos comunes del comprobante, reutilizados por todos los formatos. */
    public String obtenerDatos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Matricula No.: ").append(matricula.getNumeroMatricula()).append('\n');
        sb.append("Estudiante: ").append(matricula.getEstudiante().getNombreCompleto())
                .append(" (").append(matricula.getEstudiante().getDocumento()).append(")\n");
        sb.append("Programa: ").append(matricula.getPrograma().getNombre())
                .append(" - ").append(matricula.getPrograma().getIdioma()).append('\n');
        sb.append("Fecha de inicio: ").append(matricula.getFechaInicio().format(FORMATO_FECHA)).append('\n');
        if (matricula.getTutor() != null) {
            sb.append("Tutor asignado: ").append(matricula.getTutor().getNombre()).append('\n');
        }
        sb.append("Descuento aplicado: ").append(matricula.getDescuento()).append("%\n");
        if (!matricula.getServicios().isEmpty()) {
            sb.append("Servicios adicionales:\n");
            for (ServicioAdicional s : matricula.getServicios()) {
                sb.append("  - ").append(s.getNombre()).append(": $").append(s.getPrecio()).append('\n');
            }
        }
        sb.append("VALOR TOTAL: $").append(valorTotal).append('\n');
        return sb.toString();
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}
