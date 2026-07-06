package visual;

import javax.swing.*;
import java.awt.*;

/**
 * Panel base del torneo.
 * Aquí se organiza la estructura principal de la interfaz visual.
 */
public class PanelTorneo extends JPanel {
    private PanelEstadoTorneo panelEstadoTorneo;
    private PanelOrganizador panelOrganizador;
    private PanelClasificacion panelClasificacion;
    private PanelCalendario panelCalendario;
    private PanelEnfrentamiento panelEnfrentamiento;

    /**
     * Crea el panel principal del torneo y distribuye
     * las secciones base de la interfaz.
     */
    public PanelTorneo() {
        this.setLayout(new BorderLayout(14, 14));
        this.setBackground(EstilosVisuales.FONDO);

        panelEstadoTorneo = new PanelEstadoTorneo();
        panelClasificacion = new PanelClasificacion();
        panelCalendario = new PanelCalendario();
        panelEnfrentamiento = new PanelEnfrentamiento();
        panelOrganizador = new PanelOrganizador();

        panelEstadoTorneo.setPreferredSize(new Dimension(0, 112));
        panelOrganizador.setPreferredSize(new Dimension(320, 0));
        panelEnfrentamiento.setPreferredSize(new Dimension(0, 205));

        JTabbedPane panelCentro = new JTabbedPane();
        panelCentro.setFont(EstilosVisuales.FUENTE_ETIQUETA);
        panelCentro.setForeground(EstilosVisuales.TEXTO);
        panelCentro.setBackground(EstilosVisuales.SUPERFICIE);
        panelCentro.setBorder(BorderFactory.createEmptyBorder());
        panelCentro.addTab("Calendario mensual", panelCalendario);
        panelCentro.addTab("Clasificación", panelClasificacion);

        JPanel contenido = new JPanel(new BorderLayout(14, 14));
        contenido.setOpaque(false);
        contenido.add(panelOrganizador, BorderLayout.WEST);
        contenido.add(panelCentro, BorderLayout.CENTER);
        contenido.add(panelEnfrentamiento, BorderLayout.SOUTH);

        this.add(panelEstadoTorneo, BorderLayout.NORTH);
        this.add(contenido, BorderLayout.CENTER);

    }

    /**
     * Da acceso al resumen superior.
     *
     * @return panel que muestra el resumen del torneo
     */
    public PanelEstadoTorneo getPanelEstadoTorneo() {
        return panelEstadoTorneo;
    }

    /**
     * Da acceso al formulario lateral.
     *
     * @return formulario usado por el organizador
     */
    public PanelOrganizador getPanelOrganizador() {
        return panelOrganizador;
    }

    /**
     * Da acceso a la clasificación.
     *
     * @return panel de la tabla de clasificación
     */
    public PanelClasificacion getPanelClasificacion() {
        return panelClasificacion;
    }

    /**
     * Da acceso al calendario.
     *
     * @return panel del calendario mensual
     */
    public PanelCalendario getPanelCalendario() {
        return panelCalendario;
    }

    /**
     * Da acceso al marcador.
     *
     * @return panel usado para mostrar y editar un encuentro
     */
    public PanelEnfrentamiento getPanelEnfrentamiento() {
        return panelEnfrentamiento;
    }
}
