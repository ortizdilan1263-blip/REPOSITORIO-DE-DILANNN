import com.mycompany.simplex.Simplex;
import java.util.Scanner;

public class SimplexPrincipal {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        System.out.println("Cuantas variables tiene el problema?");
        int variables = entrada.nextInt();

        System.out.println("Cuantas restricciones tiene el problema?");
        int restricciones = entrada.nextInt();

        int filas = restricciones + 1;
        int columnas = variables + restricciones + 2;

        double[][] tabla = new double[filas][columnas];

        // Arreglos para el metodo grafico
        double[] objetivo = new double[variables];

        double[] coefX1 = new double[restricciones];
        double[] coefX2 = new double[restricciones];
        double[] rhs = new double[restricciones];

        // Valor de Z
        tabla[0][0] = 1;

        // ============================
        // FUNCION OBJETIVO
        // ============================

        System.out.println("\nFUNCION OBJETIVO");

        for (int j = 1; j <= variables; j++) {

            System.out.print(
                    "Coeficiente de X" + j + ": "
            );

            double coeficiente =
                    entrada.nextDouble();

            objetivo[j - 1] = coeficiente;

            // Para maximización se utiliza
            // el coeficiente negativo
            tabla[0][j] = -coeficiente;
        }

        // ============================
        // RESTRICCIONES
        // ============================

        System.out.println("\nRESTRICCIONES");

        for (int i = 1; i <= restricciones; i++) {

            System.out.println(
                    "\nRestriccion " + i
            );

            for (int j = 1; j <= variables; j++) {

                System.out.print(
                        "Coeficiente de X" + j + ": "
                );

                double coeficiente =
                        entrada.nextDouble();

                tabla[i][j] = coeficiente;

                // Guardar datos para la grafica
                if (variables == 2) {

                    if (j == 1) {
                        coefX1[i - 1] = coeficiente;
                    }

                    if (j == 2) {
                        coefX2[i - 1] = coeficiente;
                    }
                }
            }

            // Variable de holgura
            tabla[i][variables + i] = 1;

            System.out.print(
                    "Lado derecho: "
            );

            double ladoDerecho =
                    entrada.nextDouble();

            tabla[i][columnas - 1] =
                    ladoDerecho;

            // Guardar datos para la grafica
            rhs[i - 1] = ladoDerecho;
        }

        // ============================
        // CREAR SIMPLEX
        // ============================

        Simplex simplex =
                new Simplex(
                        tabla,
                        variables,
                        restricciones
                );

        // ============================
        // TABLA INICIAL
        // ============================

        System.out.println(
                "\nTABLA INICIAL:"
        );

        simplex.mostrarTabla();

        // ============================
        // RESOLVER SIMPLEX
        // ============================

        simplex.resolver();

        // ============================
        // SOLUCION OPTIMA
        // ============================

        System.out.println(
                "\nSOLUCION OPTIMA"
        );

        System.out.println(
                "----------------"
        );

        for (int j = 1; j <= variables; j++) {

            System.out.println(
                    "X" + j + " = "
                    + simplex.obtenerVariable(j)
            );
        }

        System.out.println(
                "Z = " + simplex.obtenerZ()
        );

        // ============================
        // METODO GRAFICO
        // ============================

        if (variables == 2) {

            double x1Optimo =
                    simplex.obtenerVariable(1);

            double x2Optimo =
                    simplex.obtenerVariable(2);

            GraficaSimplex.mostrarGrafica(
                    coefX1,
                    coefX2,
                    rhs,
                    x1Optimo,
                    x2Optimo
            );

        } else {

            System.out.println(
                    "\nEl metodo grafico solo se muestra "
                    + "para problemas con 2 variables."
            );
        }
    }
}