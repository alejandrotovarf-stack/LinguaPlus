package linguaplus.modelo.comprobante;

import linguaplus.modelo.Matricula;

public class GeneradorPDF extends GeneradorComprobante {
    @Override
    protected Comprobante crearComprobante(Matricula matricula) {
        return new ComprobantePDF(matricula, matricula.calcularValorTotal());
    }
}
