import logica.*;
import visual.*;

import javax.swing.*;
import java.time.LocalDate;
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
    }



    private void crearTorneo(String nombre,
                             String disciplina,
                             LocalDate fechaInicio,
                             FormatoStrategy formato,
                             CriterioStrategy criterio){

        organizador.crearTorneo(nombre, disciplina, fechaInicio, formato, criterio);
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
}
