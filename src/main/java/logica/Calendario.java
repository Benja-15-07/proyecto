package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *  Representa el calendario de enfrentamientos de un torneo.
 */
public class Calendario {
    private List<Enfrentamiento> calendario;

    /**
     * Crea un calendario a partir de una lista de enfrentamientos
     * y le asigna una fecha a cada uno, a partir de la fecha de inicio del torneo.
     *
     * @param enfrentamientos lista de enfrentamientos que conforman el calendario
     * @param fecha fecha de inicio del torneo
     */
    public Calendario(List<Enfrentamiento> enfrentamientos, LocalDate fecha) {
        this.calendario = new ArrayList<>(enfrentamientos);
        for(Enfrentamiento enf : calendario){
            enf.setFecha(fecha);
            fecha = fecha.plusDays(1);
        }
    }

    /**
     * Devuelve una copia de los enfrentamientos del calendario.
     *
     * @return lista de enfrentamientos del calendario
     */
    public List<Enfrentamiento> getCalendario() {
        return new  ArrayList<>(calendario);
    }

    /**
     * Devuelve una copia de los enfrentamientos que no han finalizado.
     *
     * @return lista de enfrentamientos pendientes
     */
    public List<Enfrentamiento> getProximosEncuentros(){
        List<Enfrentamiento> calendarioProx = new ArrayList<>();
        for(Enfrentamiento enf : calendario){
            if(!enf.estadoFinalizado()){
                calendarioProx.add(enf);
            }
        }
        return calendarioProx;
    }
}
