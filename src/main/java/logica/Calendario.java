package logica;

import java.util.ArrayList;

public class Calendario {
    private ArrayList<Enfrentamiento> calendario;

    public Calendario() {
        this.calendario = new ArrayList<>();
    }
    
    public ArrayList<Enfrentamiento> getCalendario() {
        return new ArrayList<>(calendario);
    }
}
