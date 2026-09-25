package linguaplus.servicio;

import linguaplus.modelo.Estudiante;

import java.util.List;
import java.util.Optional;

/**
 * Consultas sobre estudiantes (RF-11, RF-12): busqueda por telefono y
 * verificacion de numero perfecto.
 */
public class ServicioConsulta {

    private final List<Estudiante> estudiantes;
    private final AnalizadorNumero analizadorNumero;

    public ServicioConsulta(List<Estudiante> estudiantes) {
        this(estudiantes, new AnalizadorNumero());
    }

    public ServicioConsulta(List<Estudiante> estudiantes, AnalizadorNumero analizadorNumero) {
        this.estudiantes = estudiantes;
        this.analizadorNumero = analizadorNumero;
    }

    /** Busca un estudiante por su numero de telefono exacto (RF-11). */
    public Optional<Estudiante> buscarPorTelefono(String telefono) {
        if (telefono == null) {
            return Optional.empty();
        }
        return estudiantes.stream()
                .filter(e -> telefono.trim().equals(e.getTelefono()))
                .findFirst();
    }

    /** Determina si el numero es un numero perfecto (RF-12). */
    public boolean esNumeroPerfecto(int n) {
        return analizadorNumero.esPerfecto(n);
    }
}
