package linguaplus.modelo.comprobante;

import linguaplus.modelo.Matricula;

import java.math.BigDecimal;

/** Comprobante en formato PDF (simulado como texto plano, ver {@link Comprobante}). */
public class ComprobantePDF extends Comprobante {

    private static final String EXTENSION = "pdf";

    public ComprobantePDF(Matricula matricula, BigDecimal valorTotal) {
        super(matricula, valorTotal);
    }

    @Override
    public String generar() {
        String linea = "=".repeat(50);
        StringBuilder sb = new StringBuilder();
        sb.append(linea).append('\n');
        sb.append("   COMPROBANTE DE PAGO - LINGUAPLUS (Formato PDF)\n");
        sb.append(linea).append('\n');
        sb.append(obtenerDatos());
        sb.append(linea).append('\n');
        return sb.toString();
    }

    @Override
    public String getExtension() {
        return EXTENSION;
    }
}
