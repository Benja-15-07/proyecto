package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel encargado de mostrar el detalle de un enfrentamiento
 * y una base para registrar resultados.
 */
public class PanelEnfrentamiento extends JPanel {
    private JLabel lblParticipante1;
    private JLabel lblParticipante2;
    private JTextField txtResultado1;
    private JTextField txtResultado2;
    private JButton btnRegistrar;
    private JLabel lblEstado;
    private DefaultComboBoxModel<String> modeloEncuentros;
    private JComboBox<String> cbEncuentros;

    /**
     * Crea el panel de enfrentamientos con una estructura base.
     */
    public PanelEnfrentamiento() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(EstilosVisuales.crearBordePanel());
        this.setBackground(EstilosVisuales.SUPERFICIE);
        JLabel titulo = EstilosVisuales.crearTituloPanel("Marcador del encuentro");
        titulo.setForeground(EstilosVisuales.OLIVA);

        modeloEncuentros = new DefaultComboBoxModel<>();
        modeloEncuentros.addElement("Seleccione un encuentro");
        cbEncuentros = new JComboBox<>(modeloEncuentros);
        cbEncuentros.setFont(EstilosVisuales.FUENTE_NORMAL);
        cbEncuentros.setForeground(EstilosVisuales.TEXTO);
        cbEncuentros.setBackground(EstilosVisuales.CAMPO);

