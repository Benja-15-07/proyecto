package visual;

import javax.swing.*;
import java.awt.*;

/**
 * Panel principal de la aplicación.
 * Desde aquí se organiza la vista general de la interfaz.
 */
public class PanelPrincipal extends JPanel {
    /** Panel central donde se mostrará la información del torneo. */
    private PanelTorneo panelTorneo;

    /**
     * Crea el panel principal y monta el panel base del torneo.
     */
    public PanelPrincipal() {
        this.setLayout(new BorderLayout());
        this.setBackground(EstilosVisuales.FONDO);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(EstilosVisuales.PRIMARIO_OSCURO);
        cabecera.setOpaque(true);
        cabecera.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));

        JLabel nombreAplicacion = new JLabel("Sistema de gestión de torneos");
        nombreAplicacion.setFont(new Font("SansSerif", Font.BOLD, 28));
        nombreAplicacion.setForeground(Color.WHITE);

        cabecera.add(nombreAplicacion, BorderLayout.WEST);

        panelTorneo = new PanelTorneo();

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(EstilosVisuales.FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(18, 22, 22, 22));
        contenido.add(panelTorneo, BorderLayout.CENTER);

        this.add(cabecera, BorderLayout.NORTH);
        this.add(contenido, BorderLayout.CENTER);
    }

    /**
     * Da acceso al contenido principal de la aplicación.
     *
     * @return vista principal del torneo para conectarla con el controlador
     */
    public PanelTorneo getPanelTorneo() {
        return panelTorneo;
    }
}
