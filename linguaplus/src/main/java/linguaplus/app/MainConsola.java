package linguaplus.app;

import linguaplus.excepcion.LinguaPlusException;
import linguaplus.modelo.ConsecutivoMatricula;
import linguaplus.modelo.Docente;
import linguaplus.modelo.EstadoPrograma;
import linguaplus.modelo.Estudiante;
import linguaplus.modelo.Matricula;
import linguaplus.modelo.Modalidad;
import linguaplus.modelo.OfertaPrograma;
import linguaplus.modelo.PeriodoAcademico;
import linguaplus.modelo.ProgramaFormacion;
import linguaplus.modelo.ServicioAdicional;
import linguaplus.modelo.TipoPrograma;
import linguaplus.modelo.comprobante.Comprobante;
import linguaplus.modelo.comprobante.GeneradorComprobante;
import linguaplus.modelo.comprobante.GeneradorExcel;
import linguaplus.modelo.comprobante.GeneradorPDF;
import linguaplus.modelo.entrega.FabricaEntrega;
import linguaplus.modelo.entrega.ServicioEntrega;
import linguaplus.servicio.AnalizadorNumero;
import linguaplus.servicio.CalculadorIngresos;
import linguaplus.servicio.ServicioMatricula;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Punto de entrada de consola (sin JavaFX) para probar la logica de negocio
 * y los cinco patrones de diseno, siguiendo los casos definidos en la
 * Pregunta 6 y la Pregunta 8 del documento de pensamiento computacional.
 * <p>
 * Ejecutar con: {@code mvn exec:java} (usa la propiedad consola.class del pom.xml).
 */
public final class MainConsola {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println(" LINGUAPLUS - PRUEBAS DE CONSOLA (sin JavaFX)");
        System.out.println("==============================================\n");

        probarSingleton();
        probarBuilder();
        probarPrototype();
        probarFactoryMethod();
        probarAbstractFactory();
        probarNumeroPerfecto();
        probarIngresosPorPeriodo();

