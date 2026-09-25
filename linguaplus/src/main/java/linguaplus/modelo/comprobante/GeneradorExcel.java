package linguaplus.modelo.comprobante;

import linguaplus.modelo.Matricula;

public class GeneradorExcel extends GeneradorComprobante {
    @Override
    protected Comprobante crearComprobante(Matricula matricula) {
        return new ComprobanteExcel(matricula, matricula.calcularValorTotal());
    }
}
