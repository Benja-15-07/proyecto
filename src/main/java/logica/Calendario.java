package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class Calendario {
    private List<Enfrentamiento> calendario;

    public Calendario(List<Enfrentamiento> enfrentamientos) {
        this.calendario = new ArrayList<>(enfrentamientos);
    }

    public Calendario(List<Enfrentamiento> enfrentamientos, LocalDate fecha) {
        this(enfrentamientos, fecha, 1);
    }

    public Calendario(List<Enfrentamiento> enfrentamientos, LocalDate fecha, int partidosPorDia) {
        this.calendario = new ArrayList<>(enfrentamientos);
        asignarFechasAutomaticas(fecha, partidosPorDia);
    }

    public List<Enfrentamiento> getCalendario() {
        return new  ArrayList<>(calendario);
    }

    public List<Enfrentamiento> getProximosEncuentros(){
        List<Enfrentamiento> calendarioProx = new ArrayList<>();
        for(Enfrentamiento enf : calendario){
            if(!enf.estadoFinalizado()){
                calendarioProx.add(enf);
            }
        }
        return calendarioProx;
    }

    public void asignarFecha(Enfrentamiento enfrentamiento, LocalDate fecha) {
        if (!calendario.contains(enfrentamiento)) {
            throw new IllegalArgumentException("El enfrentamiento no pertenece al calendario.");
        }
        enfrentamiento.setFecha(fecha);
    }

    public ArrayList<Enfrentamiento> getEnfrentamientosPorFecha(LocalDate fecha) {
        ArrayList<Enfrentamiento> encontrados = new ArrayList<>();
        for (Enfrentamiento enfrentamiento : calendario) {
            if (fecha.equals(enfrentamiento.getFecha())) {
                encontrados.add(enfrentamiento);
            }
        }
        return encontrados;
    }

    public Map<LocalDate, ArrayList<Enfrentamiento>> getEnfrentamientosAgrupadosPorFecha() {
        Map<LocalDate, ArrayList<Enfrentamiento>> agrupados = new HashMap<>();
        for (Enfrentamiento enfrentamiento : calendario) {
            LocalDate fecha = enfrentamiento.getFecha();
            if (fecha != null) {
                agrupados.computeIfAbsent(fecha, f -> new ArrayList<>()).add(enfrentamiento);
            }
        }
        return agrupados;
    }

    public LocalDate getFechaFin() {
        LocalDate fechaFin = null;
        for (Enfrentamiento enfrentamiento : calendario) {
            LocalDate fecha = enfrentamiento.getFecha();
            if (fecha != null && (fechaFin == null || fecha.isAfter(fechaFin))) {
                fechaFin = fecha;
            }
        }
        return fechaFin;
    }

    private void asignarFechasAutomaticas(LocalDate fechaInicio, int partidosPorDia) {
        if (fechaInicio == null || calendario.isEmpty()) {
            return;
        }

        int limiteDiario = calcularLimiteDiario(partidosPorDia);
        List<Enfrentamiento> pendientes = new ArrayList<>(calendario);
        LocalDate fechaActual = fechaInicio;

        while (!pendientes.isEmpty()) {
            Set<Participante> participantesDelDia = new HashSet<>();
            int asignados = 0;

            Iterator<Enfrentamiento> iterador = pendientes.iterator();
            while (iterador.hasNext() && asignados < limiteDiario) {
                Enfrentamiento enfrentamiento = iterador.next();

                if (puedeJugarEseDia(enfrentamiento, participantesDelDia)) {
                    enfrentamiento.setFecha(fechaActual);
                    participantesDelDia.add(enfrentamiento.getParticipante1());
                    participantesDelDia.add(enfrentamiento.getParticipante2());
                    iterador.remove();
                    asignados++;
                }
            }

            fechaActual = fechaActual.plusDays(1);
        }
    }

    private int calcularLimiteDiario(int partidosPorDia) {
        Set<Participante> participantes = new HashSet<>();
        for (Enfrentamiento enfrentamiento : calendario) {
            participantes.add(enfrentamiento.getParticipante1());
            participantes.add(enfrentamiento.getParticipante2());
        }

        int limiteReal = Math.max(1, participantes.size() / 2);
        int limiteElegido = Math.max(1, partidosPorDia);
        return Math.min(limiteElegido, limiteReal);
    }

    private boolean puedeJugarEseDia(Enfrentamiento enfrentamiento, Set<Participante> participantesDelDia) {
        return !participantesDelDia.contains(enfrentamiento.getParticipante1())
                && !participantesDelDia.contains(enfrentamiento.getParticipante2());
    }
}
