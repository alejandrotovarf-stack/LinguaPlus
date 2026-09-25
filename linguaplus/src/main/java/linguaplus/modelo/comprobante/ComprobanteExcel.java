package linguaplus.modelo.comprobante;

import linguaplus.modelo.Matricula;
import linguaplus.modelo.ServicioAdicional;

import java.math.BigDecimal;

/**
 * Comprobante en formato Excel, simulado como texto con celdas separadas por
 * tabulaciones (estilo CSV/TSV), ver la nota de implementacion en {@link Comprobante}.
 */
public class ComprobanteExcel extends Comprobante {

    private static final String EXTENSION = "xlsx";

    public ComprobanteExcel(Matricula matricula, BigDecimal valorTotal) {
        super(matricula, valorTotal);
    }

    @Override
    public String generar() {
        StringBuilder sb = new StringBuilder();
        sb.append("COMPROBANTE DE PAGO - LINGUAPLUS (Formato Excel)\n\n");
        sb.append("Campo\tValor\n");
        sb.append("Matricula No.\t").append(matricula.getNumeroMatricula()).append('\n');
        sb.append("Estudiante\t").append(matricula.getEstudiante().getNombreCompleto()).append('\n');
        sb.append("Documento\t").append(matricula.getEstudiante().getDocumento()).append('\n');
        sb.append("Programa\t").append(matricula.getPrograma().getNombre()).append('\n');
        sb.append("Fecha inicio\t").append(matricula.getFechaInicio().format(FORMATO_FECHA)).append('\n');
        sb.append("Descuento (%)\t").append(matricula.getDescuento()).append('\n');
        sb.append('\n');
        sb.append("Servicio adicional\tPrecio\n");
        for (ServicioAdicional s : matricula.getServicios()) {
            sb.append(s.getNombre()).append('\t').append(s.getPrecio()).append('\n');
        }
        sb.append('\n');
        sb.append("VALOR TOTAL\t").append(valorTotal).append('\n');
        return sb.toString();
    }

    @Override
    public String getExtension() {
        return EXTENSION;
    }
}
