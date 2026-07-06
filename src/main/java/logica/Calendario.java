package logica;

import java.util.ArrayList;

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
}
