package visual;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel visual para mostrar un bracket con rondas, tarjetas y conexiones.
 */
public class PanelBracket extends JPanel {
    private BracketDibujo panelDibujo;

    /**
     * Crea la vista del cuadro de eliminacion directa.
     */
    public PanelBracket() {
        this.setLayout(new BorderLayout(0, 12));
        this.setBorder(EstilosVisuales.crearBordePanel());
        this.setBackground(EstilosVisuales.SUPERFICIE);

        JLabel titulo = EstilosVisuales.crearTituloPanel("Bracket de eliminacion");
        titulo.setForeground(EstilosVisuales.OLIVA);

        JLabel descripcion = new JLabel("Cuadro visual para los enfrentamientos de eliminacion directa.");
        descripcion.setFont(EstilosVisuales.FUENTE_NORMAL);
        descripcion.setForeground(EstilosVisuales.TEXTO_SECUNDARIO);

        JPanel cabecera = new JPanel(new BorderLayout(0, 4));
        cabecera.setOpaque(false);
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(descripcion, BorderLayout.CENTER);

        panelDibujo = new BracketDibujo();
        JScrollPane scroll = new JScrollPane(panelDibujo);
        scroll.setBorder(BorderFactory.createLineBorder(EstilosVisuales.BORDE));
        scroll.getViewport().setBackground(EstilosVisuales.CAMPO);

        this.add(cabecera, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);

        limpiarBracket();
    }

    /**
     * Muestra los enfrentamientos del bracket.
     *
     * @param enfrentamientos textos que representan cada cruce
     */
    public void actualizarBracket(List<String> enfrentamientos) {
        if (enfrentamientos == null || enfrentamientos.isEmpty()) {
            limpiarBracket();
            return;
        }

        panelDibujo.actualizar(enfrentamientos);
    }

    /**
     * Deja el panel en estado inicial.
     */
    public void limpiarBracket() {
        List<String> vacio = new ArrayList<>();
        vacio.add("No hay bracket generado");
        panelDibujo.actualizar(vacio);
    }

    /**
     * Dibuja el bracket como columnas de rondas conectadas.
     */
    private static class BracketDibujo extends JPanel {
        private static final int ANCHO_TARJETA = 190;
        private static final int ALTO_TARJETA = 46;
        private static final int ESPACIO_X = 95;
        private static final int ESPACIO_Y = 34;
        private static final int MARGEN = 28;

        private List<List<String>> rondas;

        /**
         * Deja el lienzo listo para recibir las rondas del bracket.
         */
        BracketDibujo() {
            this.rondas = new ArrayList<>();
            this.setBackground(EstilosVisuales.CAMPO);
        }

        /**
         * Recibe los cruces ya formateados y fuerza el redibujado del cuadro.
         *
         * @param enfrentamientos textos armados por el controlador
         */
        void actualizar(List<String> enfrentamientos) {
            rondas = agruparPorRonda(enfrentamientos);
            actualizarTamano();
            repaint();
        }

        /**
         * Separa los textos por numero de ronda para dibujarlos por columnas.
         *
         * @param enfrentamientos lista plana de cruces
         * @return grupos ordenados por ronda
         */
        private List<List<String>> agruparPorRonda(List<String> enfrentamientos) {
            List<List<String>> grupos = new ArrayList<>();

            for (String texto : enfrentamientos) {
                int ronda = obtenerNumeroRonda(texto);

                while (grupos.size() < ronda) {
                    grupos.add(new ArrayList<>());
                }

                grupos.get(ronda - 1).add(limpiarTextoRonda(texto));
            }

            return grupos;
        }

        private int obtenerNumeroRonda(String texto) {
            if (texto != null && texto.startsWith("Ronda ")) {
                String[] partes = texto.split(" ");
                if (partes.length > 1) {
                    try {
                        return Integer.parseInt(partes[1]);
                    } catch (NumberFormatException e) {
                        return 1;
                    }
                }
            }
            return 1;
        }

        private String limpiarTextoRonda(String texto) {
            if (texto == null) {
                return "";
            }
            return texto.replaceFirst("Ronda \\d+ \\| ", "");
        }

