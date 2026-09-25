package linguaplus.servicio;

import linguaplus.modelo.Matricula;
import linguaplus.modelo.PeriodoAcademico;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calcula los ingresos generados por las matriculas realizadas dentro de un
 * periodo academico especifico (RF-13). Una matricula pertenece a un periodo
 * cuando su fecha de inicio cae dentro del rango [fechaInicio, fechaFin] del
 * periodo (regla de asociacion elegida ante la ausencia de un campo
 * "periodo" explicito en Matricula, ver notas del modelo).
 */
public class CalculadorIngresos {

    /** Suma el valor total de las matriculas que pertenecen al periodo dado. */
    public BigDecimal calcular(PeriodoAcademico periodo, List<Matricula> matriculas) {
        return filtrarPorPeriodo(matriculas, periodo).stream()
                .map(Matricula::calcularValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Filtra, de una lista de matriculas, solo las que pertenecen al periodo dado. */
    public List<Matricula> filtrarPorPeriodo(List<Matricula> matriculas, PeriodoAcademico periodo) {
        return matriculas.stream()
                .filter(m -> periodo.contiene(m.getFechaInicio()))
                .collect(Collectors.toList());
    }
}
