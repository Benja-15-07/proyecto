package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Calendario {
    private ArrayList<Enfrentamiento> calendario;

    public Calendario(ArrayList<Enfrentamiento> enfrentamientos) {
        this.calendario = new ArrayList<>(enfrentamientos);
    }

    public ArrayList<Enfrentamiento> getCalendario() {
        return new ArrayList<>(calendario);
    }

    public ArrayList<Enfrentamiento> getProximosEncuentros(){
        ArrayList<Enfrentamiento> calendarioProx = new ArrayList<>();
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
}