        private void actualizarTamano() {
            int columnas = Math.max(1, rondas.size());
            int filasMaximas = 1;

            for (List<String> ronda : rondas) {
                filasMaximas = Math.max(filasMaximas, ronda.size());
            }

            int ancho = MARGEN * 2 + columnas * ANCHO_TARJETA + (columnas - 1) * ESPACIO_X;
            int alto = MARGEN * 2 + filasMaximas * ALTO_TARJETA + (filasMaximas - 1) * ESPACIO_Y;

            setPreferredSize(new Dimension(ancho, Math.max(alto, 260)));
            revalidate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            dibujarConexiones(g2);
            dibujarTarjetas(g2);

            g2.dispose();
        }

        /**
         * Dibuja las lineas que unen una ronda con la siguiente.
         */
        private void dibujarConexiones(Graphics2D g2) {
            g2.setColor(EstilosVisuales.BORDE);
            g2.setStroke(new BasicStroke(2f));

            for (int r = 0; r < rondas.size(); r++) {
                List<String> rondaActual = rondas.get(r);
                List<String> rondaSiguiente = r + 1 < rondas.size()
                        ? rondas.get(r + 1)
                        : new ArrayList<>();

                for (int i = 0; i < rondaActual.size(); i += 2) {
                    int destino = i / 2;

                    Rectangle caja1 = obtenerRectangulo(r, i);
                    Rectangle caja2 = i + 1 < rondaActual.size()
                            ? obtenerRectangulo(r, i + 1)
                            : caja1;

                    int xInicio = caja1.x + caja1.width;
                    int yInicio1 = caja1.y + caja1.height / 2;
                    int yInicio2 = caja2.y + caja2.height / 2;
                    int xMedio = xInicio + ESPACIO_X / 2;
                    int yMedio = (yInicio1 + yInicio2) / 2;
                    int xDestino = xMedio + ESPACIO_X / 2;
                    int yDestino = yMedio;

                    if (destino < rondaSiguiente.size()) {
                        Rectangle cajaDestino = obtenerRectangulo(r + 1, destino);
                        xDestino = cajaDestino.x;
                        yDestino = cajaDestino.y + cajaDestino.height / 2;
                    }

                    g2.drawLine(xInicio, yInicio1, xMedio, yInicio1);
                    g2.drawLine(xInicio, yInicio2, xMedio, yInicio2);
                    g2.drawLine(xMedio, yInicio1, xMedio, yInicio2);
                    g2.drawLine(xMedio, yMedio, xDestino, yDestino);
                }
            }
        }

        /**
         * Dibuja cada tarjeta del bracket dentro de su columna.
         */
        private void dibujarTarjetas(Graphics2D g2) {
            for (int r = 0; r < rondas.size(); r++) {
                List<String> ronda = rondas.get(r);

                g2.setFont(EstilosVisuales.FUENTE_ETIQUETA);
                g2.setColor(EstilosVisuales.OLIVA);
                g2.drawString("Ronda " + (r + 1), obtenerX(r), 18);

                for (int i = 0; i < ronda.size(); i++) {
                    dibujarTarjeta(g2, obtenerRectangulo(r, i), ronda.get(i));
                }
            }
        }

        private void dibujarTarjeta(Graphics2D g2, Rectangle rect, String texto) {
            g2.setColor(EstilosVisuales.SUPERFICIE);
            g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 12, 12);

            g2.setColor(EstilosVisuales.ESMERALDA);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 12, 12);

            g2.setColor(EstilosVisuales.TEXTO);
            g2.setFont(EstilosVisuales.FUENTE_NORMAL);

            String[] lineas = partirTexto(texto);
            int y = rect.y + 18;
            for (String linea : lineas) {
                g2.drawString(linea, rect.x + 10, y);
                y += 17;
            }
        }

        private String[] partirTexto(String texto) {
            if (texto.length() <= 24) {
                return new String[]{texto};
            }

            int corte = texto.indexOf(" | ");
            if (corte > 0) {
                return new String[]{
                        texto.substring(0, corte),
                        texto.substring(corte + 3)
                };
            }

            return new String[]{
                    texto.substring(0, Math.min(24, texto.length())),
                    texto.substring(Math.min(24, texto.length()))
            };
        }

        private Rectangle obtenerRectangulo(int ronda, int indice) {
            return new Rectangle(obtenerX(ronda), obtenerY(indice), ANCHO_TARJETA, ALTO_TARJETA);
        }

        private int obtenerX(int ronda) {
            return MARGEN + ronda * (ANCHO_TARJETA + ESPACIO_X);
        }

        private int obtenerY(int indice) {
            return MARGEN + 12 + indice * (ALTO_TARJETA + ESPACIO_Y);
        }
    }
}