        JPanel selector = new JPanel(new BorderLayout(14, 0));
        selector.setOpaque(false);
        selector.add(titulo, BorderLayout.WEST);
        selector.add(cbEncuentros, BorderLayout.CENTER);
        this.add(selector, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBackground(EstilosVisuales.SUPERFICIE);

        JPanel panelSuperior = new JPanel(new GridLayout(1, 3, 14, 0));
        panelSuperior.setBackground(EstilosVisuales.SUPERFICIE);

        lblParticipante1 = new JLabel("Participante 1: -");
        lblParticipante2 = new JLabel("Participante 2: -");
        txtResultado1 = new JTextField();
        txtResultado2 = new JTextField();
        EstilosVisuales.prepararEtiqueta(lblParticipante1);
        EstilosVisuales.prepararEtiqueta(lblParticipante2);
        EstilosVisuales.prepararCampo(txtResultado1);
        EstilosVisuales.prepararCampo(txtResultado2);
        txtResultado1.setHorizontalAlignment(JTextField.CENTER);
        txtResultado2.setHorizontalAlignment(JTextField.CENTER);
        txtResultado1.setFont(new Font("SansSerif", Font.BOLD, 20));
        txtResultado2.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel participante1 = crearPanelParticipante(
                lblParticipante1, txtResultado1, new Color(240, 253, 244));
        JPanel participante2 = crearPanelParticipante(
                lblParticipante2, txtResultado2, new Color(236, 253, 245));

        JPanel separador = new JPanel(new GridBagLayout());
        separador.setOpaque(false);
        JLabel versus = new JLabel("VS");
        versus.setFont(new Font("SansSerif", Font.BOLD, 22));
        versus.setForeground(EstilosVisuales.OLIVA);
        versus.setHorizontalAlignment(SwingConstants.CENTER);
        versus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 242, 100)),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        separador.add(versus);

        panelSuperior.add(participante1);
        panelSuperior.add(separador);
        panelSuperior.add(participante2);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(EstilosVisuales.SUPERFICIE);

        btnRegistrar = new JButton("Registrar resultado");
        lblEstado = new JLabel("Seleccione un enfrentamiento");
        EstilosVisuales.prepararBoton(btnRegistrar, EstilosVisuales.OLIVA);
        lblEstado.setFont(EstilosVisuales.FUENTE_NORMAL);
        lblEstado.setForeground(EstilosVisuales.TEXTO_SECUNDARIO);
        lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);

        panelInferior.add(btnRegistrar, BorderLayout.WEST);
        panelInferior.add(lblEstado, BorderLayout.EAST);

        contenido.add(panelSuperior, BorderLayout.CENTER);
        contenido.add(panelInferior, BorderLayout.SOUTH);

        this.add(contenido, BorderLayout.CENTER);

        limpiarEnfrentamiento();
    }

    /**
     * Agrupa el nombre de un participante con su campo de resultado.
     *
     * @param participante etiqueta con el nombre
     * @param resultado campo donde se escribe el resultado
     * @param fondo color de la tarjeta
     * @return panel con ambos componentes
     */
    private JPanel crearPanelParticipante(JLabel participante, JTextField resultado, Color fondo) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(fondo);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosVisuales.BORDE),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        panel.add(participante, BorderLayout.CENTER);

        resultado.setPreferredSize(new Dimension(70, 42));
        panel.add(resultado, BorderLayout.EAST);
        return panel;
    }

    /**
     * Muestra un encuentro y deja sus campos listos para ingresar resultados.
     *
     * @param encuentro texto usado en el selector
     * @param participante1 nombre del primer participante
     * @param participante2 nombre del segundo participante
     */
    public void mostrarEnfrentamiento(String encuentro,
                                      String participante1,
                                      String participante2) {
        cbEncuentros.setSelectedItem(encuentro);
        lblParticipante1.setText("Participante 1: " + participante1);
        lblParticipante2.setText("Participante 2: " + participante2);
        txtResultado1.setEnabled(true);
        txtResultado2.setEnabled(true);
        btnRegistrar.setEnabled(true);
        txtResultado1.setText("");
        txtResultado2.setText("");
        lblEstado.setText("Sin resultado registrado");
        lblEstado.setForeground(EstilosVisuales.OLIVA);
    }

    /**
     * Limpia y bloquea el marcador cuando no hay un encuentro seleccionado.
     */
    public void limpiarEnfrentamiento() {
        lblParticipante1.setText("Participante 1: -");
        lblParticipante2.setText("Participante 2: -");
        txtResultado1.setText("");
        txtResultado2.setText("");
        txtResultado1.setEnabled(false);
        txtResultado2.setEnabled(false);
        btnRegistrar.setEnabled(false);
        lblEstado.setText("Seleccione un enfrentamiento");
        lblEstado.setForeground(EstilosVisuales.TEXTO_SECUNDARIO);
    }

    /**
     * Actualiza los encuentros disponibles en el selector del marcador.
     *
     * @param encuentros lista de encuentros del torneo
     */
    public void actualizarEncuentros(List<String> encuentros) {
        modeloEncuentros.removeAllElements();
        modeloEncuentros.addElement("Seleccione un encuentro");

        for (String encuentro : encuentros) {
            modeloEncuentros.addElement(encuentro);
        }

        cbEncuentros.setSelectedIndex(0);
    }

    /**
     * Conecta el botón y la tecla Enter con el registro manejado por el controlador.
     *
     * @param listener acción encargada de registrar el resultado
     */
    public void agregarListenerRegistrar(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
        txtResultado1.addActionListener(listener);
        txtResultado2.addActionListener(listener);
    }

    /**
     * Permite reaccionar cuando cambia el encuentro del selector.
     *
     * @param listener acción que manejará el cambio
     */
    public void agregarListenerSeleccionEncuentro(ActionListener listener) {
        cbEncuentros.addActionListener(listener);
    }

    /**
     * Obtiene el elemento marcado en el selector.
     *
     * @return encuentro seleccionado o {@code null} si sigue marcada la opción inicial
     */
    public String getEncuentroSeleccionado() {
        String encuentro = (String) cbEncuentros.getSelectedItem();
        if ("Seleccione un encuentro".equals(encuentro)) {
            return null;
        }
        return encuentro;
    }

    /**
     * Lee el primer campo del marcador.
     *
     * @return resultado escrito para el primer participante
     */
    public String getResultadoParticipante1() {
        return txtResultado1.getText().trim();
    }

    /**
     * Lee el segundo campo del marcador.
     *
     * @return resultado escrito para el segundo participante
     */
    public String getResultadoParticipante2() {
        return txtResultado2.getText().trim();
    }

    /**
     * Coloca en pantalla un resultado entregado por el controlador.
     *
     * @param resultado1 resultado del primer participante
     * @param resultado2 resultado del segundo participante
     * @param estado mensaje que acompaña al resultado
     */
    public void mostrarResultado(int resultado1, int resultado2, String estado) {
        txtResultado1.setText(String.valueOf(resultado1));
        txtResultado2.setText(String.valueOf(resultado2));
        lblEstado.setText(estado);
        lblEstado.setForeground(EstilosVisuales.VERDE_CLARO);
    }

    /**
     * Cambia el mensaje de estado y su color.
     *
     * @param estado mensaje que se mostrará
     * @param color color usado para destacar el mensaje
     */
    public void mostrarEstado(String estado, Color color) {
        lblEstado.setText(estado);
        lblEstado.setForeground(color);
    }
}
