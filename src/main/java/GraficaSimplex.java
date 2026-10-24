import javax.swing.*;
import java.awt.*;

public class GraficaSimplex extends JPanel {

    private double[] coefX1;
    private double[] coefX2;
    private double[] rhs;

    private double x1Optimo;
    private double x2Optimo;

    public GraficaSimplex(double[] coefX1, double[] coefX2,
                          double[] rhs, double x1Optimo, double x2Optimo) {

        this.coefX1 = coefX1;
        this.coefX2 = coefX2;
        this.rhs = rhs;

        this.x1Optimo = x1Optimo;
        this.x2Optimo = x2Optimo;

        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D grafica = (Graphics2D) g;

        int ancho = getWidth();
        int alto = getHeight();

        int margen = 60;

        // Encontrar el valor máximo para los ejes
        double maximo = 10;

        for (int i = 0; i < rhs.length; i++) {

            if (coefX1[i] != 0) {
                maximo = Math.max(maximo, rhs[i] / Math.abs(coefX1[i]));
            }

            if (coefX2[i] != 0) {
                maximo = Math.max(maximo, rhs[i] / Math.abs(coefX2[i]));
            }
        }

        maximo = Math.ceil(maximo + 2);

        // Escala
        double escalaX = (ancho - 2.0 * margen) / maximo;
        double escalaY = (alto - 2.0 * margen) / maximo;

        // Funciones para convertir coordenadas matemáticas
        // a coordenadas de la pantalla
        int origenX = margen;
        int origenY = alto - margen;

        // Dibujar ejes
        grafica.setColor(Color.BLACK);

        grafica.drawLine(
                origenX,
                origenY,
                ancho - margen,
                origenY
        );

        grafica.drawLine(
                origenX,
                origenY,
                origenX,
                margen
        );

        // Nombres de los ejes
        grafica.drawString("X1", ancho - margen + 10, origenY);
        grafica.drawString("X2", origenX, margen - 10);

        // Marcas de los ejes
        for (int i = 0; i <= maximo; i++) {

            int x = (int) (origenX + i * escalaX);
            int y = (int) (origenY - i * escalaY);

            grafica.drawLine(
                    x,
                    origenY - 5,
                    x,
                    origenY + 5
            );

            grafica.drawLine(
                    origenX - 5,
                    y,
                    origenX + 5,
                    y
            );

            if (i > 0) {
                grafica.drawString(
                        String.valueOf(i),
                        x - 3,
                        origenY + 20
                );

                grafica.drawString(
                        String.valueOf(i),
                        origenX - 20,
                        y + 5
                );
            }
        }

        // Dibujar las restricciones
        for (int i = 0; i < rhs.length; i++) {

            double a = coefX1[i];
            double b = coefX2[i];
            double c = rhs[i];

            // Evitar división entre cero
            if (b != 0) {

                // Punto cuando X1 = 0
                double x2Inicio = c / b;

                // Punto cuando X1 = maximo
                double x2Final =
                        (c - a * maximo) / b;

                int xInicio = (int) (origenX);
                int yInicio =
                        (int) (origenY - x2Inicio * escalaY);

                int xFinal =
                        (int) (origenX + maximo * escalaX);

                int yFinal =
                        (int) (origenY - x2Final * escalaY);

                grafica.setColor(
                        Color.getHSBColor(
                                (float) i / Math.max(rhs.length, 1),
                                0.8f,
                                0.8f
                        )
                );

                grafica.drawLine(
                        xInicio,
                        yInicio,
                        xFinal,
                        yFinal
                );

                grafica.drawString(
                        "R" + (i + 1),
                        xInicio + 10,
                        yInicio - 5
                );
            }
        }

        // Dibujar el punto óptimo
        int xOptimo =
                (int) (origenX + x1Optimo * escalaX);

        int yOptimo =
                (int) (origenY - x2Optimo * escalaY);

        grafica.setColor(Color.RED);

        grafica.fillOval(
                xOptimo - 6,
                yOptimo - 6,
                12,
                12
        );

        grafica.drawString(
                "Optimo (" + x1Optimo + ", " + x2Optimo + ")",
                xOptimo + 10,
                yOptimo - 10
        );

        // Titulo
        grafica.setColor(Color.BLACK);

        grafica.drawString(
                "Metodo Grafico - Simplex",
                ancho / 2 - 70,
                25
        );
    }

    public static void mostrarGrafica(
            double[] coefX1,
            double[] coefX2,
            double[] rhs,
            double x1Optimo,
            double x2Optimo) {

        JFrame ventana = new JFrame("Metodo Grafico");

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        GraficaSimplex panel =
                new GraficaSimplex(
                        coefX1,
                        coefX2,
                        rhs,
                        x1Optimo,
                        x2Optimo
                );

        ventana.add(panel);

        ventana.setSize(800, 600);

        ventana.setLocationRelativeTo(null);

        ventana.setVisible(true);
    }
}