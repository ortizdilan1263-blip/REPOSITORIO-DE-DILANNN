package com.mycompany.simplex;

public class Simplex {

    private double[][] tabla;
    private int variables;
    private int restricciones;

    public Simplex(double[][] tabla, int variables, int restricciones) {
        this.tabla = tabla;
        this.variables = variables;
        this.restricciones = restricciones;
    }

    public void mostrarTabla() {

        // Encabezado de la tabla
        System.out.print("\tZ\t");

        // Variables
        for (int j = 1; j <= variables; j++) {
            System.out.print("X" + j + "\t");
        }

        // Variables de holgura
        for (int i = 1; i <= restricciones; i++) {
            System.out.print("S" + i + "\t");
        }

        // Lado derecho
        System.out.println("RHS");

        // Filas
        for (int i = 0; i < tabla.length; i++) {

            if (i == 0) {
                System.out.print("Z\t");
            } else {
                System.out.print("R" + i + "\t");
            }

            for (int j = 0; j < tabla[i].length; j++) {

                System.out.print(convertirFraccion(tabla[i][j]) + "\t");
            }

            System.out.println();
        }
    }

    public int buscarColumnaPivote() {

        int columnaPivote = 1;

        int ultimaFila = tabla.length - 1;

        for (int j = 1; j < tabla[0].length - 1; j++) {

            if (tabla[0][j] < tabla[0][columnaPivote]) {

                columnaPivote = j;
            }
        }

        return columnaPivote;
    }

    public int buscarFilaPivote(int columnaPivote) {

        int filaPivote = -1;

        double menorRazon = Double.MAX_VALUE;

        for (int i = 1; i < tabla.length; i++) {

            if (tabla[i][columnaPivote] > 0) {

                double razon = tabla[i][tabla[i].length - 1]
                        / tabla[i][columnaPivote];

                if (razon < menorRazon) {

                    menorRazon = razon;
                    filaPivote = i;
                }
            }
        }

        return filaPivote;
    }

    public void convertirPivote(int filaPivote, int columnaPivote) {

        double pivote = tabla[filaPivote][columnaPivote];

        for (int j = 0; j < tabla[filaPivote].length; j++) {

            tabla[filaPivote][j]
                    = tabla[filaPivote][j] / pivote;

        }

    }

    public void hacerCeros(int filaPivote, int columnaPivote) {

        for (int i = 0; i < tabla.length; i++) {

            if (i != filaPivote) {

                double factor = tabla[i][columnaPivote];

                for (int j = 0; j < tabla[i].length; j++) {

                    tabla[i][j] = tabla[i][j]
                            - factor * tabla[filaPivote][j];
                }
            }
        }
    }

    public boolean esOptimo() {

        int ultimaFila = tabla.length - 1;

        for (int j = 1; j < tabla[0].length - 1; j++) {

            if (tabla[0][j] < 0) {
                return false;
            }
        }

        return true;
    }

    public void resolver() {

        int iteracion = 0;
        while (!esOptimo()) {
            int columna = buscarColumnaPivote();
            int fila = buscarFilaPivote(columna);

            if (fila == -1) {
                System.out.println("\nEl problema no tiene solucion acotada.");
                return;
            }

            convertirPivote(fila, columna);
            hacerCeros(fila, columna);
            iteracion++;
            System.out.println("\nITERACION " + iteracion);
            mostrarTabla();

        }
    }

    public double obtenerZ() {

        int ultimaFila = tabla.length - 1;

        int ultimaColumna = tabla[0].length - 1;

        return tabla[0][ultimaColumna];
    }

    public double obtenerX1() {

        for (int i = 1; i < tabla.length; i++) {

            if (tabla[i][1] == 1) {

                boolean esBasica = true;

                for (int k = 1; k < tabla.length; k++) {

                    if (k != i && tabla[k][1] != 0) {
                        esBasica = false;
                        break;
                    }
                }

                if (esBasica) {
                    return tabla[i][tabla[i].length - 1];
                }
            }
        }

        return 0;
    }

    public double obtenerX2() {

        for (int i = 1; i < tabla.length; i++) {

            if (tabla[i][2] == 1) {

                boolean esBasica = true;

                for (int k = 1; k < tabla.length; k++) {

                    if (k != i && tabla[k][2] != 0) {
                        esBasica = false;
                        break;
                    }
                }

                if (esBasica) {
                    return tabla[i][tabla[i].length - 1];
                }
            }
        }

        return 0;
    }

    public double obtenerVariable(int columna) {

        for (int i = 1; i < tabla.length; i++) {

            if (tabla[i][columna] == 1) {

                boolean esBasica = true;

                for (int k = 1; k < tabla.length; k++) {

                    if (k != i && tabla[k][columna] != 0) {
                        esBasica = false;
                        break;
                    }
                }

                if (esBasica) {
                    return tabla[i][tabla[i].length - 1];
                }
            }
        }

        return 0;
    }

    public String convertirFraccion(double numero) {

        if (numero == 0) {
            return "0";
        }

        if (numero == Math.round(numero)) {
            return String.valueOf((long) numero);
        }

        int denominador = 1;

        while (Math.abs(numero * denominador - Math.round(numero * denominador)) > 0.000001
                && denominador < 1000) {

            denominador++;
        }

        long numerador = Math.round(numero * denominador);

        long mcd = mcd(Math.abs(numerador), denominador);

        numerador = numerador / mcd;
        denominador = (int) (denominador / mcd);

        if (denominador == 1) {
            return String.valueOf(numerador);
        }

        return numerador + "/" + denominador;
    }

    public long mcd(long a, long b) {

        while (b != 0) {

            long temporal = b;
            b = a % b;
            a = temporal;
        }

        return a;
    }
}
