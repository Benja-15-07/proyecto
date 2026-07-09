import logica.*;
import visual.*;

import javax.swing.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorTorneo {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Organizador organizador;
    private Ventana ventana;
    private Map<String, Enfrentamiento> enfrentamientosMap;

    public ControladorTorneo(Ventana ventana){
        this.ventana = ventana;
        this.organizador = new Organizador("Organizador","");
        this.enfrentamientosMap = new HashMap<>();
        eventos();
    }

    private void eventos(){
        PanelOrganizador panelOrganizador = ventana.getPanelPrincipal().getPanelTorneo().getPanelOrganizador();
        PanelEnfrentamiento panelEnfrentamiento = ventana.getPanelPrincipal().getPanelTorneo().getPanelEnfrentamiento();

        panelOrganizador.agregarListenerCrearTorneo(e -> {
            String nombre = panelOrganizador.getNombreTorneo();
            String disciplina = panelOrganizador.getDisciplinaSeleccionada();

            LocalDate fechaInicio = panelOrganizador.getFechaInicio();
            if(fechaInicio == null){
                JOptionPane.showMessageDialog(ventana,"Seleccione una fecha de inicio.");
                return;
            }

            FormatoStrategy formato = seleccionaFormato(panelOrganizador.getFormatoSeleccionado());
            CriterioStrategy criterio = seleccionaCriterio(panelOrganizador.getCriterioSeleccionado());
            int partidosPorDia = panelOrganizador.getPartidosPorDia();

            if(nombre.trim().isEmpty()){
                JOptionPane.showMessageDialog(ventana,"Ingrese nombre del torneo.");
                return;
            }

            crearTorneo(nombre, disciplina, fechaInicio, formato, criterio, partidosPorDia);
            limpiarVistaTorneoNuevo(disciplina);
            actualizarEstadoTorneo();
        });

        panelOrganizador.agregarListenerParticipante(e -> {
            if (organizador.getTorneo() == null){
                JOptionPane.showMessageDialog(ventana,"Primero debe crear un torneo.");
                return;
            }

            Participante participante = generarParticipante(panelOrganizador);
            if(participante == null){
                return;
            }

            for(Participante par : organizador.getTorneo().getParticipantes()){
                if(par.getNombre().equalsIgnoreCase(participante.getNombre())){
                    JOptionPane.showMessageDialog(ventana,"Ya existe un participante con ese nombre.");
                    return;
                }
            }

            if(!tipoParticipanteValido(participante)){
                return;
            }

            addParticipante(participante);

            panelOrganizador.limpiarNombreParticipante();

            actualizarEstadoTorneo();
            actualizarClasificacion();
            actualizarListaParticipantes();
        });

        panelOrganizador.agregarListenerGenerarEnfrentamientos(e -> {
            Torneo torneo = organizador.getTorneo();
            if (torneo == null){
                JOptionPane.showMessageDialog(ventana,"Primero debe crear un torneo.");
                return;
            }

            if(torneo.getParticipantes().size() < 2){
                JOptionPane.showMessageDialog(ventana,"Debe haber al menos 2 participantes.");
                return;
            }

            try {
                generarEnfrentamientos();

                actualizarEstadoTorneo();
                actualizarCalendario();
                actualizarClasificacion();
                actualizarEnfrentamientos();
                actualizarBracket();
            } catch (IllegalArgumentException exp   ) {
                JOptionPane.showMessageDialog(ventana,exp.getMessage());
            }
        });

        panelEnfrentamiento.agregarListenerSeleccionEncuentro(e -> {
            String seleccionado = panelEnfrentamiento.getEncuentroSeleccionado();

            if(seleccionado == null){
                panelEnfrentamiento.limpiarEnfrentamiento();
                return;
            }

            Enfrentamiento enfrentamiento = enfrentamientosMap.get(seleccionado);
            String participante1 = enfrentamiento.getParticipante1().getNombre();
            String participante2 = enfrentamiento.getParticipante2().getNombre();

            panelEnfrentamiento.mostrarEnfrentamiento(seleccionado, participante1, participante2);
        });

        panelEnfrentamiento.agregarListenerRegistrar(e -> {
            String seleccionado = panelEnfrentamiento.getEncuentroSeleccionado();

            if(seleccionado == null){
                JOptionPane.showMessageDialog(ventana,"Seleccione un enfrentamiento.");
                return;
            }

            Enfrentamiento enfrentamiento = enfrentamientosMap.get(seleccionado);
            if(enfrentamiento == null || enfrentamiento.estadoFinalizado()){
                JOptionPane.showMessageDialog(ventana,"Seleccione un enfrentamiento pendiente.");
                actualizarEnfrentamientos();
                panelEnfrentamiento.limpiarEnfrentamiento();
                return;
            }

            try {
                int puntaje1 = Integer.parseInt(panelEnfrentamiento.getResultadoParticipante1());
                int puntaje2 = Integer.parseInt(panelEnfrentamiento.getResultadoParticipante2());

                if(puntaje1 < 0 || puntaje2 < 0){
                    JOptionPane.showMessageDialog(ventana, "Los puntajes no pueden ser negativos");
                    return;
                }

                if(organizador.getTorneo().getNombreCriterio().equals("Binario") && puntaje1 == puntaje2){
                    JOptionPane.showMessageDialog(ventana, "En el criterio binario no puede haber empate.");
                    return;
                }

                if(organizador.getTorneo().getNombreFormato().equals("Eliminatoria") && puntaje1 == puntaje2){
                    JOptionPane.showMessageDialog(ventana, "En eliminacion directa no puede haber empate.");
                    return;
                }

                if(!permiteEmpate(organizador.getTorneo().getDisciplina()) && puntaje1 == puntaje2){
                    JOptionPane.showMessageDialog(ventana, "La disciplina seleccionada no permite empate.");
                    return;
                }

                organizador.registrarResultado(enfrentamiento, puntaje1, puntaje2);

                if(organizador.getTorneo().getNombreFormato().equals("Eliminatoria")){
                    generarSiguienteRonda();
                }

                actualizarEstadoTorneo();
                actualizarCalendario();
                actualizarClasificacion();
                actualizarEnfrentamientos();
                actualizarBracket();

                panelEnfrentamiento.limpiarEnfrentamiento();
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(ventana,"Los puntajes deben ser números enteros.");
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(ventana, ex.getMessage());
            }
        });
    }

    private void crearTorneo(String nombre,
                             String disciplina,
                             LocalDate fechaInicio,
                             FormatoStrategy formato,
                             CriterioStrategy criterio,
                             int partidosPorDia){

        organizador.crearTorneo(nombre, disciplina, fechaInicio, formato, criterio, partidosPorDia);
    }

    private void limpiarVistaTorneoNuevo(String disciplina) {
        PanelOrganizador panelOrganizador = ventana.getPanelPrincipal().getPanelTorneo().getPanelOrganizador();
        PanelClasificacion panelClasificacion = ventana.getPanelPrincipal().getPanelTorneo().getPanelClasificacion();
        PanelCalendario panelCalendario = ventana.getPanelPrincipal().getPanelTorneo().getPanelCalendario();
        PanelEnfrentamiento panelEnfrentamiento = ventana.getPanelPrincipal().getPanelTorneo().getPanelEnfrentamiento();
        PanelBracket panelBracket = ventana.getPanelPrincipal().getPanelTorneo().getPanelBracket();

        enfrentamientosMap.clear();

        panelOrganizador.mostrarParticipantes(new ArrayList<>());
        panelOrganizador.limpiarNombreParticipante();

        panelClasificacion.configurarColumnas(obtenerColumnasClasificacion(disciplina));
        panelClasificacion.actualizarClasificacion(new ArrayList<>());

        panelCalendario.actualizarEncuentros(new HashMap<>());
        panelEnfrentamiento.actualizarEncuentros(new ArrayList<>());
        panelEnfrentamiento.limpiarEnfrentamiento();
        panelBracket.limpiarBracket();
    }

    private void addParticipante(Participante participante){
        organizador.addParticipante(participante);
    }

    private void generarEnfrentamientos(){
        organizador.generarEnfrentamientos();
    }

    private FormatoStrategy seleccionaFormato(String formatotxt){
        if (formatotxt.equals("Liga")) {
            return new FormatoLiga();
        }
        else {
            return new FormatoEliminatoria();
        }
    }

    private CriterioStrategy seleccionaCriterio(String criteriotxt){
        if(criteriotxt.equals("Binario")){
            return new CriterioBinario();
        }
        else if (criteriotxt.equals("Mayor puntaje")) {
            return new CriterioMayorPuntaje();
        }
        else {
            return new CriterioMenorPuntaje();
        }
    }

    private Participante generarParticipante(PanelOrganizador panelOrganizador){
        String nombre = panelOrganizador.getNombreParticipante();
        String contacto = panelOrganizador.getContactoParticipante();
        String tipo = panelOrganizador.getTipoParticipante();

        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(ventana,"Ingrese un nombre.");
            return null;
        }

        if(tipo.equals("Individuo")){
            return new Individuo(nombre,contacto);
        }
        else {
            return new Equipo(nombre,contacto);
        }
    }

    private boolean tipoParticipanteValido(Participante nuevoParticipante){
        List<Participante> participantes = organizador.getTorneo().getParticipantes();

        if(participantes.isEmpty()){
            return true;
        }

        Participante primero = participantes.get(0);
        boolean torneoDeEquipos = primero instanceof Equipo;
        boolean nuevoEsEquipo = nuevoParticipante instanceof Equipo;

        if(torneoDeEquipos != nuevoEsEquipo){
            String tipoTorneo = torneoDeEquipos ? "Equipo" : "Individuo";
            String tipoNuevo = nuevoEsEquipo ? "Equipo" : "Individuo";

            JOptionPane.showMessageDialog(
                    ventana,
                    "Este torneo ya fue iniciado con participantes de tipo "
                            + tipoTorneo
                            + ". No puedes agregar un participante de tipo "
                            + tipoNuevo
                            + "."
            );
            return false;
        }

        return true;
    }

    private void actualizarEstadoTorneo(){
        PanelEstadoTorneo panelEstadoTorneo = ventana.getPanelPrincipal().getPanelTorneo().getPanelEstadoTorneo();

        Torneo torneo = organizador.getTorneo();
        String nombre = torneo.getNombre();
        String disciplina = torneo.getDisciplina();
        String formato = torneo.getNombreFormato();
        String criterio = torneo.getNombreCriterio();
        String fechaFin = torneo.getFechaFin() == null
                ? "Por calcular"
                : torneo.getFechaFin().format(FORMATO_FECHA);
        int participantes = torneo.getParticipantes().size();
        String estado = obtenerEstadoTorneo();
        String ganador = obtenerGanadorTorneo();

        panelEstadoTorneo.actualizarDatos(nombre, disciplina, formato, criterio, fechaFin, participantes, estado, ganador);
    }

    private String obtenerEstadoTorneo(){
        Torneo torneo = organizador.getTorneo();
        List<Participante> participantes = torneo.getParticipantes();
        List<Enfrentamiento> enfrentamientos = torneo.getEnfrentamientos();

        if(participantes.isEmpty()){
            return "Sin participantes";
        }
        else if (enfrentamientos.isEmpty()) {
            return "Pendiente";
        }

        for(Enfrentamiento enf : enfrentamientos){
            if(!enf.estadoFinalizado()){
                return "En curso";
            }
        }
        return "Finalizado";
    }

    private String obtenerGanadorTorneo(){
        Torneo torneo = organizador.getTorneo();

        if(torneo == null || !"Finalizado".equals(obtenerEstadoTorneo())){
            return "Sin definir";
        }

        if(torneo.getNombreFormato().equals("Eliminatoria")){
            return obtenerGanadorEliminatoria(torneo);
        }

        return obtenerGanadorLiga(torneo);
    }

    private String obtenerGanadorEliminatoria(Torneo torneo){
        Enfrentamiento finalTorneo = null;

        for(Enfrentamiento enfrentamiento : torneo.getEnfrentamientos()){
            if(finalTorneo == null || enfrentamiento.getRonda() > finalTorneo.getRonda()){
                finalTorneo = enfrentamiento;
            }
        }

        if(finalTorneo == null || finalTorneo.getGanador() == null){
            return "Sin definir";
        }

        return finalTorneo.getGanador().getNombre();
    }

    private String obtenerGanadorLiga(Torneo torneo){
        List<Participante> clasificacion = torneo.getClasificacion().getClasificacion();
        String disciplina = torneo.getDisciplina();
        clasificacion.sort((p1, p2) -> compararParticipantes(p1, p2, disciplina));

        if(clasificacion.isEmpty()){
            return "Sin definir";
        }

        return clasificacion.get(0).getNombre();
    }

    private void actualizarListaParticipantes(){
        PanelOrganizador panelOrganizador = ventana.getPanelPrincipal().getPanelTorneo().getPanelOrganizador();
        List<Participante> participantes = organizador.getTorneo().getParticipantes();
        List<String> participantesTxt = new ArrayList<>();

        for(Participante par : participantes){
            participantesTxt.add(par.getNombre());
        }

        panelOrganizador.mostrarParticipantes(participantesTxt);
    }

    private void actualizarCalendario(){
        PanelCalendario panelCalendario = ventana.getPanelPrincipal().getPanelTorneo().getPanelCalendario();
        Calendario calendario = organizador.getTorneo().getCalendario();

        if(calendario == null){
            return;
        }

        Map<LocalDate, List<String>> encuentros = new HashMap<>();

        for (Enfrentamiento enf : calendario.getCalendario()){
            LocalDate fecha = enf.getFecha();
            if (fecha == null) {
                continue;
            }

            String enfrentamientoTxt = enf.getParticipante1().getNombre() + " vs " + enf.getParticipante2().getNombre();

            encuentros.computeIfAbsent(fecha, f-> new ArrayList<>()).add(enfrentamientoTxt);
        }

        panelCalendario.actualizarEncuentros(encuentros);
    }

    private void actualizarEnfrentamientos(){
        PanelEnfrentamiento panelEnfrentamiento = ventana.getPanelPrincipal().getPanelTorneo().getPanelEnfrentamiento();

        enfrentamientosMap.clear();

        List<Enfrentamiento> enfrentamientos = organizador.getTorneo().getEnfrentamientos();
        List<String> enfrentamientosTxt = new ArrayList<>();

        for (Enfrentamiento enf : enfrentamientos){
            if(!enf.estadoFinalizado()){
                String texto = enf.getParticipante1().getNombre() + " vs " + enf.getParticipante2().getNombre();

                enfrentamientosTxt.add(texto);
                enfrentamientosMap.put(texto, enf);
            }
        }
        panelEnfrentamiento.actualizarEncuentros(enfrentamientosTxt);
        panelEnfrentamiento.limpiarEnfrentamiento();
    }

    private void actualizarClasificacion(){
        PanelClasificacion panelClasificacion = ventana.getPanelPrincipal().getPanelTorneo().getPanelClasificacion();

        Torneo torneo = organizador.getTorneo();
        String disciplina = torneo.getDisciplina();
        panelClasificacion.configurarColumnas(obtenerColumnasClasificacion(torneo));

        List<Participante> clasificacion = torneo.getClasificacion().getClasificacion();
        clasificacion.sort((p1, p2) -> compararParticipantes(p1, p2, disciplina));

        List<Object[]> filas = new ArrayList<>();

        int posicion = 1;
        for(Participante participante : clasificacion){
            filas.add(crearFilaClasificacion(posicion++, participante, disciplina));
        }
        panelClasificacion.actualizarClasificacion(filas);
    }

    private void actualizarBracket() {
        PanelBracket panelBracket = ventana.getPanelPrincipal().getPanelTorneo().getPanelBracket();
        Torneo torneo = organizador.getTorneo();

        if (torneo == null || torneo.getBracket() == null) {
            panelBracket.limpiarBracket();
            return;
        }

        List<String> enfrentamientos = new ArrayList<>();
        Enfrentamiento finalTorneo = null;
        for (Enfrentamiento enfrentamiento : torneo.getBracket().getBracket()) {
            if(finalTorneo == null || enfrentamiento.getRonda() > finalTorneo.getRonda()){
                finalTorneo = enfrentamiento;
            }

            String texto = "Ronda " + enfrentamiento.getRonda()
                    + " | "
                    + enfrentamiento.getParticipante1().getNombre()
                    + " vs "
                    + enfrentamiento.getParticipante2().getNombre();

            if (enfrentamiento.estadoFinalizado()) {
                Participante ganador = enfrentamiento.getGanador();
                texto += ganador == null
                        ? " | Empate"
                        : " | Ganador: " + ganador.getNombre();
            }

            enfrentamientos.add(texto);
        }

        if("Finalizado".equals(obtenerEstadoTorneo())
                && finalTorneo != null
                && finalTorneo.estadoFinalizado()
                && finalTorneo.getGanador() != null){
            enfrentamientos.add("Ronda " + (finalTorneo.getRonda() + 1)
                    + " | Campeon: " + finalTorneo.getGanador().getNombre());
        }

        panelBracket.actualizarBracket(enfrentamientos);
    }

    private String[] obtenerColumnasClasificacion(Torneo torneo) {
        return obtenerColumnasClasificacion(torneo.getDisciplina(), obtenerEtiquetaParticipante(torneo));
    }

    private String[] obtenerColumnasClasificacion(String disciplina) {
        return obtenerColumnasClasificacion(disciplina, obtenerEtiquetaParticipante(disciplina));
    }

    private String[] obtenerColumnasClasificacion(String disciplina, String etiquetaParticipante) {
        if (esFutbol(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PE", "PP", "GF", "GC", "DG", "PTS"};
        }
        if (esRugby(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PE", "PP", "PF", "PC", "DIF", "PTS"};
        }
        if (esBasket(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PP", "PF", "PC", "DIF", "PTS"};
        }
        if (esAjedrez(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PE", "PP", "PTS"};
        }
        if (esTenis(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PP", "SF", "SC", "DIF", "PTS"};
        }
        if (esPadel(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PP", "SF", "SC", "DIF", "PTS"};
        }
        if (esVoleibol(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PP", "SF", "SC", "DIF", "PTS"};
        }
        if (esEsports(disciplina)) {
            return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PP", "MF", "MC", "DIF", "PTS"};
        }
        return new String[]{"Pos", etiquetaParticipante, "PJ", "PG", "PE", "PP", "PF", "PC", "DIF", "PTS"};
    }

    private Object[] crearFilaClasificacion(int posicion, Participante participante, String disciplina) {
        Estadistica estadistica = participante.getEstadistica();
        Object puntos = formatearPuntos(calcularPuntosClasificacion(estadistica, disciplina), disciplina);

        if (esFutbol(disciplina) || esRugby(disciplina)) {
            return new Object[]{
                    posicion,
                    participante.getNombre(),
                    estadistica.getPartidasJugadas(),
                    estadistica.getVictorias(),
                    estadistica.getEmpates(),
                    estadistica.getDerrotas(),
                    estadistica.getPuntosFavor(),
                    estadistica.getPuntosContra(),
                    estadistica.getDiferencia(),
                    puntos
            };
        }
        if (esBasket(disciplina)
                || esTenis(disciplina)
                || esPadel(disciplina)
                || esVoleibol(disciplina)
                || esEsports(disciplina)) {
            return new Object[]{
                    posicion,
                    participante.getNombre(),
                    estadistica.getPartidasJugadas(),
                    estadistica.getVictorias(),
                    estadistica.getDerrotas(),
                    estadistica.getPuntosFavor(),
                    estadistica.getPuntosContra(),
                    estadistica.getDiferencia(),
                    puntos
            };
        }
        if (esAjedrez(disciplina)) {
            return new Object[]{
                    posicion,
                    participante.getNombre(),
                    estadistica.getPartidasJugadas(),
                    estadistica.getVictorias(),
                    estadistica.getEmpates(),
                    estadistica.getDerrotas(),
                    puntos
            };
        }
        return new Object[]{
                posicion,
                participante.getNombre(),
                estadistica.getPartidasJugadas(),
                estadistica.getVictorias(),
                estadistica.getEmpates(),
                estadistica.getDerrotas(),
                estadistica.getPuntosFavor(),
                estadistica.getPuntosContra(),
                estadistica.getDiferencia(),
                puntos
        };
    }

    private int compararParticipantes(Participante p1, Participante p2, String disciplina) {
        Estadistica e1 = p1.getEstadistica();
        Estadistica e2 = p2.getEstadistica();

        int comparacionPuntos = Double.compare(
                calcularPuntosClasificacion(e2, disciplina),
                calcularPuntosClasificacion(e1, disciplina)
        );
        if (comparacionPuntos != 0) {
            return comparacionPuntos;
        }

        int comparacionDiferencia = Integer.compare(e2.getDiferencia(), e1.getDiferencia());
        if (comparacionDiferencia != 0) {
            return comparacionDiferencia;
        }

        return Integer.compare(e2.getPuntosFavor(), e1.getPuntosFavor());
    }

    private double calcularPuntosClasificacion(Estadistica estadistica, String disciplina) {
        if (esAjedrez(disciplina)) {
            return estadistica.getVictorias() + (estadistica.getEmpates() * 0.5);
        }
        if (esBasket(disciplina)) {
            return (estadistica.getVictorias() * 2) + estadistica.getDerrotas();
        }
        if (esRugby(disciplina)) {
            return (estadistica.getVictorias() * 4) + (estadistica.getEmpates() * 2);
        }
        if (esTenis(disciplina)
                || esPadel(disciplina)
                || esVoleibol(disciplina)
                || esEsports(disciplina)) {
            return estadistica.getVictorias() * 3;
        }
        return (estadistica.getVictorias() * 3) + estadistica.getEmpates();
    }

    private Object formatearPuntos(double puntos, String disciplina) {
        if (esAjedrez(disciplina) && puntos % 1 != 0) {
            return puntos;
        }
        return (int) puntos;
    }

    private boolean esFutbol(String disciplina) {
        String texto = normalizarDisciplina(disciplina);
        return texto.contains("futbol");
    }

    private boolean esRugby(String disciplina) {
        return normalizarDisciplina(disciplina).contains("rugby");
    }

    private boolean esBasket(String disciplina) {
        String texto = normalizarDisciplina(disciplina);
        return texto.contains("baloncesto") || texto.contains("basket");
    }

    private boolean esAjedrez(String disciplina) {
        return normalizarDisciplina(disciplina).contains("ajedrez");
    }

    private boolean esTenis(String disciplina) {
        String texto = normalizarDisciplina(disciplina);
        return texto.equals("tenis");
    }

    private boolean esPadel(String disciplina) {
        return normalizarDisciplina(disciplina).contains("padel");
    }

    private boolean esVoleibol(String disciplina) {
        return normalizarDisciplina(disciplina).contains("voleibol");
    }

    private boolean esEsports(String disciplina) {
        String texto = normalizarDisciplina(disciplina);
        return texto.contains("esports");
    }

    private String obtenerEtiquetaParticipante(Torneo torneo) {
        return torneoEsDeEquipos(torneo) ? "Equipo" : "Competidor";
    }

    private String obtenerEtiquetaParticipante(String disciplina) {
        return torneoEsDeEquipos(disciplina) ? "Equipo" : "Competidor";
    }

    private boolean torneoEsDeEquipos(Torneo torneo) {
        List<Participante> participantes = torneo.getParticipantes();

        if (!participantes.isEmpty()) {
            return participantes.get(0) instanceof Equipo;
        }

        return torneoEsDeEquipos(torneo.getDisciplina());
    }

    private boolean torneoEsDeEquipos(String disciplina) {
        if (esFutbol(disciplina) || esRugby(disciplina) || esVoleibol(disciplina) || esEsports(disciplina)) {
            return true;
        }

        if (esAjedrez(disciplina) || esTenis(disciplina)) {
            return false;
        }

        PanelOrganizador panelOrganizador = ventana.getPanelPrincipal().getPanelTorneo().getPanelOrganizador();
        return "Equipo".equals(panelOrganizador.getTipoParticipante());
    }

    private boolean permiteEmpate(String disciplina) {
        return esFutbol(disciplina)
                || esRugby(disciplina)
                || esAjedrez(disciplina);
    }

    private String normalizarDisciplina(String disciplina) {
        if (disciplina == null) {
            return "";
        }
        String texto = Normalizer.normalize(disciplina, Normalizer.Form.NFD);
        return texto.replaceAll("\\p{M}", "").toLowerCase();
    }

    private void generarSiguienteRonda() {
        Torneo torneo = organizador.getTorneo();

        List<Enfrentamiento> ronda = new ArrayList<>();

        for(Enfrentamiento enf : torneo.getEnfrentamientos()){
            if(enf.getRonda() == torneo.getRondaActual()){
                ronda.add(enf);
            }
        }

        for(Enfrentamiento enf : ronda){
            if(!enf.estadoFinalizado()){
                return;
            }
        }

        if(ronda.size() == 1){
            return;
        }

        ArrayList<Participante> ganadores = new ArrayList<>();

        for(Enfrentamiento enf : ronda){
            if(enf.getGanador() == null){
                JOptionPane.showMessageDialog(ventana, "No se puede generar la siguiente ronda con enfrentamientos empatados.");
                return;
            }
            ganadores.add(enf.getGanador());
        }

        torneo.generarSiguienteRonda(ganadores);
    }
}
