package visual;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Panel encargado de mostrar la tabla de clasificación del torneo.
 */
public class PanelClasificacion extends JPanel {
    private DefaultTableModel modelo;
    private String[] columnasActuales;
    private JTable tabla;

    /**
     * Crea el panel de clasificación con una tabla base.
     */
    public PanelClasificacion() {
        this.setLayout(new BorderLayout());
        this.setBorder(EstilosVisuales.crearBordePanel());
        this.setBackground(EstilosVisuales.SUPERFICIE);
        JLabel titulo = EstilosVisuales.crearTituloPanel("Clasificación");
        titulo.setForeground(EstilosVisuales.SECUNDARIO);
        this.add(titulo, BorderLayout.NORTH);

        String[] columnas = {"Pos", "Participante", "Puntos", "PJ"};
        columnasActuales = columnas;
        Object[][] datos = {
                {"1", "Sin datos", "0", "0"},
                {"2", "-", "0", "0"},
                {"3", "-", "0", "0"},
                {"4", "-", "0", "0"}
        };

        modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(EstilosVisuales.FUENTE_NORMAL);
        tabla.setForeground(EstilosVisuales.TEXTO);
        tabla.setBackground(EstilosVisuales.SUPERFICIE);
        tabla.setSelectionBackground(EstilosVisuales.SELECCION);
        tabla.setSelectionForeground(EstilosVisuales.TEXTO);
        tabla.setGridColor(EstilosVisuales.BORDE);
        tabla.setRowHeight(30);
        tabla.setFillsViewportHeight(true);
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setDefaultRenderer(Object.class, crearRenderCeldas());

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(EstilosVisuales.FUENTE_ETIQUETA);
        encabezado.setForeground(Color.WHITE);
        encabezado.setBackground(EstilosVisuales.SECUNDARIO);
        encabezado.setOpaque(true);
        encabezado.setPreferredSize(new Dimension(0, 32));

        DefaultTableCellRenderer renderEncabezado = new DefaultTableCellRenderer();
        renderEncabezado.setHorizontalAlignment(SwingConstants.CENTER);
        renderEncabezado.setFont(EstilosVisuales.FUENTE_ETIQUETA);
        renderEncabezado.setForeground(Color.WHITE);
        renderEncabezado.setBackground(EstilosVisuales.SECUNDARIO);
        renderEncabezado.setOpaque(true);
        renderEncabezado.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 1, new Color(134, 239, 172)));
        encabezado.setDefaultRenderer(renderEncabezado);

        ajustarAnchoColumnas();

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(EstilosVisuales.BORDE));
        this.add(scroll, BorderLayout.CENTER);
    }

    /**
     * Muestra filas de clasificación preparadas por el controlador.
     *
     * @param filas datos que se mostrarán en la tabla
     */
    public void actualizarClasificacion(List<Object[]> filas) {
        modelo.setRowCount(0);

        if (filas.isEmpty()) {
            modelo.addRow(crearFilaVacia());
            return;
        }

        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
    }

    /**
     * Permite adaptar las columnas a la disciplina desde el controlador.
     *
     * @param columnas nombres de las columnas
     */
    public void configurarColumnas(String[] columnas) {
        columnasActuales = columnas;
        modelo.setColumnIdentifiers(columnas);
        ajustarAnchoColumnas();
    }

    /**
     * Crea el estilo de las celdas para que la tabla se lea ordenada.
     *
     * @return renderizador usado por la tabla
     */
    private DefaultTableCellRenderer crearRenderCeldas() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabla,
                                                           Object valor,
                                                           boolean seleccionado,
                                                           boolean tieneFoco,
                                                           int fila,
                                                           int columna) {
                Component componente = super.getTableCellRendererComponent(
                        tabla, valor, seleccionado, tieneFoco, fila, columna);

                setHorizontalAlignment(columna == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, EstilosVisuales.BORDE),
                        BorderFactory.createEmptyBorder(0, columna == 1 ? 10 : 4, 0, 4)
                ));

                if (!seleccionado) {
                    componente.setBackground(fila % 2 == 0
                            ? EstilosVisuales.SUPERFICIE
                            : EstilosVisuales.CAMPO);
                    componente.setForeground(EstilosVisuales.TEXTO);
                }

                return componente;
            }
        };
    }

    /**
     * Ajusta el espacio de columnas cada vez que cambia la disciplina.
     */
    private void ajustarAnchoColumnas() {
        if (tabla == null) {
            return;
        }

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            String nombre = tabla.getColumnName(i);
            int ancho = 70;

            if ("Pos".equals(nombre)) {
                ancho = 45;
            } else if ("Equipo".equals(nombre)
                    || "Jugador".equals(nombre)
                    || "Participante".equals(nombre)
                    || "Competidor".equals(nombre)) {
                ancho = 180;
            } else if ("PTS".equals(nombre)) {
                ancho = 65;
            }

            tabla.getColumnModel().getColumn(i).setPreferredWidth(ancho);
        }
    }

    /**
     * Crea una fila inicial con la misma cantidad de columnas visibles.
     *
     * @return fila de relleno para cuando todavia no hay datos
     */
    private Object[] crearFilaVacia() {
        Object[] fila = new Object[columnasActuales.length];
        fila[0] = "1";
        if (fila.length > 1) {
            fila[1] = "Sin datos";
        }
        for (int i = 2; i < fila.length; i++) {
            fila[i] = "0";
        }
        return fila;
    }
}