        System.out.println("\n==============================================");
        System.out.println(" FIN DE LAS PRUEBAS");
        System.out.println("==============================================");
    }

    /** Prueba 1: Singleton -- ConsecutivoMatricula siempre entrega numeros consecutivos. */
    private static void probarSingleton() {
        titulo("1) Singleton - ConsecutivoMatricula");
        ConsecutivoMatricula c1 = ConsecutivoMatricula.getInstancia();
        ConsecutivoMatricula c2 = ConsecutivoMatricula.getInstancia();
        System.out.println("¿Misma instancia? " + (c1 == c2));
        System.out.println("Siguiente: " + c1.siguiente());
        System.out.println("Siguiente: " + c2.siguiente());
        System.out.println("Siguiente: " + c1.siguiente());
    }

    /** Prueba 2: Builder -- matricula valida y matriculas invalidas rechazadas (RN-01 a RN-03). */
    private static void probarBuilder() {
        titulo("2) Builder - Matricula.Builder");

        Estudiante ana = new Estudiante("Ana Torres", "1001", "3001234567", "ana@mail.com", 22);
        ProgramaFormacion ingles = crearProgramaIngles();

        Matricula matriculaValida = new Matricula.Builder()
                .estudiante(ana)
                .programa(ingles)
                .fechaInicio(LocalDate.now())
                .descuento(BigDecimal.TEN)
                .observaciones("Cliente frecuente")
                .build();
        System.out.println("Matricula valida creada: " + matriculaValida
                + " | Total: $" + matriculaValida.calcularValorTotal());

        try {
            new Matricula.Builder()
                    .estudiante(ana)
                    .fechaInicio(LocalDate.now())
                    .build(); // sin programa -> debe fallar (RN-02)
            System.out.println("ERROR: no debio permitir una matricula sin programa");
        } catch (LinguaPlusException e) {
            System.out.println("Rechazada correctamente (sin programa): " + e.getMessage());
        }

        try {
            new Matricula.Builder()
                    .estudiante(ana)
                    .programa(ingles)
                    .fechaInicio(LocalDate.now())
                    .descuento(BigDecimal.valueOf(31)) // supera el 30% -> debe fallar (RN-03)
                    .build();
            System.out.println("ERROR: no debio permitir un descuento de 31%");
        } catch (LinguaPlusException e) {
            System.out.println("Rechazada correctamente (descuento 31%): " + e.getMessage());
        }
    }

    /** Prueba 3: Prototype -- clonar oferta base para dos periodos con cupos independientes (RN-05). */
    private static void probarPrototype() {
        titulo("3) Prototype - PeriodoAcademico / OfertaPrograma");

        ProgramaFormacion ingles = crearProgramaIngles();
        PeriodoAcademico base = new PeriodoAcademico("BASE", LocalDate.now(), LocalDate.now().plusMonths(1));
        base.addOferta(new OfertaPrograma(ingles, "Lunes y miercoles 6-8pm", 5));

        PeriodoAcademico periodoA = base.clonarDesdeBase("2026-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        PeriodoAcademico periodoB = base.clonarDesdeBase("2026-2", LocalDate.now().plusMonths(4), LocalDate.now().plusMonths(8));

        periodoA.consumirCupo(ingles.getCodigo());
        periodoA.consumirCupo(ingles.getCodigo());

        System.out.println("Cupos periodo A tras consumir 2: " + periodoA.getCupo(ingles.getCodigo()));
        System.out.println("Cupos periodo B (no debe verse afectado): " + periodoB.getCupo(ingles.getCodigo()));
    }

    /** Prueba 4: Factory Method -- mismo comprobante en PDF y Excel con la misma informacion. */
    private static void probarFactoryMethod() {
        titulo("4) Factory Method - GeneradorComprobante");

        Estudiante luis = new Estudiante("Luis Perez", "1002", "3009876543", "luis@mail.com", 30);
        ProgramaFormacion ingles = crearProgramaIngles();
        Matricula matricula = new Matricula.Builder()
                .estudiante(luis)
                .programa(ingles)
                .fechaInicio(LocalDate.now())
                .build();

        GeneradorComprobante generadorPdf = new GeneradorPDF();
        GeneradorComprobante generadorExcel = new GeneradorExcel();

        Comprobante pdf = generadorPdf.emitir(matricula);
        Comprobante excel = generadorExcel.emitir(matricula);

        System.out.println("--- Comprobante PDF ---");
        System.out.println(pdf.generar());
        System.out.println("--- Comprobante Excel ---");
        System.out.println(excel.generar());
        System.out.println("Mismo valor total en ambos formatos: "
                + pdf.getValorTotal().equals(excel.getValorTotal()));
    }

    /** Prueba 5: Abstract Factory -- entregas presencial y virtual nunca se mezclan (RN-06/RN-07). */
    private static void probarAbstractFactory() {
        titulo("5) Abstract Factory - FabricaEntrega");

        ServicioEntrega servicioEntrega = new ServicioEntrega();

        FabricaEntrega fabricaPresencial = ServicioEntrega.obtenerFabrica(Modalidad.PRESENCIAL);
        FabricaEntrega fabricaVirtual = ServicioEntrega.obtenerFabrica(Modalidad.VIRTUAL);

        System.out.println(servicioEntrega.preparar(fabricaPresencial));
        System.out.println(servicioEntrega.preparar(fabricaVirtual));
    }

    /** Prueba 6: Numero perfecto (RF-12). 28 es perfecto; 30 no lo es. */
    private static void probarNumeroPerfecto() {
        titulo("6) Consulta - Numero perfecto (RF-12)");
        AnalizadorNumero analizador = new AnalizadorNumero();
        System.out.println("¿28 es perfecto? " + analizador.esPerfecto(28) + " (suma divisores: " + analizador.sumaDivisores(28) + ")");
        System.out.println("¿30 es perfecto? " + analizador.esPerfecto(30) + " (suma divisores: " + analizador.sumaDivisores(30) + ")");
        System.out.println("¿6 es perfecto? " + analizador.esPerfecto(6) + " (suma divisores: " + analizador.sumaDivisores(6) + ")");
    }

    /** Prueba 7: Ingresos por periodo (RF-13) -- solo se acumulan matriculas dentro del periodo. */
    private static void probarIngresosPorPeriodo() {
        titulo("7) Ingresos por periodo (RF-13)");

        ProgramaFormacion ingles = crearProgramaIngles();
        Estudiante e1 = new Estudiante("Marta Diaz", "2001", "3101111111", "marta@mail.com", 25);
        Estudiante e2 = new Estudiante("Pedro Gomez", "2002", "3102222222", "pedro@mail.com", 28);
        Estudiante e3 = new Estudiante("Sofia Ruiz", "2003", "3103333333", "sofia@mail.com", 20);

        PeriodoAcademico periodo2026_1 = new PeriodoAcademico("2026-1",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30));
        PeriodoAcademico periodo2026_2 = new PeriodoAcademico("2026-2",
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 12, 31));

        List<Matricula> matriculas = new ArrayList<>();
        ServicioMatricula servicioMatricula = new ServicioMatricula(matriculas);

        servicioMatricula.registrar(new Matricula.Builder()
                .estudiante(e1).programa(ingles).fechaInicio(LocalDate.of(2026, 2, 10)).build());
        servicioMatricula.registrar(new Matricula.Builder()
                .estudiante(e2).programa(ingles).fechaInicio(LocalDate.of(2026, 3, 5)).build());
        servicioMatricula.registrar(new Matricula.Builder()
                .estudiante(e3).programa(ingles).fechaInicio(LocalDate.of(2026, 8, 20)).build());

        CalculadorIngresos calculador = new CalculadorIngresos();
        BigDecimal ingresos2026_1 = calculador.calcular(periodo2026_1, matriculas);
        BigDecimal ingresos2026_2 = calculador.calcular(periodo2026_2, matriculas);

        System.out.println("Matriculas en 2026-1: " + calculador.filtrarPorPeriodo(matriculas, periodo2026_1).size()
                + " | Ingresos: $" + ingresos2026_1);
        System.out.println("Matriculas en 2026-2: " + calculador.filtrarPorPeriodo(matriculas, periodo2026_2).size()
                + " | Ingresos: $" + ingresos2026_2);
    }

    private static ProgramaFormacion crearProgramaIngles() {
        ProgramaFormacion p = new ProgramaFormacion(
                "ING-B1", "Ingles Intensivo B1", "Ingles",
                "Curso intensivo de ingles nivel B1", 6, BigDecimal.valueOf(250000),
                EstadoPrograma.ACTIVO, TipoPrograma.INTENSIVO, Modalidad.PRESENCIAL);
        p.agregarBeneficio("Plataforma virtual");
        p.agregarBeneficio("Club de conversacion");
        return p;
    }

    private static void titulo(String texto) {
        System.out.println("\n--- " + texto + " ---");
    }
}
