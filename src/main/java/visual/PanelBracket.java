package visual;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel visual para mostrar el cuadro de eliminacion directa.
 * No calcula rondas ni ganadores; solo muestra lo que entregue el controlador.
 */
public class PanelBracket extends JPanel {
    private DefaultListModel<String> modeloBracket;
    private JList<String> listaBracket;

    /**
     * Crea una vista simple para revisar los enfrentamientos del bracket.
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

        modeloBracket = new DefaultListModel<>();
        listaBracket = new JList<>(modeloBracket);
        listaBracket.setFont(EstilosVisuales.FUENTE_NORMAL);
        listaBracket.setForeground(EstilosVisuales.TEXTO);
        listaBracket.setBackground(EstilosVisuales.CAMPO);
        listaBracket.setSelectionBackground(EstilosVisuales.SELECCION);
        listaBracket.setSelectionForeground(EstilosVisuales.PRIMARIO_OSCURO);
        listaBracket.setFixedCellHeight(34);
        listaBracket.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane scroll = new JScrollPane(listaBracket);
        scroll.setBorder(BorderFactory.createLineBorder(EstilosVisuales.BORDE));

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
        modeloBracket.clear();

        if (enfrentamientos == null || enfrentamientos.isEmpty()) {
            limpiarBracket();
            return;
        }

        for (String enfrentamiento : enfrentamientos) {
            modeloBracket.addElement(enfrentamiento);
        }
    }

    /**
     * Deja el panel en estado inicial.
     */
    public void limpiarBracket() {
        modeloBracket.clear();
        modeloBracket.addElement("No hay bracket generado");
    }
}
