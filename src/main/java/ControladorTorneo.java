import logica.*;
import visual.*;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorTorneo {
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

            LocalDate fechaInicio = null ;//panelOrganizador.getFechaInicio();
            /*if(fechaInicio == null)){
                JOptionPane.showMessageDialog(ventana,"Ingrese una fecha de inicio.");
                return;
            }*/

            FormatoStrategy formato = seleccionaFormato(panelOrganizador.getFormatoSeleccionado());
            CriterioStrategy criterio = seleccionaCriterio(panelOrganizador.getCriterioSeleccionado());

            if(nombre.trim().isEmpty()){
                JOptionPane.showMessageDialog(ventana,"Ingrese nombre del torneo.");
                return;
            }

            crearTorneo(nombre, disciplina, fechaInicio, formato, criterio);
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

                organizador.registrarResultado(enfrentamiento, puntaje1, puntaje2);

                if(organizador.getTorneo().getNombreFormato().equals("Eliminatoria")){
                    generarSiguienteRonda();
                }

                actualizarEstadoTorneo();
                actualizarCalendario();
                actualizarClasificacion();
                actualizarEnfrentamientos();

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
                             CriterioStrategy criterio){

        organizador.crearTorneo(nombre, disciplina, fechaInicio, formato, criterio);
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

    private void actualizarEstadoTorneo(){
        PanelEstadoTorneo panelEstadoTorneo = ventana.getPanelPrincipal().getPanelTorneo().getPanelEstadoTorneo();

        Torneo torneo = organizador.getTorneo();
        String nombre = torneo.getNombre();
        String disciplina = torneo.getDisciplina();
        String formato = torneo.getNombreFormato();
        int participantes = torneo.getParticipantes().size();
        String estado = obtenerEstadoTorneo();

        panelEstadoTorneo.actualizarDatos(nombre, disciplina, formato, participantes, estado);
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
    }

    private void actualizarClasificacion(){
        PanelClasificacion panelClasificacion = ventana.getPanelPrincipal().getPanelTorneo().getPanelClasificacion();

        Clasificacion clasificacion = organizador.getTorneo().getClasificacion();
        List<Object[]> filas = new ArrayList<>();

        int posicion = 1;
        for(Participante p : clasificacion.getClasificacion()){
            Estadistica e = p.getEstadistica();

            filas.add(new Object[]{posicion++, p.getNombre(), e.getPuntos(), e.getPartidasJugadas()});
        }
        panelClasificacion.actualizarClasificacion(filas);
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
            ganadores.add(enf.getGanador());
        }

        torneo.generarSiguienteRonda(ganadores);
    }
}
