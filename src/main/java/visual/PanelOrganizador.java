package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Panel encargado de mostrar los controles básicos
 * para crear y configurar un torneo.
 */
public class PanelOrganizador extends JPanel {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtNombre;
    private JComboBox<String> cbDisciplina;
    private JComboBox<String> cbFormato;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnCrear;

    private JTextField txtParticipante;
    private JButton btnAgregarParticipante;
    private DefaultListModel<String> modeloParticipantes;

    /**
     * Crea el panel del organizador con un formulario base.
     */
    public PanelOrganizador() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(EstilosVisuales.crearBordePanel());
        this.setBackground(EstilosVisuales.SUPERFICIE);
        JLabel tituloOrganizador = EstilosVisuales.crearTituloPanel("Organizador");
        tituloOrganizador.setForeground(EstilosVisuales.PRIMARIO);
        this.add(tituloOrganizador, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(EstilosVisuales.SUPERFICIE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        JLabel lblNombre = new JLabel("Nombre del torneo:");
        txtNombre = new JTextField();

        JLabel lblDisciplina = new JLabel("Disciplina:");
        cbDisciplina = new JComboBox<>(new String[]{
                "Fútbol",
                "Baloncesto",
                "Vóleibol",
                "Tenis",
                "Ajedrez",
                "eSports"
        });

        JLabel lblFormato = new JLabel("Formato:");
        cbFormato = new JComboBox<>(new String[]{
                "Liga",
                "Eliminación directa",
                "Eliminación doble"
        });

        JLabel lblFechaInicio = new JLabel("Fecha inicio:");
        txtFechaInicio = new JTextField();
        JButton btnFechaInicio = new JButton("Elegir");

        JLabel lblFechaFin = new JLabel("Fecha fin:");
        txtFechaFin = new JTextField();
        JButton btnFechaFin = new JButton("Elegir");

        btnCrear = new JButton("Crear torneo");

        JLabel lblParticipante = new JLabel("Participante / Equipo:");
        txtParticipante = new JTextField();
        btnAgregarParticipante = new JButton("Agregar participante");
        JLabel seccionTorneo = EstilosVisuales.crearEtiquetaSeccion(
                "Datos del torneo", EstilosVisuales.PRIMARIO);
        JLabel seccionParticipantes = EstilosVisuales.crearEtiquetaSeccion(
                "Participantes inscritos", EstilosVisuales.SECUNDARIO);

        EstilosVisuales.prepararEtiqueta(lblNombre);
        EstilosVisuales.prepararEtiqueta(lblDisciplina);
        EstilosVisuales.prepararEtiqueta(lblFormato);
        EstilosVisuales.prepararEtiqueta(lblFechaInicio);
        EstilosVisuales.prepararEtiqueta(lblFechaFin);
        EstilosVisuales.prepararEtiqueta(lblParticipante);
        EstilosVisuales.prepararCampo(txtNombre);
        EstilosVisuales.prepararCampo(txtFechaInicio);
        EstilosVisuales.prepararCampo(txtFechaFin);
        EstilosVisuales.prepararCampo(txtParticipante);
        txtFechaInicio.setEditable(false);
        txtFechaFin.setEditable(false);
        txtFechaInicio.setToolTipText("Selecciona la fecha desde el calendario");
        txtFechaFin.setToolTipText("Selecciona la fecha desde el calendario");
        EstilosVisuales.prepararBotonPrincipal(btnCrear);
        EstilosVisuales.prepararBoton(btnAgregarParticipante, EstilosVisuales.SECUNDARIO);
        EstilosVisuales.prepararBoton(btnFechaInicio, EstilosVisuales.ESMERALDA);
        EstilosVisuales.prepararBoton(btnFechaFin, EstilosVisuales.ESMERALDA);

        JPanel campoFechaInicio = crearCampoFecha(txtFechaInicio, btnFechaInicio);
        JPanel campoFechaFin = crearCampoFecha(txtFechaFin, btnFechaFin);

        cbFormato.setFont(EstilosVisuales.FUENTE_NORMAL);
        cbFormato.setForeground(EstilosVisuales.TEXTO);
        cbFormato.setBackground(EstilosVisuales.SUPERFICIE);
        cbDisciplina.setFont(EstilosVisuales.FUENTE_NORMAL);
        cbDisciplina.setForeground(EstilosVisuales.TEXTO);
        cbDisciplina.setBackground(EstilosVisuales.SUPERFICIE);

        modeloParticipantes = new DefaultListModel<>();
        JList<String> listaParticipantes = new JList<>(modeloParticipantes);
        listaParticipantes.setFont(EstilosVisuales.FUENTE_NORMAL);
        listaParticipantes.setForeground(EstilosVisuales.TEXTO);
        listaParticipantes.setSelectionBackground(EstilosVisuales.SELECCION);
        listaParticipantes.setFixedCellHeight(28);
        listaParticipantes.setBorder(BorderFactory.createEmptyBorder(4, 7, 4, 7));
        JScrollPane scrollParticipantes = new JScrollPane(listaParticipantes);
        scrollParticipantes.setBorder(BorderFactory.createLineBorder(EstilosVisuales.BORDE));
        scrollParticipantes.setPreferredSize(new Dimension(200, 100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(2, 6, 10, 6);
        formulario.add(seccionTorneo, gbc);

        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(6, 6, 6, 6);
        formulario.add(lblNombre, gbc);

        gbc.gridy = 2;
        gbc.weightx = 1;
        formulario.add(txtNombre, gbc);

        gbc.gridy = 3;
        gbc.weightx = 0;
        formulario.add(lblDisciplina, gbc);

        gbc.gridy = 4;
        gbc.weightx = 1;
        formulario.add(cbDisciplina, gbc);

        gbc.gridy = 5;
        gbc.weightx = 0;
        formulario.add(lblFormato, gbc);

        gbc.gridy = 6;
        gbc.weightx = 1;
        formulario.add(cbFormato, gbc);

        gbc.gridy = 7;
        gbc.weightx = 0;
        formulario.add(lblFechaInicio, gbc);

        gbc.gridy = 8;
        gbc.weightx = 1;
        formulario.add(campoFechaInicio, gbc);

        gbc.gridy = 9;
        gbc.weightx = 0;
        formulario.add(lblFechaFin, gbc);

        gbc.gridy = 10;
        gbc.weightx = 1;
        formulario.add(campoFechaFin, gbc);

        gbc.gridy = 11;
        gbc.insets = new Insets(12, 6, 6, 6);
        formulario.add(btnCrear, gbc);

        gbc.gridy = 12;
        gbc.insets = new Insets(18, 6, 10, 6);
        formulario.add(seccionParticipantes, gbc);

        gbc.gridy = 13;
        gbc.insets = new Insets(6, 6, 6, 6);
        formulario.add(lblParticipante, gbc);

        gbc.gridy = 14;
        formulario.add(txtParticipante, gbc);

        gbc.gridy = 15;
        formulario.add(btnAgregarParticipante, gbc);

        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        formulario.add(scrollParticipantes, gbc);

        JScrollPane scrollFormulario = new JScrollPane(formulario);
        scrollFormulario.setBorder(null);
        scrollFormulario.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(12);
        scrollFormulario.getViewport().setBackground(EstilosVisuales.SUPERFICIE);
        this.add(scrollFormulario, BorderLayout.CENTER);

        btnFechaInicio.addActionListener(e -> seleccionarFecha(txtFechaInicio));
        btnFechaFin.addActionListener(e -> seleccionarFecha(txtFechaFin));
    }

    /**
     * Mantiene el campo de fecha y su botón dentro del mismo bloque visual.
     *
     * @param campo campo donde se muestra la fecha
     * @param boton botón que abre el calendario
     * @return panel que contiene ambos componentes
     */
    private JPanel crearCampoFecha(JTextField campo, JButton boton) {
        JPanel panel = new JPanel(new BorderLayout(6, 0));
        panel.setOpaque(false);
        panel.add(campo, BorderLayout.CENTER);
        panel.add(boton, BorderLayout.EAST);
        return panel;
    }

    /**
     * Abre el calendario y copia la fecha elegida en el campo correspondiente.
     *
     * @param campo campo de inicio o término que se debe actualizar
     */
    private void seleccionarFecha(JTextField campo) {
        LocalDate fechaInicial = LocalDate.now();
        String textoActual = campo.getText().trim();

        if (!textoActual.isEmpty()) {
            try {
                fechaInicial = LocalDate.parse(textoActual, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                fechaInicial = LocalDate.now();
            }
        }

        LocalDate fechaElegida = SelectorFecha.mostrar(this, fechaInicial);
        if (fechaElegida != null) {
            campo.setText(fechaElegida.format(FORMATO_FECHA));
        }
    }

    /**
     * Permite que el controlador responda al botón Crear torneo.
     *
     * @param listener acción que manejará el controlador
     */
    public void agregarListenerCrearTorneo(ActionListener listener) {
        btnCrear.addActionListener(listener);
    }

    /**
     * Permite que el controlador responda al botón Agregar participante.
     *
     * @param listener acción que manejará el controlador
     */
    public void agregarListenerParticipante(ActionListener listener) {
        btnAgregarParticipante.addActionListener(listener);
    }

    /**
     * Obtiene el nombre ingresado.
     *
     * @return nombre escrito para el torneo, sin espacios en los extremos
     */
    public String getNombreTorneo() {
        return txtNombre.getText().trim();
    }

    /**
     * Obtiene la disciplina marcada.
     *
     * @return disciplina elegida en la lista
     */
    public String getDisciplinaSeleccionada() {
        return (String) cbDisciplina.getSelectedItem();
    }

    /**
     * Obtiene el formato marcado.
     *
     * @return formato de torneo seleccionado
     */
    public String getFormatoSeleccionado() {
        return (String) cbFormato.getSelectedItem();
    }

    /**
     * Lee la fecha inicial tal como aparece en pantalla.
     *
     * @return fecha de inicio mostrada en formato dd/MM/yyyy
     */
    public String getFechaInicio() {
        return txtFechaInicio.getText().trim();
    }

    /**
     * Lee la fecha final tal como aparece en pantalla.
     *
     * @return fecha de término mostrada en formato dd/MM/yyyy
     */
    public String getFechaFin() {
        return txtFechaFin.getText().trim();
    }

    /**
     * Obtiene el participante pendiente de agregar.
     *
     * @return nombre escrito en el campo de participante
     */
    public String getNombreParticipante() {
        return txtParticipante.getText().trim();
    }

    /**
     * Vacía el campo después de que el controlador agrega un participante.
     */
    public void limpiarNombreParticipante() {
        txtParticipante.setText("");
    }

    /**
     * Reemplaza la lista visible con los participantes recibidos.
     *
     * @param participantes nombres que se deben mostrar
     */
    public void mostrarParticipantes(List<String> participantes) {
        modeloParticipantes.clear();
        for (String participante : participantes) {
            modeloParticipantes.addElement(participante);
        }
    }
}
