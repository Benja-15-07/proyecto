package visual;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que muestra un resumen general del estado del torneo.
 */
public class PanelEstadoTorneo extends JPanel {
    private JTextArea txtNombre;
    private JTextArea txtDisciplina;
    private JTextArea txtFormato;
    private JTextArea txtCriterio;
    private JTextArea txtParticipantes;
    private JTextArea txtEstado;

    /**
     * Crea el panel de estado con tarjetas informativas adaptables.
     */
    public PanelEstadoTorneo() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel contenido = new JPanel(new GridLayout(1, 6, 12, 0));
        contenido.setOpaque(false);

        txtNombre = crearValor("Sin crear");
        txtDisciplina = crearValor("Sin definir");
        txtFormato = crearValor("Sin definir");
        txtCriterio = crearValor("Sin definir");
        txtParticipantes = crearValor("0");
        txtEstado = crearValor("Pendiente");

        contenido.add(crearTarjeta(
                "Torneo", txtNombre, EstilosVisuales.PRIMARIO, new Color(240, 253, 244)));
        contenido.add(crearTarjeta(
                "Disciplina", txtDisciplina, EstilosVisuales.SECUNDARIO, new Color(236, 253, 245)));
        contenido.add(crearTarjeta(
                "Formato", txtFormato, EstilosVisuales.ESMERALDA, new Color(209, 250, 229)));
        contenido.add(crearTarjeta(
                "Criterio", txtCriterio, EstilosVisuales.OLIVA, new Color(247, 254, 231)));
        contenido.add(crearTarjeta(
                "Participantes", txtParticipantes, EstilosVisuales.VERDE_CLARO,
                new Color(220, 252, 231)));
        contenido.add(crearTarjeta(
                "Estado", txtEstado, EstilosVisuales.OLIVA, new Color(247, 254, 231)));

        this.add(contenido, BorderLayout.CENTER);
    }

    /**
     * Crea un valor que puede ocupar más de una línea sin salirse de su tarjeta.
     */
    private JTextArea crearValor(String texto) {
        JTextArea valor = new JTextArea(texto);
        valor.setFont(new Font("SansSerif", Font.BOLD, 17));
        valor.setForeground(EstilosVisuales.PRIMARIO_OSCURO);
        valor.setEditable(false);
        valor.setFocusable(false);
        valor.setOpaque(false);
        valor.setLineWrap(true);
        valor.setWrapStyleWord(true);
        valor.setRows(2);
        valor.setMargin(new Insets(0, 0, 0, 0));
        return valor;
    }

    /**
     * Arma una tarjeta del resumen usando un título, un valor y un color de acento.
     */
    private JPanel crearTarjeta(String titulo, JTextArea valor, Color acento, Color fondo) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 5));
        tarjeta.setBackground(fondo);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosVisuales.BORDE),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 5, 0, 0, acento),
                        BorderFactory.createEmptyBorder(9, 11, 8, 10)
                )
        ));

        JLabel etiqueta = new JLabel(titulo.toUpperCase());
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 11));
        etiqueta.setForeground(acento);

        tarjeta.add(etiqueta, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);
        return tarjeta;
    }

    /**
     * Actualiza todos los datos visibles del resumen.
     *
     * @param nombre nombre del torneo
     * @param disciplina disciplina seleccionada
     * @param formato formato del torneo
     * @param participantes cantidad de participantes
     * @param estado estado actual
     */
    public void actualizarDatos(String nombre,
                                String disciplina,
                                String formato,
                                int participantes,
                                String estado) {
        actualizarDatos(nombre, disciplina, formato, "Sin definir", participantes, estado);
    }

    /**
     * Actualiza todos los datos visibles del resumen, incluyendo el criterio.
     *
     * @param nombre nombre del torneo
     * @param disciplina disciplina seleccionada
     * @param formato formato del torneo
     * @param criterio criterio usado para elegir ganadores
     * @param participantes cantidad de participantes
     * @param estado estado actual
     */
    public void actualizarDatos(String nombre,
                                String disciplina,
                                String formato,
                                String criterio,
                                int participantes,
                                String estado) {
        actualizarValor(txtNombre, nombre);
        actualizarValor(txtDisciplina, disciplina);
        actualizarValor(txtFormato, formato);
        actualizarValor(txtCriterio, criterio);
        actualizarValor(txtParticipantes, String.valueOf(participantes));
        actualizarValor(txtEstado, estado);
    }

    /**
     * Actualiza solamente el contador visible de participantes.
     *
     * @param participantes cantidad que se debe mostrar
     */
    public void actualizarParticipantes(int participantes) {
        actualizarValor(txtParticipantes, String.valueOf(participantes));
    }

    /**
     * Cambia un valor y conserva el texto completo como ayuda emergente.
     */
    private void actualizarValor(JTextArea componente, String valor) {
        componente.setText(valor);
        componente.setCaretPosition(0);
        componente.setToolTipText(valor);
    }
}
