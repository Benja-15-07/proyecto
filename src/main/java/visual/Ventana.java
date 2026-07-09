package visual;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema.
 * Por ahora solo monta el panel principal de la interfaz.
 */
public class Ventana extends JFrame {
    private PanelPrincipal panelPrincipal;

    /**
     * Crea la ventana principal y agrega el panel base del torneo.
     */
    public Ventana() {
        super("Gestión de Torneos");

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 700));
        this.setSize(1360, 850);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(EstilosVisuales.FONDO);

        panelPrincipal = new PanelPrincipal();
        this.add(panelPrincipal, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /**
     * Da acceso al contenido de la ventana.
     *
     * @return panel raíz de la aplicación
     */
    public PanelPrincipal getPanelPrincipal() {
        return panelPrincipal;
    }
}
