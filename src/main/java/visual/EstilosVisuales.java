package visual;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Reúne los colores, fuentes y bordes usados por la interfaz.
 */
final class EstilosVisuales {
    static final Color FONDO = new Color(243, 248, 244);
    static final Color SUPERFICIE = Color.WHITE;
    static final Color PRIMARIO = new Color(22, 101, 52);
    static final Color PRIMARIO_OSCURO = new Color(5, 46, 22);
    static final Color SECUNDARIO = new Color(21, 128, 61);
    static final Color ESMERALDA = new Color(5, 150, 105);
    static final Color OLIVA = new Color(77, 124, 15);
    static final Color VERDE_CLARO = new Color(22, 163, 74);
    static final Color TEXTO = new Color(20, 50, 32);
    static final Color TEXTO_SECUNDARIO = new Color(75, 100, 82);
    static final Color BORDE = new Color(190, 214, 196);
    static final Color SELECCION = new Color(220, 252, 231);
    static final Color CAMPO = new Color(247, 252, 248);

    static final Font FUENTE_NORMAL = new Font("SansSerif", Font.PLAIN, 14);
    static final Font FUENTE_ETIQUETA = new Font("SansSerif", Font.BOLD, 13);
    static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 18);

    private EstilosVisuales() {
    }

    /**
     * Crea el borde y el espacio interior que comparten los paneles principales.
     *
     * @return borde listo para asignar al panel
     */
    static Border crearBordePanel() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(16, 18, 18, 18)
        );
    }

    /**
     * Crea un título con la fuente y el espacio usados en toda la interfaz.
     *
     * @param texto texto que se mostrará
     * @return etiqueta con el estilo de título
     */
    static JLabel crearTituloPanel(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        return titulo;
    }

    /**
     * Aplica el estilo común a un campo de texto.
     *
     * @param campo campo que se quiere preparar
     */
    static void prepararCampo(JTextField campo) {
        campo.setFont(FUENTE_NORMAL);
        campo.setForeground(TEXTO);
        campo.setBackground(CAMPO);
        campo.setCaretColor(PRIMARIO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(7, 9, 7, 9)
        ));
    }

    /**
     * Aplica el color principal al botón más importante de una sección.
     *
     * @param boton botón que se quiere destacar
     */
    static void prepararBotonPrincipal(JButton boton) {
        prepararBoton(boton, PRIMARIO);
    }

    /**
     * Aplica color, tipografía y efecto al pasar el mouse por un botón.
     *
     * @param boton botón que recibirá el estilo
     * @param color color base del botón
     */
    static void prepararBoton(JButton boton, Color color) {
        boton.setFont(FUENTE_ETIQUETA);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setRolloverEnabled(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(color.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });
    }

    /**
     * Aplica el estilo común a las etiquetas de los formularios.
     *
     * @param etiqueta etiqueta que se quiere preparar
     */
    static void prepararEtiqueta(JLabel etiqueta) {
        etiqueta.setFont(FUENTE_ETIQUETA);
        etiqueta.setForeground(TEXTO_SECUNDARIO);
    }

    /**
     * Crea el texto que separa las secciones dentro de un formulario.
     *
     * @param texto nombre de la sección
     * @param color color usado para destacar la sección
     * @return etiqueta de sección preparada
     */
    static JLabel crearEtiquetaSeccion(String texto, Color color) {
        JLabel etiqueta = new JLabel(texto.toUpperCase());
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiqueta.setForeground(color);
        etiqueta.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, color));
        return etiqueta;
    }
}
