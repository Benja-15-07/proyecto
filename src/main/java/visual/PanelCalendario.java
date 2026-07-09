package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Panel encargado de mostrar un calendario mensual de encuentros.
 */
public class PanelCalendario extends JPanel {
    private YearMonth mesActual;
    private LocalDate fechaSeleccionada;
    private JLabel lblMes;
    private JPanel panelDias;
    private DefaultListModel<String> modelo;
    private JList<String> listaPartidos;
    private Map<LocalDate, List<String>> encuentrosPorFecha;

    /**
     * Crea el calendario mostrando el mes actual.
     */
    public PanelCalendario() {
        mesActual = YearMonth.now();
        fechaSeleccionada = LocalDate.now();
        encuentrosPorFecha = new HashMap<>();

        this.setLayout(new BorderLayout(0, 12));
        this.setBorder(EstilosVisuales.crearBordePanel());
        this.setBackground(EstilosVisuales.SUPERFICIE);

        JPanel cabecera = new JPanel(new BorderLayout(10, 0));
        cabecera.setOpaque(false);

        JButton btnAnterior = new JButton("<");
        JButton btnSiguiente = new JButton(">");
        prepararBotonNavegacion(btnAnterior);
        prepararBotonNavegacion(btnSiguiente);

        lblMes = new JLabel("", SwingConstants.CENTER);
        lblMes.setFont(EstilosVisuales.FUENTE_TITULO);
        lblMes.setForeground(EstilosVisuales.ESMERALDA);

        cabecera.add(btnAnterior, BorderLayout.WEST);
        cabecera.add(lblMes, BorderLayout.CENTER);
        cabecera.add(btnSiguiente, BorderLayout.EAST);

        JPanel calendario = new JPanel(new BorderLayout(0, 6));
        calendario.setOpaque(false);

        JPanel encabezadoDias = new JPanel(new GridLayout(1, 7, 5, 0));
        encabezadoDias.setOpaque(false);
        String[] dias = {"LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM"};

        for (String dia : dias) {
            JLabel etiqueta = new JLabel(dia, SwingConstants.CENTER);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 11));
            etiqueta.setForeground(EstilosVisuales.TEXTO_SECUNDARIO);
            encabezadoDias.add(etiqueta);
        }

        panelDias = new JPanel(new GridLayout(6, 7, 5, 5));
        panelDias.setOpaque(false);

        calendario.add(encabezadoDias, BorderLayout.NORTH);
        calendario.add(panelDias, BorderLayout.CENTER);

        JPanel encuentrosDia = new JPanel(new BorderLayout(0, 7));
        encuentrosDia.setOpaque(false);
        encuentrosDia.setPreferredSize(new Dimension(0, 125));

        JLabel tituloEncuentros = EstilosVisuales.crearEtiquetaSeccion(
                "Encuentros del día seleccionado", EstilosVisuales.ESMERALDA);

        modelo = new DefaultListModel<>();
        listaPartidos = new JList<>(modelo);
        listaPartidos.setFont(EstilosVisuales.FUENTE_NORMAL);
        listaPartidos.setForeground(EstilosVisuales.TEXTO);
        listaPartidos.setBackground(EstilosVisuales.CAMPO);
        listaPartidos.setSelectionBackground(EstilosVisuales.SELECCION);
        listaPartidos.setSelectionForeground(EstilosVisuales.ESMERALDA);
        listaPartidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPartidos.setFixedCellHeight(30);
        listaPartidos.setBorder(BorderFactory.createEmptyBorder(3, 7, 3, 7));

        JScrollPane scroll = new JScrollPane(listaPartidos);
        scroll.setBorder(BorderFactory.createLineBorder(EstilosVisuales.BORDE));

        encuentrosDia.add(tituloEncuentros, BorderLayout.NORTH);
        encuentrosDia.add(scroll, BorderLayout.CENTER);

        this.add(cabecera, BorderLayout.NORTH);
        this.add(calendario, BorderLayout.CENTER);
        this.add(encuentrosDia, BorderLayout.SOUTH);

        btnAnterior.addActionListener(e -> {
            mesActual = mesActual.minusMonths(1);
            fechaSeleccionada = mesActual.atDay(1);
            dibujarCalendario();
        });

        btnSiguiente.addActionListener(e -> {
            mesActual = mesActual.plusMonths(1);
            fechaSeleccionada = mesActual.atDay(1);
            dibujarCalendario();
        });

        dibujarCalendario();
    }

    /**
     * Aplica el mismo estilo a los botones que cambian de mes.
     *
     * @param boton boton de navegacion que se va a preparar
     */
    private void prepararBotonNavegacion(JButton boton) {
        EstilosVisuales.prepararBoton(boton, EstilosVisuales.ESMERALDA);
        boton.setPreferredSize(new Dimension(42, 32));
    }

    /**
     * Vuelve a construir los 42 espacios del calendario según el mes visible.
     */
    private void dibujarCalendario() {
        panelDias.removeAll();

        String nombreMes = mesActual.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-CL"));
        lblMes.setText(nombreMes.substring(0, 1).toUpperCase()
                + nombreMes.substring(1) + " " + mesActual.getYear());

        LocalDate primerDia = mesActual.atDay(1);
        int espaciosIniciales = primerDia.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();

        for (int i = 0; i < 42; i++) {
            int numeroDia = i - espaciosIniciales + 1;

            if (numeroDia < 1 || numeroDia > mesActual.lengthOfMonth()) {
                JPanel vacio = new JPanel();
                vacio.setBackground(EstilosVisuales.CAMPO);
                panelDias.add(vacio);
            } else {
                LocalDate fecha = mesActual.atDay(numeroDia);
                panelDias.add(crearBotonDia(fecha));
            }
        }

        panelDias.revalidate();
        panelDias.repaint();
        actualizarListaDelDia();
    }

    /**
     * Crea un día del calendario y marca si tiene encuentros.
     *
     * @param fecha fecha representada por el botón
     * @return botón listo para agregar a la cuadrícula
     */
    private JButton crearBotonDia(LocalDate fecha) {
        List<String> encuentros = encuentrosPorFecha.get(fecha);
        boolean tieneEncuentros = encuentros != null && !encuentros.isEmpty();

        String texto = tieneEncuentros
                ? "<html><center>" + fecha.getDayOfMonth() + "<br><font size='2'>● "
                + encuentros.size() + "</font></center></html>"
                : String.valueOf(fecha.getDayOfMonth());

        JButton boton = new JButton(texto);
        boton.setFont(EstilosVisuales.FUENTE_ETIQUETA);
        boton.setFocusPainted(false);
        boton.setRolloverEnabled(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createLineBorder(EstilosVisuales.BORDE));

        if (fecha.equals(fechaSeleccionada)) {
            boton.setBackground(new Color(187, 247, 208));
            boton.setForeground(EstilosVisuales.PRIMARIO_OSCURO);
            boton.setBorder(BorderFactory.createLineBorder(EstilosVisuales.ESMERALDA, 2));
        } else if (tieneEncuentros) {
            boton.setBackground(new Color(236, 253, 245));
            boton.setForeground(EstilosVisuales.ESMERALDA);
        } else {
            boton.setBackground(EstilosVisuales.SUPERFICIE);
            boton.setForeground(EstilosVisuales.TEXTO);
        }

        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        Color fondoNormal = boton.getBackground();
        Color textoNormal = boton.getForeground();
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(134, 239, 172));
                boton.setForeground(EstilosVisuales.PRIMARIO_OSCURO);
                boton.setBorder(BorderFactory.createLineBorder(EstilosVisuales.PRIMARIO, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondoNormal);
                boton.setForeground(textoNormal);
                boton.setBorder(fecha.equals(fechaSeleccionada)
                        ? BorderFactory.createLineBorder(EstilosVisuales.ESMERALDA, 2)
                        : BorderFactory.createLineBorder(EstilosVisuales.BORDE));
            }
        });
        boton.addActionListener(e -> {
            fechaSeleccionada = fecha;
            dibujarCalendario();
        });
        return boton;
    }

    /**
     * Muestra los encuentros ya organizados por fecha.
     *
     * @param encuentros encuentros entregados por el controlador
     */
    public void actualizarEncuentros(Map<LocalDate, List<String>> encuentros) {
        encuentrosPorFecha.clear();
        encuentrosPorFecha.putAll(encuentros);
        dibujarCalendario();
    }

    /**
     * Sitúa el calendario en una fecha indicada externamente.
     *
     * @param fecha fecha que se desea mostrar
     */
    public void mostrarFecha(LocalDate fecha) {
        fechaSeleccionada = fecha;
        mesActual = YearMonth.from(fecha);
        dibujarCalendario();
    }

    /**
     * Muestra en la lista solamente los encuentros de la fecha marcada.
     */
    private void actualizarListaDelDia() {
        modelo.clear();
        List<String> encuentros = encuentrosPorFecha.get(fechaSeleccionada);

        if (encuentros == null || encuentros.isEmpty()) {
            modelo.addElement("No hay encuentros programados para este día");
            return;
        }

        for (String encuentro : encuentros) {
            modelo.addElement(encuentro);
        }
    }

    /**
     * Entrega la lista para que el controlador pueda escuchar su selección.
     *
     * @return lista visual de encuentros
     */
    public JList<String> getListaPartidos() {
        return listaPartidos;
    }

    /**
     * Obtiene el encuentro marcado por el usuario.
     *
     * @return encuentro seleccionado o {@code null} si no hay uno válido
     */
    public String getEncuentroSeleccionado() {
        String seleccionado = listaPartidos.getSelectedValue();

        if (seleccionado == null
                || seleccionado.equals("No hay encuentros programados para este día")) {
            return null;
        }

        return seleccionado;
    }

    /**
     * Obtiene el día que está marcado en el calendario.
     *
     * @return fecha seleccionada
     */
    public LocalDate getFechaSeleccionada() {
        return fechaSeleccionada;
    }
}
