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
}
