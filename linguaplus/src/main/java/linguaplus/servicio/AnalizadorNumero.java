package linguaplus.servicio;

/**
 * Analiza propiedades numericas requeridas por el negocio: en particular,
 * determina si un numero (por ejemplo, el telefono de un estudiante) es un
 * numero perfecto (RF-12): un entero positivo igual a la suma de sus
 * divisores propios (todos los divisores menores que el mismo).
 * Ejemplo clasico: 28 = 1 + 2 + 4 + 7 + 14.
 */
public class AnalizadorNumero {

    public boolean esPerfecto(int n) {
        if (n <= 1) {
            return false;
        }
        return sumaDivisores(n) == n;
    }

    /** Suma de los divisores propios de n (excluyendo a n mismo). */
    public int sumaDivisores(int n) {
        if (n <= 1) {
            return 0;
        }
        int suma = 1; // 1 siempre es divisor propio de n > 1
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) {
                suma += i;
                int complementario = n / i;
                if (complementario != i) {
                    suma += complementario;
                }
            }
        }
        return suma;
    }
}
