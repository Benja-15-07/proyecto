package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Calendario {
    private List<Enfrentamiento> calendario;

    public Calendario(List<Enfrentamiento> enfrentamientos, LocalDate fecha) {
        this.calendario = new ArrayList<>(enfrentamientos);
        for(Enfrentamiento enf : calendario){
            enf.setFecha(fecha);
            fecha = fecha.plusDays(1);
        }
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
}
