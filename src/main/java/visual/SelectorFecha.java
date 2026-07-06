package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Ventana pequeña para seleccionar una fecha desde un calendario mensual.
 */
final class SelectorFecha extends JDialog {
    private YearMonth mesActual;
    private LocalDate fechaSeleccionada;
    private JLabel lblMes;
    private JPanel panelDias;

    /**
     * Prepara el selector mostrando el mes de la fecha recibida.
     *
     * @param propietario ventana desde la que se abrió el selector
     * @param fechaInicial fecha que se mostrará al abrir
     */
    private SelectorFecha(Window propietario, LocalDate fechaInicial) {
        super(propietario, "Seleccionar fecha", ModalityType.APPLICATION_MODAL);
        this.mesActual = YearMonth.from(fechaInicial);
        this.fechaSeleccionada = null;

        this.setLayout(new BorderLayout(0, 12));
        this.getContentPane().setBackground(EstilosVisuales.SUPERFICIE);
        ((JComponent) this.getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel cabecera = new JPanel(new BorderLayout(10, 0));
        cabecera.setOpaque(false);

        JButton btnAnterior = crearBotonNavegacion("<");
        JButton btnSiguiente = crearBotonNavegacion(">");

        lblMes = new JLabel("", SwingConstants.CENTER);
        lblMes.setFont(EstilosVisuales.FUENTE_TITULO);
        lblMes.setForeground(EstilosVisuales.PRIMARIO);

        cabecera.add(btnAnterior, BorderLayout.WEST);
        cabecera.add(lblMes, BorderLayout.CENTER);
        cabecera.add(btnSiguiente, BorderLayout.EAST);

        JPanel calendario = new JPanel(new BorderLayout(0, 7));
        calendario.setOpaque(false);

        JPanel nombresDias = new JPanel(new GridLayout(1, 7, 5, 0));
        nombresDias.setOpaque(false);
        String[] dias = {"L", "M", "X", "J", "V", "S", "D"};

        for (String dia : dias) {
            JLabel etiqueta = new JLabel(dia, SwingConstants.CENTER);
            etiqueta.setFont(EstilosVisuales.FUENTE_ETIQUETA);
            etiqueta.setForeground(EstilosVisuales.TEXTO_SECUNDARIO);
            nombresDias.add(etiqueta);
        }

        panelDias = new JPanel(new GridLayout(6, 7, 5, 5));
        panelDias.setOpaque(false);

        calendario.add(nombresDias, BorderLayout.NORTH);
        calendario.add(panelDias, BorderLayout.CENTER);

        JLabel ayuda = new JLabel("Selecciona un día para confirmar la fecha");
        ayuda.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ayuda.setForeground(EstilosVisuales.TEXTO_SECUNDARIO);
        ayuda.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(cabecera, BorderLayout.NORTH);
        this.add(calendario, BorderLayout.CENTER);
        this.add(ayuda, BorderLayout.SOUTH);

        btnAnterior.addActionListener(e -> {
            mesActual = mesActual.minusMonths(1);
            dibujarMes();
        });

        btnSiguiente.addActionListener(e -> {
            mesActual = mesActual.plusMonths(1);
            dibujarMes();
        });

        dibujarMes();
        this.setSize(390, 390);
        this.setResizable(false);
        this.setLocationRelativeTo(propietario);
    }

    /**
     * Crea uno de los botones usados para cambiar de mes.
     *
     * @param texto símbolo que mostrará el botón
     * @return botón de navegación preparado
     */
    private JButton crearBotonNavegacion(String texto) {
        JButton boton = new JButton(texto);
        EstilosVisuales.prepararBoton(boton, EstilosVisuales.PRIMARIO);
        boton.setPreferredSize(new Dimension(42, 32));
        return boton;
    }

    /**
     * Limpia la cuadrícula y dibuja los días del mes actual.
     */
    private void dibujarMes() {
        panelDias.removeAll();

        String nombreMes = mesActual.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-CL"));
        lblMes.setText(nombreMes.substring(0, 1).toUpperCase()
                + nombreMes.substring(1) + " " + mesActual.getYear());

        LocalDate primerDia = mesActual.atDay(1);
        int espacios = primerDia.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();

        for (int i = 0; i < 42; i++) {
            int numeroDia = i - espacios + 1;

            if (numeroDia < 1 || numeroDia > mesActual.lengthOfMonth()) {
                panelDias.add(new JLabel());
            } else {
                LocalDate fecha = mesActual.atDay(numeroDia);
                JButton botonDia = new JButton(String.valueOf(numeroDia));
                botonDia.setFont(EstilosVisuales.FUENTE_NORMAL);
                botonDia.setForeground(EstilosVisuales.TEXTO);
                botonDia.setBackground(EstilosVisuales.CAMPO);
                botonDia.setOpaque(true);
                botonDia.setContentAreaFilled(true);
                botonDia.setRolloverEnabled(false);
                botonDia.setFocusPainted(false);
                botonDia.setBorder(BorderFactory.createLineBorder(EstilosVisuales.BORDE));
                botonDia.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                botonDia.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        botonDia.setBackground(EstilosVisuales.SELECCION);
                        botonDia.setForeground(EstilosVisuales.PRIMARIO_OSCURO);
                        botonDia.setBorder(
                                BorderFactory.createLineBorder(EstilosVisuales.PRIMARIO, 2));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        botonDia.setBackground(EstilosVisuales.CAMPO);
                        botonDia.setForeground(EstilosVisuales.TEXTO);
                        botonDia.setBorder(
                                BorderFactory.createLineBorder(EstilosVisuales.BORDE));
                    }
                });
                botonDia.addActionListener(e -> {
                    fechaSeleccionada = fecha;
                    dispose();
                });
                panelDias.add(botonDia);
            }
        }

        panelDias.revalidate();
        panelDias.repaint();
    }

    /**
     * Abre el calendario y espera hasta que el usuario elija un día o lo cierre.
     *
     * @param padre componente desde el que se abre el selector
     * @param fechaInicial fecha visible al abrir
     * @return fecha elegida o {@code null} si se cerró sin seleccionar
     */
    static LocalDate mostrar(Component padre, LocalDate fechaInicial) {
        Window propietario = SwingUtilities.getWindowAncestor(padre);
        SelectorFecha dialogo = new SelectorFecha(propietario, fechaInicial);
        dialogo.setVisible(true);
        return dialogo.fechaSeleccionada;
    }
}
