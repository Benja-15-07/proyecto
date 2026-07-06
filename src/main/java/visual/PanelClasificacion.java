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

        JTable tabla = new JTable(modelo);
        tabla.setFont(EstilosVisuales.FUENTE_NORMAL);
        tabla.setForeground(EstilosVisuales.TEXTO);
        tabla.setBackground(EstilosVisuales.SUPERFICIE);
        tabla.setSelectionBackground(EstilosVisuales.SELECCION);
        tabla.setSelectionForeground(EstilosVisuales.TEXTO);
        tabla.setGridColor(EstilosVisuales.BORDE);
        tabla.setRowHeight(30);
        tabla.setFillsViewportHeight(true);
        tabla.setShowVerticalLines(false);

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

        tabla.getColumnModel().getColumn(0).setPreferredWidth(45);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(65);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(45);

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
            modelo.addRow(new Object[]{"1", "Sin datos", "0", "0"});
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
        modelo.setColumnIdentifiers(columnas);
    }
}
